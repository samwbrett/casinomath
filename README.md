# CasinoMath

A Java library for exact combinatorial enumeration of casino game probabilities.
Provides a framework for computing precise probability distributions and house
edge calculations for games like baccarat and blackjack.

## Overview

CasinoMath uses combinatorial enumeration — iterating over all possible card
combinations from a defined shoe — to compute exact probabilities, rather than
relying on Monte Carlo simulation. This yields precise results for games with
finite deck sizes.

### Supported Games

- **Baccarat** — Full enumeration of Player/Banker outcomes with support for:
  - Standard drawing rules (including the six-card draw rule)
  - Dragon Bonus side bet (margin-of-victory paylines)
  - Player/Banker pair bets
  - Single-suit optimization for performance

- **Blackjack** — Dealer outcome probability distribution:
  - Hit-on-soft-17 (H17) and stand-on-soft-17 (S17) variants
  - Dealer peek (NA standard) and no-peek rules
  - Recursive enumeration with memoization and fork-join parallelism
  - Single-suit optimization when suit does not matter

- **Extensible design** — The framework supports adding new games by extending
  the core card and enumeration abstractions.

## Architecture

```
src/
├── tables/
│   ├── cards/
│   │   └── deck/         Core card abstractions: Card, Rank, Suit, EnumeratorShoe
│   ├── evals/            Evaluation framework: BetEvaluator, PaylineEvaluator,
│   │                     HitEvaluator, Hand, statistics tracking
│   ├── baccarat/
│   │   ├── calcs/        Baccarat-specific cards, hands, and enumerators
│   │   └── gamecalcs/
│   │       └── dragonbonus/  Dragon Bonus side bet calculation
│   └── blackjack/
│       ├── calcs/        Blackjack-specific cards, hands, and enumerators
│       └── gamecalcs/
│           └── dealerodds/   Dealer outcome probability calculation
```

### Core Abstractions

| Interface/Class | Purpose |
|---|---|
| `Rank` | Card rank with index, value, and display name |
| `Suit` | Card suit with index and display name |
| `Card` | Immutable playing card (rank + suit) |
| `EnumeratorShoe` | Card-count-based shoe for combinatorial enumeration |
| `Hand` | Abstract hand of cards |
| `HitEvaluator` | Functional interface for outcome matching |
| `PaylineEvaluator` | Named payout line with hit logic |
| `BetEvaluator` | Named bet with ordered paylines |
| `CardCalculator` | Abstract base for game enumerators |

## Building

```bash
./gradlew build
```

Requires JDK 21.

```bash
# Run all tests
./gradlew test

# Run with verbose test output
./gradlew test --info
```

## Running

The project includes executable main methods for demonstration:

```bash
# Compute baccarat Dragon Bonus statistics for 8 decks, 8 threads
java src/tables/baccarat/gamecalcs/dragonbonus/DragonBonus.java

# Compute blackjack dealer outcome distribution for 8 decks, H17, 8 threads
java src/tables/blackjack/gamecalcs/dealerodds/DealerFinalOutcomes.java
```

## Usage Example

```java
// Compute baccarat Player/Banker probabilities
BaccaratSingleSuitEnumeratorShoe shoe = new BaccaratSingleSuitEnumeratorShoe(8);
BetEvaluator playerBet = new BetEvaluator("Player",
    new PaylineEvaluator("Win", 2, (hands) -> {
        BaccaratHand player = (BaccaratHand) hands[0];
        BaccaratHand banker = (BaccaratHand) hands[1];
        return player.getValue() > banker.getValue();
    }),
    new PaylineEvaluator("Tie", 1, (hands) -> {
        BaccaratHand player = (BaccaratHand) hands[0];
        BaccaratHand banker = (BaccaratHand) hands[1];
        return player.getValue() == banker.getValue();
    }));

BaccaratEnumerator engine = new BaccaratEnumerator(shoe, playerBet);
engine.run(4);  // 4 threads

BetStatistics stats = engine.getStats().getFirst();
System.out.println("House Edge: " + (1 - stats.getPctReturn()));
```

### Custom Side Bets

You can define custom baccarat side bets by composing `BetEvaluator` with
ordered `PaylineEvaluator` entries. Each payline has a name, a "for one"
payout multiplier, and a `HitEvaluator` lambda that receives both hands as
`Hand[]` — index `[0]` is the Player hand, `[1]` is the Banker hand.

Paylines are checked **in order**; the first match wins. A "Lose" payline
(payout 0) is automatically appended to capture all non-winning combinations.

The following example implements a "Player Natural" side bet that pays 4 to 1
(house edge ≈ 5.3%) when the Player is dealt a natural hand (8 or 9):

```java
BetEvaluator playerNatural = new BetEvaluator("Player Natural",
    new PaylineEvaluator("Natural 8 or 9", 5, (hands) -> {
        BaccaratHand player = (BaccaratHand) hands[0];
        int value = player.getValue();
        return player.isNatural() && (value == 8 || value == 9);
    }));

// Pass multiple bet evaluators to compute all at once
BaccaratEnumerator engine = new BaccaratEnumerator(shoe, playerNatural);
engine.run(4);
engine.getStats().forEach(System.out::println);
```

**Key points for custom side bets:**

- The `HitEvaluator` lambda receives a `Hand[]` with exactly two elements:
  Player hand at `hands[0]`, Banker hand at `hands[1]`.
- Use `BaccaratHand` methods like `getValue()`, `isNatural()`, `isFirstTwoPaired()`,
  and `getCards()` for hand inspection.
- Card ranks are accessed via `c.getRank()`. Use `c.getRank().getValue()` for
  baccarat point values (0–9) where face cards and tens are worth 0.
- Payouts are specified in **"for one"** format: `forPay` is the total returned
  (bet + profit). A `forPay` of `3` means 2-to-1 (bet 1, get back 3, profit 2).
  Even money (1-to-1) is `forPay = 2`, a push (0-to-1) is `forPay = 1`.
- Bet names and payline names are free-form strings used only in display output.

### Full example: Dragon Bonus

The `DragonBonus` class in
`src/tables/baccarat/gamecalcs/dragonbonus/DragonBonus.java` demonstrates a
complete multi-payline side bet with the following paylines:

| Payline | Pays (for one) | Pays (to 1) | Condition |
|---|---|---|---|
| Natural Win | 2 | 1 to 1 | Natural (8 or 9) and beats banker |
| Natural Tie | 1 | push | Natural and ties banker |
| Win by 9 | 31 | 30 to 1 | Margin of victory is 9 |
| Win by 8 | 11 | 10 to 1 | Margin of victory is 8 |
| Win by 7 | 7 | 6 to 1 | Margin of victory is 7 |
| Win by 6 | 5 | 4 to 1 | Margin of victory is 6 |
| Win by 5 | 3 | 2 to 1 | Margin of victory is 5 |
| Win by 4 | 2 | 1 to 1 | Margin of victory is 4 |

## License

MIT
