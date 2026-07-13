package tables.baccarat.calcs;

import tables.cards.deck.EnumeratorShoe;
import tables.cards.deck.StandardSuit;
import tables.cards.deck.Suit;

import java.util.List;

/**
 * A baccarat-specific enumerator shoe that uses all four standard suits
 * and baccarat ranks.
 * <p>
 * For performance, prefer {@link BaccaratSingleSuitEnumeratorShoe} when
 * suit does not affect the outcome, as it reduces the state space.
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

    @Override
    public BaccaratEnumeratorShoe copyShoe() {
        return new BaccaratEnumeratorShoe(this);
    }
}

