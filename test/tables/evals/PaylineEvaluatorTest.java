package tables.evals;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

class PaylineEvaluatorTest {

    @Test
    void recordStoresComponents() {
        PaylineEvaluator eval = new PaylineEvaluator("Test Bet", 2.5, (hands) -> true);
        assertEquals("Test Bet", eval.name());
        assertEquals(2.5, eval.forPay(), 1e-12);
        assertTrue(eval.hitEvaluator().isHit());
    }

    @Test
    void newLoseEvaluatorAlwaysHits() {
        PaylineEvaluator lose = PaylineEvaluator.newLoseEvaluator();
        assertEquals("Lose", lose.name());
        assertEquals(0, lose.forPay(), 1e-12);
        assertTrue(lose.hitEvaluator().isHit());
    }

    @Test
    void hitEvaluatorIsCalled() {
        AtomicBoolean called = new AtomicBoolean(false);
        PaylineEvaluator eval = new PaylineEvaluator("Test", 1, (hands) -> {
            called.set(true);
            return true;
        });
        eval.hitEvaluator().isHit();
        assertTrue(called.get());
    }

    @Test
    void newPaylineStatisticsCreatesNewInstance() {
        PaylineEvaluator eval = new PaylineEvaluator("Test", 1, (hands) -> true);
        PaylineStatistics stats = eval.newPaylineStatistics();
        assertNotNull(stats);
        assertEquals(eval, stats.getPayEvaluator());
    }
}
