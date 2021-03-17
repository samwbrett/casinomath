package tables.cards.deck;

/**
 * Generic card suit
 */
public interface Suit {

    int getIndex();

    Suit[] getSuits();

    String getName();

    default int compareTo(Suit s) {
        return getIndex() - s.getIndex();
    }
}
