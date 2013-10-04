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

/**
 * Extension interface for layer types that wish to define their own renderer.
 * 
 * @author Pieter De Graef
 */
public interface HasLayerRenderer {

	/**
	 * Return the specific renderer for the layer.
	 * 
	 * @param container
	 *            The container wherein the renderer should attach its content.
	 * @return The layer specific renderer.
	 */
	LayerRenderer getRenderer(HtmlContainer container);
}