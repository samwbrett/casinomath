package tables.cards.deck;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A shoe designed for enumerating exact combinatorial outcomes in card games.
 * <p>
 * Card counts are stored internally as a {@code Map<Card, Long>}, where each
 * card type maps to the number of remaining copies. This differs from a
 * traditional shoe that stores individual card instances, making it efficient
 * for enumeration algorithms that need to track remaining counts of unique
 * card types.
 * <p>
 * Thread safety is the responsibility of the caller. The copy constructor
 * and {@link #copyShoe()} method provide snapshot isolation for multithreaded
 * enumeration.
 */
public class EnumeratorShoe {

    protected final Map<Card, Long> counts;
    protected int cards;

    /**
     * Constructs a shoe populated with the specified number of decks.
     *
     * @param numDecks the number of standard decks to include
     * @param ranks    the ranks used in the game
     * @param suits    the suits used in the game
     */
    public EnumeratorShoe(int numDecks, List<? extends Rank> ranks, List<? extends Suit> suits) {
        counts = new HashMap<>();
        for (Rank rank : ranks) {
            for (Suit suit : suits) {
                counts.put(new Card(rank, suit), (long) numDecks);
                cards += numDecks;
            }
        }
    }

    /**
     * Copy constructor. Creates a deep copy of the shoe's internal state.
     *
     * @param shoe the shoe to copy
     */
    protected EnumeratorShoe(EnumeratorShoe shoe) {
        this.cards = shoe.cards;
        this.counts = new HashMap<>(shoe.counts);
    }

    /**
     * Creates an independent copy of this shoe. Subclasses should override
     * this to return the correct type.
     *
     * @return a new shoe with the same counts
     */
    public EnumeratorShoe copyShoe() {
        return new EnumeratorShoe(this);
    }

    @Override
    public int hashCode() {
        return counts.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EnumeratorShoe shoe && counts.equals(shoe.counts);
    }

    /**
     * Returns the set of card types that still have at least one copy remaining.
     *
     * @return an unmodifiable set of cards
     */
    public Set<Card> getCards() {
        return Collections.unmodifiableSet(counts.keySet());
    }

    /**
     * Returns the remaining count for a specific card type.
     *
     * @param c the card type to look up
     * @return the number of copies remaining, or 0 if none
     */
    public long getCount(Card c) {
        return counts.getOrDefault(c, 0L);
    }

    /**
     * Returns the probability of drawing a specific card type from the current shoe.
     *
     * @param c the card type to look up
     * @return the probability, between 0.0 and 1.0
     */
    public double getProb(Card c) {
        return getCount(c) / (double) cards;
    }

    /**
     * Returns the total number of cards remaining in the shoe.
     *
     * @return the total card count
     */
    public int getTotalCards() {
        return cards;
    }

    /**
     * Removes one copy of the specified card type from the shoe.
     *
     * @param c the card type to remove
     * @return true if a copy was present and removed, false otherwise
     */
    public boolean removeCard(Card c) {
        Long newVal = counts.computeIfPresent(c, (k, v) -> v - 1);
        if (newVal != null) {
            cards--;
            if (newVal == 0) {
                counts.remove(c);
            }
            return true;
        }
        return false;
    }

    /**
     * Adds one copy of the specified card type to the shoe.
     *
     * @param c the card type to add back
     */
    public void addCard(Card c) {
        counts.merge(c, 1L, Long::sum);
        cards++;
    }
}
