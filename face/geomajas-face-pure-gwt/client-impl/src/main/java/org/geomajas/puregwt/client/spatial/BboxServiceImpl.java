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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;

import com.google.inject.Inject;

/**
 * Implementation of the {@link BboxService}.
 * 
 * @author Pieter De Graef
 */
public class BboxServiceImpl implements BboxService {

	@Inject
	public BboxServiceImpl() {
	}

	/** {@inheritDoc} */
	public boolean equals(Bbox one, Bbox two, double delta) {
		return equals(one.getX(), two.getX(), delta) && equals(one.getY(), two.getY(), delta)
				&& equals(one.getWidth(), two.getWidth(), delta) && equals(one.getHeight(), two.getHeight(), delta);
	}

	/** {@inheritDoc} */
	public Coordinate getOrigin(Bbox bbox) {
		return new Coordinate(bbox.getX(), bbox.getY());
	}

	/** {@inheritDoc} */
	public Coordinate getCenterPoint(Bbox bbox) {
		double centerX = (bbox.getWidth() == 0 ? bbox.getX() : bbox.getX() + bbox.getWidth() / 2);
		double centerY = (bbox.getHeight() == 0 ? bbox.getY() : bbox.getY() + bbox.getHeight() / 2);
		return new Coordinate(centerX, centerY);
	}

	/** {@inheritDoc} */
	public Bbox setCenterPoint(Bbox bbox, Coordinate center) {
		double x = center.getX() - 0.5 * bbox.getWidth();
		double y = center.getY() - 0.5 * bbox.getHeight();
		return new Bbox(x, y, bbox.getWidth(), bbox.getHeight());
	}

	/** {@inheritDoc} */
	public Coordinate getEndPoint(Bbox bbox) {
		return new Coordinate(bbox.getMaxX(), bbox.getMaxY());
	}

	/** {@inheritDoc} */
	public boolean contains(Bbox parent, Bbox child) {
		if (child.getX() < parent.getX()) {
			return false;
		}
		if (child.getY() < parent.getY()) {
			return false;
		}
		if (child.getMaxX() > parent.getMaxX()) {
			return false;
		}
		if (child.getMaxY() > parent.getMaxY()) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	public boolean intersects(Bbox one, Bbox two) {
		if (two.getX() > one.getMaxX()) {
			return false;
		}
		if (two.getY() > one.getMaxY()) {
			return false;
		}
		if (two.getMaxX() < one.getX()) {
			return false;
		}
		if (two.getMaxY() < one.getY()) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	public Bbox intersection(Bbox parent, Bbox child) {
		if (!intersects(parent, child)) {
			return null;
		} else {
			double minx = child.getX() > parent.getX() ? child.getX() : parent.getX();
			double maxx = child.getMaxX() < parent.getMaxX() ? child.getMaxX() : parent.getMaxX();
			double miny = child.getY() > parent.getY() ? child.getY() : parent.getY();
			double maxy = child.getMaxY() < parent.getMaxY() ? child.getMaxY() : parent.getMaxY();
			return new Bbox(minx, miny, (maxx - minx), (maxy - miny));
		}
	}

	/** {@inheritDoc} */
	public Bbox union(Bbox parent, Bbox child) {
		if (child.getWidth() == 0 && child.getHeight() == 0 && child.getX() == 0 && child.getY() == 0) {
			return new Bbox(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight());
		}
		if (parent.getWidth() == 0 && parent.getHeight() == 0 && parent.getX() == 0 && parent.getY() == 0) {
			return new Bbox(child.getX(), child.getY(), child.getWidth(), child.getHeight());
		}

		double minx = child.getX() < parent.getX() ? child.getX() : parent.getX();
		double maxx = child.getMaxX() > parent.getMaxX() ? child.getMaxX() : parent.getMaxX();
		double miny = child.getY() < parent.getY() ? child.getY() : parent.getY();
		double maxy = child.getMaxY() > parent.getMaxY() ? child.getMaxY() : parent.getMaxY();
		return new Bbox(minx, miny, (maxx - minx), (maxy - miny));
	}

	/** {@inheritDoc} */
	public Bbox buffer(Bbox bbox, double range) {
		if (range >= 0) {
			double r2 = range * 2;
			return new Bbox(bbox.getX() - range, bbox.getY() - range, bbox.getWidth() + r2, bbox.getHeight() + r2);
		}
		throw new IllegalArgumentException("Buffer range must always be positive.");
	}

	/** {@inheritDoc} */
	public Bbox scale(Bbox bbox, double factor) {
		if (factor > 0) {
			double scaledWidth = bbox.getWidth() * factor;
			double scaledHeight = bbox.getHeight() * factor;
			Coordinate center = getCenterPoint(bbox);
			return new Bbox(center.getX() - scaledWidth / 2, center.getY() - scaledHeight / 2, scaledWidth,
					scaledHeight);
		}
		throw new IllegalArgumentException("Scale factor must always be strictly positive.");
	}

	/** {@inheritDoc} */
	public Bbox translate(Bbox bbox, double deltaX, double deltaY) {
		return new Bbox(bbox.getX() + deltaX, bbox.getY() + deltaY, bbox.getWidth(), bbox.getHeight());
	}

	/** {@inheritDoc} */
	public boolean isEmpty(Bbox bbox) {
		return bbox.getWidth() == 0 || bbox.getHeight() == 0;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private boolean equals(double d1, double d2, double delta) {
		return Math.abs(d1 - d2) <= delta;
	}
}