package tables.cards.deck;

import java.util.LinkedHashSet;
import java.util.Set;

public class Suit implements Comparable<Suit> {

    public static Suit SPADE = new Suit(3,"s");
    public static Suit HEART = new Suit(2,"h");
    public static Suit DIAMOND = new Suit(1,"d");
    public static Suit CLUB = new Suit(0,"c");

    private static Suit[] SUITS;
    protected static int TOTAL_SUITS;

    static {
        Set<Suit> suits = new LinkedHashSet<>();
        suits.add(SPADE);
        suits.add(HEART);
        suits.add(DIAMOND);
        suits.add(CLUB);
        SUITS = suits.toArray(new Suit[0]);
        TOTAL_SUITS = SUITS.length;
    }

    protected final int index;
    protected final String name;

    protected Suit(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() { return index; }

    public String getName() { return name; }

    public Suit[] getSuits() {
        return SUITS;
    }

    public int getTotalSuits() {
        return TOTAL_SUITS;
    }

    @Override
    public int compareTo(Suit o) {
        return index - o.getIndex();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Suit && compareTo((Suit)o) == 0;
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
