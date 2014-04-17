package bpgame.weapons.projectiles;

/*
 * Vector of projectile movement
 */
public class Vector {
	
	private int x, y;
	
	public Vector (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
