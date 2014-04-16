package bpgame.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import bpgame.RenderLayer;
import bpgame.gamedata.GameResults;
import bpgame.player.Player;

public class ResultsScreen extends AbstractScreen {
	
	private final int DURATION_MS = 10000;
	private final long ShownTill = System.currentTimeMillis()+DURATION_MS; 
	
	GameResults results;

	public ResultsScreen (RenderLayer layer, GameResults results) {
		super(layer);
		this.results = results;
	}
	
	@Override
	public void render(Graphics g) {
		
		// background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, this.layer.getWidth(), this.layer.getHeight());
		
		// header
		g.setColor(Color.BLACK);
		String s = "Game Results";
			
		Font fnt = new Font("Verdana", Font.PLAIN, 60);
		g.setFont(fnt);
						
		int stringLen = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();  
		int start = width/2 - stringLen/2;  
		g.drawString(s, start, 200);
		
		fnt = new Font("Verdana", Font.PLAIN, 40);
		g.setFont(fnt);
		
		for (int i = 0; i < results.getPlayersNumber(); ++i)
		{
			Player pl = results.getPlayer(i);
			
			s = pl.getName()+" player (K:"+pl.getKills()+" D:"+pl.getDeaths()+")";
			
			stringLen = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();  
			start = width/2 - stringLen/2;  
			g.drawString(s, start, 220+(i+1)*60);
		}
		
		Image dir_down = new ImageIcon("lib/gui/dir_down.png").getImage();
		g.drawImage(dir_down, 20, 20, layer);
		
	}

	@Override
	public void update() {}
	
	public boolean expired () {
		return (ShownTill < System.currentTimeMillis());
	}

}
