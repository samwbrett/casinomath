package tables.baccarat.calcs;

import tables.cards.deck.Rank;

import java.util.LinkedHashSet;
import java.util.Set;

public class BaccRank extends Rank {

    public static BaccRank TWO = new BaccRank(0,2,"2");
    public static BaccRank THREE = new BaccRank(1,3,"3");
    public static BaccRank FOUR = new BaccRank(2,4,"4");
    public static BaccRank FIVE = new BaccRank(3,5,"5");
    public static BaccRank SIX = new BaccRank(4,6,"6");
    public static BaccRank SEVEN = new BaccRank(5,7,"7");
    public static BaccRank EIGHT = new BaccRank(6,8,"8");
    public static BaccRank NINE = new BaccRank(7,9, "9");
    public static BaccRank TEN = new BaccRank(8,0,"T");
    public static BaccRank JACK = new BaccRank(9,0,"J");
    public static BaccRank QUEEN = new BaccRank(10,0,"Q");
    public static BaccRank KING = new BaccRank(11,0,"K");
    public static BaccRank ACE = new BaccRank(12,1,"A");

    private static final BaccRank[] RANKS;
    private static final int TOTAL_RANKS;

    static {
        Set<BaccRank> ranks = new LinkedHashSet<>();
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
        RANKS = ranks.toArray(new BaccRank[0]);
        TOTAL_RANKS = RANKS.length;
    }
    
    private int value;
    
    protected BaccRank(int index, int value, String name) {
        super(index, name);
        this.value = value;
    }

    public BaccRank[] getRanks() {
        return RANKS;
    }

    public int getTotalRanks() {
        return TOTAL_RANKS;
    }

    public int getValue() {
        return value;
    }
    
}
