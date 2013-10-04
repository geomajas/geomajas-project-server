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
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;

/**
 * GIN factory for {@link LayerRenderer} objects.
 * 
 * @author Jan De Moerloose
 */
public interface LayerScalesRendererFactory {

	/**
	 * create a {@link MapRenderer} using the specified {@link ViewPort}, {@link Layer} and {@link HtmlContainer}.
	 * 
	 * @param viewPort
	 * @param layer
	 * @param htmlContainer
	 * @return a new {@link MapRenderer}
	 */
	LayerRenderer create(ViewPort viewPort, Layer layer, HtmlContainer htmlContainer);
}