package tables.blackjack.calcs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BlackjackRankTest {

    @Test
    void numberedCardsValueIsFaceValue() {
        assertEquals(2, BlackjackRank.TWO.getValue());
        assertEquals(5, BlackjackRank.FIVE.getValue());
        assertEquals(10, BlackjackRank.TEN.getValue());
    }

    @Test
    void faceCardsAreWorthTen() {
        assertEquals(10, BlackjackRank.JACK.getValue());
        assertEquals(10, BlackjackRank.QUEEN.getValue());
        assertEquals(10, BlackjackRank.KING.getValue());
    }

    @Test
    void aceIsWorthEleven() {
        assertEquals(11, BlackjackRank.ACE.getValue());
    }

    @Test
    void toStringReturnsName() {
        assertEquals("T", BlackjackRank.TEN.toString());
        assertEquals("A", BlackjackRank.ACE.toString());
        assertEquals("K", BlackjackRank.KING.toString());
        assertEquals("2", BlackjackRank.TWO.toString());
    }

    @Test
    void getRanksReturnsAllRanks() {
        assertEquals(13, BlackjackRank.ACE.getRanks().size());
    }
}
