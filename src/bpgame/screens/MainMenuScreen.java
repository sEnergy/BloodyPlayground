package bpgame.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bpgame.RenderLayer;

/*
 * Main menu screen of program
 */
public class MainMenuScreen extends AbstractScreen implements MouseListener {
	
	private final int SAFE_TIME = 100; // period of time after intialiyation of screen, when no buttins work
	private long workingSince;
	
	public MainMenuScreen (RenderLayer layer) {
		super(layer);
		this.layer.addMouseListener(this);
		this.layer.setSize(layer.getProgramSettings().getCanvasDimension());
		this.workingSince = System.currentTimeMillis()+SAFE_TIME;
	}
	
	@Override
	public void render(Graphics g) {
		
		this.setAntialiasing(g);
		this.getRenderDimensions();
		
		// background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, this.layer.getWidth(), this.layer.getHeight());
		
		// header
		g.setColor(Color.BLACK);
		String s = "Bloody playground";
		
		Font header = new Font("Verdana", Font.PLAIN, 80);
		g.setFont(header);
		
		int stringLen = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();  
        int start = width/2 - stringLen/2;  
        g.drawString(s, start, 100);
        
        // menu header
        s = "Main Menu";
		
		Font header2 = new Font("Verdana", Font.PLAIN, 60);
		g.setFont(header2);
		
		stringLen = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(s, start, 200);
        
        // buttons
        g.setColor(Color.WHITE);
        g.fillRect(width/2-100, 300, 200, 100);
        g.fillRect(width/2-100, 450, 200, 100);
        g.fillRect(width/2-100, 600, 200, 100);
        
        // header
     	g.setColor(Color.BLACK);
     	
     	Font butt = new Font("Verdana", Font.PLAIN, 40);
     	g.setFont(butt);
     	
     	String b1 = "Play";
     	String b2 = "Settings";
     	String b3 = "Quit";
     		
     	stringLen = (int) g.getFontMetrics().getStringBounds(b1, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(b1, start, 360);
        
        stringLen = (int) g.getFontMetrics().getStringBounds(b2, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(b2, start, 510);
        
        stringLen = (int) g.getFontMetrics().getStringBounds(b3, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(b3, start, 660); 
	}
	
	/*
	 * Clicks on buttons
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		
		if (workingSince <= System.currentTimeMillis())
		{
			int x = e.getX();
			int y = e.getY();
			
			if (x > width/2-100 && x < width/2-100+200 && y > 600 && y < 700)
			{
				System.exit(0);
			}
			else if (x > width/2-100 && x < width/2-100+200 && y > 300 && y < 400)
			{
				this.layer.setScreen(SCREEN.GAME);
			}
			else if (x > width/2-100 && x < width/2-100+200 && y > 450 && y < 550)
			{
				this.layer.setScreen(SCREEN.SETTINGS);
			}
		}
	}
	
	public void removeListener () {
		this.layer.removeMouseListener(this);
	}

	@Override
	public void update() {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
