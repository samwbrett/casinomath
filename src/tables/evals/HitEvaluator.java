package tables.evals;

@FunctionalInterface
public interface HitEvaluator {

    boolean isHit(Hand... hands);
}
