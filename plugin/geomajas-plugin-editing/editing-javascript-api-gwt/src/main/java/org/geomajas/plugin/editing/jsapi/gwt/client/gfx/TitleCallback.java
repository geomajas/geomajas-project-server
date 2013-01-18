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
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * 
 * Closure that returns a string value with the title.
 * 
 * @since 1.0.0
 * @author Jan De Moerloose
 * 
 */
@Export
@ExportPackage("org.geomajas.plugin.editing.gfx")
@ExportClosure
@Api(allMethods = true)
public interface TitleCallback extends Exportable {

	/**
	 * Get the title of the informational panel.
	 * 
	 * @return the title
	 */
	String execute();
}