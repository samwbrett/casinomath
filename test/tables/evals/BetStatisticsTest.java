package tables.evals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BetStatisticsTest {

    @Test
    void getHitStatFindsFirstMatch() {
        Hand mockHand = new TestHand();
        BetStatistics stats = new BetStatistics("Test",
                new PaylineStatistics(new PaylineEvaluator("Pay1", 2, hands -> false)),
                new PaylineStatistics(new PaylineEvaluator("Pay2", 3, hands -> true)),
                new PaylineStatistics(new PaylineEvaluator("Pay3", 4, hands -> true)));

        PaylineStatistics hit = stats.getHitStat(mockHand);
        assertNotNull(hit);
        assertEquals("Pay2", hit.getPayEvaluator().name());
    }

    @Test
    void getHitStatReturnsNullForLoseOnly() {
        Hand mockHand = new TestHand();
        BetStatistics stats = new BetStatistics("Lose Only",
                new PaylineStatistics(new PaylineEvaluator("Lose", 0, hands -> true)));

        PaylineStatistics hit = stats.getHitStat(mockHand);
        assertNotNull(hit);
        assertEquals("Lose", hit.getPayEvaluator().name());
    }

    @Test
    void setTotalCombosComputesAggregateStats() {
        BetStatistics stats = new BetStatistics("Test",
                new PaylineStatistics(new PaylineEvaluator("Win", 2, hands -> true)));
        stats.addCombos(30);
        stats.setTotalCombos(100);
        assertEquals(100, stats.getTotalCombos());
        assertEquals(0.3, stats.getHitFrequency(), 1e-12);
        assertEquals(0.6, stats.getPctReturn(), 1e-12);
    }

    private static class TestHand extends Hand {
        TestHand() { super(); }
    }
}
