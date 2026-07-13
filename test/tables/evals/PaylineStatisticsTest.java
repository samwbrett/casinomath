package tables.evals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaylineStatisticsTest {

    @Test
    void initialStatsAreZero() {
        PaylineEvaluator eval = new PaylineEvaluator("Test", 2, (hands) -> true);
        PaylineStatistics stats = new PaylineStatistics(eval);
        assertEquals(0, stats.getCombos());
        assertEquals(0, stats.getHitFrequency(), 1e-12);
        assertEquals(0, stats.getPctReturn(), 1e-12);
    }

    @Test
    void addCombinationsIncrementsCount() {
        PaylineStatistics stats = makeStats();
        stats.addCombinations(100);
        assertEquals(100, stats.getCombos());
    }

    @Test
    void addCombinationsIsThreadSafe() throws Exception {
        PaylineStatistics stats = makeStats();
        Thread t1 = new Thread(() -> stats.addCombinations(50));
        Thread t2 = new Thread(() -> stats.addCombinations(50));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        assertEquals(100, stats.getCombos());
    }

    @Test
    void setTotalCombosComputesHitFrequency() {
        PaylineStatistics stats = makeStats();
        stats.addCombinations(25);
        stats.setTotalCombos(100);
        assertEquals(0.25, stats.getHitFrequency(), 1e-12);
    }

    @Test
    void setTotalCombosComputesPctReturn() {
        PaylineStatistics stats = new PaylineStatistics(new PaylineEvaluator("Test", 3, (hands) -> true));
        stats.addCombinations(25);
        stats.setTotalCombos(100);
        assertEquals(0.75, stats.getPctReturn(), 1e-12); // 0.25 * 3
    }

    @Test
    void setTotalCombosWithZeroCombos() {
        PaylineStatistics stats = makeStats();
        stats.setTotalCombos(100);
        assertEquals(0, stats.getHitFrequency(), 1e-12);
        assertEquals(0, stats.getPctReturn(), 1e-12);
    }

    private static PaylineStatistics makeStats() {
        return new PaylineStatistics(new PaylineEvaluator("Test", 2, (hands) -> true));
    }
}
