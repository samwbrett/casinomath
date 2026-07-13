package tables.cards.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EnumeratorShoeTest {

    private static final int NUM_DECKS = 6;

    private enum TestRank implements Rank {
        ACE(0), KING(1);
        private final int index;
        TestRank(int index) { this.index = index; }
        @Override public int getIndex() { return index; }
        @Override public int getValue() { return index + 1; }
        @Override public List<TestRank> getRanks() { return List.of(values()); }
        @Override public String getName() { return name(); }
        @Override public String toString() { return getName(); }
    }

    private EnumeratorShoe shoe;
    private Card aceSpade;
    private Card kingSpade;

    @BeforeEach
    void setUp() {
        shoe = new EnumeratorShoe(NUM_DECKS, List.of(TestRank.values()), List.of(StandardSuit.SPADE));
        aceSpade = new Card(TestRank.ACE, StandardSuit.SPADE);
        kingSpade = new Card(TestRank.KING, StandardSuit.SPADE);
    }

    @Test
    void constructorSetsCorrectTotalCards() {
        // 2 ranks * 1 suit * 6 decks = 12 cards
        assertEquals(12, shoe.getTotalCards());
    }

    @Test
    void constructorSetsCorrectCountPerCard() {
        assertEquals(NUM_DECKS, shoe.getCount(aceSpade));
        assertEquals(NUM_DECKS, shoe.getCount(kingSpade));
    }

    @Test
    void getCardsReturnsAllCardTypes() {
        assertEquals(2, shoe.getCards().size());
    }

    @Test
    void getProbReturnsCorrectProbability() {
        assertEquals(0.5, shoe.getProb(aceSpade), 1e-12);
        assertEquals(0.5, shoe.getProb(kingSpade), 1e-12);
    }

    @Test
    void removeCardDecrementsCount() {
        // removeCard decrements from 6 to 5; card stays in the set
        shoe.removeCard(aceSpade);
        assertEquals(5, shoe.getCount(aceSpade));
        assertTrue(shoe.getCards().contains(aceSpade));
    }

    @Test
    void removeCardReturnsTrueWhenCardPresent() {
        assertTrue(shoe.removeCard(aceSpade));
    }

    @Test
    void removeCardReturnsFalseWhenCardAbsent() {
        // Remove all copies of a card that's still in the shoe
        for (int i = 0; i < NUM_DECKS; i++) {
            shoe.removeCard(aceSpade);
        }
        // aceSpade is now removed from the key set
        assertFalse(shoe.removeCard(aceSpade));
    }

    @Test
    void removeCardDecrementsTotalCards() {
        shoe.removeCard(aceSpade);
        assertEquals(11, shoe.getTotalCards());
    }

    @Test
    void removeCardRemovesEntryWhenCountReachesZero() {
        for (int i = 0; i < NUM_DECKS; i++) {
            shoe.removeCard(aceSpade);
        }
        assertEquals(0, shoe.getCount(aceSpade));
        assertFalse(shoe.getCards().contains(aceSpade));
    }

    @Test
    void addCardIncrementsCount() {
        shoe.removeCard(aceSpade);
        shoe.addCard(aceSpade);
        assertEquals(NUM_DECKS, shoe.getCount(aceSpade));
    }

    @Test
    void addCardIncrementsTotalCards() {
        shoe.removeCard(aceSpade);
        int before = shoe.getTotalCards();
        shoe.addCard(aceSpade);
        assertEquals(before + 1, shoe.getTotalCards());
    }

    @Test
    void addCardRestoresRemovedCardToSet() {
        for (int i = 0; i < NUM_DECKS; i++) {
            shoe.removeCard(aceSpade);
        }
        assertFalse(shoe.getCards().contains(aceSpade));
        shoe.addCard(aceSpade);
        assertTrue(shoe.getCards().contains(aceSpade));
    }

    @Test
    void copyShoeCreatesIndependentCopy() {
        EnumeratorShoe copy = shoe.copyShoe();
        assertEquals(shoe, copy);
        assertEquals(shoe.getTotalCards(), copy.getTotalCards());

        copy.removeCard(aceSpade);
        assertNotEquals(shoe.getTotalCards(), copy.getTotalCards());
    }

    @Test
    void hashCodeIsConsistentWithEquals() {
        EnumeratorShoe copy = shoe.copyShoe();
        assertEquals(shoe.hashCode(), copy.hashCode());
    }

    @Test
    void getCardsSetIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> shoe.getCards().add(aceSpade));
    }

    @Test
    void getCountReturnsDefaultForUnknownCard() {
        Card unknown = new Card(TestRank.ACE, StandardSuit.HEART);
        assertEquals(0, shoe.getCount(unknown));
    }
}
