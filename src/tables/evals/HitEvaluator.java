package tables.evals;

@FunctionalInterface
public interface HitEvaluator<H extends Hand> {

    boolean isHit(H... hands);
}
