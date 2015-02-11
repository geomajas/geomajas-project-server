/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.feature;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * Implementation of {@link InternalFeature}.
 * <p/>
 * This feature type has support for clipped geometries. This is sometimes necessary when features are huge, and the map
 * is zoomed in. In SVG and VML you would still be bothered by the useless extra points. So in some situations clipped
 * geometries provide faster SVG/VML rendering.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public class InternalFeatureImpl implements InternalFeature {

	/** The feature's unique identifier. It's format is as follows: "[layer ID].[local ID]". */
	private String id;

	/** The full mapping of attributes. */
	private Map<String, Attribute> attributes;

	/** The feature's geometry. */
	private Geometry geometry;

	/** The style definition for this particular feature. */
	private FeatureStyleInfo styleDefinition;

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

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Create a clone.
	 */
	public InternalFeature clone() {
		InternalFeatureImpl f = (InternalFeatureImpl) cloneWithoutGeometry();
		if (null != geometry) {
			f.setGeometry((Geometry) geometry.clone());
		}
		return f;
	}

	public InternalFeature cloneWithoutGeometry() {
		InternalFeatureImpl f = new InternalFeatureImpl();
		if (null != attributes) {
			f.setAttributes(new HashMap<String, Attribute>(attributes));
		}
		f.setId(id);
		f.setLayer(layer);
		f.setStyleDefinition(styleDefinition);
		f.setClipped(clipped);
		f.setClippedGeometry(clippedGeometry);
		f.setEditable(isEditable());
		f.setDeletable(isDeletable());
		f.setLabel(label);
		return f;
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
	 * This function compares style ID's between features. Features are usually sorted by style.
	 */
	public int compareTo(InternalFeature o) {
		if (null == o) {
			return -1; // avoid NPE, put null objects at the end
		}
		if (null != styleDefinition && null != o.getStyleInfo()) {
			if (styleDefinition.getIndex() > o.getStyleInfo().getIndex()) {
				return 1;
			}
			if (styleDefinition.getIndex() < o.getStyleInfo().getIndex()) {
				return -1;
			}
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
		// return hashcode from id, consistent with equals() behaviour
		if (null != id) {
			return id.hashCode();
		}
		return super.hashCode();
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

	public Geometry getClippedGeometry() {
		return clippedGeometry;
	}

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

	public Map<String, Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public FeatureStyleInfo getStyleInfo() {
		return styleDefinition;
	}

	public void setStyleDefinition(FeatureStyleInfo style) {
		this.styleDefinition = style;
	}

	public VectorLayer getLayer() {
		return layer;
	}

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
	 * @param editable
	 *            true when edit/update is allowed for this feature
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
	 * @param deletable
	 *            true when deleting this feature is allowed
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}
}