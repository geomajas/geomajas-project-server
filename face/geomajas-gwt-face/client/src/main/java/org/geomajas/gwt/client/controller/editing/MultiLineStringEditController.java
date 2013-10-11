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

package org.geomajas.gwt.client.controller.editing;

import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Editing controller for MultiLineString geometries. Supports MultiLineStrings with only one LineString at the moment.
 * 
 * @author Pieter De Graef
 */
public class MultiLineStringEditController extends LineStringEditController {

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public MultiLineStringEditController(MapWidget mapWidget, EditController parent) {
		super(mapWidget, parent);
	}

	// -------------------------------------------------------------------------
	// EditController implementation:
	// -------------------------------------------------------------------------

	public TransactionGeomIndex getGeometryIndex() {
		if (index == null) {
			index = new TransactionGeomIndex();
			index.setGeometryIndex(0);
			index.setCoordinateIndex(0);
			index.setFeatureIndex(0);
		}
		return index;
	}
}
