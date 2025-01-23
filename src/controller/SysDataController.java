package controller;

import Model.SysData;

public class SysDataController {
   
	// Save game information using the Model
	public static void saveGame(String player1, String player2, String difficulty, String winner, String gameTime) {
		SysData.saveGameInfo(player1, player2, difficulty, winner, gameTime);
	}
}
