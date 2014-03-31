package bpgame.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import bpgame.RenderLayer;
import bpgame.eventhandling.CollisionHandling;
import bpgame.weapons.Projectile;
import bpgame.weapons.Weapon;
import bpgame.weapons.Weapon.WEAPON;

public class Player implements KeyListener {
	
	private static int playerNumber = 0;
	private static CollisionHandling ch = null;
	
	public static enum DIRECTION {
		UP, DOWN, LEFT, RIGHT
	}
	
	private int id;
	
	private int x_pos, y_pos;
	private int size = 50;
	
	private Color color;
	
	private int max_speed = 5;
	private int current_speed = 0;
	
	private DIRECTION direction;
	
	private boolean alive = false;
	private int spawnDelayMs = 1500;
	private long nextSpawnTime;
	
	private int deaths;
	private int kills;
	
	private int goUp;
	private int goDown;
	private int goLeft;
	private int goRight;
	private int fire;
	
	private long lastShot;

	private RenderLayer map;
	private Weapon w;
	private static ArrayList<Projectile> projectiles = null;
	
	public Player (RenderLayer map, ArrayList<Projectile> projectiles, CollisionHandling ch) {

		this.id = ++Player.playerNumber;
		this.lastShot = System.currentTimeMillis();
		
		this.map = map;
		Player.ch = (Player.ch==null)? ch:Player.ch;
		
		if (Player.projectiles == null) 
		{
			Player.projectiles = new ArrayList<Projectile>();
			Player.projectiles = projectiles;
		}
		
		switch(this.id)
		{
			case 1:
				this.color = Color.GREEN;
				goUp = KeyEvent.VK_NUMPAD8;
				goDown = KeyEvent.VK_NUMPAD5;
				goLeft = KeyEvent.VK_NUMPAD4;
				goRight = KeyEvent.VK_NUMPAD6;
				fire = KeyEvent.VK_NUMPAD1;
				break;
			case 2:
				this.color = Color.YELLOW;
				goUp = KeyEvent.VK_E;
				goDown = KeyEvent.VK_D;
				goLeft = KeyEvent.VK_S;
				goRight = KeyEvent.VK_F;
				fire = KeyEvent.VK_A;
				break;
			case 3:
				this.color = Color.RED;
				goUp = KeyEvent.VK_I;
				goDown = KeyEvent.VK_K;
				goLeft = KeyEvent.VK_J;
				goRight = KeyEvent.VK_L;
				fire = KeyEvent.VK_N;
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
		} while (!ch.isEntrablePosition(this.x_pos, this.y_pos, this.id));
		
		System.out.println("Player "+this.id+" spawned.");
		this.alive = true;
		this.w = new Weapon(WEAPON.PISTOL, this);
	}
	
	public void die () {
		this.x_pos = this.y_pos = -100;
		this.alive = false;
		this.deaths++;
		this.nextSpawnTime = System.currentTimeMillis() + spawnDelayMs;
	}
	
	public void fire () {
		
		if (System.currentTimeMillis() - this.lastShot > w.getFireDelayMs())
		{
			Projectile tmp = w.fire(ch);
			
			if (tmp !=null)
			{
				Player.projectiles.add(tmp);
				this.lastShot = System.currentTimeMillis();
				System.out.println("Player "+id+" fired.");
				
				if (w.getWeaponType() == WEAPON.PISTOL && w.getClipState() == 0)
					System.out.println("Player "+this.id+" started reloading.");
			}
			else
			{
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
					y += current_speed;
					break;
				case LEFT:
					x -= current_speed;
					break;
				case RIGHT:
					x += current_speed;
					break;
				case UP:
					y -= current_speed;
					break;
			}
			
			if (!ch.isEntrablePosition(x, y, this.id)) // conflict
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
		
	}

	public void render (Graphics g) {
		
		g.setColor(color);
		g.fillOval(this.x_pos-this.size/2, this.y_pos-this.size/2, this.size, this.size);
		
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
		
		g.setColor(Color.BLACK);
		g.drawLine(this.x_pos, this.y_pos, x, y);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == fire )
		{
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

}
