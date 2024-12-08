import java.awt.*;

public class Player {
    // Player holds the details for one player

	//Player now contains that players 'score' (Amount of matches won)
	//Also contains boolean 'canUseDoubleCube' boolean true if they can use the cube
	
    private int id,score;
    private String colorName;
    private Color color;
    private String name;
    private Dice dice;
    private boolean canUseDoubleCube; // CHANGED NAME TO MAKE IT EASIER TO UNDERSTAND
    private boolean hasDoubleCube;

    Player(int id, String colorName, Color color) {
        this.id = id;
        name = "";
        this.colorName = colorName;
        this.color = color;
        dice = new Dice();
        canUseDoubleCube = true;
        this.score = 0;
        
    }

    Player(Player player) {
        id = player.id;
        colorName = player.colorName;
        color = player.color;
        name = player.name;
        dice = new Dice(player.dice);
        canUseDoubleCube = player.canUseDoubleCube;
        score = 0;
    }
    
    public void updateScore(int game) {
    	this.score += game;
    }
    
    public int getScore() {
    	return this.score;
    }
    
    public void addWin(int win) {
    	this.score += win;
    }

    public void useDouble() {
    	this.canUseDoubleCube = false;
    	this.hasDoubleCube = false;
    }
    
    
    public void receiveDouble() {
    	this.canUseDoubleCube = true;
    	this.hasDoubleCube = true;
    }
    
    public void resetDouble() {
    	this.hasDoubleCube = false;
    	this.canUseDoubleCube = true;
    }
    // IF CAN USE DOUBLE
    public boolean hasDouble() {
    	return this.canUseDoubleCube;
    }
    
    // IF HAS DOUBLE CUBE
    public boolean hasDoubleCube() {
    	return this.hasDoubleCube;
    }
    
    public void setName(String name) {
        this.name = name;
        this.score = 0;
    }

    public int getId() {
        return id;
    }

    public String getColorName() {
        return this.colorName;
    }

    public Color getColor() {
        return this.color;
    }

    public Dice getDice() { return dice; }
    
    public String toString() {
        return name;
    }
    
}
