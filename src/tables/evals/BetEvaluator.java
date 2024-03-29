package tables.evals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BetEvaluator {

    private final String name;
    private final List<PaylineEvaluator> paylineEvaluators;

    public BetEvaluator(String name, PaylineEvaluator... paylineEvaluators) {
        this.name = name;
        List<PaylineEvaluator> paylineList = new ArrayList<>(paylineEvaluators.length + 1);
        Collections.addAll(paylineList, paylineEvaluators);
        paylineList.add(PaylineEvaluator.newLoseEvaluator());
        this.paylineEvaluators = Collections.unmodifiableList(paylineList);
    }

    public BetStatistics getNewBetStatistics() {
        return new BetStatistics(name,
                paylineEvaluators.stream().map(PaylineEvaluator::newPaylineStatistics).toList().toArray(PaylineStatistics[]::new));
    }

}
