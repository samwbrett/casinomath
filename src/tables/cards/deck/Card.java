package tables.cards.deck;

public class Card {

    private final int number;
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.number = suit.getIndex() * rank.getRanks().size() + rank.getIndex();
    }

    public int getNumber() { return number; }

    public Suit getSuit() { return suit; }

    public Rank getRank() { return rank; }

    @Override
    public int hashCode() { return number; }

    @Override
    public boolean equals(Object o) { return o instanceof Card && this.number == ((Card)o).number; }

    @Override
    public String toString() { return rank.toString() + suit.toString(); }

}
