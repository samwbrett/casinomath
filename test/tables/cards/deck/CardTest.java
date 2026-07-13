package tables.cards.deck;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void constructorCreatesCard() {
        Card card = new Card(TestRank.ACE, StandardSuit.SPADE);
        assertNotNull(card);
        assertEquals(TestRank.ACE, card.getRank());
        assertEquals(StandardSuit.SPADE, card.getSuit());
    }

    @Test
    void constructorThrowsOnNullRank() {
        assertThrows(NullPointerException.class, () -> new Card(null, StandardSuit.SPADE));
    }

    @Test
    void constructorThrowsOnNullSuit() {
        assertThrows(NullPointerException.class, () -> new Card(TestRank.ACE, null));
    }

    @Test
    void cardsWithSameRankAndSuitAreEqual() {
        Card card1 = new Card(TestRank.ACE, StandardSuit.SPADE);
        Card card2 = new Card(TestRank.ACE, StandardSuit.SPADE);
        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void cardsWithDifferentRanksAreNotEqual() {
        Card card1 = new Card(TestRank.ACE, StandardSuit.SPADE);
        Card card2 = new Card(TestRank.KING, StandardSuit.SPADE);
        assertNotEquals(card1, card2);
    }

    @Test
    void cardsWithDifferentSuitsAreNotEqual() {
        Card card1 = new Card(TestRank.ACE, StandardSuit.SPADE);
        Card card2 = new Card(TestRank.ACE, StandardSuit.HEART);
        assertNotEquals(card1, card2);
    }

    @Test
    void getNumberReturnsUniqueIdentifier() {
        Card aceSpades = new Card(TestRank.ACE, StandardSuit.SPADE);
        Card aceClubs = new Card(TestRank.ACE, StandardSuit.CLUB);
        Card twoSpades = new Card(TestRank.TWO, StandardSuit.SPADE);
        assertNotEquals(aceSpades.getNumber(), aceClubs.getNumber());
        assertNotEquals(aceSpades.getNumber(), twoSpades.getNumber());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void equalsHandlesNonCardAndNull() {
        Card card = new Card(TestRank.ACE, StandardSuit.SPADE);
        assertNotEquals(null, card);
        assertNotEquals("not a card", card);
    }

    // A minimal Rank implementation for testing the framework-independent Card class
    private enum TestRank implements Rank {
        TWO(0), THREE(1), FOUR(2), FIVE(3), SIX(4), SEVEN(5),
        EIGHT(6), NINE(7), TEN(8), JACK(9), QUEEN(10), KING(11), ACE(12);

        private final int index;

        TestRank(int index) { this.index = index; }

        @Override public int getIndex() { return index; }
        @Override public java.util.List<TestRank> getRanks() { return java.util.List.of(values()); }
        @Override public String getName() { return name().substring(0, 1); }
        @Override public String toString() { return getName(); }
    }
}
