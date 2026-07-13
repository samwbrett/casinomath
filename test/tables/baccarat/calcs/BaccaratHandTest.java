package tables.baccarat.calcs;

import org.junit.jupiter.api.Test;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import static org.junit.jupiter.api.Assertions.*;

class BaccaratHandTest {

    private Card card(BaccaratRank rank) {
        return new Card(rank, StandardSuit.SPADE);
    }

    @Test
    void handValueIsMod10() {
        BaccaratHand hand = new BaccaratHand(card(BaccaratRank.NINE), card(BaccaratRank.NINE));
        assertEquals(8, hand.getValue()); // (9+9) % 10 = 8
    }

    @Test
    void handValueWithBaccaratValues() {
        // K(0) + 7(7) = 7
        BaccaratHand hand = new BaccaratHand(card(BaccaratRank.KING), card(BaccaratRank.SEVEN));
        assertEquals(7, hand.getValue());
    }

    @Test
    void handValueWrapsAround() {
        // 9 + 8 = 17 -> 7
        BaccaratHand hand = new BaccaratHand(card(BaccaratRank.NINE), card(BaccaratRank.EIGHT));
        assertEquals(7, hand.getValue());
    }

    @Test
    void threeCardHandValue() {
        // A(1) + K(0) + 9(9) = 10 -> 0
        BaccaratHand hand = new BaccaratHand(
                card(BaccaratRank.ACE), card(BaccaratRank.KING), card(BaccaratRank.NINE));
        assertEquals(0, hand.getValue());
    }

    @Test
    void isNaturalNine() {
        BaccaratHand hand = new BaccaratHand(card(BaccaratRank.NINE), card(BaccaratRank.ACE)); // 9+1=10->0
        // Actually (9+1)%10 = 0, not 9. Let's use a real natural 9: 4+5=9
        BaccaratHand natural = new BaccaratHand(card(BaccaratRank.FOUR), card(BaccaratRank.FIVE));
        assertTrue(natural.isNaturalNine());
        assertTrue(natural.isNatural());
    }

    @Test
    void isNaturalEight() {
        BaccaratHand hand = new BaccaratHand(card(BaccaratRank.THREE), card(BaccaratRank.FIVE));
        assertTrue(hand.isNaturalEight());
        assertTrue(hand.isNatural());
    }

    @Test
    void threeCardHandIsNotNatural() {
        BaccaratHand hand = new BaccaratHand(
                card(BaccaratRank.FOUR), card(BaccaratRank.FIVE), card(BaccaratRank.ACE));
        assertFalse(hand.isNatural());
        assertFalse(hand.isNaturalEight());
        assertFalse(hand.isNaturalNine());
    }

    @Test
    void isFirstTwoPaired() {
        BaccaratHand hand = new BaccaratHand(card(BaccaratRank.SEVEN), card(BaccaratRank.SEVEN));
        assertTrue(hand.isFirstTwoPaired());
    }

    @Test
    void isFirstTwoPairedReturnsFalseForDifferentRanks() {
        BaccaratHand hand = new BaccaratHand(card(BaccaratRank.SEVEN), card(BaccaratRank.EIGHT));
        assertFalse(hand.isFirstTwoPaired());
    }

    @Test
    void isFirstTwoPairedWithThreeCards() {
        BaccaratHand hand = new BaccaratHand(
                card(BaccaratRank.SEVEN), card(BaccaratRank.SEVEN), card(BaccaratRank.NINE));
        assertTrue(hand.isFirstTwoPaired());
    }
}
