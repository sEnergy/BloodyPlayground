package bpgame.eventhandling;

import java.util.ArrayList;

import bpgame.BloodyPlayground;
import bpgame.collectibles.AbstractCollectible;
import bpgame.collectibles.BonusWeapon;
import bpgame.collectibles.PowerUp;
import bpgame.player.Player;
import bpgame.weapons.Weapon;
import bpgame.weapons.projectiles.Projectile;


public class CollisionHandling {
	
	private final int SAFEZONE_SCREEN_STANDARD = 40;
	private final int SAFEZONE_SCREEN_BOTTOM;
	
	private final int SAFEZONE_SPAWN_PROXIMITY = 250; // resolution dependent
	private final int SAFEZONE_SPAWN_VERTICAL = 100;  // resolution dependent
	private final int SAFEZONE_SPAWN_HORIZONTAL = 100;  // resolution dependent
	
	private final int width, height;
	
	private ArrayList<Player> players = null;
	private ArrayList<Projectile> projectiles = null;
	private ArrayList<AbstractCollectible> collectibles = null;
	
	public CollisionHandling (int w, int h, int safeZoneBottom) {
		
		SAFEZONE_SCREEN_BOTTOM = safeZoneBottom+SAFEZONE_SCREEN_STANDARD;
		
		this.width = w;
		this.height = h;
		
		this.players = new ArrayList<Player>();
		this.projectiles = new ArrayList<Projectile>();
		this.collectibles = new  ArrayList<AbstractCollectible>();
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}

	public void setCollectibles(ArrayList<AbstractCollectible> collectibles) {
		this.collectibles = collectibles;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public boolean isUnblockedPosition (int x, int y, int id) {
		if (x < SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (x > width - SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (y < SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (y > height - SAFEZONE_SCREEN_BOTTOM)
			return false;
		
		for (Player pl : players)
		{
			if (id != pl.getId())
			{			
				if ( this.computeDistance(pl.getX_pos(), pl.getY_pos(), x, y) < pl.getSize())
					return false;
			}
		}
		
		return true;
	}
	
	public boolean isValidSpawnPosition (int x, int y, int id) {
		if (x < SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (x > width - SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (y < SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (y > height - SAFEZONE_SCREEN_BOTTOM)
			return false;
		
		for (Player pl : players)
		{
			if (id != pl.getId()) 
			{			
				if ( this.computeDistance(pl.getX_pos(), pl.getY_pos(), x, y) < SAFEZONE_SPAWN_PROXIMITY)
					return false; // near enemy
				
				if (Math.abs(pl.getX_pos() - x) < SAFEZONE_SPAWN_HORIZONTAL)
					return false; // small horizontal distance
				
				if (Math.abs(pl.getY_pos() - y) < SAFEZONE_SPAWN_VERTICAL)
					return false; // small vertical distance
			}
		}
		
		return true;
	}
	
	public void checkForKills () {
		ArrayList<Integer> toDestroy = new ArrayList<Integer>();
		
		projectiles.size();
		
		for (Projectile projectile : projectiles)
		{
			for (Player player : players)
			{
				if (this.computeDistance(projectile.getX_pos(), projectile.getY_pos(), player.getX_pos(), player.getY_pos()) < player.getSize()/2)
				{
					if (player.isPuVestOn())
					{
						if (projectile.isPenetrating())
						{
							player.die();
							Player shooter = projectile.getShooter();
							
							if (shooter.getId() != player.getId())
							{
								shooter.addKill();
								System.out.println("Player "+shooter.getId()+" killed player "+player.getId()+".");
							}
							else
							{
								System.out.println("Player "+shooter.getId()+" was killed by his own bullet.");
							}
							
							BloodyPlayground.s.playSound("hit_player");
							
							projectile.peneOff();
						}
					}
					else
					{
						player.die();
						Player shooter = projectile.getShooter();
						
						if (shooter.getId() != player.getId())
						{
							shooter.addKill();
							System.out.println("Player "+shooter.getId()+" killed player "+player.getId()+".");
						}
						else
						{
							System.out.println("Player "+shooter.getId()+" was killed by his own bullet.");
						}
						
						BloodyPlayground.s.playSound("hit_player");
						
						if (!projectile.isPenetrating())
							toDestroy.add(projectiles.indexOf(projectile));
					}
				}
			}
		}	
		
		for (int i = toDestroy.size()-1; i >= 0; i--) 
		{
			Projectile tmp = projectiles.get(toDestroy.get(i));
			projectiles.remove(tmp);
		}
	}
	
	public void checkForPickUps () {
		for (AbstractCollectible tmp : this.collectibles)
		{
			for (Player player : players)
			{
				if (this.computeDistance(tmp.getX(), tmp.getY(), player.getX_pos(), player.getY_pos()) < player.getSize()/2)
				{
					if (tmp.isPowerUp())
					{
						PowerUp pu = (PowerUp)tmp;
						
						switch(pu.getType())
						{
							case AMMO:
								if (!player.isPuAmmoOn())
								{
									pu.pickup();
									player.setPuAmmoOn(pu.getBonusTime());
								}
								break;
							case MARKSMAN:
								if (!player.isPuMarksmanOn())
								{
									pu.pickup();
									player.setPuMarksmanOn(pu.getBonusTime());
								}
								break;
							case VEST:
								if (!player.isPuVestOn())
								{
									pu.pickup();
									player.setPuVestOn(pu.getBonusTime());
								}
								break;
							case PENETRATE:
								if (!player.isPuPenetrateOn())
								{
									pu.pickup();
									player.setPuPenetrateOn(pu.getBonusTime());
								}
							case RESURRECTION:
								if (!player.isPuRessurOn())
								{
									pu.pickup();
									player.setPuRessurOn(pu.getBonusTime());
								}
								break;
							case SPEED:
								if (!player.isPuSpeedOn())
								{
									pu.pickup();
									player.setPuSpeedOn(pu.getBonusTime());
								}
								break;
							default:
								break;	
						}
						
						System.out.println("Player "+player.getId()+" obtained "+pu.getName()+" for "+pu.getBonusTime()+"ms.");
					}
					else // weapons
					{
						BonusWeapon bw = (BonusWeapon) tmp;
						bw.pickup();
						
						Weapon w = bw.getWeapon();
						
						player.setWeapon(w);
						w.setPlayer(player);
						
						System.out.println("Player "+player.getId()+" obtained "+w.getName()+".");
					}
				}
			}
		}
	}
	
	private int computeDistance (int x1, int y1, int x2, int y2) {
		int delta_x = Math.abs(x1-x2);
		int delta_y = Math.abs(y1-y2);
		return (int)Math.sqrt(delta_x*delta_x+delta_y*delta_y);
	}
}
