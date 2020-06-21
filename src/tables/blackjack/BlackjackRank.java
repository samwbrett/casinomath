package tables.blackjack;

import tables.cards.deck.Rank;

import java.util.LinkedHashSet;
import java.util.Set;

public class BlackjackRank extends Rank {

    public static BlackjackRank TWO = new BlackjackRank(0,2,"2");
    public static BlackjackRank THREE = new BlackjackRank(1,3,"3");
    public static BlackjackRank FOUR = new BlackjackRank(2,4,"4");
    public static BlackjackRank FIVE = new BlackjackRank(3,5,"5");
    public static BlackjackRank SIX = new BlackjackRank(4,6,"6");
    public static BlackjackRank SEVEN = new BlackjackRank(5,7,"7");
    public static BlackjackRank EIGHT = new BlackjackRank(6,8,"8");
    public static BlackjackRank NINE = new BlackjackRank(7,9, "9");
    public static BlackjackRank TEN = new BlackjackRank(8,10,"T");
    public static BlackjackRank JACK = new BlackjackRank(9,10,"J");
    public static BlackjackRank QUEEN = new BlackjackRank(10,10,"Q");
    public static BlackjackRank KING = new BlackjackRank(11,10,"K");
    public static BlackjackRank ACE = new BlackjackRank(12,11,"A");

    private static final BlackjackRank[] RANKS;
    private static final int TOTAL_RANKS;

    static {
        Set<BlackjackRank> ranks = new LinkedHashSet<>();
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
        RANKS = ranks.toArray(new BlackjackRank[0]);
        TOTAL_RANKS = RANKS.length;
    }

    protected int value;

    protected BlackjackRank(int index, int value, String name) {
        super(index, name);
        this.value = value;
    }

    public BlackjackRank[] getRanks() {
        return RANKS;
    }

    public int getTotalRanks() {
        return TOTAL_RANKS;
    }

    public int getValue() {
        return value;
    }
    
}
