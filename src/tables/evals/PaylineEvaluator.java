package tables.evals;

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
        return new PaylineEvaluator("Lose", 0, (hands) -> true) {
            @Override
            boolean isLoss() {
                return true;
            }
        };
    }

    public PaylineStatistics getNewPaylineStatistics() {
        return new PaylineStatistics(this);
    }

    boolean isLoss() {
        return false;
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
