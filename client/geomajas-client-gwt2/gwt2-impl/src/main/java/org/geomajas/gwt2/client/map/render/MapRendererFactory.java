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
package org.geomajas.gwt2.client.map.render;

import org.geomajas.gwt2.client.gfx.HtmlContainer;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.LayersModel;

/**
 * GIN factory for {@link MapRenderer} objects.
 * 
 * @author Jan De Moerloose
 */
public interface MapRendererFactory {

	/**
	 * create a {@link MapRenderer} using the specified {@link LayersModel}, {@link ViewPort} and {@link HtmlContainer}.
	 * 
	 * @param layersModel
	 * @param viewPort
	 * @param configuration
	 * @param htmlContainer
	 * @return a new {@link MapRenderer}
	 */
	MapRenderer create(LayersModel layersModel, ViewPort viewPort, MapConfiguration configuration,
			HtmlContainer htmlContainer);
}