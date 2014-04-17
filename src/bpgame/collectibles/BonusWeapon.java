package bpgame.collectibles;

import java.awt.Color;
import java.awt.Graphics;

import bpgame.BloodyPlayground;
import bpgame.events.handling.CollisionHandling;
import bpgame.weapons.Weapon;
import bpgame.weapons.Weapon.WEAPON;

/*
 * Class of collectible weapon
 */
public class BonusWeapon extends AbstractCollectible {
	
	private final Color color;
	private final Weapon w;

	public BonusWeapon(CollisionHandling ch) {
		super(COLLECTIBLE.WEAPON, ch);
		
		/*
		 * Randomly pick weapon and set attributes accordingly
		 */
		switch (BloodyPlayground.r.nextInt(2)+1)
		{
			case 1:
				this.w = new Weapon(WEAPON.SHOTGUN, null);
				this.name = "Shotgun";
				this.color = Color.MAGENTA;
				break;
			case 2:
			default:
				this.w = new Weapon(WEAPON.SMG, null);
				this.name = "SMG";
				this.color = Color.RED;
				break;
		}
	}

	/*
	 * Override of abstract method for render of collectible
	 */
	@Override
	public void render(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.x-this.size/2, this.y-this.size/2, this.size, this.size/2);
		g.setColor(Color.BLACK);
		g.drawString(name, this.x-this.size/2-10, this.y-this.size/2-10);
	}

	/*
	 * Override of abstract method informing if collectible is powerup
	 */
	@Override
	public boolean isPowerUp() {
		return false;
	}

	/*
	 * Override of abstract method informing if collectible is weapon
	 */
	@Override
	public boolean isWeapon() {
		return true;
	}

	public Weapon getWeapon() {
		return this.w;
	}

}
