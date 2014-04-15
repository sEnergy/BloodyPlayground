package bpgame.collectibles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import bpgame.eventhandling.CollisionHandling;
import bpgame.weapons.Weapon;
import bpgame.weapons.Weapon.WEAPON;

public class BonusWeapon extends AbstractCollectible {
	
	private Color color;
	private Weapon w;

	public BonusWeapon(CollisionHandling ch) {
		super(COLLECTIBLE.WEAPON, ch);
		
		Random r = new Random();
		switch (r.nextInt(2)+1)
		{
			case 1:
				this.w = new Weapon(WEAPON.SHOTGUN, null);
				this.name = "Shotgun";
				this.color = Color.MAGENTA;
				break;
			case 2:
				this.w = new Weapon(WEAPON.SMG, null);
				this.name = "SMG";
				this.color = Color.RED;
				break;
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.x-this.size/2, this.y-this.size/2, this.size, this.size/2);
		g.setColor(Color.BLACK);
		g.drawString(name, this.x-this.size/2-10, this.y-this.size/2-10);
	}

	@Override
	public boolean isPowerUp() {
		return false;
	}

	@Override
	public boolean isWeapon() {
		return true;
	}

	public Weapon getWeapon() {
		return w;
	}

}
