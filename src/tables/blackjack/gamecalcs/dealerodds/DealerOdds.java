package tables.blackjack.gamecalcs.dealerodds;

import tables.blackjack.calcs.BlackjackEnumeratorShoe;
import tables.blackjack.calcs.BlackjackHand;
import tables.blackjack.calcs.BlackjackRank;
import tables.cards.deck.Card;
import tables.cards.deck.Suit;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

class DealerOdds {
    
    private Outcomes baseOutcomes;
    private boolean dealerPeek;
    private boolean hitSoft17;
    private Map<UpCardShoe, Outcomes> outcomeOdds;

    public DealerOdds(boolean dealerPeek, boolean hitSoft17, Outcomes outcomes) {
        this.dealerPeek = dealerPeek;
        this.hitSoft17 = hitSoft17;
        this.baseOutcomes = outcomes;
        outcomeOdds = new HashMap<>();
    }

    public Outcomes chanceDealerEnd(Card<BlackjackRank, Suit> upCard, BlackjackEnumeratorShoe shoe) {
        Outcomes probs = outcomeOdds.get(new UpCardShoe(upCard, shoe));
        if (probs != null) {
            return probs;
        }

        probs = baseOutcomes.clone();
        Map<Card<BlackjackRank, Suit>, Double> dealerDown = shoe.dealerDownProbs(upCard, dealerPeek);
        for (Card<BlackjackRank, Suit> c : dealerDown.keySet()) {
            double cardProb = dealerDown.get(c);
            if (cardProb != 0) {
                BlackjackEnumeratorShoe copyShoe = shoe.clone();
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

            Outcomes probsHit = baseOutcomes.clone();
            for (Card<BlackjackRank,Suit> c : shoe.getCards()) {
                double cardProbCount = shoe.getCount(c);
                if (cardProbCount != 0) {
                    BlackjackEnumeratorShoe copyShoe = shoe.clone();
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
            probs = baseOutcomes.clone();
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

        private BlackjackHand hand;
        private BlackjackEnumeratorShoe shoe;
        private double cardProbCount;
    
        RecursiveDealerEnd(BlackjackHand hand, BlackjackEnumeratorShoe shoe, double cardProbCount) {
            this.hand = hand;
            this.shoe = shoe;
            this.cardProbCount = cardProbCount;
        }
    
        @Override
        protected Outcomes compute() {
            if (hand.getValue() < 17 || (hand.getValue() < 18 && hitSoft17 && hand.isSoft())) {
    
                List<RecursiveDealerEnd> dealerEnds = new ArrayList<>();
                for (Card<BlackjackRank, Suit> c : shoe.getCards()) {
                    double cardProbCount = shoe.getCount(c);
                    if (cardProbCount != 0) {
                        BlackjackEnumeratorShoe copyShoe = shoe.clone();
                        BlackjackHand newHand = new BlackjackHand(hand.getCards());
                        newHand.addCard(c);
                        copyShoe.removeCard(c);
                        dealerEnds.add(new RecursiveDealerEnd(newHand, copyShoe, cardProbCount));
                    }
                }
    
                Outcomes probsHit = baseOutcomes.clone();
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
