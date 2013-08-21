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

import org.geomajas.gwt.client.gfx.HtmlContainer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;

/**
 * Factory for creating WMS layer renderers.
 * 
 * @author Pieter De Graef
 */
public interface WmsLayerRendererFactory {

	/**
	 * Create a renderer for a WMS layer.
	 * 
	 * @param layer
	 *            The WMS layer instance to create a renderer for.
	 * @param htmlContainer
	 *            The container wherein the renderer should attach its content.
	 * @return Returns a scale based renderer for WMS layers.
	 */
	WmsLayerRenderer create(WmsLayer layer, HtmlContainer htmlContainer);
}