package bpgame.weapons;

import bpgame.BloodyPlayground;
import bpgame.events.handling.CollisionHandling;
import bpgame.player.Player;
import bpgame.weapons.projectiles.Projectile;
import bpgame.weapons.projectiles.ProjectileSeedManager;
import bpgame.weapons.projectiles.Vector;

/*
 * Weapon of players
 */
public class Weapon {
	
	/*
	 * Enum for weapon types
	 */
	public enum WEAPON {
		PISTOL,
		SHOTGUN,
		SMG
	}
	
	private WEAPON type;
	private String name;
	private Player pl;
	
	private int fireDelayMs;
	private int reloadDelayMs;
	private int clipSize;
	private int clipState;
	
	private long reloadCompleteTime;
	
	public Weapon (WEAPON type, Player pl) {
		
		this.type = type;
		this.pl = pl;
		this.reloadCompleteTime = System.currentTimeMillis();
		
		// set weapon attributes according to type
		switch (this.type)
		{
			case PISTOL:
				this.name = "Pistol";
				this.fireDelayMs = 380;
				this.clipSize = this.clipState = 5;
				this.reloadDelayMs = 3400;
				break;
			case SHOTGUN:
				this.name = "Shotgun";
				this.fireDelayMs = 1250;
				this.clipSize = this.clipState = 5;
				this.reloadDelayMs = 3400;
				break;
			case SMG:
				this.name = "SMG";
				this.fireDelayMs = 1300;
				this.clipSize = this.clipState = 5;
				this.reloadDelayMs = 3400;
			default:
				break;
		}	
	}
	
	/*
	 * Fires a weapon, returns whether the attempt to fire was succesfull of not
	 */
	public boolean fire (CollisionHandling ch, boolean unlimitedAmmo, boolean penetrating, boolean marksman) {
		
		if (clipState > 0) // weapon has ammo
		{
			if (!unlimitedAmmo) // if unlimited ammo is off, decrement clipState
				--clipState;
			
			// if clip state is zero and weapon is pistol, start reloading
			if (type == WEAPON.PISTOL && clipState == 0)
			{
				this.reloadCompleteTime = System.currentTimeMillis()+reloadDelayMs/(marksman? 2:1);
				BloodyPlayground.s.playSound("weapon_pistol_reload");
			}
			
			switch(this.type)
			{
				case PISTOL:
					BloodyPlayground.s.playSound("weapon_pistol");
					ProjectileSeedManager.addSeed(this.pl, 0);
					break;
				case SHOTGUN: {
					
					if (this.clipState > 0)
					{
						if (marksman)
							BloodyPlayground.s.playSound("weapon_shotgun_marksman");
						else
							BloodyPlayground.s.playSound("weapon_shotgun");
					}
					else
						BloodyPlayground.s.playSound("weapon_shotgun_nopump");
					
					// set default elemets of vectors
					int x1, x2;
					int y1 = 2, y2 = 4;
					x1 = x2 = Projectile.getDEFAULT_SPEED();
					
					/*
					 * Transform vector elements according to direction,
					 * plant projectile seeds.
					 */
					switch(this.pl.getDirection())
					{
						case UP:
							x2 = x1 *= -1;
						case DOWN:
							ProjectileSeedManager.addSeed(this.pl, 0, new Vector(y1, x1));
							ProjectileSeedManager.addSeed(this.pl, 0, new Vector(y2, x2));
							ProjectileSeedManager.addSeed(this.pl, 0, new Vector(-y1, x1));
							ProjectileSeedManager.addSeed(this.pl, 0, new Vector(-y2, x2));
							ProjectileSeedManager.addSeed(this.pl, 0);
							break;
						case LEFT:
							x2 = x1 *= -1;
						case RIGHT:
							ProjectileSeedManager.addSeed(this.pl, 0, new Vector(x1, y1));
							ProjectileSeedManager.addSeed(this.pl, 0, new Vector(x2, y2));
							ProjectileSeedManager.addSeed(this.pl, 0, new Vector(x1, -y1));
							ProjectileSeedManager.addSeed(this.pl, 0, new Vector(x2, -y2));
							ProjectileSeedManager.addSeed(this.pl, 0);
							break;
					}
					
					}break;
				case SMG: {
					BloodyPlayground.s.playSound("weapon_smg");
					
					// set default elemets of vectors
					int x1, x2;
					int y1 = 1, y2 = 2;
					x1 = x2 = Projectile.getDEFAULT_SPEED();
					
					// making unsorted array of projectile delays,
					int[] delays = {0,100,200,300,400};
					
					for(int n = 0; n < 5; n++)
					{
						int i1 = BloodyPlayground.r.nextInt(5);
						int i2 = BloodyPlayground.r.nextInt(5);
						
						int tmp = delays[i1];
						delays[i1] = delays[i2];
						delays[i2] = tmp;
					}
					
					/*
					 * Transform vector elements according to direction,
					 * plant projectile seeds.
					 */
					switch(this.pl.getDirection())
					{
						case UP:
							x2 = x1 *= -1;
						case DOWN:
							ProjectileSeedManager.addSeed(this.pl, delays[0], new Vector(y1, x1));
							ProjectileSeedManager.addSeed(this.pl, delays[1], new Vector(y2, x2));
							ProjectileSeedManager.addSeed(this.pl, delays[2], new Vector(-y1, x1));
							ProjectileSeedManager.addSeed(this.pl, delays[3], new Vector(-y2, x2));
							ProjectileSeedManager.addSeed(this.pl, delays[4]);
							break;
						case LEFT:
							x2 = x1 *= -1;
						case RIGHT:
							ProjectileSeedManager.addSeed(this.pl, delays[0], new Vector(x1, y1));
							ProjectileSeedManager.addSeed(this.pl, delays[1], new Vector(x2, y2));
							ProjectileSeedManager.addSeed(this.pl, delays[2], new Vector(x1, -y1));
							ProjectileSeedManager.addSeed(this.pl, delays[3], new Vector(x2, -y2));
							ProjectileSeedManager.addSeed(this.pl, delays[4]);
							break;
					}
					}break;			
			}
			
			return true;
		}
		else // empty clip
		{
			return false;
		}
	}
	
	/*
	 * Test if weapon is finished reloading
	 */
	public void checkForReloadFinish () {
		if (reloadCompleteTime < System.currentTimeMillis())
		{
			clipState = clipSize;
			System.out.println(pl.getName()+" player finished reloading.");
		}
	}
	
	public void setPlayer (Player pl) {
		this.pl = pl;
	}

	public int getFireDelayMs() {
		return fireDelayMs;
	}
	
	public int getClipState() {
		return clipState;
	}
	
	public WEAPON getWeaponType () {
		return this.type;
	}
	
	public String getName () {
		return this.name;
	}
	
	public int getRemainingReloadTime () {
		return (int) Math.abs(reloadCompleteTime - System.currentTimeMillis())/1000;
	}
}
