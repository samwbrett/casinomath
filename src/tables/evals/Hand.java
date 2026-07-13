package tables.evals;

import tables.cards.deck.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class representing a hand of cards in a casino game.
 * <p>
 * The cards in a hand are stored in insertion order and duplicates are allowed
 * (e.g., multiple copies of the same card type across different hands).
 * Subclasses add game-specific logic such as hand value evaluation.
 */
public abstract class Hand {

    protected final List<Card> cards;

    protected Hand() {
        this.cards = new ArrayList<>();
    }

    protected Hand(Card... cards) {
        this();
        Collections.addAll(this.cards, cards);
    }

    protected Hand(List<Card> cards) {
        this();
        this.cards.addAll(cards);
    }

    /** Returns the number of cards currently in this hand. */
    public int getNumCards() {
        return cards.size();
    }

    /** Returns an unmodifiable view of the cards in this hand. */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    /** Adds a card to this hand. */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Removes the first occurrence of the specified card from this hand.
     *
     * @param card the card to remove
     * @return true if a card was removed
     */
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }
}
