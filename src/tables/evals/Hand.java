package tables.evals;

import tables.cards.deck.Card;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Hand holds the cards in a hand. The cards do not have to be unique.
 */
public abstract class Hand {

    protected final List<Card> cards;

    protected Hand() {
        this.cards = new LinkedList<>();
    }

    protected Hand(Card... cards) {
        this();
        Collections.addAll(this.cards, cards);
    }

    protected Hand(List<Card> cards) {
        this();
        this.cards.addAll(cards);
    }

    public int getNumCards() {
        return cards.size();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

}
