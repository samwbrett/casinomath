package tables.cards.deck;

import java.util.List;

/**
 * Generic card rank
 */
public interface Rank {

    int getIndex();

    default int getValue() {
        return getIndex();
    }

    List<? extends Rank> getRanks();

    String getName();

}
