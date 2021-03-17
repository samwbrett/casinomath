package tables.cards.deck;

public class Card<R extends Rank, S extends Suit> implements Comparable<Card<R, S>> {

    private final int number;
    private final R rank;
    private final S suit;

    public Card(R rank, S suit) {
        this.rank = rank;
        this.suit = suit;
        this.number = suit.getIndex() * rank.getRanks().length + rank.getIndex();
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
