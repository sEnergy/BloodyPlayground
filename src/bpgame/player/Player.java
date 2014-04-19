package bpgame.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import bpgame.BloodyPlayground;
import bpgame.RenderLayer;
import bpgame.events.handling.CollisionHandling;
import bpgame.map.GameMap;
import bpgame.weapons.Weapon;
import bpgame.weapons.Weapon.WEAPON;
import bpgame.weapons.projectiles.Projectile;
import bpgame.weapons.projectiles.ProjectileSeedManager;

/*
 * Class carrying all player information.
 */
public class Player extends KeyAdapter implements KeyListener, Comparable<Object> {
	
	// variable used for setting id of player
	private static int playerNumber = 0;
	
	private final int DEFAULT_SIZE = 75;
	
	/*
	 * Enum for player directions
	 */
	public static enum DIRECTION {
		UP, 
		DOWN, 
		LEFT, 
		RIGHT
	}
	
	private static CollisionHandling ch = null;
	private static ArrayList<Projectile> projectiles = null;
	
	// basic player datat
	private int id;
	private int x_pos, y_pos;
	private int size;
	private DIRECTION direction;
	
	private Color color;
	private String name;
	
	// movement
	private int max_speed = 5;
	private double speedMultiplier = 1.0;
	private double bonusSpeedMultiplier = 1.5;
	private int current_speed = 0;
	
	// life
	private boolean alive = false;
	private int spawnDelayMs = 1500;
	private long nextSpawnTime;
	
	// score
	private int deaths, kills;
	
	// control keys
	private int goUp;
	private int goDown;
	private int goLeft;
	private int goRight;
	private int fire;
	
	// power up states
	private boolean puSpeedOn = false;
	private boolean puAmmoOn = false;
	private boolean puVestOn = false;
	private boolean puMarksmanOn = false;
	private boolean puRessurOn = false;
	private boolean puPenetrateOn = false;
	
	// power up timings
	private long puSpeedUntil;
	private long puAmmoUntil;
	private long puNoVestUntil;
	private long puMarksmanUntil;
	private long puRessurUntil;
	private long puPenetrateUntil;
	
	private Weapon weapon;
	private Weapon pistolBackup = null;
	
	private long lastShotTime;
	
	public Player (RenderLayer map, ArrayList<Projectile> projectiles, CollisionHandling ch) {

		this.id = ++Player.playerNumber;
		this.size = (int)(DEFAULT_SIZE*GameMap.getGameScale());

		if (Player.ch == null)
			Player.ch = ch;
		
		if (Player.projectiles == null)
			Player.projectiles = projectiles;
		
		// according to player id, set controls, name and color
		switch(this.id)
		{
			case 1:
				this.color = Color.GREEN;
				this.name = "Green";
				this.goUp = KeyEvent.VK_NUMPAD8;
				this.goDown = KeyEvent.VK_NUMPAD5;
				this.goLeft = KeyEvent.VK_NUMPAD4;
				this.goRight = KeyEvent.VK_NUMPAD6;
				this.fire = KeyEvent.VK_NUMPAD0;
				break;
			case 2:
				this.color = Color.YELLOW;
				this.name = "Yellow";
				this.goUp = KeyEvent.VK_W;
				this.goDown = KeyEvent.VK_S;
				this.goLeft = KeyEvent.VK_A;
				this.goRight = KeyEvent.VK_D;
				this.fire = KeyEvent.VK_CONTROL;
				break;
			case 3:
				this.color = Color.RED;
				this.name = "Red";
				this.goUp = KeyEvent.VK_I;
				this.goDown = KeyEvent.VK_K;
				this.goLeft = KeyEvent.VK_J;
				this.goRight = KeyEvent.VK_L;
				this.fire = KeyEvent.VK_SPACE;
				break;
		}	
		
		this.spawn();
	}
	
	/*
	 * Spawn player
	 */
	public void spawn() {

		// finding valid spawn point
		do {
			this.x_pos = BloodyPlayground.r.nextInt(Player.ch.getWidth());
			this.y_pos = BloodyPlayground.r.nextInt(Player.ch.getHeight());
		} while (!ch.isValidSpawnPosition(this.x_pos, this.y_pos, this.id));
		
		System.out.println(this.name+" player spawned.");
		
		this.alive = true;
		this.weapon = new Weapon(WEAPON.PISTOL, this);
		this.lastShotTime = System.currentTimeMillis()-1000; // -1000 is to make sure player cas shoot rigth after spawn
		
		// setting random direction
		switch(BloodyPlayground.r.nextInt(4)+1)
		{
			case 1:
				this.direction = DIRECTION.UP;
				break;
			case 2:
				this.direction = DIRECTION.DOWN;
				break;
			case 3:
				this.direction = DIRECTION.LEFT;
				break;
			case 4:
				this.direction = DIRECTION.RIGHT;
				break;
		}
	}
	
