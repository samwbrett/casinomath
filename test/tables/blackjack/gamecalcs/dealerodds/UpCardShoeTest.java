package tables.blackjack.gamecalcs.dealerodds;

import org.junit.jupiter.api.Test;
import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.blackjack.calcs.BlackjackRank;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import static org.junit.jupiter.api.Assertions.*;

class UpCardShoeTest {

    @Test
    void equalsSameObject() {
        BlackjackEnumeratorShoe shoe = new BlackjackEnumeratorShoe(1);
        Card upCard = new Card(BlackjackRank.ACE, StandardSuit.SPADE);
        UpCardShoe key = new UpCardShoe(upCard, shoe);
        assertEquals(key, key);
    }

    @Test
    void equalsSameValues() {
        BlackjackEnumeratorShoe shoe1 = new BlackjackEnumeratorShoe(1);
        BlackjackEnumeratorShoe shoe2 = new BlackjackEnumeratorShoe(1);
        Card upCard = new Card(BlackjackRank.ACE, StandardSuit.SPADE);
        UpCardShoe key1 = new UpCardShoe(upCard, shoe1);
        UpCardShoe key2 = new UpCardShoe(upCard, shoe2);
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    void notEqualWithDifferentUpCard() {
        BlackjackEnumeratorShoe shoe = new BlackjackEnumeratorShoe(1);
        UpCardShoe key1 = new UpCardShoe(new Card(BlackjackRank.ACE, StandardSuit.SPADE), shoe);
        UpCardShoe key2 = new UpCardShoe(new Card(BlackjackRank.KING, StandardSuit.SPADE), shoe);
        assertNotEquals(key1, key2);
    }

    @Test
    void notEqualWithDifferentShoe() {
        Card upCard = new Card(BlackjackRank.ACE, StandardSuit.SPADE);
        BlackjackEnumeratorShoe shoe1 = new BlackjackEnumeratorShoe(1);
        BlackjackEnumeratorShoe shoe2 = new BlackjackEnumeratorShoe(6);
        UpCardShoe key1 = new UpCardShoe(upCard, shoe1);
        UpCardShoe key2 = new UpCardShoe(upCard, shoe2);
        assertNotEquals(key1, key2);
    }
}
