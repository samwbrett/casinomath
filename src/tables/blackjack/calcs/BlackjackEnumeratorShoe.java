package tables.blackjack.calcs;

import tables.cards.deck.Card;
import tables.cards.deck.EnumeratorShoe;
import tables.cards.deck.StandardSuit;
import tables.cards.deck.Suit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A blackjack-specific enumerator shoe that extends {@link EnumeratorShoe} with
 * functionality for computing dealer hole card probabilities, including adjustments
 * for games where the dealer peeks for blackjack.
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

    @Override
    public BlackjackEnumeratorShoe copyShoe() {
        return new BlackjackEnumeratorShoe(this);
    }

    /**
     * Computes the probability distribution for the dealer's hole card given a known up card.
     * <p>
     * When {@code denormalize} is true, this method adjusts for the case where the dealer
     * peeks for blackjack. If the dealer's up card is a ten-value card (value 10), it removes
     * aces (value 11) from the probability calculation since a blackjack would end the hand.
     * Conversely, if the up card is an ace, it removes ten-value cards.
     *
     * @param dealerUp   the dealer's face-up card
     * @param denormalize whether to adjust for dealer peek (remove the other blackjack card)
     * @return a map from possible hole cards to their conditional probabilities
     */
    public Map<Card, Double> dealerDownProbs(Card dealerUp, boolean denormalize) {
        int dealerUpValue = dealerUp.getRank().getValue();

        // Initialize dealer probabilities as a copy of the shoe counts (as doubles)
        Map<Card, Double> dealerProbs = new HashMap<>();
        for (Map.Entry<Card, Long> entry : counts.entrySet()) {
            dealerProbs.put(entry.getKey(), (double) entry.getValue());
        }

        double dealerCardCount = cards;
        // Dealer peek adjustment: remove the complementary blackjack card from consideration
        if (denormalize) {
            int secondValue = dealerUpValue == 10 ? 11 : (dealerUpValue == 11 ? 10 : -1);
            if (secondValue != -1) {
                for (Card c : counts.keySet()) {
                    if (c.getRank().getValue() == secondValue) {
                        dealerProbs.remove(c);
                        dealerCardCount -= counts.get(c);
                    }
                }
            }
        }

        // Normalize to probabilities
        for (Card c : dealerProbs.keySet()) {
            dealerProbs.put(c, counts.get(c) / dealerCardCount);
        }

        return dealerProbs;
    }

}

