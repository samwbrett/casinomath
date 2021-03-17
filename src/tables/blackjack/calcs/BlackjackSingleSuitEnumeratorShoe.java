package tables.blackjack.calcs;

import tables.cards.deck.StandardSuit;
import tables.cards.deck.Suit;

/**
 * Blackjack enumerator shoe for one suit used when suit does not affect outcomes. Preferred speedwise.
 */
public class BlackjackSingleSuitEnumeratorShoe extends BlackjackEnumeratorShoe {

    public BlackjackSingleSuitEnumeratorShoe(int numDecks) {
        super(numDecks * StandardSuit.values().length, BlackjackRank.values(), new Suit[]{StandardSuit.SPADE});
    }

    protected BlackjackSingleSuitEnumeratorShoe(BlackjackEnumeratorShoe shoe) {
        super(shoe);
    }

    @Override
    public BlackjackSingleSuitEnumeratorShoe clone() {
        return new BlackjackSingleSuitEnumeratorShoe(this);
    }
}
