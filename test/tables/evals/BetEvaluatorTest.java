package tables.evals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BetEvaluatorTest {

    @Test
    void constructorAppendsLosePayline() {
        BetEvaluator bet = new BetEvaluator("Test",
                new PaylineEvaluator("Win", 2, (hands) -> true));
        BetStatistics stats = bet.getNewBetStatistics();
        assertEquals(2, stats.getStats().size());
        assertEquals("Win", stats.getStats().get(0).getPayEvaluator().name());
        assertEquals("Lose", stats.getStats().get(1).getPayEvaluator().name());
    }

    @Test
    void getNewBetStatisticsCreatesFreshStats() {
        BetEvaluator bet = new BetEvaluator("Test",
                new PaylineEvaluator("Win", 2, (hands) -> true));
        BetStatistics stats1 = bet.getNewBetStatistics();
        BetStatistics stats2 = bet.getNewBetStatistics();
        assertNotNull(stats1);
        assertNotNull(stats2);
        assertNotSame(stats1, stats2);
    }
}
