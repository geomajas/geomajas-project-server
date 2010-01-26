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

package org.geomajas.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.geometry.Bbox;

/**
 * Utility class which handles calculations on Bbox instances.
 *
 * @author Joachim Van der Auwera
 */
public interface BboxService {

	/**
	 * Convert a bbox to a JTS envelope.
	 *
	 * @param bbox Geomajas bbox
	 * @return JTS envelope
	 */
	Envelope toEnvelope(Bbox bbox);

	/**
	 * Convert JTS envelope into a bbox.
	 *
	 * @param envelope JTS envelope
	 * @return Geomajas bbox
	 */
	Bbox fromEnvelope(Envelope envelope);

	/**
	 * Calculate intersection of two bbox objects.
	 *
	 * @param base base bbox
	 * @param other other bbox
	 * @return intersection
	 */
	Bbox intersection(Bbox base, Bbox other);

	/**
	 * Test whether the bbox covers an area.
	 *
	 * @param bbox bbox to test
	 * @return false when width or height of the bbox is zero.
	 */
	boolean isNull(Bbox bbox);

	/**
	 * Expands the base bbox to ensure it contains the other one.
	 *
	 * @param base base bbox which may be expanded
	 * @param other bbox which needs to be contained in base
	 */
	void expandToInclude(Bbox base, Bbox other);

	/**
	 * True when the coordinate is contained in the bbox.
	 *
	 * @param bbox bounding box which indicates area
	 * @param coordinate coordinate to test
	 * @return true when the coordinate is inside the area covered by the bbox
	 */
	boolean contains(Bbox bbox, Coordinate coordinate);
}
