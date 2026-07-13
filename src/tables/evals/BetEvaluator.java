package tables.evals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines a bet by its name and an ordered list of {@link PaylineEvaluator paylines}.
 * <p>
 * A "Lose" payline is automatically appended as the final payline to capture
 * all non-winning combinations. Paylines are evaluated in the order they are
 * defined, and the first matching payline determines the outcome for a given
 * set of hands.
 */
public class BetEvaluator {

    private final String name;
    private final List<PaylineEvaluator> paylineEvaluators;

    /**
     * Constructs a bet evaluator with the given name and paylines.
     * A default "Lose" payline (payout 0) is automatically appended.
     *
     * @param name             the name of the bet
     * @param paylineEvaluators the winning paylines (highest priority first)
     */
    public BetEvaluator(String name, PaylineEvaluator... paylineEvaluators) {
        this.name = name;
        List<PaylineEvaluator> paylineList = new ArrayList<>(paylineEvaluators.length + 1);
        Collections.addAll(paylineList, paylineEvaluators);
        paylineList.add(PaylineEvaluator.newLoseEvaluator());
        this.paylineEvaluators = Collections.unmodifiableList(paylineList);
    }

    /**
     * Creates a new {@link BetStatistics} instance initialized with fresh
     * statistics trackers for each payline.
     *
     * @return a new statistics object for this bet
     */
    public BetStatistics getNewBetStatistics() {
        return new BetStatistics(name,
                paylineEvaluators.stream()
                        .map(PaylineEvaluator::newPaylineStatistics)
                        .toList()
                        .toArray(PaylineStatistics[]::new));
    }
}

