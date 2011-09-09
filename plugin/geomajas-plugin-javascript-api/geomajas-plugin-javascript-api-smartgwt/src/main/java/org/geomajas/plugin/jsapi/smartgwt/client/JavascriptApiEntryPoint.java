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
package org.geomajas.plugin.jsapi.smartgwt.client;

import org.geomajas.plugin.jsapi.smartgwt.client.exporter.GeomajasRegistryImpl;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.MapRegistryImpl;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.MapImpl;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.ViewPortImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * @author Oliver May
 *
 */
public class JavascriptApiEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		GWT.create(GeomajasRegistryImpl.class);
		GWT.create(MapRegistryImpl.class);
		GWT.create(MapImpl.class);
		GWT.create(ViewPortImpl.class);
	}

}
