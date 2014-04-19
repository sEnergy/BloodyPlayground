package bpgame.map.objects;

import java.awt.Polygon;

import bpgame.program.ScaledPolygon;

public class Obstacle {
	
	private Polygon area;
	private boolean blockingProjectiles = true;

	public Obstacle (int[] xPoly, int[] yPoly, double gameMapScale, boolean blockingProjectiles) {
		this.blockingProjectiles = blockingProjectiles;
		this.area = ScaledPolygon.getScaledPolygon(xPoly, yPoly, gameMapScale);
	}
	
	public Polygon getArea() {
		return this.area;
	}

	public boolean isBlockingProjectiles() {
		return this.blockingProjectiles;
	}

}
