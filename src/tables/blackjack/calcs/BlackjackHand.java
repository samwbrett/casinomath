package tables.blackjack.calcs;

import tables.cards.deck.Card;
import tables.evals.Hand;

import java.util.List;

/**
 * Blackjack hand with ability to get the blackjack hand value
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

    private void updateValue() {
        if (!knownValue) {
            int aces = 0;
            int value = 0;
            for (Card card : cards) {
                if (card.getRank().getValue() == 11) {
                    aces++;
                }
                value += card.getRank().getValue();
            }
            while (aces != 0 && value > 21) {
                value -= 10;
                aces--;
            }
            soft = aces != 0;

            this.value = value;
            knownValue = true;
        }
    }

    public int getValue() {
        updateValue();
        return value;
    }

    public boolean isSoft() {
        updateValue();
        return soft;
    }

    public boolean isBlackjack() {
        if (cards.size() != 2) {
            return false;
        }
        updateValue();
        return soft && value == 21;
    }

    public void addCard(Card card) {
        super.addCard(card);
        knownValue = false;
    }

    public void removeCard(Card card) {
        super.removeCard(card);
        knownValue = false;
    }

}
