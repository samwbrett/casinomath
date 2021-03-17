package tables.cards.deck;

import java.util.*;

/**
 * Shoe used for enumerating exact outcomes. Card counts stored internally in a map.
 */
public class EnumeratorShoe<R extends Rank, S extends Suit> implements Cloneable {

    protected Map<Card<R,S>, Double> counts;
    protected int cards;

    public EnumeratorShoe(int numDecks, R[] ranks, S[] suits) {
        counts = new HashMap<>();
        for (R rank : ranks)  {
            for (S suit : suits) {
                counts.put(new Card<R,S>(rank, suit), (double)numDecks);
                cards += numDecks;
            }
        }
    }

    protected EnumeratorShoe(EnumeratorShoe<R,S> shoe) {
        this.cards = shoe.cards;
        this.counts = new HashMap<>(shoe.counts);
    }

    @Override
    public int hashCode() {
        return counts.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return counts.equals(((EnumeratorShoe)o).counts);
    }

    @Override
    public EnumeratorShoe<R,S> clone() {
        return new EnumeratorShoe<>(this);
    }

    public Set<Card<R,S>> getCards() {
        return counts.keySet();
    }

    public double getCount(Card<R,S> c) {
        return counts.getOrDefault(c, 0d);
    }

    public double getProb(Card<R,S> c) {
        return getCount(c) / cards;
    }

    public int getTotalCards() {
        return cards;
    }

    public boolean removeCard(Card<R,S> c) {
        if (counts.computeIfPresent(c, (k,v) -> v-1) != null) {
            cards--;
            if (counts.get(c) == 0) {
                counts.remove(c);
            }
            return true;
        }

        return false;
    }

    public void addCard(Card<R,S> c) {
        if (counts.computeIfPresent(c, (k,v) -> v+1) == null) {
            counts.put(c,1d);
        }
        cards++;
    }

}
