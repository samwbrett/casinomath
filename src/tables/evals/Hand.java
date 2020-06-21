package tables.evals;

import tables.cards.deck.Card;
import tables.cards.deck.Rank;
import tables.cards.deck.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Hand<R extends Rank, S extends Suit> {

    protected final List<Card<R,S>> cards;

    protected Hand() {
        this.cards = new ArrayList<>();
    }

    protected Hand(Card<R,S>... cards) {
        this.cards = new ArrayList<>(cards.length);
        Collections.addAll(this.cards, cards);
    }

    public int getNumCards() {
        return cards.size();
    }

    public Card<R,S>[] getCards() {
        return cards.toArray(new Card[0]);
    }

    public void addCard(Card<R,S> card) {
        cards.add(card);
    }

    public void removeCard(Card<R,S> card) {
        cards.remove(card);
    }

}
