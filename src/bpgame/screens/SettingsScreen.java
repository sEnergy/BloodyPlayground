package bpgame.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bpgame.BloodyPlayground;
import bpgame.RenderLayer;
import bpgame.gamedata.GameSettings;
import bpgame.program.ProgramSettings;

public class SettingsScreen extends AbstractScreen implements MouseListener{
	
	private final int SAFE_TIME = 100;
	private final long workingSince;
	
	private GameSettings gameSettings;
	ProgramSettings programSettings;
	
	public SettingsScreen (RenderLayer layer, GameSettings settings) {
		super(layer);
		this.layer.addMouseListener(this);
		this.workingSince = System.currentTimeMillis()+SAFE_TIME;
		this.gameSettings = settings;
		this.programSettings = layer.getProgramSettings();
		this.layer.setSize(layer.getProgramSettings().getCanvasDimension());
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
     	int players = gameSettings.getPlayers();
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
        int cond = gameSettings.getEndValue();
        
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
        
        /********* RESOULTION ***********/
        if (!this.layer.getFrame().isFullScreenOn())
        {
        	b1 = "Resolution";
            String res = this.programSettings.getCanvasX()+"x"+this.programSettings.getCanvasY();
            
         	g.setFont(textM);
         	stringLen = (int) g.getFontMetrics().getStringBounds(b1, g).getWidth();  
            start = width/2 - stringLen/2;  
            g.drawString(b1, start, 460);
            
            // players number
            stringLen = (int) g.getFontMetrics().getStringBounds(res, g).getWidth();  
            start = width/2 - stringLen/2;  
            g.drawString(res, start, 500);
            
            g.setColor(Color.YELLOW);
            g.fillRect(middle-100, 479, 19, 19);
            g.fillRect(middle+100, 479, 19, 19);
            g.setColor(Color.BLACK);
            g.drawString("-", middle-97, 499);
            g.drawString("+", middle+98, 499);
        }
        /*********** RESOULTION END :) *********/
        
        // back button
        g.setColor(Color.BLACK);
        g.setFont(butt);
        stringLen = (int) g.getFontMetrics().getStringBounds(b3, g).getWidth();  
        start = width/2 - stringLen/2;  
        g.drawString(b3, start, 660); 
        
        /*
        g.drawLine(0, 0, 1280, 720);
        g.drawLine(1200, 0, 1280, 720);
        
        g.drawLine(0, 0, 1366, 768);
        g.drawLine(1300, 0, 1366, 768);
        
        g.drawLine(0, 0, 1600, 900);
        g.drawLine(1500, 0, 1600, 900);
        
        g.drawLine(0, 0, 1920, 1080);
        g.drawLine(1800, 0, 1920, 1080);
        */
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
				int tmp = gameSettings.getPlayers();
				gameSettings.setPlayers((tmp>2)? --tmp:tmp);
			}
			else if (x > width/2+20 && x < width/2+20+19 && y > 319 && y < 319+19)
			{
				int tmp = gameSettings.getPlayers();
				gameSettings.setPlayers((tmp<3)? ++tmp:tmp);
			}
			else if (x > width/2-40 && x < width/2-40+19 && y > 319+80 && y < 319+19+80)
			{
				int tmp = gameSettings.getEndValue();
				gameSettings.setEndValue((tmp>1)? --tmp:tmp);
			}
			else if (x > width/2+20 && x < width/2+20+19 && y > 319+80 && y < 319+19+80)
			{
				int tmp = gameSettings.getEndValue();
				gameSettings.setEndValue((tmp<50)? ++tmp:tmp);
			}
			
			if (!this.layer.getFrame().isFullScreenOn())
	        {
				if (x > width/2-10-100 && x < width/2-10-100+19 && y > 479 && y < 479+19)
				{
					this.changeResolution(-1);
				}
				else if (x > width/2-10+100 && x < width/2-10+100+19 && y > 479 && y < 479+19)
				{
					this.changeResolution(1);
				}
	        }
		}
	}
	
	/*
	 * Increments or decrements resolution, according to sign of integer
	 */
	private void changeResolution (int i) {
		this.programSettings.setResolution(i);
		this.layer.setSize(programSettings.getCanvasDimension());
		
		BloodyPlayground frame = this.layer.getFrame(); 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int frameXpos = (int) screenSize.getWidth()/2-programSettings.getCanvasX()/2;
		int frameYpos = (int) screenSize.getHeight()/2-programSettings.getCanvasY()/2;
		
		frame.setVisible(false);
		frame.setSize(programSettings.getFrameDimension());
		frame.setLocation(frameXpos,frameYpos);
		frame.setVisible(true);
		
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
