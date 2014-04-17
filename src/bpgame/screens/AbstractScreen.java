package bpgame.screens;

import java.awt.Graphics;

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
	
	public void getRenderDimensions () {
		this.width = layer.getWidth();
		this.height = layer.getHeight();
	}
	
	public abstract void render (Graphics g);
	public abstract void update ();

}
