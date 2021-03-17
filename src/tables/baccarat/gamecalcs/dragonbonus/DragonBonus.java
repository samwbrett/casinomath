package tables.baccarat.gamecalcs.dragonbonus;

import tables.baccarat.calcs.*;
import tables.evals.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DragonBonus {

    private static final List<BetEvaluator> EVALS = new ArrayList<>();
    static {
        // Simple evaluator logic for easy modularity
        EVALS.add(new BetEvaluator("Banker",
                new PaylineEvaluator("Banker Tie", 1, (hands) ->
                        ((BaccaratHand)hands[1]).getValue() == ((BaccaratHand)hands[0]).getValue()),
                new PaylineEvaluator("Banker Win", 1.95, (hands) ->
                        ((BaccaratHand)hands[1]).getValue() > ((BaccaratHand)hands[0]).getValue())));

        EVALS.add(new BetEvaluator("Player",
                new PaylineEvaluator("Player Tie", 1, (hands) ->
                        ((BaccaratHand)hands[0]).getValue() == ((BaccaratHand)hands[1]).getValue()),
                new PaylineEvaluator("Player Win", 2, (hands) ->
                        ((BaccaratHand)hands[0]).getValue() > ((BaccaratHand)hands[1]).getValue())));

        EVALS.add(new BetEvaluator("Player Pair",

                new PaylineEvaluator("Pair", 12, (hands) -> ((BaccaratHand)hands[0]).isFirstTwoPaired())));

        EVALS.add(new BetEvaluator("Banker Pair",

                new PaylineEvaluator("Pair", 12, (hands) -> ((BaccaratHand)hands[1]).isFirstTwoPaired())));

        EVALS.add(new BetEvaluator("Player Dragon Bonus",

                new PaylineEvaluator("Natural Win", 2, (hands) ->
                        ((BaccaratHand)hands[0]).isNatural() &&
                                ((BaccaratHand)hands[0]).getValue() > ((BaccaratHand)hands[1]).getValue()),

                new PaylineEvaluator("Natural Tie", 1, (hands) ->
                        ((BaccaratHand)hands[0]).isNatural() &&
                                ((BaccaratHand)hands[0]).getValue() == ((BaccaratHand)hands[1]).getValue()),

                new PaylineEvaluator("Win by 9", 31, (hands) ->
                        ((BaccaratHand)hands[0]).getValue() - ((BaccaratHand)hands[1]).getValue() == 9),

                new PaylineEvaluator("Win by 8", 11, (hands) ->
                        ((BaccaratHand)hands[0]).getValue() - ((BaccaratHand)hands[1]).getValue() == 8),

                new PaylineEvaluator("Win by 7", 7, (hands) ->
                        ((BaccaratHand)hands[0]).getValue() - ((BaccaratHand)hands[1]).getValue() == 7),

                new PaylineEvaluator("Win by 6", 5, (hands) ->
                        ((BaccaratHand)hands[0]).getValue() - ((BaccaratHand)hands[1]).getValue() == 6),

                new PaylineEvaluator("Win by 5", 3, (hands) ->
                        ((BaccaratHand)hands[0]).getValue() - ((BaccaratHand)hands[1]).getValue() == 5),

                new PaylineEvaluator("Win by 4", 2, (hands) ->
                        ((BaccaratHand)hands[0]).getValue() - ((BaccaratHand)hands[1]).getValue() == 4)));

        EVALS.add(new BetEvaluator("Banker Dragon Bonus",

                new PaylineEvaluator("Natural Win", 2, (hands) ->
                        ((BaccaratHand)hands[1]).isNatural() &&
                                ((BaccaratHand)hands[1]).getValue() > ((BaccaratHand)hands[0]).getValue()),

                new PaylineEvaluator("Natural Tie", 1, (hands) ->
                        ((BaccaratHand)hands[1]).isNatural() &&
                                ((BaccaratHand)hands[1]).getValue() == ((BaccaratHand)hands[0]).getValue()),

                new PaylineEvaluator("Win by 9", 31, (hands) ->
                        ((BaccaratHand)hands[1]).getValue() - ((BaccaratHand)hands[0]).getValue() == 9),

                new PaylineEvaluator("Win by 8", 11, (hands) ->
                        ((BaccaratHand)hands[1]).getValue() - ((BaccaratHand)hands[0]).getValue() == 8),

                new PaylineEvaluator("Win by 7", 7, (hands) ->
                        ((BaccaratHand)hands[1]).getValue() - ((BaccaratHand)hands[0]).getValue() == 7),

                new PaylineEvaluator("Win by 6", 5, (hands) ->
                        ((BaccaratHand)hands[1]).getValue() - ((BaccaratHand)hands[0]).getValue() == 6),

                new PaylineEvaluator("Win by 5", 3, (hands) ->
                        ((BaccaratHand)hands[1]).getValue() - ((BaccaratHand)hands[0]).getValue() == 5),

                new PaylineEvaluator("Win by 4", 2, (hands) ->
                        ((BaccaratHand)hands[1]).getValue() - ((BaccaratHand)hands[0]).getValue() == 4)));
    }

    private final int decks;
    private final int threads;
    private final BaccaratEnumerator enumerator;

    public DragonBonus(int decks, int threads) {
        this.decks = decks;
        this.threads = threads;
        enumerator = new BaccaratEnumerator<>(new SixCardDrawRule(), new BaccaratSingleSuitEnumeratorShoe(decks), EVALS.toArray(BetEvaluator[]::new));
    }

    public void run() throws InterruptedException {
        enumerator.run(threads);
    }

    public BetStatistics[] getStats() {
        return enumerator.getStats();
    }

    public static void main(String[] args) {

        DragonBonus dragonBonus = new DragonBonus(8, 8);
        try {
            long start = System.currentTimeMillis();

            dragonBonus.run();
            System.out.println("\n\n" + Arrays.stream(dragonBonus.getStats()).map(BetStatistics::toString).collect(Collectors.joining("\n\n")));
            System.out.println("Decks: " + dragonBonus.decks);
            System.out.println("\nTook " + (System.currentTimeMillis() - start) / 1000 + " seconds.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
