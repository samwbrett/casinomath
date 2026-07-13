package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.blackjack.calcs.BlackjackHand;
import tables.blackjack.calcs.BlackjackRank;
import tables.blackjack.calcs.BlackjackSingleSuitEnumeratorShoe;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Computes the probability distribution of a dealer's final hand outcome
 * for each possible up card in blackjack.
 * <p>
 * For each rank (2 through Ace), this creates a task that removes one card
 * of that rank from the shoe (as the up card) and computes the dealer's
 * outcome distribution given that up card. Results are then averaged to
 * produce the overall dealer outcome probabilities.
 */
public class DealerFinalOutcomes {

    private static final Outcomes DEALER_FINAL = new Outcomes();

    static {
        DEALER_FINAL.addOutcome("BJ", (h) -> ((BlackjackHand) h[0]).isBlackjack());
        for (int value = 17; value < 27; value++) {
            DEALER_FINAL.addOutcome(Integer.toString(value), new ValueHitEvaluator(value));
        }
    }

    private final int decks;
    private final int threads;
    private final boolean hitSoft17;
    private final List<Outcomes> finalOutcomes = new CopyOnWriteArrayList<>();
    private final List<Callable<Void>> callables = new ArrayList<>();

    /**
     * Constructs the calculator, creating tasks for each possible up card rank.
     *
     * @param decks     the number of decks in the shoe
     * @param hitSoft17 whether the dealer hits on soft 17
     * @param threads   the number of threads to use
     */
    public DealerFinalOutcomes(int decks, boolean hitSoft17, int threads) {
        this.decks = decks;
        this.threads = threads;
        this.hitSoft17 = hitSoft17;

        BlackjackEnumeratorShoe shoe = new BlackjackSingleSuitEnumeratorShoe(decks);
        DealerOdds dealerOdds = new DealerOdds(false, hitSoft17, DEALER_FINAL);
        for (BlackjackRank rank : BlackjackRank.values()) {
            callables.add(() -> {
                BlackjackEnumeratorShoe copyShoe = shoe.copyShoe();
                Card upCard = new Card(rank, StandardSuit.SPADE);
                copyShoe.removeCard(upCard);
                Outcomes finalOutcome = dealerOdds.chanceDealerEnd(upCard, copyShoe);
                finalOutcomes.add(finalOutcome);
                return null;
            });
        }
    }

    /** Runs the computation for all up cards. */
    public void run() throws InterruptedException {
        try (ExecutorService executorService = new ForkJoinPool(threads)) {
            executorService.invokeAll(callables);
            executorService.shutdown();
        }
    }

    /**
     * Returns the per-up-card outcome distributions.
     *
     * @return a list of outcome distributions, one per up card rank
     */
    public List<Outcomes> getFinalOutcomes() {
        return List.copyOf(finalOutcomes);
    }

    /**
     * Runs the dealer outcome calculation for 8 decks, H17, with 8 threads
     * and prints the averaged results to stdout.
     */
    public static void main(String[] args) {
        DealerFinalOutcomes dealerOutcomes = new DealerFinalOutcomes(8, true, 8);
        try {
            long start = System.currentTimeMillis();
            dealerOutcomes.run();
            List<Outcomes> finalOutcomes = dealerOutcomes.getFinalOutcomes();
            System.out.println("\n\nTotal\t" + Outcomes.average(finalOutcomes));
            System.out.println("Decks: " + dealerOutcomes.decks + ", Hit Soft 17: " + dealerOutcomes.hitSoft17);
            System.out.println("\nTook " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Computation interrupted: " + e.getMessage());
        }
    }
}
