/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.client.snap;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;

/**
 * Snapping source provider that return static geometries. Meant only for test purposes.
 * 
 * @author Pieter De Graef
 */
public class StaticSnapSourceProvider implements SnapSourceProvider {

	private Geometry[] geometries;

	public StaticSnapSourceProvider() {
		Geometry polygon1 = new Geometry(Geometry.POLYGON, 0, 0);
		Geometry shell1 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell1.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(100, 0),
				new Coordinate(100, 100), new Coordinate(0, 100), new Coordinate(0, 0) });
		Geometry hole1 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole1.setCoordinates(new Coordinate[] { new Coordinate(40, 40), new Coordinate(60, 40), new Coordinate(60, 60),
				new Coordinate(40, 60), new Coordinate(40, 40) });
		polygon1.setGeometries(new Geometry[] { shell1, hole1 });

		Geometry polygon2 = new Geometry(Geometry.POLYGON, 0, 0);
		Geometry shell2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell2.setCoordinates(new Coordinate[] { new Coordinate(50, 20), new Coordinate(120, 20),
				new Coordinate(120, 80), new Coordinate(50, 80), new Coordinate(50, 20) });
		polygon2.setGeometries(new Geometry[] { shell2 });

		geometries = new Geometry[] { polygon1, polygon2 };
	}

	public StaticSnapSourceProvider(Geometry[] geometries) {
		this.geometries = geometries;
	}

	public void getSnappingSources(GeometryArrayFunction callback) {
		callback.execute(geometries);
	}

	public void update(Bbox mapBounds) {
		// No changes, we always return the same geometries. This is a test implementation after all.
	}
}