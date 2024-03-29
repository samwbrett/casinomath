package tables.blackjack.calcs;

import tables.cards.deck.Card;
import tables.cards.deck.EnumeratorShoe;
import tables.cards.deck.StandardSuit;
import tables.cards.deck.Suit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Blackjack enumerator shoe with functionality for adjusting for dealer peek.
 */
public class BlackjackEnumeratorShoe extends EnumeratorShoe {
    
    public BlackjackEnumeratorShoe(int numDecks) {
        super(numDecks, List.of(BlackjackRank.values()), List.of(StandardSuit.values()));
    }

    protected BlackjackEnumeratorShoe(BlackjackEnumeratorShoe shoe) {
        super(shoe);
    }

    protected BlackjackEnumeratorShoe(int numDecks, List<BlackjackRank> ranks, List<Suit> suits) {
        super(numDecks, ranks, suits);
    }

    public BlackjackEnumeratorShoe copyShoe() {
        return new BlackjackEnumeratorShoe(this);
    }

    public Map<Card, Double> dealerDownProbs(Card dealerUp, boolean denormalize) {
        int dealerUpValue = dealerUp.getRank().getValue();

        // Get dealer probability map from denormalized probabilities
        Map<Card, Double> dealerProbs = counts
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> Double.longBitsToDouble(e.getValue())));

        double dealerCardCount = cards;
        // Get dealer counts
        if (denormalize) {
            int secondValue = dealerUpValue == 10 ? 11 : (dealerUpValue == 11 ? 10 : -1);
            for (Card c : counts.keySet())
                if (c.getRank().getValue() == secondValue) {
                    dealerProbs.remove(c);
                    dealerCardCount -= counts.get(c);
                }
        }
        // Get dealer probabilities
        for (Card c : dealerProbs.keySet())
            dealerProbs.put(c, counts.get(c) / dealerCardCount);

        return dealerProbs;
    }
    
}
