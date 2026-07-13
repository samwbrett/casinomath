package tables.evals;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Aggregated statistics for a single bet, containing individual statistics
 * for each payline defined in the bet.
 * <p>
 * This class accumulates combination counts during enumeration and finalizes
 * hit frequency, return percentage, and house edge calculations once all
 * combinations have been tallied.
 */
public class BetStatistics {

    private final String name;
    private final List<PaylineStatistics> stats;
    private long totalCombos;
    private double hitFrequency;
    private double pctReturn;

    /**
     * Constructs bet statistics for the given bet name and paylines.
     *
     * @param name  the name of the bet
     * @param stats the statistics trackers for each payline
     */
    public BetStatistics(String name, PaylineStatistics... stats) {
        this.name = name;
        this.stats = List.of(stats);
    }

    /**
     * Returns the first payline statistics whose hit evaluator matches the
     * given hands, or {@code null} if no payline matches.
     * <p>
     * Paylines are checked in order, so the ordering defined in the
     * {@link BetEvaluator} determines priority.
     *
     * @param hands the hands to evaluate
     * @return the matching payline statistics, or null
     */
    public PaylineStatistics getHitStat(Hand... hands) {
        for (PaylineStatistics stat : stats) {
            if (stat.getPayEvaluator().hitEvaluator().isHit(hands)) {
                return stat;
            }
        }
        return null;
    }

    /**
     * Adds the given combination count to all payline statistics.
     *
     * @param combos the number of combinations to add
     */
    public void addCombos(long combos) {
        stats.forEach(stat -> stat.addCombinations(combos));
    }

    /**
     * Finalizes all statistics by computing hit frequencies and return percentages
     * relative to the total number of possible combinations.
     *
     * @param totalCombos the total number of all possible hand combinations
     */
    public void setTotalCombos(long totalCombos) {
        this.totalCombos = totalCombos;
        for (PaylineStatistics stat : stats) {
            stat.setTotalCombos(totalCombos);
            hitFrequency += stat.getHitFrequency();
            pctReturn += stat.getPctReturn();
        }
    }

    /** Returns the list of per-payline statistics. */
    public List<PaylineStatistics> getStats() {
        return stats;
    }

    /** Returns the total number of combinations evaluated. */
    public long getTotalCombos() {
        return totalCombos;
    }

    /** Returns the overall hit frequency across all paylines. */
    public double getHitFrequency() {
        return hitFrequency;
    }

    /** Returns the overall expected return (as a decimal, e.g., 0.986 for 98.6%). */
    public double getPctReturn() {
        return pctReturn;
    }

    @Override
    public String toString() {
        return "Bet\t" +
                name +
                "\nTotal Combos\t" +
                totalCombos +
                "\nHit Frequency\t" +
                hitFrequency +
                "\nPct Return\t" +
                pctReturn +
                "\nHouse Edge\t" +
                (1 - pctReturn) + "\n\n" +
                "Bet\tCombos\tHit Frequency\tPct Return\n" +
                stats.stream()
                        .map(s -> s.getPayEvaluator().name() +
                                "\t" +
                                s.getCombos() +
                                "\t" +
                                s.getHitFrequency() +
                                "\t" +
                                s.getPctReturn())
                        .collect(Collectors.joining("\n"));
    }
}

