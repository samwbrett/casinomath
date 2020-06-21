package tables.blackjack;

import tables.cards.deck.Card;
import tables.cards.deck.Suit;
import tables.evals.HitEvaluator;
import util.Pair;

import java.util.*;
import java.util.concurrent.*;

public class DealerOdds {

    static class ValueHitEvaluator implements HitEvaluator<BlackjackHand> {

        private int value;

        private ValueHitEvaluator(int value) {
            this.value = value;
        }

        @Override
        public boolean isHit(BlackjackHand... hands) {
            return hands[0].getValue() == value;
        }
    }
    
    static class Outcomes {

        private List<Outcome> outcomes;

        private Outcomes(Outcome[] outcomes) {
            this.outcomes = new ArrayList<>(outcomes.length);
            Arrays.stream(outcomes).forEach(o -> this.outcomes.add(new Outcome(o.situation, o.eval)));
        }

        private int size() {
            return outcomes.size();
        }

        private static Outcomes average(Outcomes[] outcomes) {
            Outcomes averaged = new Outcomes(outcomes[0].outcomes.toArray(new Outcome[0]));
            for (Outcomes os : outcomes) {
                for (int f = 0; f != averaged.size(); f++) {
                    averaged.outcomes.get(f).odds += os.outcomes.get(f).odds / outcomes.length;
                }
            }
            return averaged;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Situation\tOdds");
            outcomes.forEach(o -> builder.append("\n").append(o.toString()));
            return builder.toString();
        }
    }

    static class Outcome {
        private String situation;
        private double odds;
        private HitEvaluator<BlackjackHand> eval;

        private Outcome(String situation, HitEvaluator<BlackjackHand> eval) {
            this.situation = situation;
            this.eval = eval;
        }

        @Override
        public String toString() {
            return situation + "\t" + odds;
        }
    }

    private Outcome[] baseOutcomes;
    private boolean dealerPeek;
    private boolean hitSoft17;
    private Map<Pair<Card<BlackjackRank,Suit>,BlackjackEnumShoe>, Outcomes> outcomeOdds;

    public DealerOdds(boolean dealerPeek, boolean hitSoft17, Outcome... outcomes) {
        this.dealerPeek = dealerPeek;
        this.hitSoft17 = hitSoft17;
        this.baseOutcomes = outcomes;
        outcomeOdds = new HashMap<>();
    }

    public Outcomes chanceDealerEnd(Card<BlackjackRank,Suit> upCard, BlackjackEnumShoe shoe) {
        try {
            Outcomes probs = outcomeOdds.get(Pair.pairOf(upCard, shoe));
            if (probs != null) {
                return probs;
            }

            probs = new Outcomes(baseOutcomes);
            TreeMap<Card<BlackjackRank, Suit>, Double> dealerDown = shoe.dealerDownProbs(upCard, dealerPeek);
            for (Card<BlackjackRank, Suit> c : dealerDown.keySet()) {
                double cardProb = dealerDown.get(c);
                if (cardProb != 0) {
                    BlackjackEnumShoe copyShoe = shoe.clone();
                    BlackjackHand newHand = new BlackjackHand(upCard, c);
                    copyShoe.removeCard(c);
                    Outcomes newChance = chanceDealerEnd(newHand, copyShoe);
                    for (int f = 0; f < probs.outcomes.size(); f++)
                        probs.outcomes.get(f).odds += cardProb * newChance.outcomes.get(f).odds;
                }
            }

            outcomeOdds.put(Pair.pairOf(upCard, shoe), probs);

            return probs;
        } catch (Exception e) {
            System.out.println("ERRRORORO");
            return null;
        }
    }

    private class RecursiveDealerEnd extends RecursiveTask<Outcomes> {

        private BlackjackHand hand;
        private BlackjackEnumShoe shoe;
        private double cardProbCount;

        private RecursiveDealerEnd(BlackjackHand hand, BlackjackEnumShoe shoe, double cardProbCount) {
            this.hand = hand;
            this.shoe = shoe;
            this.cardProbCount = cardProbCount;
        }

