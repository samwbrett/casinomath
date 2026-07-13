package tables.blackjack.calcs;

import org.junit.jupiter.api.Test;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import static org.junit.jupiter.api.Assertions.*;

class BlackjackSingleSuitEnumeratorShoeTest {

    @Test
    void sixDeckShoeHasCorrectCount() {
        // 6 decks * 4 suits = 24 copies per rank (all spades)
        BlackjackSingleSuitEnumeratorShoe shoe = new BlackjackSingleSuitEnumeratorShoe(6);
        Card ace = new Card(BlackjackRank.ACE, StandardSuit.SPADE);
        assertEquals(24, shoe.getCount(ace));
        // 13 ranks * 24 copies each = 312
        assertEquals(312, shoe.getTotalCards());
    }

    @Test
    void copyShoeWorks() {
        BlackjackSingleSuitEnumeratorShoe shoe = new BlackjackSingleSuitEnumeratorShoe(8);
        BlackjackSingleSuitEnumeratorShoe copy = shoe.copyShoe();
        assertEquals(shoe.getTotalCards(), copy.getTotalCards());
        assertInstanceOf(BlackjackSingleSuitEnumeratorShoe.class, copy);
    }
}
