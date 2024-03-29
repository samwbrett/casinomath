package tables.evals;

import java.util.Collections;
import java.util.List;

/**
 * Generic class for running statistics and outputting the result.
 */
public abstract class CardCalculator {

    protected final List<BetStatistics> stats;

    protected CardCalculator(List<BetStatistics> stats) {
        this.stats = Collections.unmodifiableList(stats);
    }

    public List<BetStatistics> getStats() {
        return stats;
    }

    public abstract void run(int threads) throws InterruptedException;

}
