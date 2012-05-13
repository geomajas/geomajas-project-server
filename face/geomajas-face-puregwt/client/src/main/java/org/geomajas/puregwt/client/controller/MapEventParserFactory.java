/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.puregwt.client.controller;

import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.puregwt.client.map.MapPresenter;

/**
 * GIN factory for {@link MapEventParser} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface MapEventParserFactory {

	/**
	 * creates a {@link MapEventParser} using the given {@link MapPresenter}.
	 * 
	 * @param mapPresenter the map presenter
	 * @return a new {@link MapEventParser}
	 */
	MapEventParser create(MapPresenter mapPresenter);
}
