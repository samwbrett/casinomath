package tables.baccarat.calcs;

import tables.cards.deck.Card;
import tables.evals.Hand;

import java.util.Arrays;
import java.util.List;

/**
 * A baccarat hand that computes the hand value (mod 10) from the constituent cards.
 * <p>
 * Provides convenience methods for checking natural hands (8 or 9 on two cards)
 * and whether the first two cards are paired (same rank).
 */
public class BaccaratHand extends Hand {

    private final int value;

    /**
     * Constructs a baccarat hand from the given cards and computes its value.
     *
     * @param cards the cards in the hand
     */
    public BaccaratHand(Card... cards) {
        super(cards);
        this.value = computeValue(this.cards);
    }

    /**
     * Constructs a baccarat hand from a list of cards.
     *
     * @param cards the cards in the hand
     */
    public BaccaratHand(List<Card> cards) {
        super(cards);
        this.value = computeValue(this.cards);
    }

    private static int computeValue(List<Card> cards) {
        return cards.stream()
                .mapToInt(c -> c.getRank().getValue())
                .reduce(0, (a, b) -> (a + b) % 10);
    }

    /** Returns the baccarat hand value (0-9). */
    public int getValue() {
        return value;
    }

    /** Returns true if this is a two-card hand valued at 9 (natural nine). */
    public boolean isNaturalNine() {
        return getNumCards() == 2 && value == 9;
    }

    /** Returns true if this is a two-card hand valued at 8 (natural eight). */
    public boolean isNaturalEight() {
        return getNumCards() == 2 && value == 8;
    }

    /** Returns true if this is a two-card hand valued at 8 or 9 (natural). */
    public boolean isNatural() {
        return getNumCards() == 2 && value > 7;
    }

    /** Returns true if the first two cards have the same rank (a pair). */
    public boolean isFirstTwoPaired() {
        return getNumCards() >= 2 && cards.get(0).getRank() == cards.get(1).getRank();
    }
}

