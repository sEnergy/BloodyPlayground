package bpgame.weapons.projectiles;

import java.awt.Color;
import java.awt.Graphics;

import bpgame.eventhandling.CollisionHandling;
import bpgame.player.Player;

public class Projectile {
	
	private final int SIZE = 5;
	private static final int DEFAULT_SPEED = 25;
	
	private static CollisionHandling ch = null;
	
	private int x, y;
	private Player shooter;
	private Vector v;
	private boolean penetrating;
	
	public Projectile (Player pl, CollisionHandling ch, boolean penetrating) {
		
		Projectile.ch = ch;
		
		this.shooter = pl;		
		this.penetrating = penetrating;
		
		switch (pl.getDirection())
		{
			case DOWN:
				this.x = this.shooter.getX_pos();
				this.y = this.shooter.getY_pos()+this.shooter.getSize()/2+10;
				this.v = new Vector (0, DEFAULT_SPEED);
				break;
			case LEFT:
				this.x = this.shooter.getX_pos()-this.shooter.getSize()/2-10;
				this.y = this.shooter.getY_pos();
				this.v = new Vector (-DEFAULT_SPEED, 0);
				break;
			case RIGHT:
				this.x = this.shooter.getX_pos()+this.shooter.getSize()/2+10;
				this.y = this.shooter.getY_pos();
				this.v = new Vector (DEFAULT_SPEED, 0);
				break;
			case UP:
				this.x = this.shooter.getX_pos();
				this.y = this.shooter.getY_pos()-this.shooter.getSize()/2-10;
				this.v = new Vector (0, -DEFAULT_SPEED);
				break;
			default:
				this.x = this.shooter.getX_pos();
				this.y = this.shooter.getY_pos();
				break;
		}
	}
	
	public Projectile (Player pl, CollisionHandling ch, boolean penetrating, Vector v) {
		this(pl, ch, penetrating);
		this.v = v;
	}
	
	public boolean update () {
	
		this.x += v.getX();
		this.y += v.getY();
		
		if (this.y <= 0 || this.x <= 0 || this.y >= ch.getHeight() || this.x >= ch.getWidth())
			return false;
		else
			return true;
	}
	
	public void render (Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(this.x-this.SIZE/2, this.y-this.SIZE/2, this.SIZE, this.SIZE);
	}

	public int getX_pos() {
		return x;
	}

	public int getY_pos() {
		return y;
	}
	
	public Player getShooter () {
		return shooter;
	}
	
	public boolean isPenetrating () {
		return this.penetrating;
	}
	
	public void peneOff () {
		this.penetrating = false;
	}

	public static int getDEFAULT_SPEED() {
		return DEFAULT_SPEED;
	}
	
	
		
}
