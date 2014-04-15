package bpgame;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import bpgame.soundengine.SoundManager;

public class BloodyPlayground extends JFrame {
	
	public static SoundManager s = null;

	private static final long serialVersionUID = 4704860925089425366L;
	public static final String GAME_NAME = "Bloody Playground - alfa version";

	public static void main(String[] args) {
		BloodyPlayground  core = new BloodyPlayground();
		core.init();
	}
	
	private void init () {
		RenderLayer layer = new RenderLayer(1024, 768);
		this.add(layer);
		//this.setUndecorated(true);
		this.pack();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		//this.setExtendedState(MAXIMIZED_BOTH); 
		this.setVisible(true);
		
		this.repaint();
		this.setTitle(GAME_NAME);
		
		layer.start();
	}

}
