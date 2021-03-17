package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.blackjack.calcs.BlackjackRank;
import tables.cards.deck.Card;
import tables.cards.deck.Suit;

class UpCardShoe {
    private Card<BlackjackRank, Suit> upCard;
    private BlackjackEnumeratorShoe shoe;

    UpCardShoe(Card<BlackjackRank, Suit> upCard, BlackjackEnumeratorShoe shoe) {
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
