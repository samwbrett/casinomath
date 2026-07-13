package tables.baccarat.calcs;

import org.junit.jupiter.api.Test;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import tables.evals.BetEvaluator;
import tables.evals.BetStatistics;
import tables.evals.PaylineEvaluator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaccaratEnumeratorTest {

    private Card card(BaccaratRank rank) {
        return new Card(rank, StandardSuit.SPADE);
    }

    @Test
    void enumeratorConstructsSuccessfully() {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(1);
        BetEvaluator bet = new BetEvaluator("Player",
                new PaylineEvaluator("Win", 1, (hands) -> true));
        BaccaratEnumerator enumerator = new BaccaratEnumerator(shoe, bet);
        assertNotNull(enumerator);
    }

    @Test
    void enumeratorRunsOnSmallShoe() throws InterruptedException {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(1);
        BetEvaluator bet = new BetEvaluator("Player",
                new PaylineEvaluator("Win", 1, (hands) -> true));
        BaccaratEnumerator enumerator = new BaccaratEnumerator(shoe, bet);

        enumerator.run(1);
        assertNotNull(enumerator);
    }

    @Test
    void enumeratorHandlesMultipleBets() {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(1);
        BetEvaluator playerBet = new BetEvaluator("Player",
                new PaylineEvaluator("Win", 1, (hands) -> true));
        BetEvaluator bankerBet = new BetEvaluator("Banker",
                new PaylineEvaluator("Win", 1, (hands) -> true));
        BaccaratEnumerator enumerator = new BaccaratEnumerator(shoe, playerBet, bankerBet);
        assertNotNull(enumerator);
    }

    @Test
    void eightDeckBankerHouseEdge() throws InterruptedException {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(8);
        // "for one" format: pays 0.95-to-1 → forPay = 1 + 0.95 = 1.95
        BetEvaluator bankerBet = new BetEvaluator("Banker",
                new PaylineEvaluator("Banker Tie", 1, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() == ((BaccaratHand) hands[0]).getValue()),
                new PaylineEvaluator("Banker Win", 1.95, (hands) ->
                        ((BaccaratHand) hands[1]).getValue() > ((BaccaratHand) hands[0]).getValue()));

        BaccaratEnumerator enumerator = new BaccaratEnumerator(shoe, bankerBet);
        enumerator.run(4);

        BetStatistics stats = enumerator.getStats().getFirst();
        assertEquals(4_998_398_275_503_360L, stats.getTotalCombos());
        assertEquals(0.010579, 1 - stats.getPctReturn(), 0.0001);
    }

    @Test
    void eightDeckPlayerHouseEdge() throws InterruptedException {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(8);
        // "for one" format: pays 1-to-1 → forPay = 2
        BetEvaluator playerBet = new BetEvaluator("Player",
                new PaylineEvaluator("Player Tie", 1, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() == ((BaccaratHand) hands[1]).getValue()),
                new PaylineEvaluator("Player Win", 2, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() > ((BaccaratHand) hands[1]).getValue()));

        BaccaratEnumerator enumerator = new BaccaratEnumerator(shoe, playerBet);
        enumerator.run(4);

        BetStatistics stats = enumerator.getStats().getFirst();
        assertEquals(4_998_398_275_503_360L, stats.getTotalCombos());
        assertEquals(0.012351, 1 - stats.getPctReturn(), 0.0001);
    }

    @Test
    void eightDeckTieHouseEdge() throws InterruptedException {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(8);
        // "for one" format: pays 8-to-1 → forPay = 9
        BetEvaluator tieBet = new BetEvaluator("Tie",
                new PaylineEvaluator("Tie", 9, (hands) ->
                        ((BaccaratHand) hands[0]).getValue() == ((BaccaratHand) hands[1]).getValue()));

        BaccaratEnumerator enumerator = new BaccaratEnumerator(shoe, tieBet);
        enumerator.run(4);

        BetStatistics stats = enumerator.getStats().getFirst();
        assertEquals(4_998_398_275_503_360L, stats.getTotalCombos());
        assertEquals(0.143596, 1 - stats.getPctReturn(), 0.0001);
    }

    @Test
    void eightDeckPairHouseEdge() throws InterruptedException {
        BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(8);
        // "for one" format: pays 11-to-1 → forPay = 12
        BetEvaluator pairBet = new BetEvaluator("Player Pair",
                new PaylineEvaluator("Pair", 12, (hands) ->
                        ((BaccaratHand) hands[0]).isFirstTwoPaired()));

        BaccaratEnumerator enumerator = new BaccaratEnumerator(shoe, pairBet);
        enumerator.run(4);

        BetStatistics stats = enumerator.getStats().getFirst();
        assertEquals(4_998_398_275_503_360L, stats.getTotalCombos());
        assertEquals(0.103614, 1 - stats.getPctReturn(), 0.0001);
    }

}
