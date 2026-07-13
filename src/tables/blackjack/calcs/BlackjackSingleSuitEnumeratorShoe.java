package tables.blackjack.calcs;

import tables.cards.deck.StandardSuit;

import java.util.List;

/**
 * A blackjack enumerator shoe that uses a single suit only.
 * <p>
 * Since many blackjack outcomes depend only on rank values (not suits), using
 * a single suit reduces combinatorial state space while preserving correct
 * probability results. The number of decks is multiplied by the number of suits
 * to maintain accurate card counts.
 * <p>
 * This is the preferred shoe for blackjack enumeration when suit does not matter.
 */
public class BlackjackSingleSuitEnumeratorShoe extends BlackjackEnumeratorShoe {

    /**
     * Constructs a single-suit blackjack shoe. The number of decks is automatically
     * multiplied by 4 (for the four suits) to preserve total card counts.
     *
     * @param numDecks the number of physical decks to simulate
     */
    public BlackjackSingleSuitEnumeratorShoe(int numDecks) {
        super(numDecks * StandardSuit.values().length, List.of(BlackjackRank.values()), List.of(StandardSuit.SPADE));
    }

    protected BlackjackSingleSuitEnumeratorShoe(BlackjackEnumeratorShoe shoe) {
        super(shoe);
    }

    @Override
    public BlackjackSingleSuitEnumeratorShoe copyShoe() {
        return new BlackjackSingleSuitEnumeratorShoe(this);
    }
}

