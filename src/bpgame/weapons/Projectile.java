package bpgame.weapons;

import java.awt.Color;
import java.awt.Graphics;

import bpgame.eventhandling.CollisionHandling;
import bpgame.player.Player;
import bpgame.player.Player.DIRECTION;

public class Projectile {
	
	private static CollisionHandling ch = null;
	private Player shooter;
	
	private int x_pos, y_pos;
	private int size = 5;
	
	private int speed = 20;
	private DIRECTION direction;
	
	public Projectile (Player pl, CollisionHandling ch) {
		
		Projectile.ch = (Projectile.ch==null)? ch:Projectile.ch;
		
		this.shooter = pl;		
		this.direction = this.shooter.getDirection();
		
		switch (pl.getDirection())
		{
			case DOWN:
				this.x_pos = this.shooter.getX_pos();
				this.y_pos = this.shooter.getY_pos()+this.shooter.getSize()/2+10;
				break;
			case LEFT:
				this.x_pos = this.shooter.getX_pos()-this.shooter.getSize()/2-10;
				this.y_pos = this.shooter.getY_pos();
				break;
			case RIGHT:
				this.x_pos = this.shooter.getX_pos()+this.shooter.getSize()/2+10;
				this.y_pos = this.shooter.getY_pos();
				break;
			case UP:
				this.x_pos = this.shooter.getX_pos();
				this.y_pos = this.shooter.getY_pos()-this.shooter.getSize()/2-10;
				break;
			default:
				this.x_pos = this.shooter.getX_pos();
				this.y_pos = this.shooter.getY_pos();
				break;
		}
	}
	
	public boolean update () {
	
		switch (this.direction)
		{
			case DOWN:
				this.y_pos += speed;
				break;
			case LEFT:
				this.x_pos -= speed;
				break;
			case RIGHT:
				this.x_pos += speed;
				break;
			case UP:
				this.y_pos -= speed;
				break;
		}
		
		if (this.y_pos <= 0 || this.x_pos <= 0 || this.y_pos >= ch.getHeight() || this.x_pos >= ch.getWidth())
			return false;
		else
			return true;
	}
	
	public void render (Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(this.x_pos-this.size/2, this.y_pos-this.size/2, this.size, this.size);
	}

	public int getX_pos() {
		return x_pos;
	}

	public int getY_pos() {
		return y_pos;
	}
	
	public Player getShooter () {
		return shooter;
	}

}
