package tables.baccarat.calcs;

import org.junit.jupiter.api.Test;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import static org.junit.jupiter.api.Assertions.*;

class BaccaratSingleSuitEnumeratorShoeTest {

    @Test
    void singleSuitShoeHasCorrectTotalCards() {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(6);
        // 6 decks * 4 suits * 13 ranks = 312 cards (all as spades)
        assertEquals(312, shoe.getTotalCards());
    }

    @Test
    void singleSuitShoeHasCorrectDeckCount() {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(6);
        Card ace = new Card(BaccaratRank.ACE, StandardSuit.SPADE);
        // 6 decks * 4 suits = 24 copies of each rank
        assertEquals(24, shoe.getCount(ace));
    }

    @Test
    void copyShoeReturnsCorrectType() {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(8);
        BaccaratSingleSuitEnumeratorShoe copy = shoe.copyShoe();
        assertEquals(shoe.getTotalCards(), copy.getTotalCards());
    }
}
