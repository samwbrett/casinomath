package tables.blackjack.gamecalcs.dealerodds;

import org.junit.jupiter.api.Test;
import tables.blackjack.calcs.BlackjackHand;
import tables.blackjack.calcs.BlackjackRank;
import tables.cards.deck.Card;
import tables.cards.deck.StandardSuit;

import static org.junit.jupiter.api.Assertions.*;

class ValueHitEvaluatorTest {

    private Card card(BlackjackRank rank) {
        return new Card(rank, StandardSuit.SPADE);
    }

    @Test
    void matchesCorrectValue() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.TEN), card(BlackjackRank.SEVEN));
        ValueHitEvaluator eval = new ValueHitEvaluator(17);
        assertTrue(eval.isHit(hand));
    }

    @Test
    void doesNotMatchWrongValue() {
        BlackjackHand hand = new BlackjackHand(card(BlackjackRank.TEN), card(BlackjackRank.SEVEN));
        ValueHitEvaluator eval = new ValueHitEvaluator(18);
        assertFalse(eval.isHit(hand));
    }

    @Test
    void throwsOnInvalidHandCount() {
        BlackjackHand hand1 = new BlackjackHand(card(BlackjackRank.TEN));
        BlackjackHand hand2 = new BlackjackHand(card(BlackjackRank.SEVEN));
        ValueHitEvaluator eval = new ValueHitEvaluator(10);
        assertThrows(IllegalArgumentException.class, () -> eval.isHit(hand1, hand2));
    }

    @Test
    void throwsOnNonBlackjackHand() {
        ValueHitEvaluator eval = new ValueHitEvaluator(10);
        assertThrows(IllegalArgumentException.class, () -> eval.isHit(null, null));
    }
}
