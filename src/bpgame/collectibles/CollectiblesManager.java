package bpgame.collectibles;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import bpgame.eventhandling.CollisionHandling;

public class CollectiblesManager {
	
	private Random r;
	private CollisionHandling ch;
	private ArrayList<AbstractCollectible> cols;

	private final int MINIMAL_TIME = 1000;
	private final int MAXIMAL_TIME = 3000;
	
	private long nextCollectibleSpawnTime; 
	
	public CollectiblesManager (CollisionHandling ch) {
		this.r = new Random();
		this.ch = ch;
		this.cols = new ArrayList<AbstractCollectible>();
		this.nextCollectibleSpawnTime = System.currentTimeMillis() + MINIMAL_TIME + r.nextInt(MAXIMAL_TIME-MINIMAL_TIME);
	}
	
	public void spawn (CollisionHandling ch) {
		
		if (this.r.nextInt(2) == 0 )
		{
			BonusWeapon w = new BonusWeapon(ch);
			this.cols.add(w);
		}
		else
		{
			PowerUp p = new PowerUp (ch);
			this.cols.add(p);
		}
		
	}
	
	public void update () {
		
		ArrayList<Integer> toDestroy = new ArrayList<Integer>();
		
		// finding expired
		for (AbstractCollectible tmp : cols)
		{
			if (tmp.expired())
				toDestroy.add(cols.indexOf(tmp));
		}
		
		// destroying expired
		for (int i = toDestroy.size()-1; i >= 0; i--) 
		{
			AbstractCollectible tmp = cols.get(toDestroy.get(i));
			cols.remove(tmp);
		}
		
		// if its time to spawn new one
		if (nextCollectibleSpawnTime < System.currentTimeMillis())
		{
			this.spawn(this.ch);
			this.nextCollectibleSpawnTime = System.currentTimeMillis() + MINIMAL_TIME + r.nextInt(MAXIMAL_TIME-MINIMAL_TIME);
		}
		
	}

	public void render (Graphics g) {
		for (AbstractCollectible tmp : cols)
			tmp.render(g);
	}
	
	public ArrayList<AbstractCollectible> getCollectibles() {
		return cols;
	}
}
