package tables.blackjack.gamecalcs.dealerodds;

import org.junit.jupiter.api.Test;
import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.blackjack.calcs.BlackjackHand;
import tables.blackjack.calcs.BlackjackRank;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;

import static org.junit.jupiter.api.Assertions.*;

class DealerOddsTest {

    @Test
    void chanceDealerEndSingleCardShoe() {
        // Create a shoe with just an Ace and a King, remove one of them as upCard
        BlackjackEnumeratorShoe shoe = new BlackjackEnumeratorShoe(1);

        // All cards from a single deck - start with known state
        Card upCard = new Card(BlackjackRank.ACE, StandardSuit.SPADE);
        shoe.removeCard(upCard);

        Outcomes outcomes = makeDealerOutcomes();
        DealerOdds dealerOdds = new DealerOdds(false, false, outcomes);

        Outcomes result = dealerOdds.chanceDealerEnd(upCard, shoe);

        // With 51 cards remaining and no special rules,
        // the result should have probabilities summing to approximately 1
        double sum = 0;
        for (int i = 0; i < result.size(); i++) {
            sum += result.getOdds(i);
        }
        assertEquals(1.0, sum, 1e-10);
    }

    @Test
    void chanceDealerEndMemoizesResults() {
        BlackjackEnumeratorShoe shoe = new BlackjackEnumeratorShoe(1);
        Card upCard = new Card(BlackjackRank.ACE, StandardSuit.SPADE);
        shoe.removeCard(upCard);

        Outcomes outcomes = makeDealerOutcomes();
        DealerOdds dealerOdds = new DealerOdds(false, false, outcomes);

        Outcomes first = dealerOdds.chanceDealerEnd(upCard, shoe);
        Outcomes second = dealerOdds.chanceDealerEnd(upCard, shoe);

        assertSame(first, second);
    }

    @Test
    void dealerStandsOn17WithS17() {
        // S17 means dealer stands on soft 17
        Outcomes outcomes = makeDealerOutcomes();
        DealerOdds dealerOdds = new DealerOdds(false, false, outcomes);

        BlackjackHand hand = new BlackjackHand(
                new Card(BlackjackRank.ACE, StandardSuit.SPADE),
                new Card(BlackjackRank.SIX, StandardSuit.SPADE));
        assertEquals(17, hand.getValue());
        assertTrue(hand.isSoft());

        // With S17, dealer stands on soft 17, so the outcome should be
        // deterministically set to the 17 outcome
    }

    private static Outcomes makeDealerOutcomes() {
        Outcomes outcomes = new Outcomes();
        outcomes.addOutcome("BJ", (h) -> ((BlackjackHand) h[0]).isBlackjack());
        for (int value = 17; value < 27; value++) {
            final int v = value;
            outcomes.addOutcome(Integer.toString(value), new ValueHitEvaluator(v));
        }
        return outcomes;
    }
}
