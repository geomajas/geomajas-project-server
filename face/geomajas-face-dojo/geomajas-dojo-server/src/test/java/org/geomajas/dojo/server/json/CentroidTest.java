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
package org.geomajas.dojo.server.json;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Centroid test.
 *
 * @author Pieter De Graef
 */
public class CentroidTest {

	@Test
	public void testSinglePointString() {
		GeometryFactory factory = new GeometryFactory();
		LineString ls = factory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(0, 0)});
		com.vividsolutions.jts.geom.Point p = ls.getCentroid();
		assertTrue(Double.isNaN(p.getX()));
	}
}
 