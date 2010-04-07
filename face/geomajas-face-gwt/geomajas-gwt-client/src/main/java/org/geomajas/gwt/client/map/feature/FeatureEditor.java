/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.map.feature;

import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.layer.LayerType;

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

	public FeatureEditor(MapModel mapModel) {
		this.mapModel = mapModel;
		factory = new GeometryFactory(mapModel.getSrid(), mapModel.getPrecision());
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
				newFeatures = new Feature[] {feature};
				featureTransaction = new FeatureTransaction(vectorLayer, null, newFeatures);
			} else if (newFeatures == null || newFeatures.length == 0) {
				// DELETE EXISTING
				featureTransaction = new FeatureTransaction(oldFeatures[0].getLayer(), oldFeatures, null);
			} else {
				// EDIT EXISTING
				featureTransaction = new FeatureTransaction(oldFeatures[0].getLayer(), oldFeatures, newFeatures);
			}
			return featureTransaction;
		}
		return featureTransaction;
	}

	public void stopEditing() {
		featureTransaction = null;
	}

	public FeatureTransaction getFeatureTransaction() {
		return featureTransaction;
	}
}
