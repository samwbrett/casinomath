package tables.evals;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Combination counter for a payline evaluation.
 * Adding all combinations can be done concurrently, then when all calls are done set the total combos to finalize.
 */
public class PaylineStatistics {

    private final PaylineEvaluator eval;
    private volatile AtomicLong combos = new AtomicLong();
    private double hitFrequency;
    private double pctReturn;

    public PaylineStatistics(PaylineEvaluator eval) {
        this.eval = eval;
    }

    public PaylineEvaluator getPayEvaluator() {
        return eval;
    }

    public void addCombinations(long combos) {
        this.combos.addAndGet(combos);
    }

    public void setTotalCombos(long totalCombos) {
        hitFrequency = combos.get() / (double)totalCombos;
        pctReturn = hitFrequency * eval.getForPay();
    }

    public long getCombos() {
        return combos.get();
    }

    public double getHitFrequency() {
        return hitFrequency;
    }

    public double getPctReturn() {
        return pctReturn;
    }

    @Override
    public String toString() {
        return "Bet\t" + eval.getName() + "\nTotalCombos\t" + combos + "\nHit Frequency\t" + hitFrequency + "\nPct Return\t" + pctReturn;
    }
}
