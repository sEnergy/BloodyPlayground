package bpgame.events.handling;

import java.util.ArrayList;

import bpgame.BloodyPlayground;
import bpgame.collectibles.AbstractCollectible;
import bpgame.collectibles.BonusWeapon;
import bpgame.collectibles.PowerUp;
import bpgame.player.Player;
import bpgame.weapons.Weapon;
import bpgame.weapons.projectiles.Projectile;

/*
 * Class implementing collisions and interactions of game objects
 */
public class CollisionHandling {
	
	private final int SAFEZONE_SCREEN_STANDARD = 40;
	private final int SAFEZONE_SCREEN_BOTTOM;
	
	private final int SAFEZONE_SPAWN_PROXIMITY = 250; // resolution dependent
	private final int SAFEZONE_SPAWN_VERTICAL = 100;  // resolution dependent
	private final int SAFEZONE_SPAWN_HORIZONTAL = 100;  // resolution dependent
	
	private final int width, height;
	
	// lists of all interacting objects
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
	
	/*
	 * Method that determines whether is position blocked by another player or not.
	 * 
	 * For check of blocking of any plyers, call with playerId == 0.
	 */
	public boolean isUnblockedPosition (int x, int y, int playerId) {
		
		// check if position is inside game area
		if (x < SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (x > width - SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (y < SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (y > height - SAFEZONE_SCREEN_BOTTOM)
			return false;
		
		// check for bloking by another player
		for (Player pl : players)
		{
			if (playerId != pl.getId())
			{			
				if ( this.computeDistance(pl.getX(), pl.getY(), x, y) < pl.getSize())
					return false;
			}
		}
		
		return true;
	}
	
	/*
	 * Method that determines whether is position good(valid) spawn position.
	 * 
	 * For check of blocking by any plyer, call with playerId == 0.
	 */
	public boolean isValidSpawnPosition (int x, int y, int id) {
		
		// check if position is inside game area
		if (x < SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (x > width - SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (y < SAFEZONE_SCREEN_STANDARD)
			return false;
		else if (y > height - SAFEZONE_SCREEN_BOTTOM)
			return false;
		
		// check of physical proximity and shooting range of other players
		for (Player pl : players)
		{
			if (id != pl.getId()) 
			{			
				if ( this.computeDistance(pl.getX(), pl.getY(), x, y) < SAFEZONE_SPAWN_PROXIMITY)
					return false; // near enemy
				
				if (Math.abs(pl.getX() - x) < SAFEZONE_SPAWN_HORIZONTAL)
					return false; // small horizontal distance
				
				if (Math.abs(pl.getY() - y) < SAFEZONE_SPAWN_VERTICAL)
					return false; // small vertical distance
			}
		}
		
		return true;
	}
	
	/*
	 * Method that controls killing - collision of players with projectiles
	 */
	public void checkForKills () {
		
		// array for projectiles that hit somebody and should not continue in flying
		ArrayList<Integer> toDestroy = new ArrayList<Integer>();
		
		for (Projectile projectile : projectiles) // check collisions of all projectiles ...
		{
			for (Player player : players)       // ... with all players
			{
				// collision occurence == hit
				if (this.computeDistance(projectile.getX_pos(), projectile.getY_pos(), player.getX(), player.getY()) < player.getSize()/2)
				{
					if (player.isPuVestOn()) // player is protected by vest
					{
						if (projectile.isPenetrating()) // projectile is penetrating -> kill
						{
							player.die();
							Player shooter = projectile.getShooter();
							
							// if shooter and victim are not the same player, add kill
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
							
							projectile.peneOff(); // turn off penetration
						}
						else // projectile not penetrating
						{
							toDestroy.add(projectiles.indexOf(projectile));
						}
					}
					else // player is not protected by vest
					{
						player.die();
						Player shooter = projectile.getShooter();
						
						// if shooter and victim are not the same player, add kill
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
						
						// if projectile was not penetrating, dispose it
						if (!projectile.isPenetrating())
							toDestroy.add(projectiles.indexOf(projectile));
					}
				}
			}
		}	
		
		// disposion of tagged projectiles
		for (int i = toDestroy.size()-1; i >= 0; i--) 
		{
			Projectile tmp = projectiles.get(toDestroy.get(i));
			projectiles.remove(tmp);
		}
	}
	
	/*
	 * Checks for collision of player and collectibles == pickups
	 */
	public void checkForPickUps () {
		for (AbstractCollectible tmp : this.collectibles) // check collision of all collectibles...
		{
			for (Player player : players)      // ... with all players
			{
				// collision occurence
				if (this.computeDistance(tmp.getX(), tmp.getY(), player.getX(), player.getY()) < player.getSize()/2)
				{
					if (tmp.isPowerUp()) // Power Up pickup
					{
						PowerUp pu = (PowerUp)tmp;
						
						// disposion of picked up collectible and adding bonus to player
						switch(pu.getType())
						{
							case AMMO:
								if (!player.isPuAmmoOn())
								{
									pu.dispose();
									player.setPuAmmoOn(pu.getEffectTime());
								}
								break;
							case MARKSMAN:
								if (!player.isPuMarksmanOn())
								{
									pu.dispose();
									player.setPuMarksmanOn(pu.getEffectTime());
								}
								break;
							case VEST:
								if (!player.isPuVestOn())
								{
									pu.dispose();
									player.setPuVestOn(pu.getEffectTime());
								}
								break;
							case PENETRATE:
								if (!player.isPuPenetrateOn())
								{
									pu.dispose();
									player.setPuPenetrateOn(pu.getEffectTime());
								}
								break;
							case RESURRECTION:
								if (!player.isPuRessurOn())
								{
									pu.dispose();
									player.setPuRessurOn(pu.getEffectTime());
								}
								break;
							case SPEED:
								if (!player.isPuSpeedOn())
								{
									pu.dispose();
									player.setPuSpeedOn(pu.getEffectTime());
								}
								break;
							default:
								break;	
						}
						
						System.out.println("Player "+player.getId()+" obtained "+pu.getName()+" for "+pu.getEffectTime()+"ms.");
					}
					else // weapon pickup
					{
						BonusWeapon bw = (BonusWeapon) tmp;
						bw.dispose();
						
						Weapon w = bw.getWeapon();
						
						player.setWeapon(w);
						w.setPlayer(player);
						
						System.out.println("Player "+player.getId()+" obtained "+w.getName()+".");
					}
				}
			}
		}
	}
	
	/*
	 * Computes distance between two places on game field
	 */
	private int computeDistance (int x1, int y1, int x2, int y2) {
		int delta_x = Math.abs(x1-x2);
		int delta_y = Math.abs(y1-y2);
		return (int) Math.sqrt(delta_x*delta_x+delta_y*delta_y);
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
}
