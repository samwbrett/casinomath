package tables.cards.deck;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StandardSuitTest {

    @Test
    void allSuitsDefined() {
        assertEquals(4, StandardSuit.values().length);
    }

    @Test
    void suitIndicesAreDistinct() {
        long distinctIndices = java.util.Arrays.stream(StandardSuit.values())
                .mapToInt(StandardSuit::getIndex)
                .distinct()
                .count();
        assertEquals(4, distinctIndices);
    }

    @Test
    void getSuitsReturnsAllSuits() {
        assertEquals(4, StandardSuit.SPADE.getSuits().size());
    }

    @Test
    void spadeHasHighestIndex() {
        assertEquals(3, StandardSuit.SPADE.getIndex());
    }

    @Test
    void clubHasLowestIndex() {
        assertEquals(0, StandardSuit.CLUB.getIndex());
    }

    @Test
    void toStringReturnsName() {
        assertEquals("s", StandardSuit.SPADE.toString());
        assertEquals("h", StandardSuit.HEART.toString());
        assertEquals("d", StandardSuit.DIAMOND.toString());
        assertEquals("c", StandardSuit.CLUB.toString());
    }
}
