package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.blackjack.calcs.BlackjackHand;
import tables.cards.deck.Card;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

class DealerOdds {
    
    private final Outcomes baseOutcomes;
    private final boolean dealerPeek;
    private final boolean hitSoft17;
    private final Map<UpCardShoe, Outcomes> outcomeOdds;

    public DealerOdds(boolean dealerPeek, boolean hitSoft17, Outcomes outcomes) {
        this.dealerPeek = dealerPeek;
        this.hitSoft17 = hitSoft17;
        this.baseOutcomes = outcomes;
        outcomeOdds = new HashMap<>();
    }

    public Outcomes chanceDealerEnd(Card upCard, BlackjackEnumeratorShoe shoe) {
        Outcomes probs = outcomeOdds.get(new UpCardShoe(upCard, shoe));
        if (probs != null) {
            return probs;
        }

        probs = baseOutcomes.copyOutcomes();
        Map<Card, Double> dealerDown = shoe.dealerDownProbs(upCard, dealerPeek);
        for (Card c : dealerDown.keySet()) {
            double cardProb = dealerDown.get(c);
            if (cardProb != 0) {
                BlackjackEnumeratorShoe copyShoe = shoe.copyShoe();
                BlackjackHand newHand = new BlackjackHand(upCard, c);
                copyShoe.removeCard(c);
                Outcomes newChance = chanceDealerEnd(newHand, copyShoe);
                for (int f = 0; f < probs.size(); f++)
                    probs.setOdds(f, probs.getOdds(f) + cardProb * newChance.getOdds(f));
            }
        }

        outcomeOdds.put(new UpCardShoe(upCard, shoe), probs);

        return probs;
    }

    private Outcomes chanceDealerEnd(BlackjackHand hand, BlackjackEnumeratorShoe shoe) {

        int total = hand.getValue();
        Outcomes probs;
        if (total < 17 || (hitSoft17 && total == 17 && hand.isSoft())) {

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

            // Factor out ranks
            double cardProbsShoe = shoe.getTotalCards();
            for (int f = 0; f < probsHit.size(); f++) {
                probsHit.setOdds(f, probsHit.getOdds(f) / cardProbsShoe);
            }

            return probsHit;
        } else {
            probs = baseOutcomes.copyOutcomes();
            for (int f = 0; f != probs.size(); f++) {
                if (probs.isHit(f, hand)) {
                    probs.setOdds(f, 1);
                    break;
                }
            }
        }

        return probs;
    }

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
            if (hand.getValue() < 17 || (hand.getValue() < 18 && hitSoft17 && hand.isSoft())) {
    
                List<RecursiveDealerEnd> dealerEnds = new ArrayList<>();
                for (Card c : shoe.getCards()) {
                    double cardProbCount = shoe.getCount(c);
                    if (cardProbCount != 0) {
                        BlackjackEnumeratorShoe copyShoe = shoe.copyShoe();
                        BlackjackHand newHand = new BlackjackHand(hand.getCards());
                        newHand.addCard(c);
                        copyShoe.removeCard(c);
                        dealerEnds.add(new RecursiveDealerEnd(newHand, copyShoe, cardProbCount));
                    }
                }
    
                Outcomes probsHit = baseOutcomes.copyOutcomes();
                Collection<RecursiveDealerEnd> invokedDealers = ForkJoinTask.invokeAll(dealerEnds);
                for (RecursiveDealerEnd dealerEnd : invokedDealers) {
                    Outcomes output = dealerEnd.join();
                    double cardProbCount = dealerEnd.cardProbCount;
                    for (int f = 0; f < probsHit.size(); f++) {
                        probsHit.setOdds(f, probsHit.getOdds(f) + cardProbCount * output.getOdds(f));
                    }
                }
    
                // Factor out ranks
                double cardProbsShoe = shoe.getTotalCards();
                for (int f = 0; f < probsHit.size(); f++) {
                    probsHit.setOdds(f, probsHit.getOdds(f) / cardProbsShoe);
                }
                return probsHit;
            } else {
                return chanceDealerEnd(hand, shoe);
            }
        }
    }
}
