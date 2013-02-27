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
package org.geomajas.puregwt.client.map.render;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * GIN factory for {@link MapScalesRenderer} objects.
 * 
 * @author Jan De Moerloose
 */
@Api(allMethods = true)
public interface MapScalesRendererFactory {

	/**
	 * create a {@link MapRenderer} using the specified {@link ViewPort}, {@link Layer} and {@link HtmlContainer}.
	 * 
	 * @param viewPort
	 * @param layer
	 * @param htmlContainer
	 * @return a new {@link MapRenderer}
	 */
	MapScalesRenderer create(ViewPort viewPort, Layer layer, HtmlContainer htmlContainer);
}