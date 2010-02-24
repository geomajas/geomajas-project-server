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

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.VectorLayer;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Request object for getFeatures in {@link org.geomajas.service.VectorLayerService}.
 *
 * @author Joachim Van der Auwera
 */
public class GetFeaturesRequest {
	private String layerId;
	private VectorLayer layer;
	private CoordinateReferenceSystem crs;
	private NamedStyleInfo style;
	private int featureIncludes;
	private int offset;
	private int maxResultSize;
	private MathTransform crsTransform;

	public GetFeaturesRequest(String layerId, VectorLayer layer, CoordinateReferenceSystem crs,
			NamedStyleInfo style, int featureIncludes, int offset, int maxResultSize, MathTransform crsTransform) {
		this.layerId = layerId;
		this.layer = layer;
		this.crs = crs;
		this.style = style;
		this.featureIncludes = featureIncludes;
		this.offset = offset;
		this.maxResultSize = maxResultSize;
		this.crsTransform = crsTransform;
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

	public NamedStyleInfo getStyle() {
		return style;
	}

	public void setStyle(NamedStyleInfo style) {
		this.style = style;
	}

	public int getFeatureIncludes() {
		return featureIncludes;
	}

	public void setFeatureIncludes(int featureIncludes) {
		this.featureIncludes = featureIncludes;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getMaxResultSize() {
		return maxResultSize;
	}

	public void setMaxResultSize(int maxResultSize) {
		this.maxResultSize = maxResultSize;
	}

	public MathTransform getCrsTransform() {
		return crsTransform;
	}

	public void setCrsTransform(MathTransform crsTransform) {
		this.crsTransform = crsTransform;
	}
}
