package tables.cards.deck;

import java.util.List;

/**
 * Generic card suit
 */
public interface Suit {

    int getIndex();

    List<? extends Suit> getSuits();

    String getName();
}
