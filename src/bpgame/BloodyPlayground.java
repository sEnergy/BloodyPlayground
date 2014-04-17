package bpgame;

import java.util.Random;

import javax.swing.JFrame;

import bpgame.soundengine.SoundManager;

/*
 * Game frame class also carrying main function.
 */
public class BloodyPlayground extends JFrame {
	
	private static final long serialVersionUID = 4704860925089425366L;
	private static final String GAME_NAME = "Bloody Playground - alfa version";
	
	public static SoundManager s = null;
	public static Random r = new Random();

	public static void main(String[] args) {
		BloodyPlayground game = new BloodyPlayground();
		game.init();
	}
	
	private void init () {
		this.setTitle(GAME_NAME);
		
		RenderLayer layer = new RenderLayer(this);
		
		this.add(layer);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		layer.start();
	}
}
