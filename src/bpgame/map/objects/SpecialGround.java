package bpgame.map.objects;

import java.awt.Polygon;

import bpgame.program.ScaledPolygon;

public class SpecialGround {
	
	private final double speedMultiplier;
	
	private Polygon area = null;
	
	public SpecialGround (int[] xPoly, int[] yPoly, double gameMapScale, double speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
		this.area = ScaledPolygon.getScaledPolygon(xPoly, yPoly, gameMapScale);
	}

	public double getSpeedMultiplier() {
		return this.speedMultiplier;
	}

	public Polygon getArea() {
		return this.area;
	}

}
