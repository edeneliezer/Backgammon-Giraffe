package controller;

import Model.GameModel;

public class jsonController {
   
	// Save game information using the Model
    public static void saveGame(String player1, String player2, String difficulty) {
        GameModel.saveGameInfo(player1, player2, difficulty);
    }

}
