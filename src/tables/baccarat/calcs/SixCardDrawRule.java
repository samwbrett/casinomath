package tables.baccarat.calcs;

import tables.cards.deck.Suit;

public class SixCardDrawRule<R extends BaccRank, S extends Suit> {

    public SixCardDrawRule() { }

    // Banker value and player third card
    public static final boolean[][] SIX_CARD_DRAW = new boolean[10][10];

    static {
        // Set up six card draw
        for (int bScore = 0; bScore != 10; bScore++)
            for (int pThird = 0; pThird != 10; pThird++)
                SIX_CARD_DRAW[bScore][pThird] = bScore <= 2 ||
                        (bScore == 3 && pThird != 8) ||
                        (bScore == 4 && pThird >= 2 && pThird <= 7) ||
                        (bScore == 5 && pThird >= 4 && pThird <= 7) ||
                        (bScore == 6 && pThird >= 6 && pThird <= 7);
    }

    public boolean isDraw(BaccHand<R, S> playerHand, BaccHand<R, S> bankerHand) {
        return playerHand.getNumCards() == 3 && bankerHand.getNumCards() == 2 &&
                SIX_CARD_DRAW[bankerHand.getValue()][playerHand.getCards()[2].getRank().getValue()];
    }

    public boolean isDraw(int bankerScore, int playerThird) {
        return SIX_CARD_DRAW[bankerScore][playerThird];
    }

}
