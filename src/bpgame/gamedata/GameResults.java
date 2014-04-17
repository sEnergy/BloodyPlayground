package bpgame.gamedata;

import java.util.ArrayList;
import java.util.Arrays;

import bpgame.player.Player;

/*
 * Class carrying result of ended game.
 */
public class GameResults {
	
	private Player[] players;
	private int playerNumber; 
	
	public GameResults (ArrayList<Player> pls) {
		playerNumber = pls.size();
		
		players = new Player [playerNumber];
		
		for (int i = 0; i < playerNumber; ++i) 
		{
			players[i] = pls.get(i);
		}
		
		Arrays.sort(players);
	}
	
	public int getPlayersNumber () {
		return this.playerNumber;
	}
	
	public Player getPlayer (int i) {
		return this.players[i];
	}

}
