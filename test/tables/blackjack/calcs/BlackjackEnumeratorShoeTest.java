package tables.blackjack.calcs;

import org.junit.jupiter.api.Test;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BlackjackEnumeratorShoeTest {

    @Test
    void testGetDealerDownProbsOneDeckNoPeek() {
        // 1 deck: count of each card = 1
        BlackjackEnumeratorShoe shoe = new BlackjackEnumeratorShoe(1);
        Card upCard = new Card(BlackjackRank.ACE, StandardSuit.SPADE);
        Map<Card, Double> probs = shoe.dealerDownProbs(upCard, false);

        // dealerDownProbs does not modify the shoe - total should still be 52
        assertEquals(52, shoe.getTotalCards());

        // Each remaining card has probability 1/52 (no card is removed from shoe)
        Card twoSpade = new Card(BlackjackRank.TWO, StandardSuit.SPADE);
        assertTrue(probs.containsKey(twoSpade));
        assertEquals(1.0 / 52, probs.get(twoSpade), 1e-12);

        // Sum of probabilities should be 1.0
        double sum = probs.values().stream().mapToDouble(Double::doubleValue).sum();
        assertEquals(1.0, sum, 1e-12);
    }

    @Test
    void testGetDealerDownProbsOneDeckPeek() {
        BlackjackEnumeratorShoe shoe = new BlackjackEnumeratorShoe(1);
        Card upCardAce = new Card(BlackjackRank.ACE, StandardSuit.SPADE);
        Map<Card, Double> probs = shoe.dealerDownProbs(upCardAce, true);

        // When peeking with ace up, ten-value cards are removed from consideration
        Card tenSpade = new Card(BlackjackRank.TEN, StandardSuit.SPADE);
        Card kingClub = new Card(BlackjackRank.KING, StandardSuit.CLUB);
        assertFalse(probs.containsKey(tenSpade));
        assertFalse(probs.containsKey(kingClub));

        // Non-ten cards should still be present
        Card twoSpade = new Card(BlackjackRank.TWO, StandardSuit.SPADE);
        assertTrue(probs.containsKey(twoSpade));
        assertTrue(probs.get(twoSpade) > 0);
    }

    @Test
    void testCopyShoe() {
        BlackjackEnumeratorShoe shoe = new BlackjackEnumeratorShoe(6);
        BlackjackEnumeratorShoe copy = shoe.copyShoe();
        assertEquals(shoe, copy);
        assertEquals(shoe.getTotalCards(), copy.getTotalCards());

        copy.removeCard(new Card(BlackjackRank.ACE, StandardSuit.SPADE));
        assertNotEquals(shoe.getTotalCards(), copy.getTotalCards());
    }
}
