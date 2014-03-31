package bpgame.weapons;

import bpgame.eventhandling.CollisionHandling;
import bpgame.player.Player;

public class Weapon {
	
	public enum WEAPON {
		PISTOL
	}
	
	private WEAPON type;
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
		
		switch (this.type)
		{
		case PISTOL:
			this.fireDelayMs = 650;
			this.clipSize = this.clipState = 5;
			this.reloadDelayMs = 5000;
			break;
		default:
			break;
		}	
	}
	
	public Projectile fire (CollisionHandling ch) {
		
		if (clipState > 0)
		{
			if (--clipState == 0)
			{
				this.reloadCompleteTime = System.currentTimeMillis()+reloadDelayMs;
			}
			return new Projectile (pl, ch);
		}
		else
		{
			return null;
		}
	}
	
	public void update () {
		if (reloadCompleteTime < System.currentTimeMillis())
		{
			clipState = clipSize;
			System.out.println("Player "+pl.getId()+" finished reloading.");
		}

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
	
	public int getRemainingReloadTime () {
		return (int)Math.abs(reloadCompleteTime - System.currentTimeMillis())/1000;
	}
}
