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

import org.geomajas.geometry.Bbox;

import java.io.Serializable;

/**
 * A raster image represents all the metadata needed to put an image on a screen. The bounds in the metadata are
 * expressed in application coordinates, the indices are expressed in view coordinates (this means that the y-axis is
 * flipped)
 *
 * @author Jan De Moerloose
 */
public class RasterImage implements Serializable {

	private static final long serialVersionUID = 151L;

	/**
	 * URL of the image
	 */
	private String url;

	/**
	 * Bounds of the image
	 */
	private Bbox bounds;

	/**
	 * Unique id of the image (normally, id = <layer id>+"."+<tile level>+"."+<x-index>,<y-index> )
	 */
	private String id;

	/**
	 * Tile level (0 is 1 image for the maximum extent, 1 is (2x2) images for the maximum extent, etc...)
	 */
	private int level;

	/**
	 * x-index in tile coordinates (rightward, first tile is 0)
	 */
	private int xIndex;

	/**
	 * y-index in tile coordinates (upward, first tile is 0)
	 */
	private int yIndex;

	// Constructors:

	public RasterImage() {
	}

	/**
	 *
	 * @param url
	 *            url
	 * @param bounds
	 *            bounds
	 * @param id
	 *            id
	 */
	public RasterImage(String url, Bbox bounds, String id) {
		this.url = url;
		this.bounds = bounds;
		this.id = id;
	}

	public RasterImage(Bbox bounds, String id) {
		this(null, bounds, id);
	}

	// Getters and setters:

	public Bbox getBounds() {
		return bounds;
	}

	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXIndex() {
		return xIndex;
	}

	public void setXIndex(int index) {
		xIndex = index;
	}

	public int getYIndex() {
		return yIndex;
	}

	public void setYIndex(int index) {
		yIndex = index;
	}

	public String toString() {
		return "[z=" + level + ",x=" + xIndex + ",y=" + yIndex + ",bounds=" + bounds + ",url=" + url + "]";
	}
}
