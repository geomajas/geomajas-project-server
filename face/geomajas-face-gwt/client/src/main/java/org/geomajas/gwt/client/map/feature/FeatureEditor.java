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

package org.geomajas.gwt.client.map.feature;

import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.EditingEvent;
import org.geomajas.gwt.client.map.event.EditingEvent.EditingEventType;
import org.geomajas.gwt.client.map.event.EditingHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.layer.LayerType;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * ???
 * 
 * TODO: redesign!
 * 
 * @author Pieter De Graef
 */
public class FeatureEditor {

	private MapModel mapModel;

	private GeometryFactory factory;

	private FeatureTransaction featureTransaction;

	private HandlerManager handlerManager;

	public FeatureEditor(MapModel mapModel) {
		this.mapModel = mapModel;
		factory = new GeometryFactory(mapModel.getSrid(), mapModel.getPrecision());
		handlerManager = new HandlerManager(this);
	}

	public final HandlerRegistration addEditingHandler(final EditingHandler handler) {
		return handlerManager.addHandler(EditingHandler.TYPE, handler);
	}

	public FeatureTransaction startEditing(Feature[] oldFeatures, Feature[] targetFeatures) {
		Feature[] newFeatures = targetFeatures;
		if (featureTransaction == null) {
			if (oldFeatures == null || oldFeatures.length == 0) { // NEW FEATURE
				Layer<?> layer = mapModel.getSelectedLayer();
				if (layer == null || !(layer instanceof VectorLayer)) {
					return null;
				}
				VectorLayer vectorLayer = (VectorLayer) layer;
				Feature feature = vectorLayer.getFeatureStore().newFeature();
				if (vectorLayer.getLayerInfo().getLayerType() == LayerType.POINT) {
					feature.setGeometry(factory.createPoint(null));
				} else if (vectorLayer.getLayerInfo().getLayerType() == LayerType.MULTIPOINT) {
					feature.setGeometry(factory.createMultiPoint(null));
				} else if (vectorLayer.getLayerInfo().getLayerType() == LayerType.LINESTRING) {
					feature.setGeometry(factory.createLineString(null));
				} else if (vectorLayer.getLayerInfo().getLayerType() == LayerType.MULTILINESTRING) {
					feature.setGeometry(factory.createMultiLineString(null));
				} else if (vectorLayer.getLayerInfo().getLayerType() == LayerType.POLYGON) {
					feature.setGeometry(factory.createPolygon(null, null));
				} else if (vectorLayer.getLayerInfo().getLayerType() == LayerType.MULTIPOLYGON) {
					feature.setGeometry(factory.createMultiPolygon(null));
				}
				newFeatures = new Feature[] { feature };
				featureTransaction = new FeatureTransaction(vectorLayer, null, newFeatures);
			} else if (newFeatures == null || newFeatures.length == 0) {
				// DELETE EXISTING
				featureTransaction = new FeatureTransaction(oldFeatures[0].getLayer(), oldFeatures, null);
			} else {
				// EDIT EXISTING
				featureTransaction = new FeatureTransaction(oldFeatures[0].getLayer(), oldFeatures, newFeatures);
			}
			handlerManager.fireEvent(new EditingEvent(EditingEventType.START_EDITING));
			return featureTransaction;
		}
		return featureTransaction;
	}

	public void stopEditing() {
		handlerManager.fireEvent(new EditingEvent(EditingEventType.STOP_EDITING));
		featureTransaction = null;
	}

	public FeatureTransaction getFeatureTransaction() {
		return featureTransaction;
	}
}
