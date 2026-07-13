package tables.evals;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe statistics tracker for a single payline outcome.
 * <p>
 * Combinations can be accumulated concurrently from multiple threads via
 * {@link #addCombinations(long)}. After all enumeration is complete, call
 * {@link #setTotalCombos(long)} to finalize the hit frequency and return
 * percentage calculations.
 */
public class PaylineStatistics {

    private final PaylineEvaluator eval;
    private final AtomicLong combos = new AtomicLong();
    private volatile double hitFrequency;
    private volatile double pctReturn;

    PaylineStatistics(PaylineEvaluator eval) {
        this.eval = eval;
    }

    /** Returns the payline evaluator associated with these statistics. */
    public PaylineEvaluator getPayEvaluator() {
        return eval;
    }

    /**
     * Increments the combination count by the specified amount. Thread-safe.
     *
     * @param combos the number of combinations to add
     */
    public void addCombinations(long combos) {
        this.combos.addAndGet(combos);
    }

    /**
     * Finalizes statistics by computing the hit frequency and return percentage
     * based on the total number of possible combinations.
     *
     * @param totalCombos the total number of all possible hand combinations
     */
    public void setTotalCombos(long totalCombos) {
        hitFrequency = combos.get() / (double) totalCombos;
        pctReturn = hitFrequency * eval.forPay();
    }

    /** Returns the total number of winning combinations counted so far. */
    public long getCombos() {
        return combos.get();
    }

    /** Returns the hit frequency (winning combos / total combos). */
    public double getHitFrequency() {
        return hitFrequency;
    }

    /** Returns the percentage return (hit frequency * payout). */
    public double getPctReturn() {
        return pctReturn;
    }

    @Override
    public String toString() {
        return "Bet\t" +
                eval.name() +
                "\nTotalCombos\t" +
                combos +
                "\nHit Frequency\t" +
                hitFrequency +
                "\nPct Return\t" +
                pctReturn;
    }
}

