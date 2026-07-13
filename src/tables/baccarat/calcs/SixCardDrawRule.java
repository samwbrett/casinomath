package tables.baccarat.calcs;

/**
 * Implements the standard baccarat "six-card draw" rule that determines
 * whether the banker draws a third card based on the banker's current score
 * and the player's third card value.
 * <p>
 * The rules are:
 * <ul>
 *   <li>Banker draws on 0-2 regardless of player's third card</li>
 *   <li>Banker draws on 3 unless player's third card is 8</li>
 *   <li>Banker draws on 4 if player's third card is 2-7</li>
 *   <li>Banker draws on 5 if player's third card is 4-7</li>
 *   <li>Banker draws on 6 if player's third card is 6-7</li>
 *   <li>Banker stands on 7</li>
 * </ul>
 */
public final class SixCardDrawRule {

    private SixCardDrawRule() {
        // Utility class - prevent instantiation
    }

    // Lookup table: [bankerScore][playerThirdCard] -> draw decision
    private static final boolean[][] DRAW_TABLE = new boolean[10][10];

    static {
        for (int bScore = 0; bScore < 10; bScore++) {
            for (int pThird = 0; pThird < 10; pThird++) {
                DRAW_TABLE[bScore][pThird] = bScore <= 2 ||
                        (bScore == 3 && pThird != 8) ||
                        (bScore == 4 && pThird >= 2 && pThird <= 7) ||
                        (bScore == 5 && pThird >= 4 && pThird <= 7) ||
                        (bScore == 6 && pThird >= 6 && pThird <= 7);
            }
        }
    }

    /**
     * Determines whether the banker should draw based on two baccarat hands.
     * The player hand must have 3 cards and the banker hand must have 2 cards
     * for a valid six-card draw scenario.
     *
     * @param playerHand the player's hand (with third card drawn)
     * @param bankerHand the banker's hand (before potential third card)
     * @return true if the banker should draw
     */
    public static boolean isDraw(BaccaratHand playerHand, BaccaratHand bankerHand) {
        if (playerHand.getNumCards() != 3 || bankerHand.getNumCards() != 2) {
            return false;
        }
        int playerThirdValue = playerHand.getCards().get(2).getRank().getValue();
        return isDraw(bankerHand.getValue(), playerThirdValue);
    }

    /**
     * Determines whether the banker should draw based on the banker's score
     * and the player's third card value.
     *
     * @param bankerScore the banker's current score (0-9)
     * @param playerThird the player's third card value (0-9)
     * @return true if the banker should draw
     */
    public static boolean isDraw(int bankerScore, int playerThird) {
        return DRAW_TABLE[bankerScore][playerThird];
    }
}

