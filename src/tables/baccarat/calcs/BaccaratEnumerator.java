package tables.baccarat.calcs;

import tables.cards.deck.Card;
import tables.cards.deck.EnumShoe;
import tables.cards.deck.Suit;
import tables.evals.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class BaccaratEnumerator<R extends BaccRank, S extends Suit, H extends BaccHand<R,S>> extends CardCalculator {

    private final EnumShoe<R,S> shoe;
    private final SixCardDrawRule drawRule;

    public BaccaratEnumerator(SixCardDrawRule drawRule, EnumShoe<R,S> shoe, BetEvaluator... evals) {
        super(Arrays.stream(evals).map(BetEvaluator::getNewBetStatistics).collect(Collectors.toList()).toArray(BetStatistics[]::new));
        this.drawRule = drawRule;
        this.shoe = shoe;
    }

    @Override
    public void run(int threads) {

        // Set up constants
        Set<Card<R,S>> uniqueCards = new HashSet<>(shoe.getCards());
        int totalCards = shoe.getTotalCards();
        int combosExtra = (totalCards - 5);
        int combosExtra2 = (totalCards - 4) * (totalCards - 5);
        final AtomicLong totalCombos = new AtomicLong();

        final AtomicLong cardNumber = new AtomicLong();
        long startTime = System.nanoTime();
        ArrayList<Callable<Void>> callables = new ArrayList<>(uniqueCards.size());
        for (Card<R,S> c1 : uniqueCards) {

            callables.add(() -> {

                EnumShoe<R,S> threadShoe = shoe.clone();
                int p1Num = c1.getRank().getValue();
                long combos = (long)threadShoe.getCount(c1);
                threadShoe.removeCard(c1);
                for (Card<R,S> c2 : uniqueCards) {

                    int b1Num = c2.getRank().getValue();
                    combos *= threadShoe.getCount(c2);
                    threadShoe.removeCard(c2);
                    for (Card<R,S> c3 : uniqueCards) {

                        int p2Num = c3.getRank().getValue();
                        combos *= threadShoe.getCount(c3);
                        threadShoe.removeCard(c3);
                        for (Card<R,S> c4 : uniqueCards) {

                            int b2Num = c4.getRank().getValue();
                            combos *= threadShoe.getCount(c4);
                            threadShoe.removeCard(c4);
                            int player2 = (p1Num + p2Num) % 10;
                            int banker2 = (b1Num + b2Num) % 10;

                            // Natural 9/8 or both stand with 6+
                            if (player2 >= 8 || banker2 >= 8 ||
                                    (player2 > 5 && banker2 > 5)) {

                                combos *= combosExtra2;
                                totalCombos.addAndGet(combos);
                                for (BetStatistics stat : stats) {
                                    PaylineStatistics hitStat = stat.getHitStat(new BaccHand(c1, c3), new BaccHand(c2, c4));
                                    hitStat.addCombinations(combos);
                                }

                                combos /= combosExtra2;
                            }
                            // Player draws
                            else if (player2 < 6) {
                                for (Card<R,S> c5 : uniqueCards) {

                                    int p3Num = c5.getRank().getValue();
                                    combos *= threadShoe.getCount(c5);
                                    threadShoe.removeCard(c5);
                                    // Banker draws
                                    if (drawRule.isDraw(banker2, p3Num)) {
                                        for (Card<R,S> c6 : uniqueCards) {

                                            combos *= threadShoe.getCount(c6);
                                            threadShoe.removeCard(c6);
                                            totalCombos.addAndGet(combos);
                                            for (BetStatistics stat : stats) {
                                                PaylineStatistics hitStat = stat.getHitStat(new BaccHand(c1, c3, c5), new BaccHand(c2, c4, c6));
                                                hitStat.addCombinations(combos);
                                            }

                                            threadShoe.addCard(c6);
                                            combos /= threadShoe.getCount(c6);
                                        }
                                    }
                                    // Banker stands
                                    else {

                                        combos *= combosExtra;
                                        totalCombos.addAndGet(combos);
                                        for (BetStatistics stat : stats) {
                                            PaylineStatistics hitStat = stat.getHitStat(new BaccHand(c1, c3, c5), new BaccHand(c2, c4));
                                            hitStat.addCombinations(combos);
                                        }

                                        combos /= combosExtra;
                                    }

                                    threadShoe.addCard(c5);
                                    combos /= threadShoe.getCount(c5);
                                }
                            }
                            // Player stands and banker draws
                            else {

                                combos *= combosExtra;
                                for (Card<R,S> c5 : uniqueCards) {

                                    combos *= threadShoe.getCount(c5);
                                    threadShoe.removeCard(c5);
                                    totalCombos.addAndGet(combos);
                                    for (BetStatistics stat : stats) {
                                        PaylineStatistics hitStat = stat.getHitStat(new BaccHand(c1, c3), new BaccHand(c2, c4, c5));
                                        hitStat.addCombinations(combos);
                                    }

                                    threadShoe.addCard(c5);
                                    combos /= threadShoe.getCount(c5);
                                }
                                combos /= combosExtra;
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

                // Increment progress counter
                System.out.println(
                        cardNumber.incrementAndGet() / (double) uniqueCards.size() * 100 + "%: " +
                                (System.nanoTime() - startTime) / 1000000000d / cardNumber.get() * uniqueCards.size() +
                                "s total estimated and " +
                                (1d - cardNumber.get() / (double) uniqueCards.size()) *
                                        (System.nanoTime() - startTime) / 1000000000d / cardNumber.get() * uniqueCards.size() +
                                "s left estimated");
                return null;
            });
        }

        // Run starting hands
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(threads);
            executorService.invokeAll(callables);
            executorService.shutdown();
            System.out.println("Total Time: " + (System.nanoTime() - startTime) / 1000000000d + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (BetStatistics stat : stats) {
            stat.setTotalCombos(totalCombos.get());
        }
    }

}
