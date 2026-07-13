package tables.blackjack.gamecalcs.dealerodds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tables.blackjack.calcs.BlackjackHand;
import tables.blackjack.calcs.BlackjackRank;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OutcomesTest {

    private Outcomes outcomes;

    private Card card(BlackjackRank rank) {
        return new Card(rank, StandardSuit.SPADE);
    }

    @BeforeEach
    void setUp() {
        outcomes = new Outcomes();
        outcomes.addOutcome("BJ", (h) -> ((BlackjackHand) h[0]).isBlackjack());
        outcomes.addOutcome("17", new ValueHitEvaluator(17));
        outcomes.addOutcome("18", new ValueHitEvaluator(18));
    }

    @Test
    void sizeReturnsCorrectCount() {
        assertEquals(3, outcomes.size());
    }

    @Test
    void isHitDetectsMatchingOutcome() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.TEN), card(BlackjackRank.SEVEN));
        assertFalse(outcomes.isHit(0, hand)); // not BJ
        assertTrue(outcomes.isHit(1, hand));  // is 17
        assertFalse(outcomes.isHit(2, hand)); // not 18
    }

    @Test
    void getOddsReturnsInitialZero() {
        assertEquals(0, outcomes.getOdds(0), 1e-12);
    }

    @Test
    void setAndGetOdds() {
        outcomes.setOdds(0, 0.5);
        assertEquals(0.5, outcomes.getOdds(0), 1e-12);
    }

    @Test
    void getOddsThrowsOnInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> outcomes.getOdds(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> outcomes.getOdds(3));
    }

    @Test
    void copyOutcomesCreatesIndependentCopy() {
        outcomes.setOdds(0, 0.25);
        Outcomes copy = outcomes.copyOutcomes();

        assertEquals(outcomes.getOdds(0), copy.getOdds(0), 1e-12);
        copy.setOdds(0, 0.5);
        assertEquals(0.25, outcomes.getOdds(0), 1e-12);
    }

    @Test
    void averageOfSingleOutcomesIsItself() {
        outcomes.setOdds(0, 0.1);
        outcomes.setOdds(1, 0.2);
        outcomes.setOdds(2, 0.3);

        Outcomes avg = Outcomes.average(List.of(outcomes));
        assertEquals(0.1, avg.getOdds(0), 1e-12);
        assertEquals(0.2, avg.getOdds(1), 1e-12);
        assertEquals(0.3, avg.getOdds(2), 1e-12);
    }

    @Test
    void averageOfTwoOutcomes() {
        Outcomes o1 = new Outcomes();
        o1.addOutcome("A", (h) -> true);
        o1.setOdds(0, 0.6);

        Outcomes o2 = new Outcomes();
        o2.addOutcome("A", (h) -> true);
        o2.setOdds(0, 0.4);

        Outcomes avg = Outcomes.average(List.of(o1, o2));
        assertEquals(0.5, avg.getOdds(0), 1e-12);
    }

    @Test
    void averageThrowsOnEmptyList() {
        assertThrows(IllegalArgumentException.class, () -> Outcomes.average(List.of()));
    }

    @Test
    void testToString() {
        String str = outcomes.toString();
        assertTrue(str.contains("Situation"));
        assertTrue(str.contains("Odds"));
    }
}
