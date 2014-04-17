package bpgame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import bpgame.gamedata.GameResults;
import bpgame.gamedata.GameSettings;
import bpgame.program.ProgramSettings;
import bpgame.screens.AbstractScreen;
import bpgame.screens.AbstractScreen.SCREEN;
import bpgame.screens.GameScreen;
import bpgame.screens.MainMenuScreen;
import bpgame.screens.PauseScreen;
import bpgame.screens.ResultsScreen;
import bpgame.screens.SettingsScreen;
import bpgame.soundengine.Sound;
import bpgame.soundengine.SoundManager;

/*
 * Core of the game - this class runs all screen loops.
 */
public class RenderLayer extends Canvas implements Runnable {

	private static final long serialVersionUID = 5145277739867449106L;
	
	private final int TICKS_PER_SECOND = 60; // number of times game is updated per second == game speed
	private final double NS_PER_TICK = Math.pow(10,9)/TICKS_PER_SECOND; // target is 60 ticks per second
	
	private Thread t;
	
	/*
	 * These variables are used when pausing game. 
	 * 
	 * pause            --- represents state of game
	 * unprocessedTicks --- number of updates to perform
	 */
	private boolean pause = false;
	private double unprocessedTicks;
	

	// represents state of program (what screen is user currently using)
	private SCREEN currentScreen = SCREEN.MAIN_MENU;
	
	private GameScreen gs;
	private GameSettings settings;
	private ProgramSettings ps;
	
	private BloodyPlayground frame;
	
