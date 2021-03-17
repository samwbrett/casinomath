package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackHand;
import tables.evals.HitEvaluator;

class ValueHitEvaluator implements HitEvaluator<BlackjackHand> {

    private int value;

    ValueHitEvaluator(int value) {
        this.value = value;
    }

    @Override
    public boolean isHit(BlackjackHand... hands) {
        return hands[0].getValue() == value;
    }
}
