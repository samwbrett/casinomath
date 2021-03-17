package tables.evals;

/**
 * Payline evaluator holds the hit logic, the payout, and the name of the payline.
 */
public class PaylineEvaluator {

    private final String name;
    private final HitEvaluator hitEvaluator;
    private final double forPay;

    public PaylineEvaluator(String name, double forPay, HitEvaluator hitEvaluator) {
        this.name = name;
        this.forPay = forPay;
        this.hitEvaluator = hitEvaluator;
    }

    public static PaylineEvaluator newLoseEvaluator() {
        return new PaylineEvaluator("Lose", 0, (hands) -> true);
    }

    public PaylineStatistics getNewPaylineStatistics() {
        return new PaylineStatistics(this);
    }

    public String getName() {
        return name;
    }

    public HitEvaluator getHitEvaluator() {
        return hitEvaluator;
    }

    public double getForPay() {
        return forPay;
    }
}
