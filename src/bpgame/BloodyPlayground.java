/**
 * 
 */
package bpgame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * @author marcel
 *
 */
public class BloodyPlayground extends JFrame {

	private static final long serialVersionUID = 4704860925089425366L;
	public static final String GAME_NAME = "Bloody Playground v1.0";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BloodyPlayground  core = new BloodyPlayground();
		core.init();
	}
	
	private void init () {
		
		RenderLayer layer = new RenderLayer();
		this.add(layer);
		this.pack();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle(GAME_NAME);
		
		layer.start();
	}

}
