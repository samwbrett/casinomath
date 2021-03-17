package tables.evals;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Full information on a bet, along with information for each payline.
 */
public class BetStatistics {

    private final String name;
    private final PaylineStatistics[] stats;
    private long totalCombos;
    private double hitFrequency;
    private double pctReturn;

    public BetStatistics(String name, PaylineStatistics... stats) {
        this.name = name;
        this.stats = stats;
    }

    public PaylineStatistics getHitStat(Hand... hands) {
        for (PaylineStatistics stat : stats) {
            if (stat.getPayEvaluator().getHitEvaluator().isHit(hands)) {
                return stat;
            }
        }
        return null;
    }

    public void addCombos(long combos) {
        Arrays.stream(stats).forEach(stat -> stat.addCombinations(combos));
    }

    public void setTotalCombos(long totalCombos) {
        this.totalCombos = totalCombos;
        for (PaylineStatistics stat : stats) {
            stat.setTotalCombos(totalCombos);
            hitFrequency += stat.getHitFrequency();
            pctReturn += stat.getPctReturn();
        }
    }

    public PaylineStatistics[] getStats() {
        return stats;
    }

    public double getTotalCombos() {
        return totalCombos;
    }

    public double getHitFrequency() {
        return hitFrequency;
    }

    public double getPctReturn() {
        return pctReturn;
    }

    @Override
    public String toString() {
        return "Bet\t" + name + "\nTotal Combos\t" + totalCombos + "\nHit Frequency\t" + hitFrequency + "\nPct Return\t" + pctReturn + "\nHouse Edge\t" + (1-pctReturn) + "\n\n" +
                "Bet\tCombos\tHit Frequency\tPct Return\n" +
                Arrays.stream(stats).map(s -> s.getPayEvaluator().getName() + "\t" + s.getCombos() + "\t" + s.getHitFrequency() + "\t" + s.getPctReturn()).collect(Collectors.joining("\n"));
    }
}
