/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Configuration of a specific layer in map theming, this contains visibility and transparency of the layer.
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
	 * What is the transparency level of this layer [0-100].
	 */
	private double transparency;

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
	 * @return the transparency
	 */
	public double getTransparency() {
		return transparency;
	}

	
	/**
	 * @param transparency the transparency to set
	 */
	public void setTransparency(double transparency) {
		this.transparency = transparency;
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
