/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.internal.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.geometry.Bbox;
import org.geomajas.service.BboxService;
import org.springframework.stereotype.Component;

/**
 * Utility class which handles calculations on Bbox instances.
 *
 * @author Joachim Van der Auwera
 */
@Component()
public final class BboxServiceImpl implements BboxService {

	/**
	 * Convert a bbox to a JTS envelope.
	 *
	 * @param bbox Geomajas bbox
	 * @return JTS envelope
	 */
	public Envelope toEnvelope(Bbox bbox) {
		return new Envelope(bbox.getX(), bbox.getMaxX(), bbox.getY(), bbox.getMaxY());
	}

	public Bbox fromEnvelope(Envelope envelope) {
		return new Bbox(envelope.getMinX(), envelope.getMinY(), envelope.getWidth(), envelope.getHeight());
	}

	/**
	 * Calculate intersection of two bbox objects.
	 *
	 * @param base base bbox
	 * @param other other bbox
	 * @return intersection
	 */
	public Bbox intersection(Bbox base, Bbox other) {
		if (null == base || null == other) {
			return new Bbox();
		}
		double minX = base.getX() > other.getX() ? base.getX() : other.getX();
		double minY = base.getY() > other.getY() ? base.getY() : other.getY();
		double maxX = base.getMaxX() < other.getMaxX() ? base.getMaxX() : other.getMaxX();
		double maxY = base.getMaxY() < other.getMaxY() ? base.getMaxY() : other.getMaxY();
		return new Bbox(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * Test whether the bbox covers an area.
	 *
	 * @param bbox bbox to test
	 * @return false when width or height of the bbox is zero.
	 */
	public boolean isNull(Bbox bbox) {
		return 0 == bbox.getWidth() || 0 == bbox.getHeight();
	}

	/**
	 * Expands the base bbox to ensure it contains the other one.
	 *
	 * @param base base bbox which may be expanded
	 * @param other bbox which needs to be contained in base
	 */
	public void expandToInclude(Bbox base, Bbox other) {
		if (isNull(other)) {
			return;
		}
		if (isNull(base)) {
			base.setX(other.getX());
			base.setY(other.getY());
			base.setWidth(other.getWidth());
			base.setHeight(other.getHeight());
		} else {
			double minX = base.getX();
			double minY = base.getY();
			double maxX = base.getMaxX();
			double maxY = base.getMaxY();
			if (other.getX() < minX) {
				minX = other.getX();
			}
			if (other.getY() < minY) {
				minY = other.getY();
			}
			if (other.getMaxX() > maxX) {
				maxX = other.getMaxX();
			}
			if (other.getMaxY() > maxY) {
				maxY = other.getMaxY();
			}
			base.setX(minX);
			base.setY(minY);
			base.setWidth(maxX - minX);
			base.setHeight(maxY - minY);
		}
	}

	public boolean contains(Bbox bbox, Coordinate coordinate) {
		return bbox.getX() <= coordinate.x && bbox.getMaxX() >= coordinate.x &&
				bbox.getY() <= coordinate.y && bbox.getMaxY() >= coordinate.y;
	}

}
