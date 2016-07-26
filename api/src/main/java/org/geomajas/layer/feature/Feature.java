/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.feature;

import java.io.Serializable;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;

/**
 * DTO version of a {@link InternalFeature}. This object can be sent to the client.
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class Feature implements Serializable {

	private static final long serialVersionUID = 151L;

	private String id;

	private String crs;

	private Map<String, Attribute> attributes;

	private Geometry geometry;

	private String label;

	private boolean clipped;

	private boolean updatable;

	private boolean deletable;

	private String styleId;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor - does nothing.
	 */
	public Feature() {
	}

	/**
	 * Initialize a feature with it's identifier.
	 * 
	 * @param id
	 *            The feature's unique identifier in the layer.
	 */
	public Feature(String id) {
		this.id = id;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Transform this feature to an easily readable string (id + " - " + label).
	 *
	 * @return human readable representation
	 */
	public String toString() {
		return id + " - " + label;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Get the feature's unique identifier.
	 *
	 * @return feature id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the feature's unique identifier.
	 *
	 * @param id feature id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the full mapping of attributes for this feature. The attribute names are those from the vector layer
	 * configuration, and the {@link Attribute} objects contain the actual values for this feature.
	 *
	 * @return map which contains the attribute values indexed on the name
	 */
	public Map<String, Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * Set the full mapping of attributes for this feature. The attribute names are those from the vector layer
	 * configuration, and the {@link Attribute} objects contain the actual values for this feature.
	 * 
	 * @param attributes
	 *            The name-value mapping of attributes for this feature.
	 */
	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Get this feature's geometric attribute. It is always considered separately from the alphanumerical mapping of
	 * attributes.
	 *
	 * @return geometry for this feature
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Set this feature's geometric attribute. It is always considered separately from the alphanumerical mapping of
	 * attributes.
	 * 
	 * @param geometry
	 *            The geometric object.
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Get the label-string for this feature.
	 *
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set a new label-string for this feature.
	 * 
	 * @param label
	 *            The new label value.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Has this feature's geometry been clipped? If so, it was probably because it was too large for the client to
	 * handle. In that case the client must make sure that it is never persisted again to the layer.
	 *
	 * @return true when feature geometry is clipped
	 */
	public boolean isClipped() {
		return clipped;
	}

	/**
	 * Set whether or not the feature's geometry has been clipped.
	 * 
	 * @param clipped
	 *            Is the geometry clipped?
	 */
	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}

	/**
	 * Is the logged in user allowed to edit this feature?
	 * 
	 * @return true when edit/update is allowed for this feature
	 */
	public boolean isUpdatable() {
		return updatable;
	}

	/**
	 * Set whether the logged in user is allowed to edit/update this feature.
	 * 
	 * @param editable
	 *            true when edit/update is allowed for this feature
	 */
	public void setUpdatable(boolean editable) {
		this.updatable = editable;
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

	/**
	 * Get the identifier for the styling that applies on this feature. Depending on the vector layer styling
	 * configuration, only 1 style can be valid for a feature. In the tile's it is that style that is used to render the
	 * feature. This can be handy for some client side rendering of this feature.
	 *
	 * @return style id
	 */
	public String getStyleId() {
		return styleId;
	}

	/**
	 * Set the identifier for the styling that applies on this feature. Depending on the vector layer styling
	 * configuration, only 1 style can be valid for a feature. In the tile's it is that style that is used to render the
	 * feature. This can be handy for some client side rendering of this feature.
	 * 
	 * @param styleId
	 *            The style configuration identifier from the vector layer's styling configuration.
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	/**
	 * Crs code which is used (or should be used) for the geometry in the feature.
	 * <p/>
	 * This may be null if the crs is unknown.
	 * 
	 * @return crs code
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the crs code which us used (or should be used) for the geometry in the feature.
	 * 
	 * @param crs
	 *            crs code for feature
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}
}
