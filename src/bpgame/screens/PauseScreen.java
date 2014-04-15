package bpgame.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bpgame.RenderLayer;

public class PauseScreen extends AbstractScreen implements MouseListener {

	private final int height = 250;
	private final int width = 250;
	
	public PauseScreen(RenderLayer layer) {
		super(layer);
		layer.addMouseListener(this);
	}
	
	@Override
	public void render(Graphics g) {
		int midX = layer.getWidth()/2;
		int midY = layer.getHeight()/2-100;
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(midX-width/2, midY-height/2, width, height);
		
		String b1 = "Resume";
     	String b2 = "End game";
     	
     	g.setColor(Color.WHITE);
     	g.fillRect(midX-100, midY-height/2+25, 200, 80);
     	g.fillRect(midX-100, midY-height/2+135, 200, 80);
     	
     	Font butt = new Font("Verdana", Font.PLAIN, 30);
     	g.setFont(butt);;
     	
     	g.setColor(Color.BLACK);
     	int stringLen = (int) g.getFontMetrics().getStringBounds(b1, g).getWidth();  
        int start = midX - stringLen/2;  
        g.drawString(b1, start, 235);
        
        stringLen = (int) g.getFontMetrics().getStringBounds(b2, g).getWidth();  
        start = midX - stringLen/2;  
        g.drawString(b2, start, 345);
	}

	@Override
	public void update() {}
	
	public void removeListener() {
		layer.removeMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (layer.isPaused())
		{
			int x = e.getX();
			int y = e.getY();
			
			if (x > layer.getWidth()/2-100 && x < layer.getWidth()/2-100+200 && y > layer.getHeight()/2-100-height/2+25 && y < layer.getHeight()/2-100-height/2+25+80)
			{
					layer.pause();
			}
			else if (x > layer.getWidth()/2-100 && x < layer.getWidth()/2-100+200 && y > layer.getHeight()/2-100-height/2+135 && y < layer.getHeight()/2-100-height/2+135+80)
			{
				layer.pause();
				this.layer.getGameScreen().GameOver();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
