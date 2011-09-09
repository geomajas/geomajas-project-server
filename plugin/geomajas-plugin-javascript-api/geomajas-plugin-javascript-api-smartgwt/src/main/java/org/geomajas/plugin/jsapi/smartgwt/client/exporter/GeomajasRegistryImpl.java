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
package org.geomajas.plugin.jsapi.smartgwt.client.exporter;

import org.geomajas.jsapi.GeomajasRegistry;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;


/**
 * @author Oliver May
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi")
public class GeomajasRegistryImpl implements Exportable, GeomajasRegistry  {
	
	/**
	 * TODO.
	 * @return
	 * @since 1.0.0
	 */
//	@FutureApi
	public MapRegistryImpl getMapRegistry() {
		return MapRegistryImpl.getInstance();
	}
	
}
