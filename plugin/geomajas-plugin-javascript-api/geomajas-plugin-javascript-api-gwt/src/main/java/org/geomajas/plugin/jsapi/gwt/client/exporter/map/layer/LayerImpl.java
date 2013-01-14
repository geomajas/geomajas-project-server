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
package org.geomajas.plugin.jsapi.gwt.client.exporter.map.layer;

import org.geomajas.gwt.client.map.layer.AbstractLayer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.plugin.jsapi.client.map.layer.Layer;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Definition of a layer for the JavaScript API. Used for raster layers.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 */
@Export("LayerImpl")
@ExportPackage("org.geomajas.jsapi.map.layer")
public class LayerImpl implements Layer, Exportable {

	protected org.geomajas.gwt.client.map.layer.Layer<?> layer;

	public LayerImpl() {
	}

	/**
	 * Create a new layer.
	 * 
	 * @param layer
	 * @since 1.0.0
	 */
	public LayerImpl(org.geomajas.gwt.client.map.layer.Layer<?> layer) {
		this.layer = layer;
	}

	/** Completely clear all rendering of this layer and redraw. */
	public void refresh() {
		if (layer instanceof RasterLayer) {
			RasterLayer rLayer = (RasterLayer) layer;
			rLayer.setVisible(false);
			rLayer.getStore().clear();
			rLayer.setVisible(true);
		} else if (layer instanceof org.geomajas.gwt.client.map.layer.VectorLayer) {
			org.geomajas.gwt.client.map.layer.VectorLayer vl = (org.geomajas.gwt.client.map.layer.VectorLayer) layer;
			vl.setVisible(false);
			vl.clearSelectedFeatures();
			vl.getFeatureStore().clear();
			vl.setVisible(true);
		}
	}

	/**
	 * Return this layer's client ID.
	 * 
	 * @return id of the client layer
	 */
	public String getId() {
		return layer.getId();
	}

	/**
	 * Return this layer's server ID. Multiple client side layer (connected with a map) can point to a single data
	 * source on the back-end. This returns the actual layer name as used on the server.
	 * 
	 * @return id of the server layer
	 */
	public String getServerLayerId() {
		return layer.getServerLayerId();
	}

	/**
	 * Return the layer's title. The difference between the ID and the title, is that the ID is used behind the screens,
	 * while the title is the visible name to the user.
	 * 
	 * @return
	 */
	public String getTitle() {
		return layer.getLabel();
	}

	/**
	 * Mark this layer as selected or not. Only one layer can be selected at any given time within a map.
	 * 
	 * @param selected
	 *            The boolean value that indicates whether or not to select this layer.
	 */
	public void setSelected(boolean selected) {
		layer.setLabeled(selected);
	}

	/**
	 * Is this layer currently marked as selected or not?
	 * 
	 * @return Returns true or false.
	 */
	public boolean isSelected() {
		return layer.isSelected();
	}

	/**
	 * Mark this layer as visible or invisible. This may toggle the visibility flag, but does not guarantee that the
	 * layer be visible. This is because other factors might intrude upon the layer visibility, such as the minimum and
	 * maximum scales between which a layer can be visible.
	 * 
	 * @param markedAsVisible
	 *            Should the layer be marked as visible or invisible?
	 */
	public void setMarkedAsVisible(boolean markedAsVisible) {
		layer.setVisible(markedAsVisible);
	}

	/**
	 * Has the layer been marked as visible?
	 * 
	 * @return True or false.
	 */
	public boolean isMarkedAsVisible() {
		// TODO revisit
		return ((AbstractLayer<?>) layer).isVisible();
	}

	/**
	 * Is the layer currently showing on the map or not? In other words, can we actually look at the layer on the map? A
	 * layer may be marked as visible but other factors (such as scale) may intrude upon the actual visibility of a
	 * layer.<br/>
	 * This value will return the final result of all these factors and clearly state whether or not the layer can be
	 * seen.
	 * 
	 * @return true or false.
	 */
	public boolean isShowing() {
		return layer.isShowing();
	}
}