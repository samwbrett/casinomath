package tables.cards.deck;

import java.util.*;

/**
 * Shoe used for enumerating exact outcomes. Card counts stored internally in a map.
 */
public class EnumeratorShoe {

    protected final Map<Card, Long> counts;
    protected int cards;

    public EnumeratorShoe(int numDecks, List<? extends Rank> ranks, List<? extends Suit> suits) {
        counts = new HashMap<>();
        for (Rank rank : ranks)  {
            for (Suit suit : suits) {
                counts.put(new Card(rank, suit), (long)numDecks);
                cards += numDecks;
            }
        }
    }

    protected EnumeratorShoe(EnumeratorShoe shoe) {
        this.cards = shoe.cards;
        this.counts = new HashMap<>(shoe.counts);
    }

    public EnumeratorShoe copyShoe() {
        return new EnumeratorShoe(this);
    }

    @Override
    public int hashCode() {
        return counts.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EnumeratorShoe && counts.equals(((EnumeratorShoe)o).counts);
    }

    public Set<Card> getCards() {
        return counts.keySet();
    }

    public long getCount(Card c) {
        return counts.getOrDefault(c, 0L);
    }

    public double getProb(Card c) {
        return getCount(c) / (double)cards;
    }

    public int getTotalCards() {
        return cards;
    }

    public boolean removeCard(Card c) {
        if (counts.computeIfPresent(c, (k,v) -> v-1) != null) {
            cards--;
            if (counts.get(c) == 0) {
                counts.remove(c);
            }
            return true;
        }

        return false;
    }

    public void addCard(Card c) {
        if (counts.computeIfPresent(c, (k,v) -> v+1) == null) {
            counts.put(c, 1L);
        }
        cards++;
    }

}
