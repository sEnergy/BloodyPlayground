package bpgame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import bpgame.screens.*;

public class RenderLayer extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	
	//private boolean quit = false;
	private boolean GameOver = false;
	private Thread t;
	
	public RenderLayer () {
		super();
		this.setSize(new Dimension(WIDTH, HEIGHT));
		t = new Thread(this);
	}

	@Override
	public void run() {
		
		long lastCycleTime = System.nanoTime(); // time of beginning of last cycle
		long lastOutputTime = System.currentTimeMillis(); // last info output time
		
		double unprocessedTicks = 0;
		double nsPerTick = Math.pow(10,9)/60; // target is 60 ticks per second
		
		int fps = 0;
		int ticks = 0;
		
		if (true) // fake game choose
		{
			GameScreen gs  = new GameScreen(this, 3);
			
			while (!GameOver) 
			{
				long CurrentCycleTime = System.nanoTime();
				unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / nsPerTick; 
				lastCycleTime = CurrentCycleTime;
				
				while (unprocessedTicks >= 1) {
					ticks++;
					unprocessedTicks--;
					this.update(gs);
				}
				
				fps++;
				this.render(gs);
				
				if (System.currentTimeMillis() - lastOutputTime > 1000)
				{
					System.out.println("Ticks: "+ticks+" FPS:"+fps);
					
					lastOutputTime += 1000;
					ticks = fps = 0;
				}
			}
		}	
		
		System.exit(0);
	}
	
	private void render(AbstractScreen gs) {
		
		BufferStrategy buffer = this.getBufferStrategy();
		
		if (buffer == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		else
		{
			Graphics g = buffer.getDrawGraphics();
			gs.render(g);
			g.dispose();
			
			buffer.show();
		}
	}

	private void update(AbstractScreen gs) {
		gs.update();
	}

	public void start () {
		t.start();
	}
}
