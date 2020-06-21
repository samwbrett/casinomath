package tables.cards.deck;

import java.util.LinkedHashSet;
import java.util.Set;

public class Rank implements Comparable<Rank> {

    public static Rank TWO = new Rank(0,"2");
    public static Rank THREE = new Rank(1,"3");
    public static Rank FOUR = new Rank(2,"4");
    public static Rank FIVE = new Rank(3,"5");
    public static Rank SIX = new Rank(4,"6");
    public static Rank SEVEN = new Rank(5,"7");
    public static Rank EIGHT = new Rank(6,"8");
    public static Rank NINE = new Rank(7,"9");
    public static Rank TEN = new Rank(8,"T");
    public static Rank JACK = new Rank(9,"J");
    public static Rank QUEEN = new Rank(10,"Q");
    public static Rank KING = new Rank(11,"K");
    public static Rank ACE = new Rank(12,"A");
    
    private static final Rank[] RANKS;
    private static final int TOTAL_RANKS;
    
    static {
        Set<Rank> ranks = new LinkedHashSet<>();
        ranks.add(TWO);
        ranks.add(THREE);
        ranks.add(FOUR);
        ranks.add(FIVE);
        ranks.add(SIX);
        ranks.add(SEVEN);
        ranks.add(EIGHT);
        ranks.add(NINE);
        ranks.add(TEN);
        ranks.add(JACK);
        ranks.add(QUEEN);
        ranks.add(KING);
        ranks.add(ACE);
        RANKS = ranks.toArray(new Rank[0]);
        TOTAL_RANKS = RANKS.length;
    }

    protected final int index;
    protected final String name;

    protected Rank(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() { return index; }

    public String getName() { return name; }

    public Rank[] getRanks() {
        return RANKS;
    }

    public int getTotalRanks() {
        return TOTAL_RANKS;
    }

    @Override
    public int compareTo(Rank o) {
        return index - o.index;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Rank && compareTo((Rank) o) == 0;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return name;
    }
}
