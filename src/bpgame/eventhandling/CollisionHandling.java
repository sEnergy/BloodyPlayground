package bpgame.eventhandling;

import java.util.ArrayList;

import bpgame.player.Player;
import bpgame.weapons.Projectile;


public class CollisionHandling {
	
	private final int SAFEZONE_STANDARD = 40;
	private final int SAFEZONE_BOTTOM;
	
	private final int width, height;
	
	private ArrayList<Player> players;
	private ArrayList<Projectile> projectiles;
	
	public CollisionHandling (int w, int h, ArrayList<Player> players, ArrayList<Projectile> projectiles, int safeZoneBottom) {
		
		SAFEZONE_BOTTOM = safeZoneBottom+SAFEZONE_STANDARD;
		
		this.width = w;
		this.height = h;
		this.players = new ArrayList<Player>();
		this.players = players;
		
		this.projectiles = new ArrayList<Projectile>();
		this.projectiles = projectiles;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public boolean isEntrablePosition (int x, int y, int id) {
		if (x < SAFEZONE_STANDARD)
			return false;
		else if (x > width - SAFEZONE_STANDARD)
			return false;
		else if (y < SAFEZONE_STANDARD)
			return false;
		else if (y > height - SAFEZONE_BOTTOM)
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
	
	public void checkForKills () {
		ArrayList<Integer> toDestroy = new ArrayList<Integer>();
		
		projectiles.size();
		
		for (Projectile projectile : projectiles)
		{
			for (Player player : players)
			{
				if (this.computeDistance(projectile.getX_pos(), projectile.getY_pos(), player.getX_pos(), player.getY_pos()) < player.getSize()/2)
				{
					player.die();
					
					Player shooter = projectile.getShooter();
					shooter.addKill();
					
					System.out.println("Player "+shooter.getId()+" killed player "+player.getId());
					toDestroy.add(projectiles.indexOf(projectile));
				}
			}
		}	
		
		for (int i = toDestroy.size()-1; i >= 0; i--) 
		{
			Projectile tmp = projectiles.get(toDestroy.get(i));
			projectiles.remove(tmp);
		}
	}
	
	private int computeDistance (int x1, int y1, int x2, int y2) {
		int delta_x = Math.abs(x1-x2);
		int delta_y = Math.abs(y1-y2);
		return (int)Math.sqrt(delta_x*delta_x+delta_y*delta_y);
	}
}
