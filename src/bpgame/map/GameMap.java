package bpgame.map;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import bpgame.RenderLayer;
import bpgame.map.objects.Obstacle;
import bpgame.map.objects.SpecialGround;

public class GameMap {
	
	private final double DEFAULT_WIDTH = 1920.0;
	
	private ArrayList<Obstacle> obstacles = null;
	private ArrayList<SpecialGround> grounds = null;
	
	private static double gameMapScale = 1.0;
	
	private String mapName = "bonapetit";
	
	Image background, foreground;
	
	public GameMap (int width) {
		
		GameMap.gameMapScale = width/DEFAULT_WIDTH;
		
		this.obstacles = new ArrayList<Obstacle>();
		this.grounds = new ArrayList<SpecialGround>();
	    
	    this.loadTextures();	
	    this.setObstacles();
	    this.setSpecialGrounds();
	}
	
	private void loadTextures () {
		this.background = new ImageIcon("src/bpgame/resources/gui/maps/"+this.mapName+"/background.png").getImage();
		this.foreground = new ImageIcon("src/bpgame/resources/gui/maps/"+this.mapName+"/foreground.png").getImage();
	}
	
	public void renderBackground (Graphics g, RenderLayer rl) {
		g.drawImage(this.background, 0, 0, (int)(rl.getWidth()), (int)(rl.getHeight()), rl);
	}
	
	public void renderForeground (Graphics g, RenderLayer rl) {
		g.drawImage(this.foreground, 0, 0, (int)(rl.getWidth()), (int)(rl.getHeight()), rl);
	}
	
	private void setObstacles () {
		
		int xPoly[], yPoly[];
				
		switch (this.mapName)
		{
			case "bonapetit":
				// top bush pl
				xPoly = new int[]{184,235,283,327,369,397,370,327,271,232,190,157,150};
			    yPoly = new int[]{102, 75, 69, 75, 97,144,220,249,261,253,229,183,145};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, false));
				// tree pl
				xPoly = new int[]{451,459,718,760,790,814,819,782,783,710,637,525,459};
			    yPoly = new int[]{802,776,618,627,648,689,720,758,805,858,857,903,825};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, false));
				// left bush pl
				xPoly = new int[]{1461,1501,1552,1589,1624,1669,1707,1725,1728,1752,1739,1704,1659,1512,1424,1385,1361,1362,1377,1412};
			    yPoly = new int[]{443,  407, 386, 388, 394, 414, 435, 473, 506, 550, 602, 639, 656, 652, 615, 588, 546, 511, 484, 457};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, false));
				// house top pl
				xPoly = new int[]{759,768,838,971,1094,1177,1206,1205,1184,1165,768};
			    yPoly = new int[]{310,201, 88, 51,  57, 110, 187, 271, 299, 316,318};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, false));
				// house bootom pl
				xPoly = new int[]{801,825,1182,1203,1175,1131,1047,949,815,793};
			    yPoly = new int[]{351,343, 336, 362, 388, 426, 455,451,425,385};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, false));
				// top bush pr
				xPoly = new int[]{189,236,284,356,376,358,333,312,285,243,197,188};
			    yPoly = new int[]{142,111, 98,125,140,178,208,223,228,218,190,150};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, true));
				// tree pr
				xPoly = new int[]{476,728,765,783,548};
			    yPoly = new int[]{806,655,676,714,872};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, true));
				// left bush pr
				xPoly = new int[]{1585,1652,1704,1714,1699,1649,1503,1419,1400,1489,1517};
			    yPoly = new int[]{ 419, 450, 523, 564, 569, 616, 609, 571, 524, 477, 443};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, true));
				// house top pr
				xPoly = new int[]{784,1000,1156};
			    yPoly = new int[]{284,  69, 265};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, true));
				// house bootom pr
				xPoly = new int[]{833,1161,1002};
			    yPoly = new int[]{385, 369 ,430};
				this.obstacles.add(new Obstacle(xPoly, yPoly, GameMap.gameMapScale, true));
				break;
		}
	}
	
	private void setSpecialGrounds () {
		
		int xPoly[], yPoly[];
				
		switch (this.mapName)
		{
			case "bonapetit":
				xPoly = new int[]{276,444,537,562,589,579,399,303,277,247};
			    yPoly = new int[]{457,397,454,436,541,582,643,606,612,502};
				this.grounds.add(new SpecialGround(xPoly, yPoly, GameMap.gameMapScale, 0.45));
				break;
		}
	}
	
	public double getFloorSpeedMultiplier (int x, int y) {
		
		for (SpecialGround sg: this.grounds)
			if (sg.getArea().contains(x, y))
				return sg.getSpeedMultiplier();
		
		return 1.0; // default speed multiplier
	}
	
	public ArrayList<Obstacle> getObstacles () {
		return this.obstacles;
	}
	
	public ArrayList<SpecialGround> getSpecialGrounds () {
		return this.grounds;
	}
	
	public static double getGameScale () {
		return GameMap.gameMapScale;
	}
}
