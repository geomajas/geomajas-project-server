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
package org.geomajas.jsapi;


/**
 * The GeomajasRegistry provides an entry point for GWT components that need to be exposed to plain JavaScript.
 * 
 * @author Oliver May
 *
 */
public interface GeomajasRegistry {
	/**
	 * Return the registry containing all (registered) instances of {@link org.geomajas.jsapi.map.Map}.
	 * 
	 * @return the map registry.
	 */
	MapRegistry getMapRegistry();
}
