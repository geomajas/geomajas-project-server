/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import org.geomajas.gwt.client.spatial.geometry.Geometry;

/**
 * Callback handler for handling geometry completion in {@link AbstractFreeDrawingController}.
 * 
 * @author Jan De Moerloose
 */
public interface GeometryDrawHandler {

	/**
	 * Called when a geometry has been drawn.
	 * 
	 * @param geometry the new geometry
	 */
	void onDraw(Geometry geometry);

}
