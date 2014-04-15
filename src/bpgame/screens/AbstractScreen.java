package bpgame.screens;

import java.awt.Graphics;

import bpgame.RenderLayer;

public abstract class AbstractScreen {
	
	protected final int width, height;
	protected RenderLayer layer;
	
	public enum SCREEN {
		MAIN_MENU,
		SETTINGS,
		GAME
	}
	
	public AbstractScreen (RenderLayer layer) {
		this.layer = layer;
		this.width = layer.getWidth();
		this.height = layer.getHeight();
	}
	
	public abstract void render (Graphics g);
	public abstract void update ();

}
