package org.vaadin.gwtgraphics.client.shape.path;

/**
 * Helper class to calculate scaled coordinates.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ScaleHelper {

	double lastX = 0;

	double lastY = 0;

	double scaleX = 0;

	double scaleY = 0;

	int x = 0;

	int y = 0;

	public ScaleHelper(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public int getScaledX(boolean relativeCoords, double userX) {
		if (relativeCoords) {
			return roundToInt((lastX + userX) * scaleX) - x;
		} else {
			return roundToInt(userX * scaleX);
		}
	}

	public int getScaledY(boolean relativeCoords, double userY) {
		if (relativeCoords) {
			return roundToInt((lastY + userY) * scaleY) - y;
		} else {
			return roundToInt(userY * scaleY);
		}
	}

	public void moveTo(boolean relativeCoords, double userX, double userY) {
		if (relativeCoords) {
			lastX += userX;
			lastY += userY;

		} else {
			lastX = userX;
			lastY = userY;
		}
		x = roundToInt(lastX * scaleX);
		y = roundToInt(lastY * scaleY);
	}

	protected int roundToInt(double d) {
		return (int) Math.round(d);
	}

}
