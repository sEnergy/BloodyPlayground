package bpgame.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import bpgame.RenderLayer;
import bpgame.collectibles.CollectiblesManager;
import bpgame.events.handling.CollisionHandling;
import bpgame.gamedata.GameSettings;
import bpgame.map.GameMap;
import bpgame.map.objects.Obstacle;
import bpgame.map.objects.SpecialGround;
import bpgame.player.Player;
import bpgame.weapons.projectiles.Projectile;
import bpgame.weapons.projectiles.ProjectileSeedManager;

/*
 * Screen of gameplay
 */
public class GameScreen extends AbstractScreen implements KeyListener {
	
	private final int SAFEZONE_BOTTOM = 170;

	private CollisionHandling ch = null;
	private CollectiblesManager cm = null;
	
	@SuppressWarnings("unused")
	private ProjectileSeedManager psm = null;
	
	private GameSettings gameSettings = null;
	
	private GameMap map = null;
	
	private ArrayList<Player> players = null;
	private ArrayList<Projectile> projectiles = null;
	
	private boolean gameOver = false;
	
	public GameScreen (RenderLayer layer) {
		
		super(layer);
		
		this.layer.setFocusable(true);
		this.layer = layer;
		this.gameSettings = layer.getGameSettings();
		
		this.ch = new CollisionHandling (layer.getProgramSettings().getCanvasX(), layer.getProgramSettings().getCanvasY(), SAFEZONE_BOTTOM);
		
		this.players = new ArrayList<Player>();
		this.projectiles = new ArrayList<Projectile>();
		this.cm = new CollectiblesManager (this.ch);
		
		this.layer.setSize(layer.getProgramSettings().getCanvasDimension());
		
		this.map = new GameMap (layer.getProgramSettings().getCanvasX());
		
		ch.setCollectibles(this.cm.getCollectibles());
		ch.setPlayers(this.players);
		ch.setProjectiles(this.projectiles);
		ch.setObstacles(this.map.getObstacles());
		
		Player.resetId();
		
		for (int i = 0; i < gameSettings.getPlayers(); i++)
		{	
			Player tmp =  new Player(layer, projectiles, ch);
			layer.addKeyListener(tmp);
			this.players.add(tmp);
		}
		
		this.psm = new ProjectileSeedManager();
		layer.addKeyListener(this);
	}

	@Override
	public void render (Graphics g) {
		
		this.setAntialiasing(g);
		this.getRenderDimensions();
		
		this.map.renderBackground(g, layer);
		
		for (Player pl : players) pl.render(g);
		for (Projectile projectile : projectiles) projectile.render(g);	
		
		this.cm.render(g);
		
		/*
		 * Render of GUI
		 */
		
		int guiTop = this.layer.getHeight()-SAFEZONE_BOTTOM;
		
		g.setColor(Color.GRAY);
		g.fillRect(0, guiTop, this.layer.getWidth(), this.layer.getHeight());
		
		g.setColor(Color.BLACK);
		Font header = new Font("Verdana", Font.PLAIN, 30);
		Font content = new Font("Verdana", Font.PLAIN, 20);
		Font ammo = new Font("Verdana", Font.BOLD, 30);
		
		for (int i = 0; i < players.size(); ++i)
		{
			Player pl = players.get(i); 
			
			g.setFont(header);
			g.drawString(pl.getName()+" player", (int)(i*200*1.5)+100, guiTop+30);
			
			g.setFont(content);
			g.drawString("Weapon:  "+pl.getWeapon().getName(), (int)(i*200*1.5)+100, guiTop+55);
			g.drawString("Kills:  "+pl.getKills(), (int)(i*200*1.5)+100, guiTop+80);
			g.drawString("Deaths: "+pl.getDeaths(), (int)(i*200*1.5)+100, guiTop+105);
			
			int clipState = pl.getWeapon().getClipState();
			
			if (clipState == 0)
			{
				g.drawString("Reload: "+pl.getWeapon().getRemainingReloadTime(), (int)(i*200*1.5)+100, guiTop+130);
			}
			else
			{
				g.setColor((pl.isPuAmmoOn())? Color.RED:Color.BLACK);
				g.setFont(ammo);
				for (int j = 0; j < clipState; j++)
				{
					g.drawString("#", (int)(i*200*1.5)+100+j*22, guiTop+140);
				}
				
				g.setColor(Color.BLACK);
			}
			
		}
		
		this.map.renderForeground(g, layer);
		
		// debug of obstacle polygons
		for (Obstacle o: this.map.getObstacles())
		{
			if (o.isBlockingProjectiles())
				g.setColor(Color.MAGENTA);
			else
				g.setColor(Color.RED);
			
			g.drawPolygon(o.getArea());
		}
		
		// debug of special grounds polygons
		g.setColor(Color.CYAN);
		for (SpecialGround sg: this.map.getSpecialGrounds())
				g.drawPolygon(sg.getArea());
		
	}

	/*
     * Calls update methods of all game objects + checks collisions
	 */
	@Override
	public void update() {
		for (Player pl : players) pl.update(this.map);	
		this.cm.update();
		
		ProjectileSeedManager.update();
		
		ArrayList<Integer> toDestroy = new ArrayList<Integer>();
		
		for (Projectile projectile : projectiles)
		{
			if (!projectile.update())
			{
				toDestroy.add(projectiles.indexOf(projectile));
			}
		}	
		
		for (int i = toDestroy.size()-1; i >= 0; i--) 
		{
			System.out.println("Distant projectile disposed.");
			Projectile tmp = projectiles.get(toDestroy.get(i));
			projectiles.remove(tmp);
		}
		
		ch.checkForKills();
		ch.checkForPickUps();
		this.checkForGameOver();
	}
	
	/*
	 * Method that checks for gameover
	 */
	private void checkForGameOver () {
		for (Player pl : players)
		{
			if (pl.getKills() >= this.gameSettings.getEndValue())		
				this.GameOver();
		}
	}

	public boolean isGameOver () {
		return this.gameOver;
	}
	
	public void GameOver () {
		this.gameOver = true;
	}
	
	/*
	 * Removes all listeners from render layer
	 */
	public void removeListeners () {
		for (Player pl : this.players)
			this.layer.removeKeyListener(pl);
		layer.removeKeyListener(this);
	}

	/*
	 * Reaction to some key presses
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.layer.pause();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public ArrayList<Player> getPlayerList () {
		return this.players;
	}
	
}
