package bpgame.screens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import bpgame.RenderLayer;

/*
 * Abstract class of program screen
 */
public abstract class AbstractScreen {
	
	protected int width, height;
	protected RenderLayer layer;
	
	/*
	 * Enum for unique program screens
	 */
	public enum SCREEN {
		MAIN_MENU,
		SETTINGS,
		GAME
	}
	
	public AbstractScreen (RenderLayer layer) {
		this.layer = layer;
	}
	
	/*
	 * Turns on antialiasing for texts and rendered objects
	 */
	public void setAntialiasing (Graphics g) {		
		((Graphics2D)g).setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP));
	}
	
	public void getRenderDimensions () {
		this.width = layer.getWidth();
		this.height = layer.getHeight();
	}
	
	public abstract void render (Graphics g);
	public abstract void update ();

}
