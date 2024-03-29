package tables.baccarat.calcs;

import tables.cards.deck.EnumeratorShoe;
import tables.cards.deck.StandardSuit;
import tables.cards.deck.Suit;

import java.util.List;

/**
 * Enumerator shoe for baccarat with all standard suits
 */
public class BaccaratEnumeratorShoe extends EnumeratorShoe {

    public BaccaratEnumeratorShoe(int numDecks) {
        super(numDecks, List.of(BaccaratRank.values()), List.of(StandardSuit.values()));
    }

    protected BaccaratEnumeratorShoe(BaccaratEnumeratorShoe shoe) {
        super(shoe);
    }

    protected BaccaratEnumeratorShoe(int numDecks, List<BaccaratRank> ranks, List<Suit> suits) {
        super(numDecks, ranks, suits);
    }

    public BaccaratEnumeratorShoe copyShoe() {
        return new BaccaratEnumeratorShoe(this);
    }

}
