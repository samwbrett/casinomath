package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackHand;
import tables.evals.HitEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores a set of named outcomes with associated odds, used to track the
 * probability distribution of a dealer's final hand result.
 * <p>
 * Each outcome has a name (e.g., "17", "BJ", "Bust"), an evaluator to
 * determine if a hand matches, and a probability value.
 */
class Outcomes {

    private final List<Outcome> outcomes;

    public Outcomes() {
        this.outcomes = new ArrayList<>();
    }

    private Outcomes(Outcome[] outcomes) {
        this.outcomes = new ArrayList<>(outcomes.length);
        Arrays.stream(outcomes).forEach(o -> this.outcomes.add(new Outcome(o.situation, o.eval)));
    }

    private Outcomes(Outcomes source) {
        this.outcomes = new ArrayList<>(source.outcomes.size());
        for (Outcome outcome : source.outcomes) {
            Outcome copy = new Outcome(outcome.situation, outcome.eval);
            copy.odds = outcome.odds;
            this.outcomes.add(copy);
        }
    }

    /** Creates an independent copy of these outcomes with their odds. */
    public Outcomes copyOutcomes() {
        return new Outcomes(this);
    }

    /** Adds a named outcome with the given evaluator, initialized with zero odds. */
    public void addOutcome(String situation, HitEvaluator eval) {
        outcomes.add(new Outcome(situation, eval));
    }

    /** Returns the number of outcomes. */
    public int size() {
        return outcomes.size();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index " + index + " outside of outcome bounds (0-" + (size() - 1) + ").");
        }
    }

    /** Checks if the hand matches the outcome at the given index. */
    public boolean isHit(int index, BlackjackHand h) {
        checkIndex(index);
        return outcomes.get(index).eval.isHit(h);
    }

    /** Returns the odds at the given index. */
    public double getOdds(int index) {
        checkIndex(index);
        return outcomes.get(index).odds;
    }

    /** Sets the odds at the given index. */
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

    /**
     * Computes the average of a list of outcome probability distributions.
     *
     * @param outcomes the list of outcome distributions to average
     * @return a new Outcomes object with averaged probabilities
     * @throws IllegalArgumentException if the list is empty
     */
    public static Outcomes average(List<Outcomes> outcomes) {
        if (outcomes.isEmpty()) {
            throw new IllegalArgumentException("Must have at least one outcome to average");
        }
        Outcomes averaged = new Outcomes(outcomes.getFirst().outcomes.toArray(new Outcome[0]));
        for (Outcomes os : outcomes) {
            for (int f = 0; f < averaged.outcomes.size(); f++) {
                averaged.outcomes.get(f).odds += os.outcomes.get(f).odds / outcomes.size();
            }
        }
        return averaged;
    }

    /** A single outcome with a name, evaluator, and probability. */
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
            return situation + "\t" + odds;
        }
    }
}

