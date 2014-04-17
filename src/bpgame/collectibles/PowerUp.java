package bpgame.collectibles;

import java.awt.Color;
import java.awt.Graphics;

import bpgame.BloodyPlayground;
import bpgame.events.handling.CollisionHandling;

/*
 * Class of collectible Power Up - gameplay bonuses
 */
public class PowerUp extends AbstractCollectible{
	
	private final int MINIMAL_EFFECT_TIME = 5000;
	private final int MAXIMAL_EFFECT_TIME = 15000;

	/*
	 * Enum for types of Power Ups
	 */
	public enum POWERUP {
		SPEED, // increased player movement
		AMMO, // unlimited ammo
		VEST, // protects player from standard not penetrating projectiles
		MARKSMAN, // faster weapon handling
		RESURRECTION, // instant respawn
		PENETRATE // penetrating ammo
	}
	
	private final int effectTimeMs; // time of Power Up effect
	private final POWERUP powerUpType;
	private final Color color;
	
	public PowerUp (CollisionHandling ch) {
		
		super(COLLECTIBLE.POWERUP, ch);
		
		this.effectTimeMs = MINIMAL_EFFECT_TIME + BloodyPlayground.r.nextInt(MAXIMAL_EFFECT_TIME-MINIMAL_EFFECT_TIME);
		
		/*
		 * Randomly pick type of Powr Up and set attributes accordingly
		 */
		switch(BloodyPlayground.r.nextInt(6)+1)
		{
			case 1:
				this.powerUpType = POWERUP.SPEED;
				this.name = "Sprint";
				this.color = Color.MAGENTA;
				break;
			case 2:
				this.powerUpType = POWERUP.AMMO;
				this.name = "Infinite ammo";
				this.color = Color.RED;
				break;
			case 3:
				this.powerUpType = POWERUP.VEST;
				this.name = "Vest";
				this.color = Color.BLUE;
				break;
			case 4:
				this.powerUpType = POWERUP.MARKSMAN;
				this.name = "Marksman skills";
				this.color = Color.GREEN;
				break;
			case 5:
				this.powerUpType = POWERUP.RESURRECTION;
				this.name = "Instant respawn";
				this.color = Color.YELLOW;
				break;
			case 6:
			default:
				this.powerUpType = POWERUP.PENETRATE;
				this.name = "Penetrating ammo";
				this.color = Color.CYAN;
				break;
		}
	}
	
	/*
	 * Override of abstract method for render of collectible
	 */
	@Override
	public void render(Graphics g) {
		g.setColor(this.color);
		g.fillOval(this.x-this.size/2, this.y-this.size/2, this.size, this.size/2);
		g.setColor(Color.BLACK);
		g.drawString(name, this.x-this.size/2-10, this.y-this.size/2-10);
	}

	/*
	 * Override of abstract method informing if collectible is powerup
	 */
	@Override
	public boolean isPowerUp() {
		return true;
	}

	/*
	 * Override of abstract method informing if collectible is weapon
	 */
	@Override
	public boolean isWeapon() {
		return false;
	}
	
	public int getEffectTime() {
		return effectTimeMs;
	}

	public String getName() {
		return name;
	}

	public POWERUP getType() {
		return powerUpType;
	}

}