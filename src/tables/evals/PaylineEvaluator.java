package tables.evals;

/**
 * Represents a single payline in a bet: a named outcome with a payout multiplier
 * and hit condition logic.
 * <p>
 * This is a Java record with the following components:
 * <ul>
 *   <li>{@code name} - the display name of this payline (e.g., "Pair", "Win by 9")</li>
 *   <li>{@code forPay} - the "for one" payout multiplier (e.g., 1.95 means a 1-unit bet returns 1.95)</li>
 *   <li>{@code hitEvaluator} - the logic that determines if a hand satisfies this payline</li>
 * </ul>
 *
 * @param name          the display name of this payline
 * @param forPay        the payout multiplier (for one)
 * @param hitEvaluator  the hit condition evaluator
 */
public record PaylineEvaluator(String name, double forPay, HitEvaluator hitEvaluator) {

    /**
     * Creates a default "Lose" evaluator that always returns true.
     * This is automatically appended as the final payline in every
     * {@link BetEvaluator} to capture all combinations that did not
     * match any winning payline.
     *
     * @return a catch-all lose evaluator
     */
    public static PaylineEvaluator newLoseEvaluator() {
        return new PaylineEvaluator("Lose", 0, (hands) -> true);
    }

    /**
     * Creates a new {@link PaylineStatistics} instance associated with this evaluator.
     *
     * @return a new statistics tracker for this payline
     */
    public PaylineStatistics newPaylineStatistics() {
        return new PaylineStatistics(this);
    }
}

