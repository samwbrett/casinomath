package tables.blackjack.calcs;

import tables.cards.deck.Card;
import tables.cards.deck.EnumeratorShoe;
import tables.cards.deck.StandardSuit;
import tables.cards.deck.Suit;

import java.util.HashMap;
import java.util.Map;

/**
 * Blackjack enumerator shoe with functionality for adjusting for dealer peek.
 */
public class BlackjackEnumeratorShoe extends EnumeratorShoe<BlackjackRank, Suit> {
    
    public BlackjackEnumeratorShoe(int numDecks) {
        super(numDecks, BlackjackRank.values(), StandardSuit.values());
    }

    protected BlackjackEnumeratorShoe(BlackjackEnumeratorShoe shoe) {
        super(shoe);
    }

    protected BlackjackEnumeratorShoe(int numDecks, BlackjackRank[] ranks, Suit[] suits) {
        super(numDecks, ranks, suits);
    }

    @Override
    public BlackjackEnumeratorShoe clone() {
        return new BlackjackEnumeratorShoe(this);
    }

    public Map<Card<BlackjackRank, Suit>, Double> dealerDownProbs(Card<BlackjackRank,Suit> dealerUp, boolean denormalize) {
        int dealerUpValue = dealerUp.getRank().getValue();

        // Get dealer probability map from denormalized probabilities
        Map<Card<BlackjackRank, Suit>, Double> dealerProbs = new HashMap<>(counts);
        double dealerCardCount = cards;
        // Get dealer counts
        if (denormalize) {
            int secondValue = dealerUpValue == 10 ? 11 : (dealerUpValue == 11 ? 10 : -1);
            for (Card<BlackjackRank, Suit> c : counts.keySet())
                if (c.getRank().getValue() == secondValue) {
                    dealerProbs.remove(c);
                    dealerCardCount -= counts.get(c);
                }
        }
        // Get dealer probabilities
        for (Card<BlackjackRank, Suit> c : dealerProbs.keySet())
            dealerProbs.put(c, counts.get(c) / dealerCardCount);

        return dealerProbs;
    }
    
}