	public RenderLayer (BloodyPlayground frame) {
		super();
		
		// setting default resolution of the game and size of frame
		this.ps = new ProgramSettings(frame);
		this.ps.setResolution("r1280");
		
		this.settings = new GameSettings();
		this.frame = frame;
		
		// setting position on frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int frameXpos = (int) screenSize.getWidth()/2-ps.getCanvasX()/2;
		int frameYpos = (int) screenSize.getHeight()/2-ps.getCanvasY()/2;
		
		this.frame.setLocation(frameXpos,frameYpos);
		this.frame.setVisible(true);
		
		this.t = new Thread(this);
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
		
		ps.setResolution("r1280");
		this.setSize(ps.getCanvasDimension());
		this.frame.setSize(ps.getFrameDimension());
		
		long lastCycleTime = System.nanoTime(); // time of beginning of last cycle
		long lastOutputTime = System.currentTimeMillis(); // last info output time
		
		int fps = 0;
		int ticks = 0;
		
		/*
		 * This is infinite loop of program screens.
		 * 
		 * The very core of this program.
		 */
		while (true)
		{
			/*
			 * Game screen
			 */
			if (currentScreen == SCREEN.GAME)
			{
				BloodyPlayground.s.playSound("music");
				
				// initialization if gameplay screens
				gs  = new GameScreen(this);
				PauseScreen ps = new PauseScreen(this);
				
				// game itself
				while (!gs.isGameOver()) 
				{
					// counting unprocessed ticks
					long CurrentCycleTime = System.nanoTime();
					unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / NS_PER_TICK; 	

					lastCycleTime = CurrentCycleTime;
					
					if (!pause)
					{
						/*
						 * Game not paused:
						 * 		-> render current state of game screen
						 *      -> update game screen
						 */
						this.render(gs);
						fps++;
						
						while (unprocessedTicks >= 1) {
							ticks++;
							unprocessedTicks--;
							gs.update();
						}
					}
					else
					{
						/*
						 * Game is paused:
						 * 		-> render pause menu
						 */
						this.render(ps);					
					}
					
					/*
					 * This is just backend state watch.
					 */
					if (System.currentTimeMillis() - lastOutputTime > 1000)
					{
						System.out.println("Ticks: "+ticks+" FPS:"+fps);
						lastOutputTime += 1000;
						ticks = fps = 0;
					}
				}
				
				/*
				 * End of game. Stop all sounds and remove gameplay screens listeners
				 */
				BloodyPlayground.s.stopAllSounds();
				gs.removeListeners();
				ps.removeListener();
				
				/*
				 * Generating game results based on endgame gameScreen state,
				 * Initialization of game screnn.
				 */
				GameResults results = new GameResults (this.gs.getPlayerList());
				ResultsScreen rs = new ResultsScreen (this, results);
				
				// results screen loop
				while (true) 
				{
					/*
					 * When there is something to update, uncomment foolowing:
					 */
					/*
					long CurrentCycleTime = System.nanoTime();
					unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / NS_PER_TICK;
					lastCycleTime = CurrentCycleTime;
					
					this.render(rs);
					fps++;	
					
					while (unprocessedTicks >= 1) {
						ticks++;
						unprocessedTicks--;
						rs.update();
					}
					*/
					
					this.render(rs);
					
					/*
					 * This is just backend state watch.
					 */
					if (System.currentTimeMillis() - lastOutputTime > 1000)
					{
						System.out.println("Ticks: "+ticks+" FPS:"+fps);
						lastOutputTime += 1000;
						ticks = fps = 0;
					}
					
					if (rs.expired()) // development-stage end condition
						break;
				}
				
				currentScreen = SCREEN.MAIN_MENU; // go to main menu
			}
			/*
			 * Main menu screen
			 */
			else if (currentScreen == SCREEN.MAIN_MENU)
			{
				MainMenuScreen menu = new MainMenuScreen(this);
				
				while (true) 
				{
					/*
					 * When there is something to update, uncomment foolowing:
					 */
					/*
					long CurrentCycleTime = System.nanoTime();
					unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / NS_PER_TICK; 
					
					this.render(menu);
					fps++;	
					
					lastCycleTime = CurrentCycleTime;
					
					while (unprocessedTicks >= 1) {
						ticks++;
						unprocessedTicks--;
						menu.update();
					}
					*/
					
					this.render(menu);
					
					/*
					 * User moved from Main menu screen
					 * 		-> remove Main menu mouse listener
					 *		-> break Main menu loop
					 */
					if (currentScreen != SCREEN.MAIN_MENU)
					{
						menu.removeListener();
						break;
					}
					
					/*
					 * This is just backend state watch.
					 */
					if (System.currentTimeMillis() - lastOutputTime > 1000)
					{
						System.out.println("Ticks: "+ticks+" FPS:"+fps);
						
						lastOutputTime += 1000;
						ticks = fps = 0;
					}
				}
			}
			/*
			 * Settings screen
			 */
			else if (currentScreen == SCREEN.SETTINGS)
			{
				SettingsScreen menu  = new SettingsScreen(this, this.settings);
				
				while (true) 
				{
					/*
					 * When there is something to update, uncomment foolowing:
					 */
					/*
					long CurrentCycleTime = System.nanoTime();
					unprocessedTicks += ( CurrentCycleTime - lastCycleTime ) / NS_PER_TICK; 
					
					this.render(menu);
					fps++;	
					
					lastCycleTime = CurrentCycleTime;
					
					while (unprocessedTicks >= 1) {
						ticks++;
						unprocessedTicks--;
						menu.update();
					}
					*/
					
					this.render(menu);
					
					/*
					 * User moved from Settings screen
					 * 		-> remove Settings screen mouse listener
					 *		-> break Settings screen loop
					 */
					if (currentScreen != SCREEN.SETTINGS) 
					{
						menu.removeListener();
						break;
					}
					
					/*
					 * This is just backend state watch.
					 */
					if (System.currentTimeMillis() - lastOutputTime > 1000)
					{
						System.out.println("Ticks: "+ticks+" FPS:"+fps);
						
						lastOutputTime += 1000;
						ticks = fps = 0;
					}
				} // end of Settings screen loop
			} // end of Settings screen
		} // end of screens loop
	}

	/*
	 * Prepares buffer strategy for rendering and calls render method of current screen
	 */
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
	
	public void start () {
		this.t.start();
	}
	
	public void setScreen (SCREEN screen) {
		this.currentScreen = screen;
	}
	
	/*
	 * Pauses/unpauses the game.
	 * 
	 * When unpause, set number of unprocessed ticks to zero in order
	 * to prevent running of pause-time woth of updates.
	 */
	public void pause () {
		this.pause = !this.pause;
		
		if (!pause) 
			this.unprocessedTicks = 0;
	}
	
	public GameSettings getGameSettings() {
		return this.settings;
	}
	
	public boolean isPaused () {
		return this.pause;
	}
	
	public GameScreen getGameScreen () {
		return this.gs;
	}
	
	public ProgramSettings getProgramSettings () {
		return this.ps;
	}
	
	public BloodyPlayground getFrame () {
		return this.frame;
	}
}
