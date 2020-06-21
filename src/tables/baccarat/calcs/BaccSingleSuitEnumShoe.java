package tables.baccarat.calcs;

import tables.cards.deck.Suit;

public class BaccSingleSuitEnumShoe extends BaccEnumShoe {

    public BaccSingleSuitEnumShoe(int numDecks) {
        super(numDecks * Suit.SPADE.getTotalSuits(), BaccRank.TWO.getRanks(), new Suit[]{Suit.SPADE});
    }

    protected BaccSingleSuitEnumShoe(BaccEnumShoe shoe) {
        super(shoe);
    }

    @Override
    public BaccSingleSuitEnumShoe clone() {
        return new BaccSingleSuitEnumShoe(this);
    }
}
