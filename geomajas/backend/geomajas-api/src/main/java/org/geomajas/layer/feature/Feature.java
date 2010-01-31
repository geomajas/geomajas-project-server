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

package org.geomajas.layer.feature;

import org.geomajas.geometry.Geometry;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO version of a {@link InternalFeature}. This object can be sent to the client.
 * 
 * @author Pieter De Graef
 */
public class Feature implements Serializable {

	private static final long serialVersionUID = 151L;

	/**
	 * The feature's unique identifier. It's format is as follows: "[layer ID].[local ID]".
	 */
	private String id;

	/**
	 * The full mapping of attributes.
	 */
	private Map<String, Attribute> attributes;

	/**
	 * The feature's geometry.
	 */
	private Geometry geometry;

	/**
	 * The label-string for this feature.
	 */
	private String label;

	/**
	 * Has this feature's geometry been clipped?
	 */
	private boolean clipped;

	/**
	 * Is it allowed for the user in question to edit this feature?
	 */
	private boolean editable;

	/**
	 * Is it allowed for the user in question to delete this feature?
	 */
	private boolean deletable;

	/**
	 * The feature's style ID.
	 */
	private int styleId;

	// Constructors:

	public Feature() {
	}

	public Feature(String id) {
		this.id = id;
	}

	// Class specific functions:

	public String toString() {
		return id + " - " + label;
	}

	// Getters and setters:

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isClipped() {
		return clipped;
	}

	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}

	public boolean isEditable() {
		return editable;
	}

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

	public int getStyleId() {
		return styleId;
	}

	public void setStyleId(int styleId) {
		this.styleId = styleId;
	}
}
