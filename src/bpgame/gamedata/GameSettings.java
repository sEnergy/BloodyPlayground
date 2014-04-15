package bpgame.gamedata;

public class GameSettings {
	
	public enum END_CONDITION {
		SCORE
	}
	
	private int Players = 2;
	
	private END_CONDITION ec = END_CONDITION.SCORE;
	private int eValue = 5;
	
	public int getPlayers() {
		return Players;
	}
	
	public void setPlayers (int players) {
		Players = players;
	}
	
	public END_CONDITION getEndCondition () {
		return ec;
	}
	
	public void setEndCondition (END_CONDITION ec) {
		this.ec = ec;
	}
	
	public int getEndValue() {
		return eValue;
	}
	
	public void setEndValue(int eValue) {
		this.eValue = eValue;
	}
	
}
