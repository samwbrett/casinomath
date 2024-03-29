package tables.blackjack.calcs;

import tables.cards.deck.Rank;

import java.util.List;

/**
 * Blackjack rank with standard values. Ace here is counted as 11.
 */
public enum BlackjackRank implements Rank {

    TWO(0,2,"2"),
    THREE(1,3,"3"),
    FOUR(2,4,"4"),
    FIVE(3,5,"5"),
    SIX(4,6,"6"),
    SEVEN(5,7,"7"),
    EIGHT(6,8,"8"),
    NINE(7,9, "9"),
    TEN(8,10,"T"),
    JACK(9,10,"J"),
    QUEEN(10,10,"Q"),
    KING(11,10,"K"),
    ACE(12,11,"A");

    private final int index;
    private final int value;
    private final String name;

    BlackjackRank(int index, int value, String name) {
        this.index = index;
        this.value = value;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public List<BlackjackRank> getRanks() {
        return List.of(values());
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
    
}
