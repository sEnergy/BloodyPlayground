package bpgame.collectibles;

import java.awt.Color;
import java.awt.Graphics;
//import java.util.Random;

import java.util.Random;

import bpgame.eventhandling.CollisionHandling;

public class PowerUp extends AbstractCollectible{
	
	private POWERUP powerUpType;
	private Color color;
	
	private final int MINIMAL_TIME = 5000;
	private final int MAXIMAL_TIME = 15000;
	private int bonusTimeMs;

	public enum POWERUP {
		SPEED,
		AMMO,
		VEST,
		MARKSMAN,
		RESURRECTION,
		PENETRATE
	}
	
	public PowerUp (CollisionHandling ch) {
		
		super(COLLECTIBLE.POWERUP, ch);
		
		Random r = new Random();
		this.bonusTimeMs = MINIMAL_TIME + r.nextInt(MAXIMAL_TIME-MINIMAL_TIME);
		
		switch(r.nextInt(6)+1)
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
				this.powerUpType = POWERUP.PENETRATE;
				this.name = "Penetrating ammo";
				this.color = Color.CYAN;
				break;
		}
		
		System.out.println("Bonus "+name+" spawned for "+(this.disposeTime-System.currentTimeMillis())+"ms.");
		
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(this.color);
		g.fillOval(this.x-this.size/2, this.y-this.size/2, this.size, this.size/2);
		g.setColor(Color.BLACK);
		g.drawString(name, this.x-this.size/2-10, this.y-this.size/2-10);
	}
	
	public int getBonusTime() {
		return bonusTimeMs;
	}

	public String getName() {
		return name;
	}

	public POWERUP getType() {
		return powerUpType;
	}

	@Override
	public boolean isPowerUp() {
		return true;
	}

	@Override
	public boolean isWeapon() {
		return false;
	}

}