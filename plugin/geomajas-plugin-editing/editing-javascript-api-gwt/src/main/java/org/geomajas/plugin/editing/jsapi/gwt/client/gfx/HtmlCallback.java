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
package org.geomajas.plugin.editing.jsapi.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * 
 * Closure that returns an HTML fragment with information about the geometry.
 * 
 * @since 1.0.0
 * @author Jan De Moerloose
 * 
 */
@Export
@ExportPackage("org.geomajas.plugin.editing.gfx")
@ExportClosure
@Api(allMethods = true)
public interface HtmlCallback extends Exportable {

	/**
	 * Get the HTML content of the informational panel.
	 * 
	 * @param geometry the edited geometry
	 * @param dragPoint the drag point (or null if not dragging/inserting)
	 * @param startA the start point of the first drag line (or null if not dragging/inserting)
	 * @param startB the start point of the second drag line (or null if not dragging/inserting)
	 * @return the HTML content
	 */
	String execute(Geometry geometry, Coordinate dragPoint, Coordinate startA, Coordinate startB);
}