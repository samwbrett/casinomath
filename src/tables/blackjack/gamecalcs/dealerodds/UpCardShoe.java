package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.cards.deck.Card;

class UpCardShoe {
    private final Card upCard;
    private final BlackjackEnumeratorShoe shoe;

    UpCardShoe(Card upCard, BlackjackEnumeratorShoe shoe) {
        this.upCard = upCard;
        this.shoe = shoe;
    }

    @Override
    public int hashCode() {
        return upCard.hashCode() * 7 + shoe.hashCode() * 3;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UpCardShoe
                && upCard.equals(((UpCardShoe) o).upCard)
                && shoe.equals(((UpCardShoe) o).shoe);
    }
}
