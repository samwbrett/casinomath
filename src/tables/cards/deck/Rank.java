package tables.cards.deck;

import java.util.List;

/**
 * Represents the rank of a playing card.
 * <p>
 * Implementations provide an index for ordering, a value for game calculations,
 * the list of all ranks in the system, and a display name. The default
 * implementation of {@link #getValue()} returns the same value as {@link #getIndex()},
 * but game-specific ranks (e.g., blackjack, baccarat) may override this.
 */
public interface Rank {

    /**
     * Returns the positional index of this rank within the rank system.
     *
     * @return the zero-based index
     */
    int getIndex();

    /**
     * Returns the game value of this rank. By default, this is the same as
     * {@link #getIndex()}, but subclasses may override for game-specific values
     * (e.g., face cards worth 0 in baccarat or 10 in blackjack).
     *
     * @return the game value
     */
    default int getValue() {
        return getIndex();
    }

    /**
     * Returns the full list of ranks defined in this rank system.
     *
     * @return an unmodifiable list of all ranks
     */
    List<? extends Rank> getRanks();

    /**
     * Returns the display name of this rank (e.g., "A", "K", "3").
     *
     * @return the display name
     */
    String getName();
}

