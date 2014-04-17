package bpgame.collectibles;

import java.awt.Graphics;
import java.util.ArrayList;

import bpgame.BloodyPlayground;
import bpgame.events.handling.CollisionHandling;

/*
 * Collectibles Manager - main controller of collectibles
 */
public class CollectiblesManager {
	
	private CollisionHandling ch;
	private ArrayList<AbstractCollectible> collectibles; // list of all spawned collectibles

	private final int MINIMAL_SPAWN_DELAY = 10000;
	private final int MAXIMAL_SPAWN_DELAY = 30000;
	private final int SPAWN_DELAY_VARIABLE = MAXIMAL_SPAWN_DELAY-MINIMAL_SPAWN_DELAY;
	
	private long nextCollectibleSpawnTime;
	
	public CollectiblesManager (CollisionHandling ch) {
		this.ch = ch;
		this.collectibles = new ArrayList<AbstractCollectible>();
		this.nextCollectibleSpawnTime = System.currentTimeMillis() + MINIMAL_SPAWN_DELAY + BloodyPlayground.r.nextInt(SPAWN_DELAY_VARIABLE);
	}
	
	/*
	 * Method that checks expiration times of all currently spawned collectibles.
	 * Disposes expired ones.
	 * 
	 * Also, if it is time to spawn new collectible, method performs this action.
	 */
	public void update () {
		
		// list for saving expired collectibles
		ArrayList<Integer> toDestroy = new ArrayList<Integer>();
		
		// finding expired expired collectibles and adding them to toDestroy list
		for (AbstractCollectible tmp : collectibles)
		{
			if (tmp.isExpired())
				toDestroy.add(collectibles.indexOf(tmp));
		}
		
		// destroying expired collectibles
		for (int i = toDestroy.size()-1; i >= 0; i--) 
		{
			AbstractCollectible tmp = collectibles.get(toDestroy.get(i));
			collectibles.remove(tmp);
		}
		
		// if it is time to spawn new collectible, do so
		if (nextCollectibleSpawnTime < System.currentTimeMillis())
		{
			this.spawn(this.ch);
			this.nextCollectibleSpawnTime = System.currentTimeMillis() + MINIMAL_SPAWN_DELAY + BloodyPlayground.r.nextInt(SPAWN_DELAY_VARIABLE);
		}
		
	}
	
	/*
	 * Method that spawns new collectible
	 */
	public void spawn (CollisionHandling ch) {
		
		// random pick of one of collectible types, spawn and addition to collectible list
		if (BloodyPlayground.r.nextInt(2) == 0 )
		{
			BonusWeapon w = new BonusWeapon(ch);
			this.collectibles.add(w);
		}
		else
		{
			PowerUp p = new PowerUp (ch);
			this.collectibles.add(p);
		}
		
	}

	/*
	 * Method that calls render method for each ccurrently spawned collectible
	 */
	public void render (Graphics g) {
		for (AbstractCollectible tmp : this.collectibles)
			tmp.render(g);
	}
	
	public ArrayList<AbstractCollectible> getCollectibles() {
		return this.collectibles;
	}
}
