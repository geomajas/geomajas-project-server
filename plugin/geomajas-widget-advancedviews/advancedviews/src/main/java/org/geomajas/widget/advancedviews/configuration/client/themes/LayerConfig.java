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
package org.geomajas.widget.advancedviews.configuration.client.themes;

import java.io.Serializable;

import org.geomajas.configuration.client.ClientLayerInfo;


/**
 * Configuration of a specific layer in map theming, this contains visibility and opacity of the layer.
 * @author Oliver May
 *
 */
public class LayerConfig implements Serializable {

	private static final long serialVersionUID = 100L;

	/**
	 * Is the layer visible.
	 */
	private boolean visible;
	
	/**
	 * What is the opacity level of this layer, value between 0 and 1.
	 */
	private double opacity;

	/**
	 * The layer to apply on.
	 */
	private ClientLayerInfo layer;
	
	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	
	/**
	 * @return the opacity, a value between 0 and 1.
	 */
	public double getOpacity() {
		return opacity;
	}

	
	/**
	 * @param opacity the opacity to set, a value between 0 and 1.
	 */
	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	/**
	 * @return the opacity, a value between 0 and 1.
	 * @deprecated use getOpacity()
	 */
	@Deprecated
	public double getTransparency() {
		return opacity;
	}

	
	/**
	 * @param opacity the opacity to set, a value between 0 and 1.
	 * @deprecated use setOpacity()
	 */
	@Deprecated
	public void setTransparency(double opacity) {
		this.opacity = opacity;
	}


	/**
	 * @param layer the layer to set
	 */
	public void setLayer(ClientLayerInfo layer) {
		this.layer = layer;
	}


	/**
	 * @return the layer
	 */
	public ClientLayerInfo getLayer() {
		return layer;
	}

	
	
}
