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
package org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.layer;

import org.geomajas.gwt.client.map.layer.AbstractLayer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.jsapi.map.layer.Layer;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Exportable facade for {@link org.geomajas.gwt.client.map.layer.Layer} in javascript.
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
	 * TODO.
	 * 
	 * @param layer
	 * @since 1.0.0
	 */
	public LayerImpl(org.geomajas.gwt.client.map.layer.Layer<?> layer) {
		this.layer = layer;
	}

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

	public String getId() {
		return layer.getId();
	}

	public String getServerLayerId() {
		return layer.getServerLayerId();
	}

	public String getTitle() {
		return layer.getLabel();
	}

	public void setSelected(boolean selected) {
		layer.setLabeled(selected);
	}

	public boolean isSelected() {
		return layer.isSelected();
	}

	public void setMarkedAsVisible(boolean markedAsVisible) {
		layer.setVisible(markedAsVisible);
	}

	public boolean isMarkedAsVisible() {
		// TODO revisit
		return ((AbstractLayer<?>) layer).isVisible();
	}

	public boolean isShowing() {
		return layer.isShowing();
	}
}