package Model;

import java.util.Random;

/**
 * Represents a dice for difficulty levels in a game.
 */
public class DifficultyDice {
    private final String[] difficultyLevels = {"Easy", "Medium", "Hard"};
    private final Random random;

    public DifficultyDice() {
        random = new Random();
    }

    /**
     * Rolls the dice and returns the rolled difficulty level.
     * 
     * @return The rolled difficulty level.
     */
    public String roll() {
        int index = random.nextInt(difficultyLevels.length);
        return difficultyLevels[index];
    }
}