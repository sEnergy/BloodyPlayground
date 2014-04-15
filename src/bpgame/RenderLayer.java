package bpgame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import bpgame.gamedata.GameResults;
import bpgame.gamedata.GameSettings;
import bpgame.screens.*;
import bpgame.screens.AbstractScreen.SCREEN;
import bpgame.soundengine.Sound;
import bpgame.soundengine.SoundManager;

public class RenderLayer extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int TICKS_PER_SECOND = 60;
	
	private Thread t;
	private boolean exit = false;
	private boolean pause = false;
	
	private double unprocessedTicks;
	
	private SCREEN cScreen = SCREEN.MAIN_MENU;
	
	private GameScreen gs;
	private GameSettings settings;
	
	public RenderLayer (int w, int h) {
		super();
		this.setSize(new Dimension(w, h));
		this.t = new Thread(this);
		this.settings = new GameSettings();
	}

	@Override
	public void run() {
		
		BloodyPlayground.s = new SoundManager () {
			@Override
			public void initSounds () {
				sounds.add(new Sound("dry_fire", Sound.getURL("dry_fire.wav")));
				
				sounds.add(new Sound("weapon_pistol", Sound.getURL("weapon_pistol.wav")));
				sounds.add(new Sound("weapon_pistol_reload", Sound.getURL("weapon_pistol_reload.wav")));
				
				sounds.add(new Sound("weapon_shotgun", Sound.getURL("weapon_shotgun.wav")));
				sounds.add(new Sound("weapon_shotgun_marksman", Sound.getURL("weapon_shotgun_marksman.wav")));
				sounds.add(new Sound("weapon_shotgun_nopump", Sound.getURL("weapon_shotgun_nopump.wav")));
				
				sounds.add(new Sound("weapon_smg", Sound.getURL("weapon_smg.wav")));
				
				sounds.add(new Sound("hit_player", Sound.getURL("hit_player.wav")));
				
				sounds.add(new Sound("music", Sound.getURL("music.wav")));
			}
		};
		
		long lastCycleTime = System.nanoTime(); // time of beginning of last cycle
		long lastOutputTime = System.currentTimeMillis(); // last info output time
		
		double nsPerTick = Math.pow(10,9)/TICKS_PER_SECOND; // target is 60 ticks per second
		
		int fps = 0;
		int ticks = 0;
		
		while (!exit)
		{
			if (cScreen == SCREEN.GAME) // fake game choose
			{
				BloodyPlayground.s.playSound("music");
				gs  = new GameScreen(this, this.settings);
				PauseScreen ps = new PauseScreen(this);
				
				while (!gs.isGameOver()) 
				{
					long CurrentCycleTime = System.nanoTime();
					unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / nsPerTick; 	
					
					lastCycleTime = CurrentCycleTime;
					
					if (!pause)
					{
						this.render(gs);
						fps++;
						
						while (unprocessedTicks >= 1) {
							ticks++;
							unprocessedTicks--;
							this.update(gs);
						}
					}
					else
					{
						this.render(ps);					
					}
					
					if (System.currentTimeMillis() - lastOutputTime > 1000)
					{
						System.out.println("Ticks: "+ticks+" FPS:"+fps);
						
						lastOutputTime += 1000;
						ticks = fps = 0;
					}
				}
				
				BloodyPlayground.s.stopSound("music");
				gs.removeListeners();
				ps.removeListener();
				
				
				GameResults results = new GameResults (this.gs.getPlayerList());
				ResultsScreen rs = new ResultsScreen (this, results);
				while (true) 
				{
					long CurrentCycleTime = System.nanoTime();
					unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / nsPerTick; 
					
					this.render(rs);
					fps++;	
					
					lastCycleTime = CurrentCycleTime;
					
					while (unprocessedTicks >= 1) {
						ticks++;
						unprocessedTicks--;
					}
					
					if (System.currentTimeMillis() - lastOutputTime > 1000)
					{
						System.out.println("Ticks: "+ticks+" FPS:"+fps);
						
						lastOutputTime += 1000;
						ticks = fps = 0;
					}
					
					if (rs.expired())
						break;
				}
				
				cScreen = SCREEN.MAIN_MENU;
			}
			else if (cScreen == SCREEN.MAIN_MENU)
			{
				MainMenuScreen menu = new MainMenuScreen(this);
				
				while (true) 
				{
					long CurrentCycleTime = System.nanoTime();
					unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / nsPerTick; 
					
					this.render(menu);
					fps++;	
					
					lastCycleTime = CurrentCycleTime;
					
					while (unprocessedTicks >= 1) {
						ticks++;
						unprocessedTicks--;
						this.update(menu);
					}
					
					if (System.currentTimeMillis() - lastOutputTime > 1000)
					{
						System.out.println("Ticks: "+ticks+" FPS:"+fps);
						
						lastOutputTime += 1000;
						ticks = fps = 0;
					}
					
					if (cScreen != SCREEN.MAIN_MENU)
					{
						menu.removeListener();
						break;
					}
				}
			}
			else if (cScreen == SCREEN.SETTINGS)
			{
				SettingsScreen menu  = new SettingsScreen(this, this.settings);
				
				while (true) 
				{
					long CurrentCycleTime = System.nanoTime();
					unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / nsPerTick; 
					
					this.render(menu);
					fps++;	
					
					lastCycleTime = CurrentCycleTime;
					
					while (unprocessedTicks >= 1) {
						ticks++;
						unprocessedTicks--;
						this.update(menu);
					}
					
					if (System.currentTimeMillis() - lastOutputTime > 1000)
					{
						System.out.println("Ticks: "+ticks+" FPS:"+fps);
						
						lastOutputTime += 1000;
						ticks = fps = 0;
					}
					
					if (cScreen != SCREEN.SETTINGS) 
					{
						menu.removeListener();
						break;
					}
				}
			}
		}	
		
		System.exit(0);
	}

	private void render (AbstractScreen as) {
		
		BufferStrategy buffer = this.getBufferStrategy();
		
		if (buffer == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		else
		{
			Graphics g = buffer.getDrawGraphics();
			as.render(g);
			g.dispose();
			
			buffer.show();
		}
	}

	private void update(AbstractScreen as) {
		as.update();
	}

	public void start () {
		t.start();
	}
	
	public void setScreen (SCREEN s) {
		this.cScreen = s;
	}
	
	public GameSettings getGameSettings() {
		return this.settings;
	}
	
	public boolean isPaused () {
		return this.pause;
	}
	
	public void pause () {
		this.pause = !this.pause;
		if (!pause) unprocessedTicks = 0;
	}
	
	public GameScreen getGameScreen () {
		return this.gs;
	}
}
