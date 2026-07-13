package tables.baccarat.calcs;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BaccaratRankTest {

    @Test
    void allRanksDefined() {
        assertEquals(13, BaccaratRank.values().length);
    }

    @Test
    void faceCardsAreWorthZero() {
        assertEquals(0, BaccaratRank.TEN.getValue());
        assertEquals(0, BaccaratRank.JACK.getValue());
        assertEquals(0, BaccaratRank.QUEEN.getValue());
        assertEquals(0, BaccaratRank.KING.getValue());
    }

    @Test
    void aceIsWorthOne() {
        assertEquals(1, BaccaratRank.ACE.getValue());
    }

    @Test
    void numberedCardsAreWorthFaceValue() {
        assertEquals(2, BaccaratRank.TWO.getValue());
        assertEquals(5, BaccaratRank.FIVE.getValue());
        assertEquals(9, BaccaratRank.NINE.getValue());
    }

    @Test
    void getRanksReturnsAllRanks() {
        List<BaccaratRank> ranks = BaccaratRank.ACE.getRanks();
        assertEquals(13, ranks.size());
        assertTrue(ranks.contains(BaccaratRank.ACE));
        assertTrue(ranks.contains(BaccaratRank.TWO));
    }

    @Test
    void toStringReturnsName() {
        assertEquals("A", BaccaratRank.ACE.toString());
        assertEquals("K", BaccaratRank.KING.toString());
        assertEquals("T", BaccaratRank.TEN.toString());
    }
}
