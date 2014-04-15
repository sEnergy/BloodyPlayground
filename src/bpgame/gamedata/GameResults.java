package bpgame.gamedata;

import java.util.ArrayList;
import java.util.Arrays;

import bpgame.player.Player;

public class GameResults {
	
	Player[] order;
	
	int players; 
	
	public GameResults (ArrayList<Player> pls) {
		players = pls.size();
		
		order = new Player [players];
		
		for (int i = 0; i < players; ++i) 
		{
			order[i] = pls.get(i);
		}
		
		Arrays.sort(order);
	}
	
	public int getPlayersNumber () {
		return this.players;
	}
	
	public Player getPlayer (int i) {
		return this.order[i];
	}

}
