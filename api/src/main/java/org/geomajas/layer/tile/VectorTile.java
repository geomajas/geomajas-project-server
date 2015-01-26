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

package org.geomajas.layer.tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.global.Json;
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

	private static final long serialVersionUID = 190L;

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
	 * @deprecated features are no longer included in tile
	 */
	@Deprecated
	@Json(serialize = false)
	public List<Feature> getFeatures() {
		return new ArrayList<Feature>(); // for backward compatibility
	}

	/**
	 * Set the list of features that are stored in this tile.
	 * 
	 * @param features
	 *            The full list of features stored and rendered in this tile.
	 * @deprecated features are no longer included in tile
	 */
	@Deprecated
	public void setFeatures(List<Feature> features) {
		// nothing to do, just available for backwards compatibility
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

	/**
	 * String representation of object.
	 *
	 * @return string representation of object
	 * @since 1.8.0
	 */	
	@Override
	public String toString() {
		return "VectorTile{" +
				"codes=" + codes +
				", code=" + code +
				", screenWidth=" + screenWidth +
				", screenHeight=" + screenHeight +
				", clipped=" + clipped +
				", featureContent='" + featureContent + '\'' +
				", labelContent='" + labelContent + '\'' +
				", contentType=" + contentType +
				'}';
	}
}
