package tables.blackjack.gamecalcs.dealerodds;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DealerFinalOutcomesTest {

    @Test
    void smallDeckRunsWithoutError() throws InterruptedException {
        DealerFinalOutcomes dealer = new DealerFinalOutcomes(1, false, 2);
        dealer.run();
        List<Outcomes> results = dealer.getFinalOutcomes();
        assertEquals(13, results.size());
    }

    @Test
    void resultsAreNotNull() throws InterruptedException {
        DealerFinalOutcomes dealer = new DealerFinalOutcomes(1, false, 2);
        dealer.run();
        for (Outcomes o : dealer.getFinalOutcomes()) {
            assertNotNull(o);
        }
    }

    @Test
    void eightDeckHitSoft17Probabilities() throws InterruptedException {
        DealerFinalOutcomes dealer = new DealerFinalOutcomes(8, true, 8);
        dealer.run();

        Outcomes avg = Outcomes.average(dealer.getFinalOutcomes());

        // Expected probabilities for 8 decks, dealer hits soft 17
        // Order: BJ(0), 17(1), 18(2), 19(3), 20(4), 21(5), 22(6), 23(7), 24(8), 25(9), 26(10)
        double[][] expected = {
                {0, 0.047451},
                {1, 0.133409},
                {2, 0.141280},
                {3, 0.135630},
                {4, 0.181714},
                {5, 0.074841},
                {6, 0.073544},
                {7, 0.065761},
                {8, 0.057440},
                {9, 0.048957},
                {10, 0.039973}
        };

        for (double[] pair : expected) {
            int index = (int) pair[0];
            double expectedOdds = pair[1];
            assertEquals(expectedOdds, avg.getOdds(index), 0.00001,
                    "Mismatch at outcome index " + index);
        }
    }
}
