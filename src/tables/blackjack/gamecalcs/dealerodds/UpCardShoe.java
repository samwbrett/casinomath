package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.cards.deck.Card;

import java.util.Objects;

/**
 * Composite key for caching dealer outcome calculations.
 * <p>
 * Combines an up card with a shoe state to serve as a key in the
 * memoization map used by {@link DealerOdds}. This avoids redundant
 * computation when the same up card and shoe state are encountered
 * multiple times during recursive enumeration.
 */
class UpCardShoe {
    private final Card upCard;
    private final BlackjackEnumeratorShoe shoe;

    UpCardShoe(Card upCard, BlackjackEnumeratorShoe shoe) {
        this.upCard = upCard;
        this.shoe = shoe;
    }

    @Override
    public int hashCode() {
        return Objects.hash(upCard, shoe);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UpCardShoe other
                && upCard.equals(other.upCard)
                && shoe.equals(other.shoe);
    }
}

