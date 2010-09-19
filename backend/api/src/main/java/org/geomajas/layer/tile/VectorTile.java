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

package org.geomajas.layer.tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.Api;
import org.geomajas.layer.feature.Feature;

/**
 * <p>
 * DTO version of an {@link InternalTile}. This object can be sent to the client.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class VectorTile implements Serializable {

	private static final long serialVersionUID = 151L;

	/**
	 * <p>
	 * The rendering method used for a specific tile. Geomajas supports different rendering strategies with different
	 * ways of rendering a tile. This enumeration defines the content types that these rendering methods produce.
	 * </p>
	 * 
	 * @author Pieter De Graef
	 */
	public enum VectorTileContentType {
		/**
		 * Rendering method that contains an entire tile in a string format, such as SVG or VML. On the client side,
		 * this string will be plugged directly into the HTML DOM tree.
		 */
		STRING_CONTENT,

		/** Rendering method that contains an URL to an image that contains the actual rendering of a tile. */
		URL_CONTENT
	}

	private List<Feature> features = new ArrayList<Feature>();

	private List<TileCode> codes = new ArrayList<TileCode>();

	private TileCode code;

	private int screenWidth;

	private int screenHeight;

	private boolean clipped;

	private String featureContent;

	private String labelContent;

	private VectorTileContentType contentType;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a vector tile.
	 */
	public VectorTile() {
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Return the rendered content of a tile's features. Depending on the rendering method used, a different content
	 * type will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 *
	 * @return rendered features as SVG, VML or URL
	 */
	public String getFeatureContent() {
		return featureContent;
	}

	/**
	 * Set the rendered content of a tile's features. Depending on the rendering method used, a different content type
	 * will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 * 
	 * @param featureContent
	 *            The new value for the actual rendered feature content of this tile.
	 */
	public void setFeatureContent(String featureContent) {
		this.featureContent = featureContent;
	}

	/**
	 * Return the rendered content of a tile's labels. Depending on the rendering method used, a different content type
	 * will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 *
	 * @return rendered labels as SVG, VML or URL
	 */
	public String getLabelContent() {
		return labelContent;
	}

	/**
	 * Set the rendered content of a tile's labels. Depending on the rendering method used, a different content type
	 * will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 * 
	 * @param labelContent
	 *            The new value for the actual rendered label content of this tile.
	 */
	public void setLabelContent(String labelContent) {
		this.labelContent = labelContent;
	}

	/**
	 * Returns the type of content for the rendered features and labels that are stored within this tile.
	 *
	 * @return content type
	 */
	public VectorTileContentType getContentType() {
		return contentType;
	}

	/**
	 * Determine the type of content for the rendered features and labels that are stored within this tile.
	 * 
	 * @param contentType
	 *            The value for the content type.
	 */
	public void setContentType(VectorTileContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns the list of features that are stored in this tile.
	 *
	 * @return list of features in tile
	 */
	public List<Feature> getFeatures() {
		return features;
	}

	/**
	 * Set the list of features that are stored in this tile.
	 * 
	 * @param features
	 *            The full list of features stored and rendered in this tile.
	 */
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	/**
	 * All tiles that have at least one feature that geographically intersects with this tile are called dependent
	 * tiles. This list returns their codes.
	 * 
	 * @return Return the list of tiles that are dependent on this one.
	 */
	public List<TileCode> getCodes() {
		return codes;
	}

	/**
	 * All tiles that have at least one feature that geographically intersects with this tile are called dependent
	 * tiles.
	 * 
	 * @param codes
	 *            Set the list of tiles that are dependent on this one.
	 */
	public void setCodes(List<TileCode> codes) {
		this.codes = codes;
	}

	/**
	 * Returns the unique code for this tile. Consider this it's unique identifier within a vector layer.
	 *
	 * @return tile code
	 */
	public TileCode getCode() {
		return code;
	}

	/**
	 * Set the unique code for this tile. Consider this it's unique identifier within a vector layer.
	 * 
	 * @param code
	 *            The tile's code.
	 */
	public void setCode(TileCode code) {
		this.code = code;
	}

	/**
	 * Return the tile's width, expressed in client side pixels.
	 *
	 * @return tile width in pixels
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Set the tile's width, expressed in client side pixels.
	 * 
	 * @param screenWidth
	 *            The new value.
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * Return the tile's height, expressed in client side pixels.
	 *
	 * @return tile height in pixels
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Set the tile's height, expressed in client side pixels.
	 * 
	 * @param screenHeight
	 *            The new value.
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	/**
	 * Returns whether or not at least one feature within this tile has been clipped. This may sometimes be necessary
	 * when a feature's bounds are larger then the space allowed on the client side.
	 *
	 * @return true when at least one geometry is clipped
	 */
	public boolean isClipped() {
		return clipped;
	}

	/**
	 * Set whether or not at least one feature within this tile has been clipped. This may sometimes be necessary when a
	 * feature's bounds are larger then the space allowed on the client side.
	 * 
	 * @param clipped
	 *            The new value.
	 */
	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}
}
