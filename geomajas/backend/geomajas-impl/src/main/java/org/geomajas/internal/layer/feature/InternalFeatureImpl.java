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

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.StyleInfo;
import org.geomajas.global.Json;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * Extension of the <code>InternalFeature</code> interface that is used only in the
 * {@link org.geomajas.internal.rendering.strategy.VectorRendering} rendering strategy. This feature type has support
 * for clipped geometries. This is sometimes necessary when features are huge, and the map is zoomed in. In SVG and VML
 * you would still be bothered by the useless extra points. So in some situations clipped geometries provide faster
 * SVG/VML rendering.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public class InternalFeatureImpl implements InternalFeature {

	/** The feature's unique identifier. It's format is as follows: "[layer ID].[local ID]". */
	private String id;

	/** The full mapping of attributes. */
	private Map<String, Object> attributes;

	/** The feature's geometry. */
	private Geometry geometry;

	/** The style definition for this particular feature. */
	private StyleInfo styleDefinition;

	/** The label-string for this feature. */
	private String label;

	/** Reference to the feature's vector layer. */
	private VectorLayer layer;

	/** Has this feature's geometry been clipped? */
	private boolean clipped;

	/** If the feature's geometry has been clipped, store it here. */
	private Geometry clippedGeometry;

	/**
	 * Is it allowed for the user in question to edit this feature?
	 */
	private boolean editable;

	/**
	 * Is it allowed for the user in question to delete this feature?
	 */
	private boolean deletable;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public InternalFeatureImpl() {
	}

	public InternalFeatureImpl(InternalFeature other) {
		attributes = new HashMap<String, Object>(other.getAttributes());
		geometry = (Geometry) other.getGeometry().clone();
		id = other.getId();
		layer = other.getLayer();
		styleDefinition = other.getStyleInfo();
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Create a clone.
	 */
	public InternalFeature clone() {
		InternalFeatureImpl f = new InternalFeatureImpl();
		f.setAttributes(new HashMap<String, Object>(attributes));
		if (null != geometry) {
			f.setGeometry((Geometry) geometry.clone());
		}
		f.setId(id);
		f.setLayer(layer);
		f.setStyleDefinition(styleDefinition);
		f.setClipped(clipped);
		f.setClippedGeometry(clippedGeometry);
		f.setEditable(isEditable());
		f.setDeletable(isDeletable());
		return f;
	}

	/**
	 * Retrieve the local feature ID. That is the ID without the layer ID attached to it. This ID will still be unique
	 * within the layer.
	 * 
	 * @return local id
	 */
	public String getLocalId() {
		if (id != null) {
			return id.substring(id.indexOf('.') + 1);
		}
		return null;
	}

	/**
	 * Get the feature's bounding box.
	 * 
	 * @return feature's bounding box
	 */
	public Envelope getBounds() {
		if (geometry != null) {
			return geometry.getEnvelopeInternal();
		}
		return null;
	}

	/**
	 * Is this a new feature or not? This is tested by the ID. If the ID is null, then the feature is new.
	 * 
	 * @return true when new
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
	// Specific getters and setters:
	// -------------------------------------------------------------------------

	public boolean isClipped() {
		return clipped;
	}

	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}

	@Json(serialize = false)
	public Geometry getClippedGeometry() {
		return clippedGeometry;
	}

	@Json(serialize = false)
	public void setClippedGeometry(Geometry clippedGeometry) {
		this.clippedGeometry = clippedGeometry;
	}

	// -------------------------------------------------------------------------
	// Delegated methods
	// -------------------------------------------------------------------------

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

	/**
	 * Is the logged in user allowed to edit this feature?
	 *
	 * @return true when edit/update is allowed for this feature
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Set whether the logged in user is allowed to edit/update this feature.
	 *
	 * @param editable true when edit/update is allowed for this feature
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Is the logged in user allowed to delete this feature?
	 *
	 * @return true when delete is allowed for this feature
	 */
	public boolean isDeletable() {
		return deletable;
	}

	/**
	 * Set whether the logged in user is allowed to delete this feature.
	 *
	 * @param deletable true when deleting this feature is allowed
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}
}