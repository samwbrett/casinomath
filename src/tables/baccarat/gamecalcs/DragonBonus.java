package tables.baccarat.gamecalcs;

import tables.baccarat.calcs.*;
import tables.cards.deck.Suit;
import tables.evals.BetEvaluator;
import tables.evals.BetStatistics;
import tables.evals.PaylineEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DragonBonus {

    public static void main(String[] args) {

        int decks = 8;
        List<BetEvaluator> evals = new ArrayList<>();

        evals.add(new BetEvaluator("Player Pair",

                new PaylineEvaluator("Pair", 12, (hands) ->
                                hands[0].getCards()[0].getRank() == hands[0].getCards()[1].getRank())));

        evals.add(new BetEvaluator("Banker Pair",

                new PaylineEvaluator("Pair", 12, (hands) ->
                        hands[0].getCards()[1].getRank() == hands[1].getCards()[1].getRank())));

        evals.add(new BetEvaluator("Player Dragon Bonus",

                new PaylineEvaluator("Natural Win", 2, (hands) ->
                        hands[0].getNumCards() == 2 &&
                                ((BaccHand)hands[0]).getValue() >= 8 &&
                                ((BaccHand)hands[0]).getValue() > ((BaccHand)hands[1]).getValue()),

                new PaylineEvaluator("Natural Tie", 1, (hands) ->
                        hands[0].getNumCards() == 2 &&
                                ((BaccHand)hands[0]).getValue() >= 8 &&
                                ((BaccHand)hands[0]).getValue() == ((BaccHand)hands[1]).getValue()),

                new PaylineEvaluator("Win by 9", 31, (hands) ->
                        ((BaccHand)hands[0]).getValue() - ((BaccHand)hands[1]).getValue() == 9),

                new PaylineEvaluator("Win by 8", 11, (hands) ->
                        ((BaccHand)hands[0]).getValue() - ((BaccHand)hands[1]).getValue() == 8),

                new PaylineEvaluator("Win by 7", 7, (hands) ->
                        ((BaccHand)hands[0]).getValue() - ((BaccHand)hands[1]).getValue() == 7),

                new PaylineEvaluator("Win by 6", 5, (hands) ->
                        ((BaccHand)hands[0]).getValue() - ((BaccHand)hands[1]).getValue() == 6),

                new PaylineEvaluator("Win by 5", 3, (hands) ->
                        ((BaccHand)hands[0]).getValue() - ((BaccHand)hands[1]).getValue() == 5),

                new PaylineEvaluator("Win by 4", 2, (hands) ->
                        ((BaccHand)hands[0]).getValue() - ((BaccHand)hands[1]).getValue() == 4)));

        evals.add(new BetEvaluator("Banker Dragon Bonus",

                new PaylineEvaluator("Natural Win", 2, (hands) ->
                        hands[1].getNumCards() == 2 &&
                                ((BaccHand)hands[1]).getValue() >= 8 &&
                                ((BaccHand)hands[1]).getValue() > ((BaccHand)hands[0]).getValue()),

                new PaylineEvaluator("Natural Tie", 1, (hands) ->
                        hands[1].getNumCards() == 2 &&
                                ((BaccHand)hands[1]).getValue() >= 8 &&
                                ((BaccHand)hands[1]).getValue() == ((BaccHand)hands[0]).getValue()),

                new PaylineEvaluator("Win by 9", 31, (hands) ->
                        ((BaccHand)hands[1]).getValue() - ((BaccHand)hands[0]).getValue() == 9),

                new PaylineEvaluator("Win by 8", 11, (hands) ->
                        ((BaccHand)hands[1]).getValue() - ((BaccHand)hands[0]).getValue() == 8),

                new PaylineEvaluator("Win by 7", 7, (hands) ->
                        ((BaccHand)hands[1]).getValue() - ((BaccHand)hands[0]).getValue() == 7),

                new PaylineEvaluator("Win by 6", 5, (hands) ->
                        ((BaccHand)hands[1]).getValue() - ((BaccHand)hands[0]).getValue() == 6),

                new PaylineEvaluator("Win by 5", 3, (hands) ->
                        ((BaccHand)hands[1]).getValue() - ((BaccHand)hands[0]).getValue() == 5),

                new PaylineEvaluator("Win by 4", 2, (hands) ->
                        ((BaccHand)hands[1]).getValue() - ((BaccHand)hands[0]).getValue() == 4)));


        BaccaratEnumerator<BaccRank, Suit, BaccHand<BaccRank,Suit>> enumerator = new BaccaratEnumerator<>(new SixCardDrawRule(), new BaccSingleSuitEnumShoe(decks), evals.toArray(BetEvaluator[]::new));
        enumerator.run(3);
        System.out.println("\n\n" + Arrays.stream(enumerator.getStats()).map(BetStatistics::toString).collect(Collectors.joining("\n\n")));
    }

}
