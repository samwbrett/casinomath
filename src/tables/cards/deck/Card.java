package tables.cards.deck;

import java.util.Objects;

/**
 * Represents a single playing card composed of a {@link Rank} and a {@link Suit}.
 * <p>
 * Cards are immutable and uniquely identified by a computed number derived from
 * the suit index and the number of ranks, enabling efficient {@code hashCode}
 * and {@code equals} implementations.
 */
public class Card {

    private final int number;
    private final Rank rank;
    private final Suit suit;

    /**
     * Constructs a card with the given rank and suit.
     * The card's unique number is computed as:
     * {@code suit.getIndex() * rank.getRanks().size() + rank.getIndex()}.
     *
     * @param rank the rank of the card (must not be null)
     * @param suit the suit of the card (must not be null)
     */
    public Card(Rank rank, Suit suit) {
        this.rank = Objects.requireNonNull(rank, "rank must not be null");
        this.suit = Objects.requireNonNull(suit, "suit must not be null");
        this.number = suit.getIndex() * rank.getRanks().size() + rank.getIndex();
    }

    /** Returns the unique numeric identifier for this card. */
    public int getNumber() {
        return number;
    }

    /** Returns the suit of this card. */
    public Suit getSuit() {
        return suit;
    }

    /** Returns the rank of this card. */
    public Rank getRank() {
        return rank;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Card card && this.number == card.number;
    }

    @Override
    public String toString() {
        return rank.toString() + suit.toString();
    }
}

