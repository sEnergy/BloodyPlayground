package bpgame.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bpgame.RenderLayer;
import bpgame.gamedata.GameSettings;

public class SettingsScreen extends AbstractScreen implements MouseListener{
	
	private final int SAFE_TIME = 100;
	private final long workingSince;
	
	private GameSettings settings;
	
	public SettingsScreen (RenderLayer layer, GameSettings settings) {
		super(layer);
		this.layer.addMouseListener(this);
		this.workingSince = System.currentTimeMillis()+SAFE_TIME;
		this.settings = settings;
	}

	@Override
	public void render(Graphics g) {
		
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
		s = "Settings";
				
		Font header2 = new Font("Verdana", Font.PLAIN, 60);
		g.setFont(header2);
				
		stringLen = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();  
		start = width/2 - stringLen/2;  
		g.drawString(s, start, 200);
        
        // buttons
        g.setColor(Color.WHITE);
        g.fillRect(width/2-100, 600, 200, 100);
        
        // header
     	g.setColor(Color.BLACK);
     	
     	Font butt = new Font("Verdana", Font.PLAIN, 40);
     	Font textM = new Font("Verdana", Font.PLAIN, 30);
     	
     	String b1 = "Number of players";
     	int players = settings.getPlayers();
     	String b3 = "Back";
     	
     	/********* PLAYER NUMBER ***********/
     	g.setFont(textM);
     	stringLen = (int) g.getFontMetrics().getStringBounds(b1, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(b1, start, 300);
        
        // players number
        stringLen = (int) g.getFontMetrics().getStringBounds(""+players, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(""+players, start, 340);
        
        int middle = width/2 - 10;
        
        g.setColor(Color.YELLOW);
        g.fillRect(middle-30, 319, 19, 19);
        g.fillRect(middle+30, 319, 19, 19);
        g.setColor(Color.BLACK);
        g.drawString("-", middle-27, 338);
        g.drawString("+", middle+28, 338);
        /*********** PLAYER NUMBER END *********/
        
        /********* END VALUE ***********/
        b1 = "Win condition (kills)";
        int cond = settings.getEndValue();
        
     	g.setFont(textM);
     	stringLen = (int) g.getFontMetrics().getStringBounds(b1, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(b1, start, 380);
        
        // players number
        stringLen = (int) g.getFontMetrics().getStringBounds(""+cond, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(""+cond, start, 420);
        
        g.setColor(Color.YELLOW);
        g.fillRect(middle-30, 399, 19, 19);
        g.fillRect(middle+30, 399, 19, 19);
        g.setColor(Color.BLACK);
        g.drawString("-", middle-27, 418);
        g.drawString("+", middle+28, 418);
        /*********** END VALUE END :) *********/
        
        // back button
        g.setColor(Color.BLACK);
        g.setFont(butt);
        stringLen = (int) g.getFontMetrics().getStringBounds(b3, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(b3, start, 660); 
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		if (workingSince <= System.currentTimeMillis())
		{
			int x = e.getX();
			int y = e.getY();
			
			if (x > width/2-100 && x < width/2-100+200 && y > 600 && y < 700)
			{
				this.layer.setScreen(SCREEN.MAIN_MENU);
			}
			else if (x > width/2-40 && x < width/2-40+19 && y > 319 && y < 319+19)
			{
				int tmp = settings.getPlayers();
				settings.setPlayers((tmp>2)? --tmp:tmp);
			}
			else if (x > width/2+20 && x < width/2+20+19 && y > 319 && y < 319+19)
			{
				int tmp = settings.getPlayers();
				settings.setPlayers((tmp<3)? ++tmp:tmp);
			}
			else if (x > width/2-40 && x < width/2-40+19 && y > 319+80 && y < 319+19+80)
			{
				int tmp = settings.getEndValue();
				settings.setEndValue((tmp>1)? --tmp:tmp);
			}
			else if (x > width/2+20 && x < width/2+20+19 && y > 319+80 && y < 319+19+80)
			{
				int tmp = settings.getEndValue();
				settings.setEndValue((tmp<50)? ++tmp:tmp);
			}
		}
	}
	
	public void removeListener () {
		this.layer.removeMouseListener(this);
	}

	@Override
	public void update() {}

	@Override
	public void mouseClicked(MouseEvent arg0) {		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
