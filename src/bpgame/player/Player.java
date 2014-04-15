package bpgame.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import bpgame.BloodyPlayground;
import bpgame.RenderLayer;
import bpgame.eventhandling.CollisionHandling;
import bpgame.weapons.*;
import bpgame.weapons.Weapon.WEAPON;
import bpgame.weapons.projectiles.Projectile;
import bpgame.weapons.projectiles.ProjectileSeedManager;

public class Player implements KeyListener, Comparable<Object> {
	
	private static int playerNumber = 0;
	
	private static CollisionHandling ch = null;
	
	public static enum DIRECTION {
		UP, DOWN, LEFT, RIGHT
	}
	
	private int id;
	
	private int x_pos, y_pos;
	private int size = 50;
	
	private Color color;
	private String name;
	
	private int max_speed = 5;
	private double speedMultiplier = 1.0;
	private double bonusSpeedMultiplier = 1.5;
	private int current_speed = 0;
	
	private DIRECTION direction;
	
	private boolean alive = false;
	private int spawnDelayMs = 1500;
	private long nextSpawnTime;
	
	private int deaths, kills;
	
	private int goUp;
	private int goDown;
	private int goLeft;
	private int goRight;
	private int fire;
	
	private long lastShot;
	
	private boolean puSpeedOn = false;
	private boolean puAmmoOn = false;
	private boolean puVestOn = false;
	private boolean puMarksmanOn = false;
	private boolean puRessurOn = false;
	private boolean puPenetrateOn = false;
	
	private long puSpeedUntil;
	private long puAmmoUntil;
	private long puNoVestUntil;
	private long puMarksmanUntil;
	private long puRessurUntil;
	private long puPenetrateUntil;

	private RenderLayer map;
	private Weapon w, pistolBackup = null;
	private static ArrayList<Projectile> projectiles = null;
	
	public Player (RenderLayer map, ArrayList<Projectile> projectiles, CollisionHandling ch) {

		this.id = ++Player.playerNumber;
		this.lastShot = System.currentTimeMillis();
		
		this.map = map;
		Player.ch = ch;
		
		Player.projectiles = new ArrayList<Projectile>();
		Player.projectiles = projectiles;
		
		switch(this.id)
		{
			case 1:
				this.color = Color.GREEN;
				this.name = "Green";
				this.goUp = KeyEvent.VK_NUMPAD8;
				this.goDown = KeyEvent.VK_NUMPAD5;
				this.goLeft = KeyEvent.VK_NUMPAD4;
				this.goRight = KeyEvent.VK_NUMPAD6;
				this.fire = KeyEvent.VK_NUMPAD1;
				break;
			case 2:
				this.color = Color.YELLOW;
				this.name = "Yellow";
				this.goUp = KeyEvent.VK_E;
				this.goDown = KeyEvent.VK_D;
				this.goLeft = KeyEvent.VK_S;
				this.goRight = KeyEvent.VK_F;
				this.fire = KeyEvent.VK_A;
				break;
			case 3:
				this.color = Color.RED;
				this.name = "Red";
				this.goUp = KeyEvent.VK_I;
				this.goDown = KeyEvent.VK_K;
				this.goLeft = KeyEvent.VK_J;
				this.goRight = KeyEvent.VK_L;
				this.fire = KeyEvent.VK_N;
				break;
		}
		
		Random r = new Random(); 
		switch(r.nextInt(4)+1)
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
		
		this.spawn();
	}
	
	public void spawn() {
		Random r = new Random();
		
		do {
			this.x_pos = r.nextInt(map.getWidth());
			this.y_pos = r.nextInt(map.getHeight());
		} while (!ch.isValidSpawnPosition(this.x_pos, this.y_pos, this.id));
		
		System.out.println("Player "+this.id+" spawned.");
		this.alive = true;
		this.w = new Weapon(WEAPON.PISTOL, this);
	}
	
	public void die () {
		this.x_pos = this.y_pos = -100;
		this.alive = false;
		this.deaths++;
		this.nextSpawnTime = System.currentTimeMillis() + (puRessurOn? 0:spawnDelayMs);
		
		if (ProjectileSeedManager.countSeedsOfPlayer(this.id) != 0)
			BloodyPlayground.s.stopSound("weapon_smg");
		
		ProjectileSeedManager.deleteSeedsOfPlayer(this.id);
	}
	
