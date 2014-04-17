package bpgame.collectibles;

import java.awt.Graphics;

import bpgame.BloodyPlayground;
import bpgame.events.handling.CollisionHandling;

/*
 * Abstract class for collectibles - weapons and powerups
 */
public abstract class AbstractCollectible {
	
	private final int MIN_VISIBILITY_DURATION = 3000;
	private final int MAX_VISIBILITY_DURATION = 10000;
	
	private final int MAX_VISIBILITY_DURATION_VARIABLE = MAX_VISIBILITY_DURATION-MIN_VISIBILITY_DURATION;
	
	/*
	 * Enum for types of collectibles
	 */
	public enum COLLECTIBLE {
		POWERUP,
		WEAPON
	}
	
	private long disposeTime; // time of end of visibility
	
	protected String name;
	protected COLLECTIBLE type;
	
	protected int size = 10;
	protected int x, y; // position
	
	public AbstractCollectible (COLLECTIBLE type, CollisionHandling ch) {
		
		// find valid spawn position
		do {
			this.x = BloodyPlayground.r.nextInt(ch.getWidth());
			this.y = BloodyPlayground.r.nextInt(ch.getHeight());
		} while (!ch.isValidSpawnPosition(this.x, this.y, 0));
		
		this.type = type;
		this.disposeTime = System.currentTimeMillis() + MIN_VISIBILITY_DURATION + BloodyPlayground.r.nextInt(MAX_VISIBILITY_DURATION_VARIABLE);
		
		System.out.println("Bonus "+name+" spawned for "+(this.disposeTime-System.currentTimeMillis())+"ms.");
	}
	
	/*
	 * Abstract method for rendering collectible
	 */
	public abstract void render (Graphics g);
	
	/*
	 * Abstract method informing if collectible is powerup
	 */
	public abstract boolean isPowerUp ();
	
	/*
	 * Abstract method informing if collectible is weapon
	 */
	public abstract boolean isWeapon ();
	
	/*
	 * Is collectible expired == should it loose visibility?
	 */
	public boolean isExpired () {
		return (this.disposeTime < System.currentTimeMillis());
	}
	
	/*
	 * Set dispose time to current time, so Collectible manager will
	 * dispose this object the next tick.
	 */
	public void dispose () {
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
