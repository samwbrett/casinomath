package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackHand;
import tables.evals.Hand;
import tables.evals.HitEvaluator;

/**
 * Evaluates whether a blackjack hand has a specific total value.
 * <p>
 * Used by {@link Outcomes} to define paylines for each possible
 * dealer final hand value (17-26).
 */
class ValueHitEvaluator implements HitEvaluator {

    private final int value;

    /**
     * @param value the hand value to match (e.g., 17, 18, ..., 26)
     */
    ValueHitEvaluator(int value) {
        this.value = value;
    }

    @Override
    public boolean isHit(Hand... hands) {
        if (hands.length != 1 || !(hands[0] instanceof BlackjackHand bjHand)) {
            throw new IllegalArgumentException("Must be a single blackjack hand");
        }
        return bjHand.getValue() == value;
    }
}

