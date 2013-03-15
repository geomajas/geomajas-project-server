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
package org.geomajas.plugin.printing.client.template;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * Builder that prepares a specific type of layer for printing. Server layers should enhance their existing client info,
 * other layers should return a suitable {@link ClientLayerInfo} instance that can be picked up by a
 * {@link org.geomajas.plugin.rasterizing.api.LayerFactory} in the rasterizing plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface PrintableLayerBuilder {

	/**
	 * 
	 * @param mapPresenter
	 * @param layer
	 * @param worldBounds
	 * @param rasterScale
	 * @return
	 */
	ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double rasterScale);

	/**
	 * Is the specified layer supported ?
	 * @param layer
	 * @return
	 */
	boolean supports(Layer layer);
}
