package bpgame.program;

import java.awt.Polygon;

public class ScaledPolygon {
	
	public static Polygon getScaledPolygon (int[] xPoints, int[] yPoints, double scale) {
		for(int i=0; i < xPoints.length;++i)
	    {
	    	xPoints[i] = (int) (xPoints[i]*scale);
	    	yPoints[i] = (int) (yPoints[i]*scale);
	    }

	    return new Polygon(xPoints, yPoints, xPoints.length);
	}
}
