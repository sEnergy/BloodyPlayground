package bpgame.gamedata;

/*
 * Class that carries game settings
 */
public class GameSettings {
	
	/*
	 * Enum of gameover conditions
	 */
	public enum END_CONDITION {
		SCORE
	}
	
	private int players = 2;
	
	private END_CONDITION ec = END_CONDITION.SCORE;
	private int eValue = 5;
	
	public int getPlayers() {
		return this.players;
	}
	
	public void setPlayers (int players) {
		this.players = players;
	}
	
	public END_CONDITION getEndCondition () {
		return this.ec;
	}
	
	public void setEndCondition (END_CONDITION ec) {
		this.ec = ec;
	}
	
	public int getEndValue() {
		return this.eValue;
	}
	
	public void setEndValue(int eValue) {
		this.eValue = eValue;
	}
	
}
