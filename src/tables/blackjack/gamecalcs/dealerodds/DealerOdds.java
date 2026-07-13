package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.blackjack.calcs.BlackjackHand;
import tables.cards.deck.Card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Computes the probability distribution of a dealer's final hand outcome
 * in blackjack through recursive enumeration over all possible hole cards
 * and drawing decisions.
 * <p>
 * Results are memoized using {@link UpCardShoe} keys to avoid redundant
 * computation. The enumeration supports both dealer peek and no-peek rules
 * as well as the hit-on-soft-17 (H17) vs. stand-on-soft-17 (S17) variants.
 */
class DealerOdds {

    private final Outcomes baseOutcomes;
    private final boolean dealerPeek;
    private final boolean hitSoft17;
    private final Map<UpCardShoe, Outcomes> outcomeOdds;

    /**
     * @param dealerPeek whether the dealer peeks for blackjack
     * @param hitSoft17  whether the dealer hits on soft 17 (H17 vs S17)
     * @param outcomes   the outcome definitions to compute probabilities for
     */
    public DealerOdds(boolean dealerPeek, boolean hitSoft17, Outcomes outcomes) {
        this.dealerPeek = dealerPeek;
        this.hitSoft17 = hitSoft17;
        this.baseOutcomes = outcomes;
        this.outcomeOdds = new HashMap<>();
    }

    /**
     * Computes the outcome distribution for a known dealer up card and shoe state.
     * Results are cached for repeated calls with the same inputs.
     *
     * @param upCard the dealer's face-up card
     * @param shoe   the current shoe state
     * @return the probability distribution over defined outcomes
     */
    public Outcomes chanceDealerEnd(Card upCard, BlackjackEnumeratorShoe shoe) {
        UpCardShoe key = new UpCardShoe(upCard, shoe);
        Outcomes cached = outcomeOdds.get(key);
        if (cached != null) {
            return cached;
        }

        Outcomes probs = baseOutcomes.copyOutcomes();
        Map<Card, Double> dealerDown = shoe.dealerDownProbs(upCard, dealerPeek);
        for (Map.Entry<Card, Double> entry : dealerDown.entrySet()) {
            double cardProb = entry.getValue();
            if (cardProb != 0) {
                BlackjackEnumeratorShoe copyShoe = shoe.copyShoe();
                BlackjackHand newHand = new BlackjackHand(upCard, entry.getKey());
                copyShoe.removeCard(entry.getKey());
                Outcomes newChance = chanceDealerEnd(newHand, copyShoe);
                for (int f = 0; f < probs.size(); f++) {
                    probs.setOdds(f, probs.getOdds(f) + cardProb * newChance.getOdds(f));
                }
            }
        }

        outcomeOdds.put(key, probs);
        return probs;
    }

    /**
     * Recursively computes the outcome distribution given a partial dealer hand.
     * If the dealer must hit (value < 17, or soft 17 with H17), it enumerates
     * all possible next cards and combines the results.
     * <p>
     * When the dealer stands, the outcome that matches the final hand value is set to 1.
     */
    private Outcomes chanceDealerEnd(BlackjackHand hand, BlackjackEnumeratorShoe shoe) {
        if (mustHit(hand.getValue(), hand)) {
            Outcomes probsHit = baseOutcomes.copyOutcomes();
            for (Card c : shoe.getCards()) {
                double cardProbCount = shoe.getCount(c);
                if (cardProbCount != 0) {
                    BlackjackEnumeratorShoe copyShoe = shoe.copyShoe();
                    BlackjackHand newHand = new BlackjackHand(hand.getCards());
                    newHand.addCard(c);
                    copyShoe.removeCard(c);
                    Outcomes newChance = new RecursiveDealerEnd(newHand, copyShoe, cardProbCount).compute();
                    for (int f = 0; f < probsHit.size(); f++) {
                        probsHit.setOdds(f, probsHit.getOdds(f) + cardProbCount * newChance.getOdds(f));
                    }
                }
            }

            // Normalize by total cards
            double totalCards = shoe.getTotalCards();
            for (int f = 0; f < probsHit.size(); f++) {
                probsHit.setOdds(f, probsHit.getOdds(f) / totalCards);
            }
            return probsHit;
        } else {
            Outcomes probs = baseOutcomes.copyOutcomes();
            for (int f = 0; f < probs.size(); f++) {
                if (probs.isHit(f, hand)) {
                    probs.setOdds(f, 1);
                    break;
                }
            }
            return probs;
        }
    }

    private boolean mustHit(int total, BlackjackHand hand) {
        return total < 17 || (hitSoft17 && total == 17 && hand.isSoft());
    }

    /**
     * Recursive task for fork-join parallelization of the dealer hit enumeration.
     * When the dealer must hit, this task spawns subtasks for each possible
     * next card and combines their results.
     */
    @SuppressWarnings("serial")
    class RecursiveDealerEnd extends RecursiveTask<Outcomes> {

        private final BlackjackHand hand;
        private final BlackjackEnumeratorShoe shoe;
        private final double cardProbCount;

        RecursiveDealerEnd(BlackjackHand hand, BlackjackEnumeratorShoe shoe, double cardProbCount) {
            this.hand = hand;
            this.shoe = shoe;
            this.cardProbCount = cardProbCount;
        }

        @Override
        protected Outcomes compute() {
            if (mustHit(hand.getValue(), hand)) {
                List<RecursiveDealerEnd> dealerEnds = new ArrayList<>();
                for (Card c : shoe.getCards()) {
                    double count = shoe.getCount(c);
                    if (count != 0) {
                        BlackjackEnumeratorShoe copyShoe = shoe.copyShoe();
                        BlackjackHand newHand = new BlackjackHand(hand.getCards());
                        newHand.addCard(c);
                        copyShoe.removeCard(c);
                        dealerEnds.add(new RecursiveDealerEnd(newHand, copyShoe, count));
                    }
                }

                Outcomes probsHit = baseOutcomes.copyOutcomes();
                Collection<RecursiveDealerEnd> invokedDealers = ForkJoinTask.invokeAll(dealerEnds);
                for (RecursiveDealerEnd dealerEnd : invokedDealers) {
                    Outcomes output = dealerEnd.join();
                    double count = dealerEnd.cardProbCount;
                    for (int f = 0; f < probsHit.size(); f++) {
                        probsHit.setOdds(f, probsHit.getOdds(f) + count * output.getOdds(f));
                    }
                }

                double totalCards = shoe.getTotalCards();
                for (int f = 0; f < probsHit.size(); f++) {
                    probsHit.setOdds(f, probsHit.getOdds(f) / totalCards);
                }
                return probsHit;
            } else {
                return chanceDealerEnd(hand, shoe);
            }
        }
    }
}
