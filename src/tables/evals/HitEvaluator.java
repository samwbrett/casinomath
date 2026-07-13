package tables.evals;

/**
 * A functional interface for determining whether a hand (or set of hands)
 * satisfies a specific outcome condition.
 * <p>
 * This is used by {@link PaylineEvaluator} to define the hit logic for
 * each payline in a bet. Implementations should be stateless and idempotent.
 */
@FunctionalInterface
public interface HitEvaluator {

    /**
     * Evaluates whether the given hand(s) satisfy this hit condition.
     *
     * @param hands the hands to evaluate (varargs allows single or multi-hand evaluation)
     * @return true if the condition is met
     */
    boolean isHit(Hand... hands);
}