        @Override
        protected Outcomes compute() {
            if (hand.getValue() < 17 || (hand.getValue() < 18 && hitSoft17 && hand.isSoft())) {

                List<RecursiveDealerEnd> dealerEnds = new ArrayList<>();
                for (Card<BlackjackRank,Suit> c : shoe.getCards()) {
                    double cardProbCount = shoe.getCount(c);
                    if (cardProbCount != 0) {
                        BlackjackEnumShoe copyShoe = shoe.clone();
                        BlackjackHand newHand = new BlackjackHand(hand.getCards());
                        newHand.addCard(c);
                        copyShoe.removeCard(c);
                        dealerEnds.add(new RecursiveDealerEnd(newHand, copyShoe, cardProbCount));
                    }
                }

                Outcomes probsHit = new Outcomes(baseOutcomes);
                Collection<RecursiveDealerEnd> invokedDealers = ForkJoinTask.invokeAll(dealerEnds);
                for (RecursiveDealerEnd dealerEnd : invokedDealers) {
                    Outcomes output = dealerEnd.join();
                    double cardProbCount = dealerEnd.cardProbCount;
                    for (int f = 0; f < probsHit.size(); f++) {
                        probsHit.outcomes.get(f).odds += cardProbCount * output.outcomes.get(f).odds;
                    }
                }

                // Factor out ranks
                double cardProbsShoe = shoe.getTotalCards();
                for (int f = 0; f < probsHit.size(); f++) {
                    probsHit.outcomes.get(f).odds /= cardProbsShoe;
                }
                return probsHit;
            } else {
                return chanceDealerEnd(hand, shoe);
            }
        }
    }

    private Outcomes chanceDealerEnd(BlackjackHand hand, BlackjackEnumShoe shoe) {

        int total = hand.getValue();
        Outcomes probs;
        if (total < 17 || (hitSoft17 && total == 17 && hand.isSoft())) {

            Outcomes probsHit = new Outcomes(baseOutcomes);
            for (Card<BlackjackRank,Suit> c : shoe.getCards()) {
                double cardProbCount = shoe.getCount(c);
                if (cardProbCount != 0) {
                    BlackjackEnumShoe copyShoe = shoe.clone();
                    BlackjackHand newHand = new BlackjackHand(hand.getCards());
                    newHand.addCard(c);
                    copyShoe.removeCard(c);
                    Outcomes newChance = new RecursiveDealerEnd(newHand, copyShoe, cardProbCount).compute();
                    for (int f = 0; f < probsHit.size(); f++) {
                        probsHit.outcomes.get(f).odds += cardProbCount * newChance.outcomes.get(f).odds;
                    }
                }
            }

            // Factor out ranks
            double cardProbsShoe = shoe.getTotalCards();
            for (int f = 0; f < probsHit.size(); f++) {
                probsHit.outcomes.get(f).odds /= cardProbsShoe;
            }

            return probsHit;
        } else {
            probs = new Outcomes(baseOutcomes);
            for (Outcome outcome : probs.outcomes) {
                if (outcome.eval.isHit(hand)) {
                    outcome.odds = 1;
                    break;
                }
            }
        }

        return probs;
    }

    public static void main(String[] args) {

        int decks = 6;
        boolean hitSoft17 = true;
        long start = System.currentTimeMillis();
        BlackjackEnumShoe shoe = new BlackjackEnumShoe(decks);
        List<Outcome> outcomes = new ArrayList<>();
        outcomes.add(new Outcome("BJ", (h) -> h[0].isBlackjack()));
        // Generic for ordered options, speedups exist here
        for (int value = 17; value != 27; value++) {
            outcomes.add(new Outcome(Integer.toString(value), new ValueHitEvaluator(value)));
        }
        DealerOdds dealerOdds = new DealerOdds(false, hitSoft17, outcomes.toArray(new Outcome[0]));
        List<Outcomes> finalOutcomes = new ArrayList<>();
        List<Callable<Void>> callables = new ArrayList<>();
        for (BlackjackRank rank : BlackjackRank.ACE.getRanks()) {

            callables.add(() -> {
                BlackjackEnumShoe copyShoe = shoe.clone();
                copyShoe.removeCard(new Card<>(rank, Suit.SPADE));
                Outcomes finalOutcome = dealerOdds.chanceDealerEnd(new Card<>(rank, Suit.SPADE), copyShoe);
                finalOutcomes.add(finalOutcome);
                System.out.println("\n\n" + rank + "\t" + finalOutcome);
                return null;
            });
        }

        // Run starting hands
        try {
            ExecutorService executorService = new ForkJoinPool(8);
            executorService.invokeAll(callables);
            executorService.shutdown();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("\n\nTotal\t" + Outcomes.average(finalOutcomes.toArray(new Outcomes[0])));
        System.out.println("\nTook " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
        System.out.println("Decks: " + decks + ", Hit Soft 17: " + hitSoft17);
    }

}
