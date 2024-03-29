package tables.evals;

/**
 * Payline evaluator holds the hit logic, the payout, and the name of the payline.
 */
public record PaylineEvaluator(String name, double forPay, HitEvaluator hitEvaluator) {

    public static PaylineEvaluator newLoseEvaluator() {
        return new PaylineEvaluator("Lose", 0, (hands) -> true);
    }

    public PaylineStatistics newPaylineStatistics() {
        return new PaylineStatistics(this);
    }
}
