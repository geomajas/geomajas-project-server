/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.jsapi.spatial.geometry;

import org.geomajas.annotation.FutureApi;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable implementation of a Bounding Box.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
@Export
@ExportPackage("org.geomajas.jsapi.spatial.geometry")
public class Bbox implements Exportable {

	/**
	 * The ordinate along the X-axis.
	 */
	private double x;

	/**
	 * The ordinate along the Y-axis.
	 */
	private double y;

	/**
	 * The bounding box' width.
	 */
	private double width;

	/**
	 * The bounding box' height.
	 */
	private double height;

	/**
	 * Default constructor setting the position to (0, 0), and a width and height of 1.
	 */
	public Bbox() {
		this(0, 0, 1, 1);
	}

	/**
	 * constructor that immediately applies values to all the fields.
	 * 
	 * @param x
	 *            The ordinate along the X-axis.
	 * @param y
	 *            The ordinate along the Y-axis.
	 * @param width
	 *            The bounding box' width.
	 * @param height
	 *            The bounding box' height.
	 */
	public Bbox(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Construct a bbox by copying another one.
	 * 
	 * @param bounds
	 *            Another bounding box instance.
	 */
	public Bbox(Bbox bounds) {
		x = bounds.getX();
		y = bounds.getY();
		width = bounds.getWidth();
		height = bounds.getHeight();
	}

	/**
	 * Return the origin (x, y) as a Coordinate.
	 */
	public Coordinate getOrigin() {
		return new Coordinate(x, y);
	}

	/**
	 * Get the center of the bounding box as a Coordinate.
	 */
	public Coordinate getCenterPoint() {
		double centerX = (width == 0 ? x : x + width / 2);
		double centerY = (height == 0 ? y : y + height / 2);
		return new Coordinate(centerX, centerY);
	}

	/**
	 * Moves center to the specified coordinate.
	 * 
	 * @param center
	 *            new center point
	 */
	public void setCenterPoint(Coordinate center) {
		this.x = center.getX() - 0.5 * this.width;
		this.y = center.getY() - 0.5 * this.height;
	}

	/**
	 * Get the end-point of the bounding box as a Coordinate.
	 */
	public Coordinate getEndPoint() {
		return new Coordinate(x + width, y + height);
	}

	/**
	 * Does this bounding box contain the given bounding box?
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return true if the other is completely surrounded by this one, false otherwise.
	 */
	public boolean contains(Bbox other) {
		if (other.getX() < this.getX()) {
			return false;
		}
		if (other.getY() < this.getY()) {
			return false;
		}
		if (other.getEndPoint().getX() > this.getEndPoint().getX()) {
			return false;
		}
		if (other.getEndPoint().getY() > this.getEndPoint().getY()) {
			return false;
		}
		return true;
	}

	/**
	 * Does this bounding box intersect the given bounding box?
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return true if the other intersects this one, false otherwise.
	 */
	public boolean intersects(Bbox other) {
		if (other.getX() > this.getEndPoint().getX()) {
			return false;
		}
		if (other.getY() > this.getEndPoint().getY()) {
			return false;
		}
		if (other.getEndPoint().getX() < this.getX()) {
			return false;
		}
		if (other.getEndPoint().getY() < this.getY()) {
			return false;
		}
		return true;
	}

	/**
	 * Computes the intersection of this bounding box with the specified bounding box.
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return bounding box of intersection or null if they do not intersect.
	 */
	public Bbox intersection(Bbox other) {
		if (!this.intersects(other)) {
			return null;
		} else {
			double minx = other.getX() > this.getX() ? other.getX() : this.getX();
			double maxx = other.getEndPoint().getX() < this.getEndPoint().getX() ? other.getEndPoint().getX() : this
					.getEndPoint().getX();
			double miny = other.getY() > this.getY() ? other.getY() : this.getY();
			double maxy = other.getEndPoint().getY() < this.getEndPoint().getY() ? other.getEndPoint().getY() : this
					.getEndPoint().getY();
			return new Bbox(minx, miny, (maxx - minx), (maxy - miny));
		}
	}

	/**
	 * Calculates the union of 2 bounding boxes.
	 * 
	 * @param other
	 *            The other bounding box. Can be a bounding box with width and height equal to 0.
	 */
	public Bbox union(Bbox other) {
		if (other.getWidth() == 0 && other.getHeight() == 0 && other.getX() == 0 && other.getY() == 0) {
			return new Bbox(this);
		}
		if (width == 0 && height == 0 && x == 0 && y == 0) {
			return new Bbox(other);
		}

		double minx = other.getX() < this.getX() ? other.getX() : this.getX();
		double maxx = other.getEndPoint().getX() > this.getEndPoint().getX() ? other.getEndPoint().getX() : this
				.getEndPoint().getX();
		double miny = other.getY() < this.getY() ? other.getY() : this.getY();
		double maxy = other.getEndPoint().getY() > this.getEndPoint().getY() ? other.getEndPoint().getY() : this
				.getEndPoint().getY();
		return new Bbox(minx, miny, (maxx - minx), (maxy - miny));
	}

	/**
	 * Return a new bounding box that has increased in size by adding a range to this bounding box.
	 * 
	 * @param range
	 *            Must be a positive number, otherwise null will be returned.
	 * @return
	 */
	public Bbox buffer(double range) {
		if (range > 0) {
			double r2 = range * 2;
			return new Bbox(x - range, y - range, width + r2, height + r2);
		}
		return null;
	}

	/**
	 * Return a new bounding box which has the same center position but has been scaled with the specified factor.
	 * 
	 * @param factor
	 *            The scale factor (must be > 0).
	 * @return
	 */
	public Bbox scale(double factor) {
		if (factor > 0) {
			double scaledWidth = width * factor;
			double scaledHeight = height * factor;
			Coordinate center = getCenterPoint();
			return new Bbox(center.getX() - scaledWidth / 2, center.getY() - scaledHeight / 2, scaledWidth,
					scaledHeight);
		} else {
			return new Bbox(this);
		}
	}

	/**
	 * Translates this bounds with displacement dx and dy.
	 * 
	 * @param dx
	 *            x displacement
	 * @param dy
	 *            y displacement
	 */
	public Bbox translate(double dx, double dy) {
		return new Bbox(this.x + dx, this.y + dy, this.width, this.height);
	}
	
	/**
	 * Returns whether or not this bounding box is empty. A bounding box is considered empty when either the width or
	 * the height is equal to zero.
	 */
	public boolean isEmpty() {
		return width == 0 || height == 0;
	}

	/**
	 * Return the height of the bounding box.
	 * 
	 * @return The bounding box height.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Return the width of the bounding box.
	 * 
	 * @return The bounding box width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Return the X-ordinate of the bounding box' origin.
	 * 
	 * @return Returns the X-ordinate.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Return the Y-ordinate of the bounding box' origin.
	 * 
	 * @return Returns the Y-ordinate.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Return the maximum X value of this bounding box. This is the X-ordinate of the origin plus the width.
	 * 
	 * @return Returns the maximum X value of this bounding box.
	 */
	public double getMaxX() {
		return getX() + getWidth();
	}

	/**
	 * Return the maximum Y value of this bounding box. This is the Y-ordinate of the origin plus the height.
	 * 
	 * @return Returns the maximum Y value of this bounding box.
	 */
	public double getMaxY() {
		return getY() + getHeight();		
	}

	public String toString() {
		return "Bbox[" + x + " " + y + ", " + width + " " + height + "]";
	}
	
}