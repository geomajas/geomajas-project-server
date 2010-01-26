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

package org.geomajas.rendering.tile;

/**
 * DTO version of a {@link RasterTile}. This object can be sent to the client.
 *
 * @author Pieter De Graef
 */
public class RasterTile extends Tile {

	private static final long serialVersionUID = 151L;

	private RasterImage featureImage;

	private RasterImage labelImage;

	// Constructors:

	public RasterTile() {
	}

	// Getters and setters:

	public RasterImage getFeatureImage() {
		return featureImage;
	}

	public void setFeatureImage(RasterImage featureImage) {
		this.featureImage = featureImage;
	}

	public RasterImage getLabelImage() {
		return labelImage;
	}

	public void setLabelImage(RasterImage labelImage) {
		this.labelImage = labelImage;
	}
}
