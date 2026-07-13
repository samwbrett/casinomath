package tables.cards.deck;

import java.util.List;

/**
 * Represents the suit of a playing card.
 * <p>
 * Implementations provide an index for ordering, the list of all suits in the
 * system, and a display name.
 */
public interface Suit {

    /**
     * Returns the positional index of this suit within the suit system.
     *
     * @return the zero-based index
     */
    int getIndex();

    /**
     * Returns the full list of suits defined in this suit system.
     *
     * @return an unmodifiable list of all suits
     */
    List<? extends Suit> getSuits();

    /**
     * Returns the display name of this suit (e.g., "s" for spades).
     *
     * @return the display name
     */
    String getName();
}

