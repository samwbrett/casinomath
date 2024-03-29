package tables.baccarat.calcs;

import tables.cards.deck.Card;
import tables.evals.Hand;

import java.util.Arrays;

/**
 * Baccarat hand with basic checks for different hand types
 */
public class BaccaratHand extends Hand {

    protected final int value;

    public BaccaratHand(Card... cards) {
        super(cards);
        this.value = Arrays.stream(cards).mapToInt(c -> c.getRank().getValue()).reduce(0, (a, b) -> (a+b)%10);
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