	/*
	 * Kills player
	 */
	public void die () {
		this.x_pos = this.y_pos = -1000; // move out of map
		this.alive = false;
		this.deaths++;
		this.nextSpawnTime = System.currentTimeMillis() + (puRessurOn? 0:spawnDelayMs);
		
		if (ProjectileSeedManager.countSeedsOfPlayer(this.id) != 0)
			BloodyPlayground.s.stopSound("weapon_smg");
		
		ProjectileSeedManager.deleteSeedsOfPlayer(this.id);
	}
	
	/*
	 * Fires a weapon
	 */
	public void fire () {
		
		// is enough time since last shot passed
		if (System.currentTimeMillis() - this.lastShotTime > weapon.getFireDelayMs() / (puMarksmanOn? 1.5:1))
		{	
			// attempt to fire
			if (weapon.fire(ch, this.isPuAmmoOn(), this.isPuPenetrateOn(), this.isPuMarksmanOn()))
			{
				// firing successed
				this.lastShotTime = System.currentTimeMillis();
				System.out.println(this.name+" player fired.");
				
				if (weapon.getClipState() == 0) // this was last shot of weapon
				{
					if (weapon.getWeaponType() == WEAPON.PISTOL) // pistol realoads
						System.out.println(this.name+" player started reloading.");
					else // other weapon is dropped
					{
						this.pullPistol();
						System.out.println(this.name+" player has only pistol again.");
					}
				}
				
			}
			else // no rounds - reloading
			{
				this.lastShotTime = System.currentTimeMillis();
				BloodyPlayground.s.playSound("dry_fire");
				System.out.println("Player "+this.id+" could not fire(empty clip).");
			}
		}
	}
	
	/*
	 * Updates player object (one tick)
	 */
	public void update (GameMap map) {
		
		if (this.alive) // alive player
		{
			if (this.current_speed > 0)
			{
				int x = this.x_pos;
				int y = this.y_pos;
			
				switch (this.direction)
				{
					case DOWN:
						y += this.current_speed*this.speedMultiplier*map.getFloorSpeedMultiplier(this.x_pos, this.y_pos);
						break;
					case LEFT:
						x -= this.current_speed*this.speedMultiplier*map.getFloorSpeedMultiplier(this.x_pos, this.y_pos);
						break;
					case RIGHT:
						x += this.current_speed*this.speedMultiplier*map.getFloorSpeedMultiplier(this.x_pos, this.y_pos);
						break;
					case UP:
						y -= this.current_speed*this.speedMultiplier*map.getFloorSpeedMultiplier(this.x_pos, this.y_pos);
						break;
				}
				
				if (ch.isUnblockedPosition(x, y, this.id)) // no conflict
				{
					this.x_pos = x;
					this.y_pos = y;
				}
			}

			if (weapon.getClipState() == 0)
				weapon.checkForReloadFinish();
		}
		else // dead player
		{
			if (nextSpawnTime < System.currentTimeMillis())
				this.spawn();
		}
	
		this.updatePowerUps();
	}

	/*
	 * Updates states of players powerups
	 */
	private void updatePowerUps() {
		
		if (this.puAmmoOn && this.puAmmoUntil < System.currentTimeMillis())
		{
			this.puAmmoOn = false;
			System.out.println("Bonus Infinite ammo of Player "+id+" expired.");
		}
		
		if (this.puVestOn && this.puNoVestUntil < System.currentTimeMillis())
		{
			this.puVestOn = false;
			System.out.println("Bonus vest of Player "+id+" expired.");
		}	
		
		if (this.puPenetrateOn && this.puPenetrateUntil < System.currentTimeMillis())
		{
			this.puPenetrateOn = false;
			System.out.println("Bonus Penetrating ammo of Player "+id+" expired.");
		}
		
		if (this.puRessurOn && this.puRessurUntil < System.currentTimeMillis())
		{
			this.puRessurOn = false;
			System.out.println("Bonus Instant respawn of Player "+id+" expired.");
		}
		
		if (this.puMarksmanOn && this.puMarksmanUntil < System.currentTimeMillis())
		{
			this.puMarksmanOn = false;
			System.out.println("Bonus Marksman skills of Player "+id+" expired.");
		}
		
		if (this.puSpeedOn && this.puSpeedUntil < System.currentTimeMillis())
		{
			this.puSpeedOn = false;
			this.speedMultiplier = 1.0;
			System.out.println("Bonus Sprint of Player "+id+" expired.");
		}	
	}

	/*
	 * Render player
	 */
	public void render (Graphics g) {
		
		//Image test = Toolkit.getDefaultToolkit().getImage("src/bpgame/resources/gui/test.png");
		//g.drawImage(test, 0, 0, this.layer);
		
		if (this.puVestOn)
		{
			g.setColor(Color.BLUE);
			g.fillOval(this.x_pos-this.size/2-4, this.y_pos-this.size/2-4, this.size+8, this.size+8);
		}
		
		g.setColor(color);
		g.fillOval(this.x_pos-this.size/2, this.y_pos-this.size/2, this.size, this.size);
		
		if (this.puRessurOn)
		{
			g.setColor(Color.BLUE);
			g.fillOval(this.x_pos-this.size/2+15, this.y_pos-this.size/2+15, this.size-30, this.size-30);
		}
		
		int x,y;
		
		switch (this.direction)
		{
			case DOWN:
				x = this.x_pos;
				y = this.y_pos+this.size/2+5;
				break;
			case LEFT:
				x = this.x_pos-this.size/2-5;
				y = this.y_pos;
				break;
			case RIGHT:
				x = this.x_pos+this.size/2+5;
				y = this.y_pos;
				break;
			case UP:
				x = this.x_pos;
				y = this.y_pos-this.size/2-5;
				break;
			default:
				x = this.x_pos;
				y = this.y_pos;
				break;
		}
		
		g.setColor(this.isPuPenetrateOn()? Color.ORANGE:Color.BLACK);
		g.drawLine(this.x_pos, this.y_pos, x, y);
	}

