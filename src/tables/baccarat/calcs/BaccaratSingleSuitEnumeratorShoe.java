package tables.baccarat.calcs;

import tables.cards.deck.StandardSuit;
import tables.cards.deck.Suit;

/**
 * Single suit baccarat enumerator shoe. Preferred speedwise
 */
public class BaccaratSingleSuitEnumeratorShoe extends BaccaratEnumeratorShoe {

    public BaccaratSingleSuitEnumeratorShoe(int numDecks) {
        super(numDecks * StandardSuit.values().length, BaccaratRank.values(), new Suit[]{StandardSuit.SPADE});
    }

    protected BaccaratSingleSuitEnumeratorShoe(BaccaratEnumeratorShoe shoe) {
        super(shoe);
    }

    @Override
    public BaccaratSingleSuitEnumeratorShoe clone() {
        return new BaccaratSingleSuitEnumeratorShoe(this);
    }
}
