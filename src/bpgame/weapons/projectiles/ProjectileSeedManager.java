package bpgame.weapons.projectiles;

import java.util.ArrayList;

import bpgame.player.Player;

public class ProjectileSeedManager {
	
	private static ArrayList<Projectile> projectiles = null;
	private static ArrayList<ProjectileSeed> seeds = null;
	
	public ProjectileSeedManager () {
		ProjectileSeedManager.seeds = new ArrayList<ProjectileSeed>();
		ProjectileSeedManager.projectiles = new ArrayList<Projectile>();
		ProjectileSeedManager.projectiles = Player.getProjectiles();
	}
	
	public static void addSeed (Player pl, int delayMs) {
		ProjectileSeedManager.seeds.add(new ProjectileSeed(pl, delayMs));
	}
	
	public static void addSeed (Player pl, boolean penetrating, int delayMs, Vector v) {
		ProjectileSeedManager.seeds.add(new ProjectileSeed(pl, delayMs, v));
	}
	
	public static void update () {
		
		ArrayList<Integer> spawned = new ArrayList<Integer>();
		
		for (ProjectileSeed s : ProjectileSeedManager.seeds)
		{
			if (s.getSpawnTime() < System.currentTimeMillis())
			{
				if (s.getVector() == null)
					ProjectileSeedManager.projectiles.add(new Projectile(s.getPl(), Player.getCh(), s.isPenetrating()));
				else
				{
					ProjectileSeedManager.projectiles.add(new Projectile(s.getPl(), Player.getCh(), s.isPenetrating(), s.getVector()));
				}
				
				spawned.add(ProjectileSeedManager.seeds.indexOf(s));
			}
		}
		
		// destroying expired
		for (int i = spawned.size()-1; i >= 0; i--) 
		{
			ProjectileSeed tmp = ProjectileSeedManager.seeds.get(spawned.get(i));
			ProjectileSeedManager.seeds.remove(tmp);
		}
	}
	
	public static void deleteSeedsOfPlayer (int id) {
		ArrayList<Integer> toDelete = new ArrayList<Integer>();
		
		for (ProjectileSeed s : ProjectileSeedManager.seeds)
			if (s.getPl().getId() == id)
				toDelete.add(ProjectileSeedManager.seeds.indexOf(s));
		
		// deleting 
		for (int i = toDelete.size()-1; i >= 0; i--) 
		{
			ProjectileSeed tmp = ProjectileSeedManager.seeds.get(toDelete.get(i));
			ProjectileSeedManager.seeds.remove(tmp);
		}
	}
	
	public static int countSeedsOfPlayer (int id) {
		int counter = 0;
		
		for (ProjectileSeed s : ProjectileSeedManager.seeds)
			if (s.getPl().getId() == id)
				counter++;
		
		return counter;
	}	
}
