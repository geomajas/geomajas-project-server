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

import java.io.Serializable;

/**
 * DTO version of a {@link RenderedFeature}. This object can be sent to the client.
 *
 * @author Pieter De Graef
 */
public class FeatureTransaction implements Serializable {

	private static final long serialVersionUID = 151L;

	private String layerId;

	private Feature[] oldFeatures;

	private Feature[] newFeatures;

	// Constructors:

	public FeatureTransaction() {
	}

	// Getters and setters:

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public Feature[] getOldFeatures() {
		return oldFeatures;
	}

	public void setOldFeatures(Feature[] oldFeatures) {
		this.oldFeatures = oldFeatures;
	}

	public Feature[] getNewFeatures() {
		return newFeatures;
	}

	public void setNewFeatures(Feature[] newFeatures) {
		this.newFeatures = newFeatures;
	}
}
