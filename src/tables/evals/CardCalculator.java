package tables.evals;

import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for card game enumerators that compute exact statistics
 * by iterating over all possible hand combinations.
 * <p>
 * Subclasses implement {@link #run(int)} to perform the enumeration, potentially
 * using multiple threads for performance. Results are stored as a list of
 * {@link BetStatistics} objects.
 */
public abstract class CardCalculator {

    protected final List<BetStatistics> stats;

    /**
     * Constructs a calculator with the given bet statistics trackers.
     *
     * @param stats the statistics objects to populate during enumeration
     */
    protected CardCalculator(List<BetStatistics> stats) {
        this.stats = Collections.unmodifiableList(stats);
    }

    /**
     * Returns the (unmodifiable) list of bet statistics.
     *
     * @return the list of statistics
     */
    public List<BetStatistics> getStats() {
        return stats;
    }

    /**
     * Runs the enumeration over all possible hand combinations,
     * updating the bet statistics.
     *
     * @param threads the number of threads to use for parallel computation
     * @throws InterruptedException if the computation is interrupted
     */
    public abstract void run(int threads) throws InterruptedException;
}

