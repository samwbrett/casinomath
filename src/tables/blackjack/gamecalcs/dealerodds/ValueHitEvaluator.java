package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackHand;
import tables.evals.Hand;
import tables.evals.HitEvaluator;

class ValueHitEvaluator implements HitEvaluator {

    private final int value;

    ValueHitEvaluator(int value) {
        this.value = value;
    }

    @Override
    public boolean isHit(Hand... hands) {
        if (hands.length != 1 || !(hands[0] instanceof BlackjackHand)) {
            throw new IllegalArgumentException("Must be a single blackjack hand");
        }
        return ((BlackjackHand)hands[0]).getValue() == value;
    }
}
