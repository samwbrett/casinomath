package tables.cards.deck;

import java.util.Comparator;

public class Card<R extends Rank, S extends Suit> implements Comparable<Card<R,S>> {

    public static final Comparator<Card> RANK_COMPARATOR = Comparator.comparing((Card o) -> o.rank);
    public static final Comparator<Card> SUIT_COMPARATOR = Comparator.comparing(o -> o.suit);
    public static final Comparator<Card> RANK_SUIT_COMPARATOR = Comparator.comparing((Card o) -> o.rank).thenComparing(o -> o.suit);

    private final int number;
    private final R rank;
    private final S suit;

    public Card(R rank, S suit) {
        this.rank = rank;
        this.suit = suit;
        this.number = suit.getIndex() * rank.getTotalRanks() + rank.getIndex();
    }

    public Card(int number, Rank dummyRank, Suit dummySuit) {
        this.number = number;
        this.rank = (R) dummyRank.getRanks()[number % dummyRank.getTotalRanks()];
        this.suit = (S) dummySuit.getSuits()[number / dummyRank.getTotalRanks()];
    }

    public int getNumber()
    {
        return number;
    }

    public S getSuit() { return suit; }

    public R getRank() { return rank; }

    @Override
    public int hashCode() { return number; }

    @Override
    public int compareTo(Card o) { return number - o.number; }

    @Override
    public boolean equals(Object o) { return o instanceof Card && compareTo((Card)o) == 0; }

    @Override
    public String toString() { return rank.toString() + suit.toString(); }

}
