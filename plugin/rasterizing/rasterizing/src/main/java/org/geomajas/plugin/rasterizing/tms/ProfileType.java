/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.tms;

/**
 * Profile type as defined in http://wiki.osgeo.org/wiki/Tile_Map_Service_Specification.
 * 
 * @author Jan De Moerloose
 *
 */
public enum ProfileType {

	/**
	 * Web mercator or google (1 tile at level 0).
	 */
	GLOBAL_MERCATOR("global-mercator", "EPSG:3857"),

	/**
	 * LatLon (2 tiles at level 0).
	 */
	GLOBAL_GEODETIC("global-geodetic", "EPSG:4326"),

	/**
	 * Custom (layer's crs).
	 */
	LOCAL("local", null);

	private String crs;

	private String name;

	private ProfileType(String name, String crs) {
		this.name = name;
		this.crs = crs;
	}

	public String getCrs() {
		return crs;
	}

	public String getName() {
		return name;
	}

}