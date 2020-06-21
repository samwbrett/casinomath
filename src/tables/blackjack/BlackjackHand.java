package tables.blackjack;

import tables.cards.deck.Card;
import tables.cards.deck.Suit;
import tables.evals.Hand;

public class BlackjackHand extends Hand<BlackjackRank, Suit> {

    private boolean knownValue;
    private boolean soft;
    private int value;

    public BlackjackHand(Card<BlackjackRank, Suit>... cards) {
        super(cards);
    }

    private void updateValue() {
        if (!knownValue) {
            int aces = 0;
            int value = 0;
            for (Card<BlackjackRank,Suit> card : cards) {
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

    public void addCard(Card<BlackjackRank,Suit> card) {
        super.addCard(card);
        knownValue = false;
    }

    public void removeCard(Card<BlackjackRank,Suit> card) {
        super.removeCard(card);
        knownValue = false;
    }

}