	public void fire () {
		
		if (System.currentTimeMillis() - this.lastShot > w.getFireDelayMs() / (puMarksmanOn? 1.5:1))
		{			
			if (w.fire(ch, this.isPuAmmoOn(), this.isPuPenetrateOn(), this.isPuMarksmanOn()))
			{
				this.lastShot = System.currentTimeMillis();
				System.out.println("Player "+id+" fired.");
				
				if (w.getClipState() == 0)
				{
					if (w.getWeaponType() == WEAPON.PISTOL)
						System.out.println("Player "+this.id+" started reloading.");
					else
					{
						this.pullPistol();
						System.out.println("Player "+this.id+" has only pistol again.");
					}
				}
				
			}
			else
			{
				BloodyPlayground.s.playSound("dry_fire");
				System.out.println("Player "+this.id+" could not fire(empty clip).");
			}
		}
	}
	
	public void update () {
		
		if (this.alive)
		{
			int x = this.x_pos;
			int y = this.y_pos;
		
			switch (this.direction)
			{
				case DOWN:
					y += current_speed*speedMultiplier;
					break;
				case LEFT:
					x -= current_speed*speedMultiplier;
					break;
				case RIGHT:
					x += current_speed*speedMultiplier;
					break;
				case UP:
					y -= current_speed*speedMultiplier;
					break;
			}
			
			if (!ch.isUnblockedPosition(x, y, this.id)) // conflict
			{
				return;
			}
			else
			{
				this.x_pos = x;
				this.y_pos = y;
			}
			
			if (w.getClipState() == 0)
				w.update();
		}
		else
		{
			if (nextSpawnTime < System.currentTimeMillis())
				this.spawn();
		}
		
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

	public void render (Graphics g) {
		
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

	@Override
	public void keyPressed(KeyEvent e) {
		
		DIRECTION original = this.direction;
		
		if (e.getKeyCode() == fire )
		{
			if (this.alive)
				this.fire();
		}
		
		if (e.getKeyCode() == goUp )
		{
			direction = DIRECTION.UP;
		}
		else if (e.getKeyCode() == goDown)
		{
			direction = DIRECTION.DOWN;
		}	
		else if (e.getKeyCode() == goLeft)
		{
			direction = DIRECTION.LEFT;
		}	
		else if (e.getKeyCode() == goRight)
		{
			direction = DIRECTION.RIGHT;
		}	
		else
		{
			return;
		}
		
		if (original != this.direction)
		{
			if (ProjectileSeedManager.countSeedsOfPlayer(this.id) != 0)
				BloodyPlayground.s.stopSound("weapon_smg");
			
			ProjectileSeedManager.deleteSeedsOfPlayer(this.id);
		}
		
		current_speed = max_speed;
	}

	@Override
	public void keyReleased (KeyEvent e) {
		
		switch (this.direction)
		{
			case DOWN:
				if (e.getKeyCode() == goDown) current_speed = 0;
				break;
			case LEFT:
				if (e.getKeyCode() == goLeft) current_speed = 0;
				break;
			case RIGHT:
				if (e.getKeyCode() == goRight) current_speed = 0;
				break;
			case UP:
				if (e.getKeyCode() == goUp) current_speed = 0;
				break;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	public int getX_pos() {
		return x_pos;
	}
	
	public static ArrayList<Projectile> getProjectiles () {
		return Player.projectiles;
	}
	
	
	public static CollisionHandling getCh() {
		return ch;
	}

	public void addKill() {
		kills++;
	}

	public int getY_pos() {
		return y_pos;
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
		return w;
	}
	
	public void setWeapon(Weapon w) {
		this.pistolBackup = (this.w.getWeaponType()==WEAPON.PISTOL)? this.w:new Weapon(WEAPON.PISTOL, this);
		this.w = w;
	}
	
	public void pullPistol () {
		this.w = this.pistolBackup;
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
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    if (this.kills > pl.getKills())
	    	return BEFORE;
	    else if (this.deaths > pl.getDeaths())
	    	return AFTER;
	    else
	    	return EQUAL;
	}
}
