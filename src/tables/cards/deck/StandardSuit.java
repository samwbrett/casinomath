package tables.cards.deck;

import java.util.List;

/**
 * The four standard French playing card suits: spades, hearts, diamonds, and clubs.
 * <p>
 * Each suit has a numeric index (used for card identification) and a single-character
 * display name.
 */
public enum StandardSuit implements Suit {

    SPADE(3, "s"),
    HEART(2, "h"),
    DIAMOND(1, "d"),
    CLUB(0, "c");

    private final int index;
    private final String name;

    StandardSuit(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public List<StandardSuit> getSuits() {
        return List.of(values());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

