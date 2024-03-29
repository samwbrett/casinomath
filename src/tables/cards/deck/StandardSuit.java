package tables.cards.deck;

import java.util.List;

/**
 * Most common card suit
 */
public enum StandardSuit implements Suit {

    SPADE(3,"s"),
    HEART(2,"h"),
    DIAMOND(1,"d"),
    CLUB(0,"c");

    private final int index;
    private final String name;

    StandardSuit(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() { return index; }

    public List<StandardSuit> getSuits() {
        return List.of(values());
    }

    public String getName() { return name; }
}
