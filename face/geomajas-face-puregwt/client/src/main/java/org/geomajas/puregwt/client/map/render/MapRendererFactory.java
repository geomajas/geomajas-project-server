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
package org.geomajas.puregwt.client.map.render;

import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.LayersModel;
import org.geomajas.puregwt.client.map.ViewPort;

/**
 * GIN factory for {@link MapRenderer} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface MapRendererFactory {

	/**
	 * create a {@link MapRenderer} using the specified {@link LayersModel}, {@link ViewPort} and {@link HtmlContainer}.
	 * 
	 * @param layersModel
	 * @param viewPort
	 * @param htmlContainer
	 * @return a new {@link MapRenderer}
	 */
	MapRenderer create(LayersModel layersModel, ViewPort viewPort, HtmlContainer htmlContainer);
}
