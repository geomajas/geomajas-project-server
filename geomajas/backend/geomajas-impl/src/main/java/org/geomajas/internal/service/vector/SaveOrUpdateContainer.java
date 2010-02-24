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

package org.geomajas.internal.service.vector;

import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.util.List;

/**
 * Request and response object for the "vectorLayer.saveOrUpdate" pipeline.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateContainer {

	private String layerId;
	private VectorLayer layer;
	private CoordinateReferenceSystem crs;
	private List<InternalFeature> oldFeatures;
	private List<InternalFeature> newFeatures;
	private MathTransform mapToLayer;

	public SaveOrUpdateContainer(String layerId, VectorLayer layer, CoordinateReferenceSystem crs,
			List<InternalFeature> oldFeatures, List<InternalFeature> newFeatures, MathTransform mapToLayer) {
		this.layerId = layerId;
		this.layer = layer;
		this.crs = crs;
		this.oldFeatures = oldFeatures;
		this.newFeatures = newFeatures;
		this.mapToLayer = mapToLayer;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public VectorLayer getLayer() {
		return layer;
	}

	public void setLayer(VectorLayer layer) {
		this.layer = layer;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public void setCrs(CoordinateReferenceSystem crs) {
		this.crs = crs;
	}

	public List<InternalFeature> getOldFeatures() {
		return oldFeatures;
	}

	public void setOldFeatures(List<InternalFeature> oldFeatures) {
		this.oldFeatures = oldFeatures;
	}

	public List<InternalFeature> getNewFeatures() {
		return newFeatures;
	}

	public void setNewFeatures(List<InternalFeature> newFeatures) {
		this.newFeatures = newFeatures;
	}

	public MathTransform getMapToLayer() {
		return mapToLayer;
	}

	public void setMapToLayer(MathTransform mapToLayer) {
		this.mapToLayer = mapToLayer;
	}
}
