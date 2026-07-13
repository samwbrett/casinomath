package tables.blackjack.calcs;

import tables.cards.deck.Card;
import tables.evals.Hand;

import java.util.List;

/**
 * A blackjack hand that computes the hand value with proper ace handling.
 * <p>
 * Aces are initially counted as 11. If the total exceeds 21, aces are
 * converted from 11 to 1 (subtracting 10) until the hand is 21 or under,
 * or all aces have been reduced.
 * <p>
 * The hand value is lazily computed and cached. It is invalidated when cards
 * are added or removed.
 */
public class BlackjackHand extends Hand {

    private boolean knownValue;
    private boolean soft;
    private int value;

    public BlackjackHand(Card... cards) {
        super(cards);
    }

    public BlackjackHand(List<Card> cards) {
        super(cards);
    }

    /**
     * Computes the hand value if it has not been computed yet or has been
     * invalidated by card changes.
     */
    private void computeValue() {
        if (!knownValue) {
            int aces = 0;
            int total = 0;
            for (Card card : cards) {
                if (card.getRank().getValue() == 11) {
                    aces++;
                }
                total += card.getRank().getValue();
            }
            while (aces > 0 && total > 21) {
                total -= 10;
                aces--;
            }
            soft = aces > 0;
            this.value = total;
            knownValue = true;
        }
    }

    /** Returns the blackjack hand value (4-21). */
    public int getValue() {
        computeValue();
        return value;
    }

    /** Returns true if the hand is soft (contains a counted-as-11 ace). */
    public boolean isSoft() {
        computeValue();
        return soft;
    }

    /**
     * Returns true if this is a two-card 21 (blackjack).
     * A blackjack must have exactly 2 cards, be soft (contain an ace),
     * and total exactly 21.
     */
    public boolean isBlackjack() {
        if (cards.size() != 2) {
            return false;
        }
        computeValue();
        return soft && value == 21;
    }

    @Override
    public void addCard(Card card) {
        super.addCard(card);
        knownValue = false;
    }

    @Override
    public boolean removeCard(Card card) {
        boolean removed = super.removeCard(card);
        if (removed) {
            knownValue = false;
        }
        return removed;
    }
}

