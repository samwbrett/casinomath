package tables.blackjack.calcs;

import org.junit.jupiter.api.Test;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import static org.junit.jupiter.api.Assertions.*;

class BlackjackHandTest {

    private Card card(BlackjackRank rank) {
        return new Card(rank, StandardSuit.SPADE);
    }

    @Test
    void simpleHandValue() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.FIVE), card(BlackjackRank.THREE));
        assertEquals(8, hand.getValue());
        assertFalse(hand.isSoft());
    }

    @Test
    void aceCountsAsElevenInitially() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.ACE), card(BlackjackRank.SEVEN));
        assertEquals(18, hand.getValue());
        assertTrue(hand.isSoft());
    }

    @Test
    void aceConvertsToOneToAvoidBust() {
        // A(11) + 9 + 7 = 27 -> bust, ace converts: A(1) + 9 + 7 = 17
        BlackjackHand hand = new BlackjackHand(
                card(BlackjackRank.ACE), card(BlackjackRank.NINE), card(BlackjackRank.SEVEN));
        assertEquals(17, hand.getValue());
        assertFalse(hand.isSoft()); // ace used as 1
    }

    @Test
    void multipleAcesConverted() {
        // A(11) + A(11) + 9 = 31 -> convert: A(1) + A(11) + 9 = 21
        BlackjackHand hand = new BlackjackHand(
                card(BlackjackRank.ACE), card(BlackjackRank.ACE), card(BlackjackRank.NINE));
        assertEquals(21, hand.getValue());
        assertTrue(hand.isSoft());
    }

    @Test
    void twoAcesWithHighCard() {
        // A(11) + A(11) + 5 = 27 -> A(1) + A(11) + 5 = 17
        BlackjackHand hand = new BlackjackHand(
                card(BlackjackRank.ACE), card(BlackjackRank.ACE), card(BlackjackRank.FIVE));
        assertEquals(17, hand.getValue());
        // One ace is still counted as 11, so the hand is soft
        assertTrue(hand.isSoft());
    }

    @Test
    void isBlackjackWithAceTen() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.ACE), card(BlackjackRank.TEN));
        assertTrue(hand.isBlackjack());
    }

    @Test
    void isBlackjackWithAllTens() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.TEN), card(BlackjackRank.KING));
        assertFalse(hand.isBlackjack()); // not soft
        assertEquals(20, hand.getValue());
    }

    @Test
    void threeCardTwentyOneIsNotBlackjack() {
        BlackjackHand hand = new BlackjackHand(
                card(BlackjackRank.SEVEN), card(BlackjackRank.SEVEN), card(BlackjackRank.SEVEN));
        assertFalse(hand.isBlackjack());
        assertEquals(21, hand.getValue());
    }

    @Test
    void addCardInvalidatesCachedValue() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.ACE), card(BlackjackRank.SIX));
        assertEquals(17, hand.getValue());
        hand.addCard(card(BlackjackRank.FIVE));
        assertEquals(12, hand.getValue()); // A(1) + 6 + 5 = 12
    }

    @Test
    void removeCardInvalidatesCachedValue() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.ACE), card(BlackjackRank.SIX), card(BlackjackRank.FIVE));
        assertEquals(12, hand.getValue());
        hand.removeCard(card(BlackjackRank.FIVE));
        assertEquals(17, hand.getValue()); // A(11) + 6 = 17
    }

    @Test
    void bustHand() {
        BlackjackHand hand = new BlackjackHand(
                card(BlackjackRank.TEN), card(BlackjackRank.QUEEN), card(BlackjackRank.THREE));
        assertEquals(23, hand.getValue());
    }

    @Test
    void addCardsPreservesOrder() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.ACE));
        hand.addCard(card(BlackjackRank.KING));
        assertEquals(21, hand.getValue());
        assertTrue(hand.isBlackjack());
    }
}
