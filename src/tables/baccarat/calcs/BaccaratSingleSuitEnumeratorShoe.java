package tables.baccarat.calcs;

import tables.cards.deck.StandardSuit;

import java.util.List;

/**
 * Single suit baccarat enumerator shoe. Preferred speedwise
 */
public class BaccaratSingleSuitEnumeratorShoe extends BaccaratEnumeratorShoe {

    public BaccaratSingleSuitEnumeratorShoe(int numDecks) {
        super(numDecks * StandardSuit.values().length, BaccaratRank.ACE.getRanks(), List.of(StandardSuit.SPADE));
    }

    protected BaccaratSingleSuitEnumeratorShoe(BaccaratEnumeratorShoe shoe) {
        super(shoe);
    }

    public BaccaratSingleSuitEnumeratorShoe copyShoe() {
        return new BaccaratSingleSuitEnumeratorShoe(this);
    }
}
