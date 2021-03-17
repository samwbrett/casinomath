package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackHand;
import tables.evals.Hand;
import tables.evals.HitEvaluator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Outcomes implements Cloneable {

    private List<Outcome> outcomes;

    public Outcomes() {
        this.outcomes = new ArrayList<>();
    }

    private Outcomes(Outcome[] outcomes) {
        this.outcomes = new ArrayList<>(outcomes.length);
        Arrays.stream(outcomes).forEach(o -> this.outcomes.add(new Outcome(o.situation, o.eval)));
    }

    private Outcomes(Outcomes outcomes) {
        this.outcomes = new ArrayList<>(outcomes.size());
        for (Outcome outcome : outcomes.outcomes) {
            Outcome newOutcome = new Outcome(outcome.situation, outcome.eval);
            this.outcomes.add(newOutcome);
            newOutcome.odds = outcome.odds;
        }
    }

    @Override
    public Outcomes clone() {
        return new Outcomes(this);
    }

    public void addOutcome(String situation, HitEvaluator<BlackjackHand> eval) {
        outcomes.add(new Outcome(situation, eval));
    }

    public int size() {
        return outcomes.size();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index " + index + " outside of outcome bounds.");
        }
    }

    public boolean isHit(int index, BlackjackHand h) {
        checkIndex(index);
        return outcomes.get(index).eval.isHit(h);
    }

    public double getOdds(int index) {
        checkIndex(index);
        return outcomes.get(index).odds;
    }

    public void setOdds(int index, double odds) {
        checkIndex(index);
        outcomes.get(index).odds = odds;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Situation\tOdds");
        outcomes.forEach(o -> builder.append("\n").append(o.toString()));
        return builder.toString();
    }

    public static Outcomes average(Outcomes[] outcomes) {
        Outcomes averaged = new Outcomes(outcomes[0].outcomes.toArray(new Outcome[0]));
        for (Outcomes os : outcomes) {
            for (int f = 0; f != averaged.outcomes.size(); f++) {
                averaged.outcomes.get(f).odds += os.outcomes.get(f).odds / outcomes.length;
            }
        }
        return averaged;
    }

    private static class Outcome {

        private final String situation;
        private final HitEvaluator<BlackjackHand> eval;
        private double odds;

        Outcome(String situation, HitEvaluator<BlackjackHand> eval) {
            this.situation = situation;
            this.eval = eval;
        }

        @Override
        public String toString() {
            return situation + "\t" + odds;
        }
    }
}
