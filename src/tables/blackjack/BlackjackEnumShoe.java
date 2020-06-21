package tables.blackjack;

import tables.cards.deck.Card;
import tables.cards.deck.EnumShoe;
import tables.cards.deck.Suit;

import java.util.TreeMap;

public class BlackjackEnumShoe extends EnumShoe<BlackjackRank, Suit> {
    
    public BlackjackEnumShoe(int numDecks) {
        super(numDecks, BlackjackRank.ACE.getRanks(), Suit.SPADE.getSuits());
    }

    protected BlackjackEnumShoe(int numDecks, BlackjackRank[] ranks, Suit[] suits) {
        super(numDecks, ranks, suits);
    }

    protected BlackjackEnumShoe(BlackjackEnumShoe shoe) {
        super(shoe);
    }

    @Override
    public BlackjackEnumShoe clone() {
        return new BlackjackEnumShoe(this);
    }

    public TreeMap<Card<BlackjackRank, Suit>, Double> dealerDownProbs(Card<BlackjackRank,Suit> dealerUp, boolean denormalize) {
        int dealerUpValue = dealerUp.getRank().getValue();
        TreeMap<Card<BlackjackRank,Suit>, Double> dealerProbs;

        // Get dealer probability map from denormalized probabilities
        dealerProbs = new TreeMap<>(counts);
        double dealerCardCount = cards;
        // Get dealer counts
        if (denormalize) {
            int secondValue = dealerUpValue == 10 ? 11 : (dealerUpValue == 11 ? 10 : -1);
            for (Card<BlackjackRank,Suit> c : counts.keySet())
                if (c.getRank().getValue() == secondValue) {
                    dealerProbs.remove(c);
                    dealerCardCount -= counts.get(c);
                }
        }
        // Get dealer probabilities
        for (Card<BlackjackRank,Suit> c : dealerProbs.keySet())
            dealerProbs.put(c, counts.get(c) / dealerCardCount);

        return dealerProbs;
    }
    
}
