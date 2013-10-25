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

package org.geomajas.plugin.wmsclient.client.render;

import org.geomajas.gwt2.client.gfx.HtmlContainer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;

/**
 * Factory that creates renderers for a fixed scale within a WMS layer.
 * 
 * @author Pieter De Graef
 */
public interface WmsTiledScaleRendererFactory {

	/**
	 * Create a new renderer instance that can render a certain scale level of a WMS layer.
	 * 
	 * @param container
	 *            The container wherein the renderer should attach its content.
	 * @param layer
	 *            The WMS layer we are trying to render.
	 * @param scale
	 *            The fixed scale that we are trying to get rendered.
	 * @return Returns a renderer for the WMS layer at the given scale.
	 */
	WmsTiledScaleRenderer create(HtmlContainer container, WmsLayer layer, double scale);
}