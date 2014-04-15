package bpgame.collectibles;

import java.awt.Graphics;
import java.util.Random;

import bpgame.eventhandling.CollisionHandling;

public abstract class AbstractCollectible {
	
	private final int MIN_DURATION = 30000;
	private final int MAX_DURATION = 100000;
	
	protected String name;
	protected COLLECTIBLE type;
	
	protected int size = 10;
	protected int x, y;

	protected long disposeTime;
	
	public enum COLLECTIBLE {
		POWERUP,
		WEAPON
	}
	
	public AbstractCollectible (COLLECTIBLE type, CollisionHandling ch) {
		
		Random r = new Random();
		
		do {
			this.x = r.nextInt(ch.getWidth());
			this.y = r.nextInt(ch.getHeight());
		} while (!ch.isValidSpawnPosition(this.x, this.y, 0));
		
		this.type = type;
		this.disposeTime = System.currentTimeMillis() + MIN_DURATION + r.nextInt(MAX_DURATION-MIN_DURATION);
	}
	
	public abstract void render (Graphics g);
	public abstract boolean isPowerUp ();
	public abstract boolean isWeapon ();
	
	public boolean expired () {
		return (this.disposeTime < System.currentTimeMillis());
	}
	
	public void pickup () {
		this.disposeTime = System.currentTimeMillis();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}
}