	/*
     * Reactions to presses of some keys
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		if (keyCode == this.fire)
		{
			if (this.alive)
				this.fire();
		}
		
		DIRECTION original = this.direction;
		if (keyCode == this.goUp )
		{
			this.direction = DIRECTION.UP;
			this.current_speed = this.max_speed;
		}
		else if (keyCode == this.goLeft)
		{
			this.direction = DIRECTION.LEFT;
			this.current_speed = this.max_speed;
		}	
		else if (keyCode == this.goRight)
		{
			this.direction = DIRECTION.RIGHT;
			this.current_speed = this.max_speed;
		}
		else if (keyCode == this.goDown)
		{
			this.direction = DIRECTION.DOWN;
			this.current_speed = this.max_speed;
		}		
		
		if (original != this.direction)
		{
			if (ProjectileSeedManager.countSeedsOfPlayer(this.id) != 0)
				BloodyPlayground.s.stopSound("weapon_smg");
			
			ProjectileSeedManager.deleteSeedsOfPlayer(this.id);
		}
	}

	/*
     * Reactions to releases of some keys
	 */
	@Override
	public void keyReleased (KeyEvent e) {
		switch (this.direction)
		{
			case DOWN:
				if (e.getKeyCode() == this.goDown) 
					current_speed = 0;
				break;
			case LEFT:
				if (e.getKeyCode() == this.goLeft) 
					current_speed = 0;
				break;
			case RIGHT:
				if (e.getKeyCode() == this.goRight) 
					current_speed = 0;
				break;
			case UP:
				if (e.getKeyCode() == this.goUp) 
					current_speed = 0;
				break;
		}	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public static ArrayList<Projectile> getProjectiles () {
		return Player.projectiles;
	}
	
	
	public static CollisionHandling getCh() {
		return Player.ch;
	}

	public void addKill() {
		kills++;
	}

	public int getY() {
		return y_pos;
	}
	
	public int getX() {
		return this.x_pos;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName () {
		return this.name;
	}
	
	public DIRECTION getDirection() {
		return direction;
	}
	
	public int getDeaths() {
		return deaths;
	}

	public int getKills() {
		return kills;
	}

	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Weapon w) {
		this.pistolBackup = (this.weapon.getWeaponType()==WEAPON.PISTOL)? this.weapon:new Weapon(WEAPON.PISTOL, this);
		this.weapon = w;
	}
	
	public void pullPistol () {
		this.weapon = this.pistolBackup;
	}

	public void setPuSpeedOn(int duration) {
		this.puSpeedOn = true;
		this.puSpeedUntil = System.currentTimeMillis() + duration;
		this.speedMultiplier = this.bonusSpeedMultiplier;
	}

	public void setPuAmmoOn(int duration) {
		this.puAmmoOn = true;
		this.puAmmoUntil = System.currentTimeMillis() + duration;
	}
	
	public void setPuVestOn(int duration) {
		this.puVestOn = true;
		this.puNoVestUntil = System.currentTimeMillis() + duration;
	}
	
	public void setPuMarksmanOn(int duration) {
		this.puMarksmanOn = true;
		this.puMarksmanUntil = System.currentTimeMillis() + duration;
	}
	
	public void setPuRessurOn(int duration) {
		this.puRessurOn = true;
		this.puRessurUntil = System.currentTimeMillis() + duration;
	}

	public void setPuPenetrateOn(int duration) {
		this.puPenetrateOn = true;
		this.puPenetrateUntil = System.currentTimeMillis() + duration;
	}
	
	public boolean isPuSpeedOn() {
		return puSpeedOn;
	}

	public boolean isPuAmmoOn() {
		return puAmmoOn;
	}
	
	public boolean isPuVestOn() {
		return puVestOn;
	}

	public boolean isPuMarksmanOn() {
		return puMarksmanOn;
	}

	public boolean isPuRessurOn() {
		return puRessurOn;
	}

	public boolean isPuPenetrateOn() {
		return puPenetrateOn;
	}
	
	public static void resetId () {
		Player.playerNumber = 0;
	}
	
	@Override
	public int compareTo (Object o) {
		
		Player pl = (Player)o;
		
	    int BEFORE = -1;
	    int EQUAL = 0;
	    int AFTER = 1;
	    
	    if (this.kills > pl.getKills())
	    	return BEFORE;
	    else if (this.deaths > pl.getDeaths())
	    	return AFTER;
	    else
	    	return EQUAL;
	}
}
