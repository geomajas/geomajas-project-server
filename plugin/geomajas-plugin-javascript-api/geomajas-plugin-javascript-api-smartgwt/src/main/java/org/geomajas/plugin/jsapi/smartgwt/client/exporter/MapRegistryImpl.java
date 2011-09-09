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

import java.util.HashMap;

import org.geomajas.annotation.FutureApi;
import org.geomajas.jsapi.MapRegistry;
import org.geomajas.jsapi.map.Map;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.MapImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;


/**
 * Registry singleton class that keeps track of all Map instances used in a page. This can be used to retrieve a 
 * MapWidget instance that was created in GWT code. 
 * 
 * @author Oliver May
 */
@Export
@ExportPackage("org.geomajas.jsapi")
public final class MapRegistryImpl implements Exportable, MapRegistry {

	private static final MapRegistryImpl INSTANCE = new MapRegistryImpl();
	
	private HashMap<String, HashMap<String, MapImpl>> maps = new HashMap<String, HashMap<String, MapImpl>>();
	
	private MapRegistryImpl(){};
	
	public void registerMap(String applicationId, String mapId, Map map) {
		MapImpl m = (MapImpl) map;
		HashMap<String, MapImpl> application = maps.get(applicationId);
		if (null == application) {
			application = new HashMap<String, MapImpl>();
			maps.put(applicationId, application);
		}
		application.put(mapId, m);
	}
	
	
	public MapImpl getMap(String applicationId, String mapId) {
		HashMap<String, MapImpl> application = maps.get(applicationId);
		if (null == application) {
			return null;
		}
		return application.get(mapId);
	}
	
	/**
	 * Get the singleton instance of the MapRegistry.
	 * @return the {@link MapRegistryImpl}.
	 * @since 1.0.0
	 */
	@FutureApi
	public static MapRegistryImpl getInstance() {
		return INSTANCE;
	}


}
