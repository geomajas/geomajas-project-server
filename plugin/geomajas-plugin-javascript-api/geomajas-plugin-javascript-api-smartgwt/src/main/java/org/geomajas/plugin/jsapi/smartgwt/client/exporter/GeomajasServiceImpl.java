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

import org.geomajas.annotation.Api;
import org.geomajas.jsapi.GeomajasService;
import org.geomajas.jsapi.map.Map;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.MapImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

/**
 * Implementation of the {@link org.geomajas.jsapi.GeomajasService} for the GWT face.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@ExportPackage("org.geomajas.jsapi")
public final class GeomajasServiceImpl implements Exportable, GeomajasService {

	private static final GeomajasServiceImpl INSTANCE = new GeomajasServiceImpl();

	private HashMap<String, HashMap<String, Map>> maps = new HashMap<String, HashMap<String, Map>>();

	private GeomajasServiceImpl() {
	}

	/**
	 * Get the singleton instance of the GeomajasRegistry.
	 * 
	 * @return the {@link GeomajasServiceImpl}.
	 * @since 1.0.0
	 */
	@Api
	@Export("$wnd.Geomajas")
	public static GeomajasService getInstance() {
		return INSTANCE;
	}

	@Export
	public Map createMap(String applicationId, String mapId, String elementId) {
		Map map = getMap(applicationId, mapId);
		if (map == null) {
			map = new MapImpl(applicationId, mapId);
			Element element = DOM.getElementById(elementId);
			if (element != null) {
				int width = element.getClientWidth();
				int height = element.getClientHeight();
				map.setSize(width, height);
				map.setHtmlElementId(elementId);
			}
			registerMap(mapId, applicationId, map);
		} else {
			((MapImpl) map).getMapWidget().redraw();
		}
		return map;
	}

	public void registerMap(String applicationId, String mapId, Map map) {
		HashMap<String, Map> mapmap = null;
		if (maps.containsKey(applicationId)) {
			mapmap = maps.get(applicationId);
			if (!mapmap.containsKey(mapId)) {
				mapmap.put(mapId, map);
			}
		} else {
			mapmap = new HashMap<String, Map>();
			mapmap.put(mapId, map);
			maps.put(applicationId, mapmap);
		}
	}

	@Export
	public Map getMap(String applicationId, String mapId) {
		HashMap<String, Map> application = maps.get(applicationId);
		if (null == application) {
			return null;
		}
		return application.get(mapId);
	}
}