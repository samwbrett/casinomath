package tables.baccarat.calcs;

import tables.cards.deck.EnumeratorShoe;
import tables.cards.deck.StandardSuit;
import tables.cards.deck.Suit;

/**
 * Enumerator shoe for baccarat with all standard suits
 */
public class BaccaratEnumeratorShoe extends EnumeratorShoe<BaccaratRank, Suit> {

    public BaccaratEnumeratorShoe(int numDecks) {
        super(numDecks, BaccaratRank.values(), StandardSuit.values());
    }

    protected BaccaratEnumeratorShoe(BaccaratEnumeratorShoe shoe) {
        super(shoe);
    }

    protected BaccaratEnumeratorShoe(int numDecks, BaccaratRank[] ranks, Suit[] suits) {
        super(numDecks, ranks, suits);
    }

    @Override
    public BaccaratEnumeratorShoe clone() {
        return new BaccaratEnumeratorShoe(this);
    }

}
