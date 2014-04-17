package bpgame.program;

import java.awt.Dimension;
import java.awt.Insets;

import bpgame.BloodyPlayground;

public class ProgramSettings {
	
	private BloodyPlayground frame = null;
	
	private Dimension frameDimension = null;
	private Dimension canvasDimension = null;
	
	int x, y;
	
	public ProgramSettings (BloodyPlayground f) {
		this.frame = f;
	}
	
	public void setResolution (String s) {
		
		switch (s) 
		{
			case "r1920": 
				this.x = 1920;
				this.y = 1080;
				break;
			case "r1600": 
				this.x = 1600;
				this.y = 900;
				break;
			case "r1366": 
				this.x = 1366;
				this.y = 768;
				break;
			case "r1280": 
			default:
				this.x = 1280;
				this.y = 720;
				break;
		}
		
		this.computeDimensions();
	}
	
	public void setResolution (int i) {
		
		if (i > 0)
		{
			switch (this.x) 
			{
				case 1600: 
					this.x = 1920;
					this.y = 1080;
					break;
				case 1366: 
					this.x = 1600;
					this.y = 900;
					break;
				case 1280: 
					this.x = 1366;
					this.y = 768;
					break;
			}
		}
		else if (i < 0)
		{
			switch (this.x) 
			{
				case 1920: 
					this.x = 1600;
					this.y = 900;
					break;
				case 1600: 
					this.x = 1366;
					this.y = 768;
					break;
				case 1366: 
					this.x = 1280;
					this.y = 720;
					break;
				default:
					return;
			}
		}
		
		this.computeDimensions();
	}
	
	private void computeDimensions () {
		Insets i = this.frame.getInsets();
		
		int hInsets = i.left+i.right;
		int vInsets = i.top+i.bottom;
		
		this.canvasDimension = new Dimension(this.x,this.y);
		this.frameDimension = new Dimension(this.x+hInsets,this.y+vInsets);
	}
	
	public Dimension getFrameDimension() {
		return this.frameDimension;
	}

	public Dimension getCanvasDimension() {
		return this.canvasDimension;
	}

	public int getCanvasX() {
		return this.x;
	}

	public int getCanvasY() {
		return this.y;
	}

}
