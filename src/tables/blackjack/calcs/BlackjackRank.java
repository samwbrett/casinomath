package tables.blackjack.calcs;

import tables.cards.deck.Rank;

import java.util.List;

/**
 * Blackjack card ranks with standard blackjack values.
 * <p>
 * Numbered cards (2-10) are worth their face value, face cards (J, Q, K)
 * are worth 10, and Ace is worth 11 (soft aces are handled by
 * {@link BlackjackHand}).
 */
public enum BlackjackRank implements Rank {

    TWO(0, 2, "2"),
    THREE(1, 3, "3"),
    FOUR(2, 4, "4"),
    FIVE(3, 5, "5"),
    SIX(4, 6, "6"),
    SEVEN(5, 7, "7"),
    EIGHT(6, 8, "8"),
    NINE(7, 9, "9"),
    TEN(8, 10, "T"),
    JACK(9, 10, "J"),
    QUEEN(10, 10, "Q"),
    KING(11, 10, "K"),
    ACE(12, 11, "A");

    private final int index;
    private final int value;
    private final String name;

    BlackjackRank(int index, int value, String name) {
        this.index = index;
        this.value = value;
        this.name = name;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public List<BlackjackRank> getRanks() {
        return List.of(values());
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

