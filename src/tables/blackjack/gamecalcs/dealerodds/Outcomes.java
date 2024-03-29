package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackHand;
import tables.evals.HitEvaluator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Outcomes {

    private final List<Outcome> outcomes;

    public Outcomes() {
        this.outcomes = new LinkedList<>();
    }

    private Outcomes(Outcome[] outcomes) {
        this.outcomes = new LinkedList<>();
        Arrays.stream(outcomes).forEach(o -> this.outcomes.add(new Outcome(o.situation, o.eval)));
    }

    private Outcomes(Outcomes outcomes) {
        this.outcomes = new LinkedList<>();
        for (Outcome outcome : outcomes.outcomes) {
            Outcome newOutcome = new Outcome(outcome.situation, outcome.eval);
            this.outcomes.add(newOutcome);
            newOutcome.odds = outcome.odds;
        }
    }

    public Outcomes copyOutcomes() {
        return new Outcomes(this);
    }

    public void addOutcome(String situation, HitEvaluator eval) {
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

    public static Outcomes average(List<Outcomes> outcomes) {
        if (outcomes.isEmpty()) {
            throw new IllegalArgumentException("Must have at least one outcome to average");
        }
        Outcomes averaged = new Outcomes(outcomes.getFirst().outcomes.toArray(new Outcome[0]));
        for (Outcomes os : outcomes) {
            for (int f = 0; f != averaged.outcomes.size(); f++) {
                averaged.outcomes.get(f).odds += os.outcomes.get(f).odds / outcomes.size();
            }
        }
        return averaged;
    }

    private static class Outcome {

        private final String situation;
        private final HitEvaluator eval;
        private double odds;

        Outcome(String situation, HitEvaluator eval) {
            this.situation = situation;
            this.eval = eval;
        }

        @Override
        public String toString() {
            return situation +
                    "\t" +
                    odds;
        }
    }
}
