package bpgame.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import bpgame.RenderLayer;
import bpgame.eventhandling.CollisionHandling;
import bpgame.player.Player;
import bpgame.weapons.Projectile;

public class GameScreen extends AbstractScreen {
	
	private final int SAFEZONE_BOTTOM = 170;
	
	RenderLayer layer;
	CollisionHandling ch;
	
	private final int width, height;
	
	private ArrayList<Player> players = null;
	private ArrayList<Projectile> projectiles = null;
	
	public GameScreen (RenderLayer layer, int players) {
		
		this.layer = layer;
		this.width = this.layer.getWidth();
		this.height = this.layer.getHeight();
		
		this.players = new ArrayList<Player>();
		this.projectiles = new ArrayList<Projectile>();
		
		this.ch = new CollisionHandling (width, height, this.players, this.projectiles, SAFEZONE_BOTTOM);
		
		for (int i = 0; i < players; i++)
		{	
			Player tmp =  new Player(layer, projectiles, ch);
			layer.addKeyListener(tmp);
			this.players.add(tmp);
		}
	}

	@Override
	public void render (Graphics g) {
		
		g.setColor(this.layer.getForeground());
		g.fillRect(0, 0, this.layer.getWidth(), this.layer.getHeight());
		
		for (Player pl : players) pl.render(g);
		for (Projectile projectile : projectiles) projectile.render(g);	
		
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
			g.drawString("Player "+pl.getId(), (int)(i*200*1.5)+100, guiTop+50);
			
			g.setFont(content);
			g.drawString("Kills:  "+pl.getKills(), (int)(i*200*1.5)+100, guiTop+80);
			g.drawString("Deaths: "+pl.getDeaths(), (int)(i*200*1.5)+100, guiTop+105);
			
			int clipState = pl.getWeapon().getClipState();
			
			if (clipState == 0)
			{
				g.drawString("Reload: "+pl.getWeapon().getRemainingReloadTime(), (int)(i*200*1.5)+100, guiTop+130);
			}
			else
			{
				g.setFont(ammo);
				for (int j = 0; j < clipState; j++)
				{
					g.drawString("#", (int)(i*200*1.5)+100+j*22, guiTop+140);
				}
			}
			
		}
		
	}

	@Override
	public void update() {
		for (Player pl : players) pl.update();	
		
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
	}

}
