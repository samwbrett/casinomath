package tables.baccarat.calcs;

import tables.cards.deck.EnumShoe;
import tables.cards.deck.Suit;

public class BaccEnumShoe extends EnumShoe<BaccRank,Suit> {

    public BaccEnumShoe(int numDecks) {
        super(numDecks, BaccRank.TWO.getRanks(), Suit.SPADE.getSuits());
    }

    protected BaccEnumShoe(BaccEnumShoe shoe) {
        super(shoe);
    }

    protected BaccEnumShoe(int numDecks, BaccRank[] ranks, Suit[] suits) {
        super(numDecks, ranks, suits);
    }

    @Override
    public BaccEnumShoe clone() {
        return new BaccEnumShoe(this);
    }

}
