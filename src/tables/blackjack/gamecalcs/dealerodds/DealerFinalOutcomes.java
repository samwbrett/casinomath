package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.*;
import tables.cards.deck.*;

import java.util.*;
import java.util.concurrent.*;

public class DealerFinalOutcomes {

    private static final Outcomes DEALER_FINAL = new Outcomes();
    static {
        // Generic for ordered options and easy modularity, it is easy to input but for simple cases such as shown iteration is slower
        DEALER_FINAL.addOutcome("BJ", (h) -> ((BlackjackHand)h[0]).isBlackjack());
        for (int value = 17; value != 27; value++) {
            DEALER_FINAL.addOutcome(Integer.toString(value), new ValueHitEvaluator(value));
        }
    }

    private final int decks;
    private final int threads;
    private final boolean hitSoft17;
    private final List<Outcomes> finalOutcomes = new CopyOnWriteArrayList<>();
    private final List<Callable<Void>> callables = new ArrayList<>();

    public DealerFinalOutcomes(int decks, boolean hitSoft17, int threads) {

        this.decks = decks;
        this.threads = threads;
        this.hitSoft17 = hitSoft17;

        BlackjackEnumeratorShoe shoe = new BlackjackSingleSuitEnumeratorShoe(decks);
        DealerOdds dealerOdds = new DealerOdds(false, hitSoft17, DEALER_FINAL);
        for (BlackjackRank rank : BlackjackRank.values()) {
            callables.add(() -> {
                BlackjackEnumeratorShoe copyShoe = shoe.copyShoe();
                copyShoe.removeCard(new Card(rank, StandardSuit.SPADE));
                Outcomes finalOutcome = dealerOdds.chanceDealerEnd(new Card(rank, StandardSuit.SPADE), copyShoe);
                finalOutcomes.add(finalOutcome);
                return null;
            });
        }
    }

    public void run() throws InterruptedException {
        try (ExecutorService executorService = new ForkJoinPool(threads)) {
            executorService.invokeAll(callables);
            executorService.shutdown();
        }
    }

    private List<Outcomes> getFinalOutcomes() {
        return finalOutcomes;
    }

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
            e.printStackTrace();
        }
    }

}
