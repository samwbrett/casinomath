package tables.baccarat.calcs;

import tables.cards.deck.Card;
import tables.cards.deck.Suit;
import tables.evals.Hand;

import java.util.Arrays;

/**
 * Baccarat hand with basic checks for different hand types
 */
public class BaccaratHand<R extends BaccaratRank,S extends Suit> extends Hand<R, S> {

    protected final int value;

    public BaccaratHand(Card<R,S>... cards) {
        super(cards);
        this.value = Arrays.stream(cards).mapToInt(c -> ((BaccaratRank)c.getRank()).getValue()).reduce(0, (a, b) -> (a+b)%10);
    }

    public int getValue() {
        return value;
    }

    public boolean isNaturalNine() {
        return getNumCards() == 2 && value == 9;
    }

    public boolean isNaturalEight() {
        return getNumCards() == 2 && value == 8;
    }

    public boolean isNatural() {
        return getNumCards() == 2 && value > 7;
    }

    public boolean isFirstTwoPaired() {
        return cards.get(0).getRank() == cards.get(1).getRank();
    }

}
