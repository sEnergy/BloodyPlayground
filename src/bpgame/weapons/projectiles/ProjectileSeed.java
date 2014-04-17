package bpgame.weapons.projectiles;

import bpgame.player.Player;

/*
 * Seed of projetile - preparational stage of projectile
 */
public class ProjectileSeed {
	
	private long spawnTime;
	private boolean penetrating;
	private Player pl;
	
	Vector v = null;
	
	public ProjectileSeed (Player pl, int delayMs) {
		this.pl = pl;
		this.penetrating = pl.isPuPenetrateOn();
		this.spawnTime = System.currentTimeMillis() + delayMs;
	}
	
	public ProjectileSeed (Player pl, int delayMs, Vector v) {
		this(pl, delayMs);
		this.v = v;
	}

	public long getSpawnTime() {
		return spawnTime;
	}

	public boolean isPenetrating() {
		return penetrating;
	}

	public Player getPl() {
		return pl;
	}

	public Vector getVector() {
		return v;
	}

}
