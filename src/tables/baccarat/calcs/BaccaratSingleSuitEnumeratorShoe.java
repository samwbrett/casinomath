package tables.baccarat.calcs;

import tables.cards.deck.StandardSuit;

import java.util.List;

/**
 * A baccarat enumerator shoe that uses a single suit only.
 * <p>
 * Since baccarat outcomes depend only on rank values (not suits), using a single
 * suit dramatically reduces combinatorial state space while producing identical
 * probability results. The number of decks is multiplied by the number of suits
 * to preserve the correct card counts.
 * <p>
 * This is the preferred shoe for baccarat enumeration due to its performance benefits.
 */
public class BaccaratSingleSuitEnumeratorShoe extends BaccaratEnumeratorShoe {

    /**
     * Constructs a single-suit baccarat shoe. The number of decks is automatically
     * multiplied by 4 (for the four suits) to preserve total card counts.
     *
     * @param numDecks the number of physical decks to simulate
     */
    public BaccaratSingleSuitEnumeratorShoe(int numDecks) {
        super(numDecks * StandardSuit.values().length, BaccaratRank.ACE.getRanks(), List.of(StandardSuit.SPADE));
    }

    protected BaccaratSingleSuitEnumeratorShoe(BaccaratEnumeratorShoe shoe) {
        super(shoe);
    }

    @Override
    public BaccaratSingleSuitEnumeratorShoe copyShoe() {
        return new BaccaratSingleSuitEnumeratorShoe(this);
    }
}

