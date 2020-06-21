package tables.evals;

public abstract class CardCalculator {

    protected final BetStatistics[] stats;

    protected CardCalculator(BetStatistics... stats) {
        this.stats = stats;
    }

    public BetStatistics[] getStats() {
        return stats;
    }

    public abstract void run(int threads);

}
