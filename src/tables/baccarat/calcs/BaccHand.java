package tables.baccarat.calcs;

import tables.cards.deck.Card;
import tables.cards.deck.Suit;
import tables.evals.Hand;

import java.util.Arrays;

public class BaccHand<R extends BaccRank,S extends Suit> extends Hand {

    protected final int value;

    public BaccHand(Card<R,S>... cards) {
        super(cards);
        this.value = Arrays.stream(cards).mapToInt(c -> ((BaccRank)c.getRank()).getValue()).reduce(0, (a,b) -> (a+b)%10);
    }

    public Card<R,S>[] getCards() {
        return super.getCards();
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

}
