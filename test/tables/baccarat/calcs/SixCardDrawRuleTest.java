package tables.baccarat.calcs;

import org.junit.jupiter.api.Test;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;
import static org.junit.jupiter.api.Assertions.*;

class SixCardDrawRuleTest {

    private Card card(BaccaratRank rank) {
        return new Card(rank, StandardSuit.SPADE);
    }

    @Test
    void bankerDrawsOnZeroToTwo() {
        for (int bScore = 0; bScore <= 2; bScore++) {
            for (int pThird = 0; pThird <= 9; pThird++) {
                assertTrue(SixCardDrawRule.isDraw(bScore, pThird),
                        "Banker " + bScore + " should draw vs player third " + pThird);
            }
        }
    }

    @Test
    void bankerDrawsOnThreeUnlessPlayerThirdIsEight() {
        assertTrue(SixCardDrawRule.isDraw(3, 0));
        assertTrue(SixCardDrawRule.isDraw(3, 7));
        assertFalse(SixCardDrawRule.isDraw(3, 8));
        assertTrue(SixCardDrawRule.isDraw(3, 9));
    }

    @Test
    void bankerDrawsOnFourOnlyIfPlayerThirdIsTwoToSeven() {
        assertFalse(SixCardDrawRule.isDraw(4, 0));
        assertFalse(SixCardDrawRule.isDraw(4, 1));
        assertTrue(SixCardDrawRule.isDraw(4, 2));
        assertTrue(SixCardDrawRule.isDraw(4, 5));
        assertTrue(SixCardDrawRule.isDraw(4, 7));
        assertFalse(SixCardDrawRule.isDraw(4, 8));
        assertFalse(SixCardDrawRule.isDraw(4, 9));
    }

    @Test
    void bankerDrawsOnFiveOnlyIfPlayerThirdIsFourToSeven() {
        assertFalse(SixCardDrawRule.isDraw(5, 0));
        assertFalse(SixCardDrawRule.isDraw(5, 3));
        assertTrue(SixCardDrawRule.isDraw(5, 4));
        assertTrue(SixCardDrawRule.isDraw(5, 7));
        assertFalse(SixCardDrawRule.isDraw(5, 8));
    }

    @Test
    void bankerDrawsOnSixOnlyIfPlayerThirdIsSixOrSeven() {
        assertFalse(SixCardDrawRule.isDraw(6, 0));
        assertFalse(SixCardDrawRule.isDraw(6, 5));
        assertTrue(SixCardDrawRule.isDraw(6, 6));
        assertTrue(SixCardDrawRule.isDraw(6, 7));
        assertFalse(SixCardDrawRule.isDraw(6, 8));
    }

    @Test
    void bankerStandsOnSeven() {
        for (int pThird = 0; pThird <= 9; pThird++) {
            assertFalse(SixCardDrawRule.isDraw(7, pThird),
                    "Banker 7 should stand vs player third " + pThird);
        }
    }

    @Test
    void isDrawWithHandsReturnsFalseWhenPlayerNotDrawn() {
        BaccaratHand player = new BaccaratHand(card(BaccaratRank.FOUR), card(BaccaratRank.FIVE)); // natural
        BaccaratHand banker = new BaccaratHand(card(BaccaratRank.KING), card(BaccaratRank.QUEEN));
        assertFalse(SixCardDrawRule.isDraw(player, banker));
    }

    @Test
    void isDrawWithHandsReturnsFalseWhenBankerAlreadyHasThreeCards() {
        BaccaratHand player = new BaccaratHand(
                card(BaccaratRank.ACE), card(BaccaratRank.THREE), card(BaccaratRank.FIVE));
        BaccaratHand banker = new BaccaratHand(
                card(BaccaratRank.KING), card(BaccaratRank.QUEEN), card(BaccaratRank.TWO));
        assertFalse(SixCardDrawRule.isDraw(player, banker));
    }

    @Test
    void isDrawWithHandsReturnsCorrectDecision() {
        // Player has 1 + 3 = 4, draws third card 6 (value 6)
        // Banker has 0 + 0 = 0, should always draw
        BaccaratHand player1 = new BaccaratHand(
                card(BaccaratRank.ACE), card(BaccaratRank.THREE), card(BaccaratRank.SIX));
        BaccaratHand banker1 = new BaccaratHand(card(BaccaratRank.KING), card(BaccaratRank.QUEEN));
        assertTrue(SixCardDrawRule.isDraw(player1, banker1));

        // Banker has 3 + 4 = 7, should stand regardless
        BaccaratHand player2 = new BaccaratHand(
                card(BaccaratRank.ACE), card(BaccaratRank.THREE), card(BaccaratRank.SIX));
        BaccaratHand banker2 = new BaccaratHand(card(BaccaratRank.THREE), card(BaccaratRank.FOUR));
        assertFalse(SixCardDrawRule.isDraw(player2, banker2));
    }
}
