package tables.cards.deck;

/**
 * Generic card rank
 */
public interface Rank {

    int getIndex();

    default int getValue() {
        return getIndex();
    }

    Rank[] getRanks();

    String getName();

    default int compareTo(Rank rank) {
        return getIndex() - rank.getIndex();
    }

}
