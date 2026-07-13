package tables.baccarat.calcs;

import tables.cards.deck.Card;
import tables.cards.deck.EnumeratorShoe;
import tables.evals.BetEvaluator;
import tables.evals.BetStatistics;
import tables.evals.CardCalculator;
import tables.evals.PaylineStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Enumerator that computes exact baccarat hand statistics by iterating over
 * all possible card combinations from a given shoe.
 * <p>
 * The enumeration follows standard baccarat drawing rules:
 * <ol>
 *   <li>Two cards each are dealt to Player and Banker.</li>
 *   <li>If either has a natural (8 or 9), no more cards are drawn.</li>
 *   <li>If Player has 0-5, Player draws a third card.</li>
 *   <li>If Player stands (6+), Banker draws on 0-5 and stands on 6+.</li>
 *   <li>If Player drew, the six-card draw rule determines Banker's action.</li>
 * </ol>
 * <p>
 * This implementation uses multi-threading by partitioning work across unique
 * first-card choices. Each thread maintains its own shoe copy for thread safety.
 */
public class BaccaratEnumerator extends CardCalculator {

    private final EnumeratorShoe shoe;

    /**
     * Constructs a baccarat enumerator.
     *
     * @param shoe  the shoe to use for enumeration
     * @param evals the bet evaluators defining the statistics to compute
     */
    public BaccaratEnumerator(EnumeratorShoe shoe, BetEvaluator... evals) {
        super(Arrays.stream(evals).map(BetEvaluator::getNewBetStatistics).toList());
        this.shoe = shoe;
    }

    @Override
    public void run(int threads) throws InterruptedException {
        Set<Card> uniqueCards = new HashSet<>(shoe.getCards());
        int totalCards = shoe.getTotalCards();
        int combosExtra = (totalCards - 5);
        int combosExtra2 = (totalCards - 4) * (totalCards - 5);
        final AtomicLong totalCombos = new AtomicLong();

        // Partition work by first card dealt to Player
        List<Callable<Void>> callables = new ArrayList<>(uniqueCards.size());
        for (Card c1 : uniqueCards) {
            callables.add(() -> {
                EnumeratorShoe threadShoe = shoe.copyShoe();
                long combos = threadShoe.getCount(c1);
                threadShoe.removeCard(c1);

                for (Card c2 : uniqueCards) {
                    combos *= threadShoe.getCount(c2);
                    threadShoe.removeCard(c2);

                    for (Card c3 : uniqueCards) {
                        combos *= threadShoe.getCount(c3);
                        threadShoe.removeCard(c3);

                        for (Card c4 : uniqueCards) {
                            combos *= threadShoe.getCount(c4);
                            threadShoe.removeCard(c4);

                            int playerScore = (c1.getRank().getValue() + c3.getRank().getValue()) % 10;
                            int bankerScore = (c2.getRank().getValue() + c4.getRank().getValue()) % 10;

                            if (isNaturalOrBothStand(playerScore, bankerScore)) {
                                // No third cards drawn
                                recordOutcome(combos, combosExtra2, totalCombos,
                                        new BaccaratHand(c1, c3), new BaccaratHand(c2, c4));
                            } else if (playerScore < 6) {
                                // Player draws
                                enumeratePlayerDraws(threadShoe, c1, c2, c3, c4,
                                        bankerScore, combos, combosExtra, combosExtra2, totalCombos);
                            } else {
                                // Player stands (6+), Banker draws on 0-5
                                enumerateBankerDraws(threadShoe, c1, c2, c3, c4,
                                        combos, combosExtra, totalCombos);
                            }

                            threadShoe.addCard(c4);
                            combos /= threadShoe.getCount(c4);
                        }
                        threadShoe.addCard(c3);
                        combos /= threadShoe.getCount(c3);
                    }
                    threadShoe.addCard(c2);
                    combos /= threadShoe.getCount(c2);
                }
                return null;
            });
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(threads)) {
            executorService.invokeAll(callables);
            executorService.shutdown();
            for (BetStatistics stat : stats) {
                stat.setTotalCombos(totalCombos.get());
            }
        }
    }

    private static boolean isNaturalOrBothStand(int playerScore, int bankerScore) {
        return playerScore >= 8 || bankerScore >= 8 || (playerScore > 5 && bankerScore > 5);
    }

    private void enumeratePlayerDraws(EnumeratorShoe threadShoe, Card c1, Card c2, Card c3, Card c4,
                                       int bankerScore,
                                       long combos, int combosExtra, int combosExtra2, AtomicLong totalCombos) {

        for (Card c5 : threadShoe.getCards()) {
            combos *= threadShoe.getCount(c5);
            threadShoe.removeCard(c5);

            if (SixCardDrawRule.isDraw(bankerScore, c5.getRank().getValue())) {
                // Banker draws
                for (Card c6 : threadShoe.getCards()) {
                    combos *= threadShoe.getCount(c6);
                    threadShoe.removeCard(c6);
                    recordOutcome(combos, 1, totalCombos,
                            new BaccaratHand(c1, c3, c5), new BaccaratHand(c2, c4, c6));
                    threadShoe.addCard(c6);
                    combos /= threadShoe.getCount(c6);
                }
            } else {
                // Banker stands
                recordOutcome(combos, combosExtra, totalCombos,
                        new BaccaratHand(c1, c3, c5), new BaccaratHand(c2, c4));
            }

            threadShoe.addCard(c5);
            combos /= threadShoe.getCount(c5);
        }
    }

    private void enumerateBankerDraws(EnumeratorShoe threadShoe, Card c1, Card c2, Card c3, Card c4,
                                       long combos, int combosExtra, AtomicLong totalCombos) {

        // Multiply by combosExtra to account for the 6th card position that is not drawn
        // (only 5 cards are used: 4 initial + 1 banker third card)
        combos *= combosExtra;
        for (Card c5 : threadShoe.getCards()) {
            combos *= threadShoe.getCount(c5);
            threadShoe.removeCard(c5);
            recordOutcome(combos, 1, totalCombos,
                    new BaccaratHand(c1, c3), new BaccaratHand(c2, c4, c5));
            threadShoe.addCard(c5);
            combos /= threadShoe.getCount(c5);
        }
    }


    private void recordOutcome(long baseCombos, int multiplier, AtomicLong totalCombos,
                                BaccaratHand playerHand, BaccaratHand bankerHand) {
        long weightedCombos = baseCombos * multiplier;
        totalCombos.addAndGet(weightedCombos);
        for (BetStatistics stat : stats) {
            PaylineStatistics hitStat = stat.getHitStat(playerHand, bankerHand);
            hitStat.addCombinations(weightedCombos);
        }
    }
}
