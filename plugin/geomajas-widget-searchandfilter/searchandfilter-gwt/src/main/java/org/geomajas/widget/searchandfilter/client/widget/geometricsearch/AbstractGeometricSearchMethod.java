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
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * @author Kristof Heirwegh
 */
public abstract class AbstractGeometricSearchMethod implements GeometricSearchMethod {

	protected MapWidget mapWidget;
	protected GeometryUpdateHandler handler;

	public void initialize(MapWidget mapWidget, GeometryUpdateHandler handler) {
		if (mapWidget == null) {
			throw new IllegalArgumentException("Please provide a mapWidget.");
		}
			if (handler == null) {
			throw new IllegalArgumentException("Please provide a handler.");
		}
		this.mapWidget = mapWidget;
		this.handler = handler;
	}

	/**
	 * Conveniencemethod. Call this whenever geometry changed to notify the
	 * GeometricSearchPanel of the change.
	 */
	protected void updateGeometry(Geometry oldGeometry, Geometry newGeometry) {
		handler.geometryUpdate(oldGeometry, newGeometry);
	}
}
