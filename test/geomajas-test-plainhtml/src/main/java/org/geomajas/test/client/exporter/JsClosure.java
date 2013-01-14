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
package org.geomajas.test.client.exporter;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Used to pass a javascript closure as GWT method argument (see gwt-exporter docs).
 * 
 * @author Jan De Moerloose
 * 
 */
@Export
@ExportPackage("org.geomajas")
@ExportClosure
public interface JsClosure extends Exportable {

	/**
	 * This calls the closure function object.
	 */
	void execute();
}