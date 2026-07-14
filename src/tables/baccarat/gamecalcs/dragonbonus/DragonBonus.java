package tables.baccarat.gamecalcs.dragonbonus;

import tables.baccarat.calcs.BaccaratEnumerator;
import tables.baccarat.calcs.BaccaratHand;
import tables.baccarat.calcs.BaccaratSingleSuitEnumeratorShoe;
import tables.evals.BetEvaluator;
import tables.evals.BetStatistics;
import tables.evals.PaylineEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Computes exact statistics for the Dragon Bonus side bet and standard
 * baccarat bets (Banker, Player, Pair) using combinatorial enumeration.
 * <p>
 * The Dragon Bonus is a side bet on the margin of victory.
 * Payline values below are in "for one" format (total return including bet):
 * <ul>
 *   <li>Natural Win: forPay=2 (1 to 1)</li>
 *   <li>Natural Tie: forPay=1 (push)</li>
 *   <li>Win by 9: forPay=31 (30 to 1)</li>
 *   <li>Win by 8: forPay=11 (10 to 1)</li>
 *   <li>Win by 7: forPay=7 (6 to 1)</li>
 *   <li>Win by 6: forPay=5 (4 to 1)</li>
 *   <li>Win by 5: forPay=3 (2 to 1)</li>
 *   <li>Win by 4: forPay=2 (1 to 1)</li>
 * </ul>

 */
public class DragonBonus {

    private static final List<BetEvaluator> EVALS = new ArrayList<>();

    static {
        EVALS.add(new BetEvaluator("Banker",
                new PaylineEvaluator("Banker Tie", 1, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() == ((BaccaratHand) hands[0]).getValue()),
                new PaylineEvaluator("Banker Win", 1.95, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() > ((BaccaratHand) hands[0]).getValue())));

        EVALS.add(new BetEvaluator("Player",
                new PaylineEvaluator("Player Tie", 1, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() == ((BaccaratHand) hands[1]).getValue()),
                new PaylineEvaluator("Player Win", 2, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() > ((BaccaratHand) hands[1]).getValue())));

        EVALS.add(new BetEvaluator("Player Pair",
                new PaylineEvaluator("Pair", 12, (hands) ->
                        ((BaccaratHand) hands[0]).isFirstTwoPaired())));

        EVALS.add(new BetEvaluator("Banker Pair",
                new PaylineEvaluator("Pair", 12, (hands) ->
                        ((BaccaratHand) hands[1]).isFirstTwoPaired())));

        EVALS.add(new BetEvaluator("Player Dragon Bonus",
                new PaylineEvaluator("Natural Win", 2, (hands) ->
                        ((BaccaratHand) hands[0]).isNatural() &&
                                ((BaccaratHand) hands[0]).getValue() > ((BaccaratHand) hands[1]).getValue()),
                new PaylineEvaluator("Natural Tie", 1, (hands) ->
                        ((BaccaratHand) hands[0]).isNatural() &&
                                ((BaccaratHand) hands[0]).getValue() == ((BaccaratHand) hands[1]).getValue()),
                new PaylineEvaluator("Win by 9", 31, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() - ((BaccaratHand) hands[1]).getValue() == 9),
                new PaylineEvaluator("Win by 8", 11, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() - ((BaccaratHand) hands[1]).getValue() == 8),
                new PaylineEvaluator("Win by 7", 7, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() - ((BaccaratHand) hands[1]).getValue() == 7),
                new PaylineEvaluator("Win by 6", 5, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() - ((BaccaratHand) hands[1]).getValue() == 6),
                new PaylineEvaluator("Win by 5", 3, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() - ((BaccaratHand) hands[1]).getValue() == 5),
                new PaylineEvaluator("Win by 4", 2, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() - ((BaccaratHand) hands[1]).getValue() == 4)));

        EVALS.add(new BetEvaluator("Banker Dragon Bonus",
                new PaylineEvaluator("Natural Win", 2, (hands) ->
                        ((BaccaratHand) hands[1]).isNatural() &&
                                ((BaccaratHand) hands[1]).getValue() > ((BaccaratHand) hands[0]).getValue()),
                new PaylineEvaluator("Natural Tie", 1, (hands) ->
                        ((BaccaratHand) hands[1]).isNatural() &&
                                ((BaccaratHand) hands[1]).getValue() == ((BaccaratHand) hands[0]).getValue()),
                new PaylineEvaluator("Win by 9", 31, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() - ((BaccaratHand) hands[0]).getValue() == 9),
                new PaylineEvaluator("Win by 8", 11, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() - ((BaccaratHand) hands[0]).getValue() == 8),
                new PaylineEvaluator("Win by 7", 7, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() - ((BaccaratHand) hands[0]).getValue() == 7),
                new PaylineEvaluator("Win by 6", 5, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() - ((BaccaratHand) hands[0]).getValue() == 6),
                new PaylineEvaluator("Win by 5", 3, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() - ((BaccaratHand) hands[0]).getValue() == 5),
                new PaylineEvaluator("Win by 4", 2, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() - ((BaccaratHand) hands[0]).getValue() == 4)));
    }

    private final int decks;
    private final int threads;
    private final BaccaratEnumerator enumerator;

    /**
     * Constructs a Dragon Bonus calculator for the given number of decks and threads.
     *
     * @param decks   the number of decks in the shoe
     * @param threads the number of threads to use for parallel enumeration
     */
    public DragonBonus(int decks, int threads) {
        this.decks = decks;
        this.threads = threads;
        enumerator = new BaccaratEnumerator(
                new BaccaratSingleSuitEnumeratorShoe(decks),
                EVALS.toArray(BetEvaluator[]::new));
    }

    /** Runs the enumeration to compute statistics. */
    public void run() throws InterruptedException {
        enumerator.run(threads);
    }

    /** Returns the computed bet statistics. */
    public List<BetStatistics> getStats() {
        return enumerator.getStats();
    }

    /**
     * Runs the Dragon Bonus calculation for an 8-deck shoe using 8 threads
     * and prints the results to stdout.
     */
    public static void main(String[] args) {
        DragonBonus dragonBonus = new DragonBonus(8, 8);
        try {
            long start = System.currentTimeMillis();
            dragonBonus.run();
            System.out.println("\n\n" + dragonBonus.getStats().stream()
                    .map(BetStatistics::toString)
                    .collect(Collectors.joining("\n\n")));
            System.out.println("Decks: " + dragonBonus.decks);
            System.out.println("\nTook " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Computation was interrupted: " + e.getMessage());
        }
    }
}
