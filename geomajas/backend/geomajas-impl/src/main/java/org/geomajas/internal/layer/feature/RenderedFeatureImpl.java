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

package org.geomajas.internal.layer.feature;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.configuration.StyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.Json;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.BboxService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * This is the main feature class used in communication from and to the client. Even though each
 * {@link org.geomajas.layer.LayerModel} may have it's own specific feature objects, this is the common
 * feature type that comes out of the {@link org.geomajas.rendering.painter.feature.FeaturePainter} classes. This
 * common feature definition is the definition used on the client side.
 * </p>
 *
 * @author Pieter De Graef
 */
@Component
@Scope("prototype")
public class RenderedFeatureImpl implements InternalFeature {

	private BboxService bboxService;

	/**
	 * The feature's unique identifier. It's format is as follows: "[layer ID].[local ID]".
	 */
	private String id;

	/**
	 * The full mapping of attributes.
	 */
	private Map<String, Object> attributes;

	/**
	 * The feature's geometry.
	 */
	private Geometry geometry;

	/**
	 * The style definition for this particular feature.
	 */
	private StyleInfo styleDefinition;

	/**
	 * The label-string for this feature.
	 */
	private String label;

	/**
	 * Reference to the feature's vector layer.
	 */
	private VectorLayer layer;

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	public RenderedFeatureImpl(BboxService bboxService) {
		this.bboxService = bboxService;
	}

	/**
	 * Create a clone.
	 */
	public Object clone() {
		InternalFeature f = new RenderedFeatureImpl(bboxService);
		f.setAttributes(attributes);
		f.setGeometry((Geometry) geometry.clone());
		f.setId(id);
		f.setLayer(layer);
		f.setStyleDefinition(styleDefinition);
		return f;
	}

	/**
	 * Retrieve the local feature ID. That is the ID without the layer ID attached to it. This ID will still be unique
	 * within the layer.
	 *
	 * @return
	 */
	public String getLocalId() {
		if (id != null) {
			return id.substring(id.lastIndexOf('.') + 1);
		}
		return null;
	}

	/**
	 * Get the feature's bounding box.
	 *
	 * @return
	 */
	public Bbox getBounds() {
		if (geometry != null) {
			return bboxService.fromEnvelope(geometry.getEnvelopeInternal());
		}
		return null;
	}

	/**
	 * Is this a new feature or not? This is tested by the ID. If the ID is null, then the feature is new.
	 *
	 * @return
	 */
	public boolean isNew() {
		return id == null;
	}

	/**
	 * This function compares style ID's between features. Features are usually sorted by style.
	 */
	public int compareTo(InternalFeature o) {
		if (styleDefinition.getId() > o.getStyleInfo().getId()) {
			return 1;
		}
		if (styleDefinition.getId() < o.getStyleInfo().getId()) {
			return -1;
		}
		return 0;
	}

	public String toString() {
		return getId();
	}

	public boolean equals(Object obj) {
		if (obj == null || id == null) {
			return false;
		} else {
			if (!(obj instanceof InternalFeature)) {
				return false;
			}
			InternalFeature other = (InternalFeature) obj;
			return id.equals(other.getId());
		}
	}

	public int hashCode() {
		String temp = id + geometry.toString();
		return temp.hashCode();
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Json(serialize = false)
	public StyleInfo getStyleInfo() {
		return styleDefinition;
	}

	@Json(serialize = false)
	public void setStyleDefinition(StyleInfo style) {
		this.styleDefinition = style;
	}

	@Json(serialize = false)
	public VectorLayer getLayer() {
		return layer;
	}

	@Json(serialize = false)
	public void setLayer(VectorLayer layer) {
		this.layer = layer;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
