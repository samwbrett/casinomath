package tables.baccarat.calcs;

import org.junit.jupiter.api.Test;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import static org.junit.jupiter.api.Assertions.*;

class BaccaratEnumeratorShoeTest {

    @Test
    void shoeCreatedWithAllSuits() {
        BaccaratEnumeratorShoe shoe = new BaccaratEnumeratorShoe(1);
        // 13 ranks * 4 suits * 1 deck = 52 cards
        assertEquals(52, shoe.getTotalCards());
    }

    @Test
    void copyShoeReturnsCorrectType() {
        BaccaratEnumeratorShoe shoe = new BaccaratEnumeratorShoe(1);
        BaccaratEnumeratorShoe copy = shoe.copyShoe();
        assertEquals(shoe.getTotalCards(), copy.getTotalCards());
    }

    @Test
    void shoeContainsExpectedCards() {
        BaccaratEnumeratorShoe shoe = new BaccaratEnumeratorShoe(2);
        Card aceSpade = new Card(BaccaratRank.ACE, StandardSuit.SPADE);
        assertEquals(2, shoe.getCount(aceSpade));
    }
}
