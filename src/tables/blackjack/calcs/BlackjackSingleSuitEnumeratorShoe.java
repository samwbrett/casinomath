package tables.blackjack.calcs;

import tables.cards.deck.StandardSuit;

import java.util.List;

/**
 * Blackjack enumerator shoe for one suit used when suit does not affect outcomes. Preferred speedwise.
 */
public class BlackjackSingleSuitEnumeratorShoe extends BlackjackEnumeratorShoe {

    public BlackjackSingleSuitEnumeratorShoe(int numDecks) {
        super(numDecks * StandardSuit.values().length, List.of(BlackjackRank.values()), List.of(StandardSuit.SPADE));
    }

    protected BlackjackSingleSuitEnumeratorShoe(BlackjackEnumeratorShoe shoe) {
        super(shoe);
    }

    public BlackjackSingleSuitEnumeratorShoe copyShoe() {
        return new BlackjackSingleSuitEnumeratorShoe(this);
    }
}
