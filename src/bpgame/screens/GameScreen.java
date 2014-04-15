package bpgame.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import bpgame.RenderLayer;
import bpgame.collectibles.CollectiblesManager;
import bpgame.eventhandling.CollisionHandling;
import bpgame.gamedata.GameSettings;
import bpgame.player.Player;
import bpgame.weapons.projectiles.Projectile;
import bpgame.weapons.projectiles.ProjectileSeedManager;

public class GameScreen extends AbstractScreen implements KeyListener {
	
	private final int SAFEZONE_BOTTOM = 170;

	CollisionHandling ch = null;
	CollectiblesManager cm = null;
	ProjectileSeedManager pcm = null;
	
	GameSettings settings;
	
	private ArrayList<Player> players = null;
	private ArrayList<Projectile> projectiles = null;
	
	private boolean gameOver = false;
	
	public GameScreen (RenderLayer layer, GameSettings settings) {
		
		super(layer);
		
		this.layer = layer;
		this.settings = settings;
		
		this.ch = new CollisionHandling (this.width, this.height, SAFEZONE_BOTTOM);
		
		this.players = new ArrayList<Player>();
		this.projectiles = new ArrayList<Projectile>();
		this.cm = new CollectiblesManager (this.ch);
		
		ch.setCollectibles(this.cm.getCollectibles());
		ch.setPlayers(this.players);
		ch.setProjectiles(this.projectiles);
		
		Player.resetId();
		
		for (int i = 0; i < settings.getPlayers(); i++)
		{	
			Player tmp =  new Player(layer, projectiles, ch);
			layer.addKeyListener(tmp);
			this.players.add(tmp);
		}
		
		layer.addKeyListener(this);
		
		this.pcm = new ProjectileSeedManager();
	}

	@Override
	public void render (Graphics g) {
		
		g.setColor(new Color(247,247,247));
		g.fillRect(0, 0, this.layer.getWidth(), this.layer.getHeight());
		
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
		
	}

	@Override
	public void update() {
		for (Player pl : players) pl.update();	
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
		
		ProjectileSeedManager.update();
	}
	
	private void checkForGameOver () {
		for (Player pl : players)
		{
			if (pl.getKills()  >= this.settings.getEndValue())		
				this.gameOver = true;
		}
	}

	public boolean isGameOver () {
		return this.gameOver;
	}
	
	public void GameOver () {
		this.gameOver = true;
	}
	
	public void removeListeners () {
		for (Player pl : this.players)
			this.layer.removeKeyListener(pl);
		layer.removeKeyListener(this);
	}

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
