package bpgame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;

import bpgame.soundengine.SoundManager;

/*
 * Game frame class also carrying main function.
 */
public class BloodyPlayground extends JFrame {
	
	private static final long serialVersionUID = 4704860925089425366L;
	private static final String GAME_NAME = "Bloody Playground - alfa version";
	
	private boolean fullScreen = false;
	
	public static SoundManager s = null;
	public static Random r = new Random();

	public static void main(String[] args) {
		BloodyPlayground game = new BloodyPlayground();
		game.init();
	}
	
	private void init () {
		this.setTitle(GAME_NAME);
		this.trySetFullscreen();
		
		RenderLayer layer = new RenderLayer(this);
		
		this.add(layer);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		layer.start();
	}
	
	/*
	 * Method checks, if current screen resolution matches one of supported resolutions.
	 * If yes, frame is set undecorated and fullscreen.
	 */
	private void trySetFullscreen() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		
		if ((width == 1920 && height == 1080) || (width == 1600 && height == 900)
		  ||(width == 1366 && height == 768)  || (width == 1280 && height == 720))
		{
			this.setUndecorated(true);
			this.setExtendedState(MAXIMIZED_BOTH);
			this.fullScreen = true;
		}
	}
	
	public boolean isFullScreenOn () {
		return this.fullScreen;
	}
}
