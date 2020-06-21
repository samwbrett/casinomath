package tables.evals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BetEvaluator {

    private String name;
    private PaylineEvaluator[] paylineEvaluators;

    public BetEvaluator(String name, PaylineEvaluator... paylineEvaluators) {
        this.name = name;
        this.paylineEvaluators = new PaylineEvaluator[paylineEvaluators.length+1];
        System.arraycopy(paylineEvaluators, 0, this.paylineEvaluators, 0, paylineEvaluators.length);
        this.paylineEvaluators[this.paylineEvaluators.length - 1] = PaylineEvaluator.newLoseEvaluator();
    }

    public BetStatistics getNewBetStatistics() {
        return new BetStatistics(name, Arrays.stream(paylineEvaluators).map(PaylineEvaluator::getNewPaylineStatistics).collect(Collectors.toList()).toArray(PaylineStatistics[]::new));
    }

}
