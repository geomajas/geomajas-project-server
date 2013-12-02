/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.tile;

import java.io.Serializable;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * General definition for a tile that is used internally on the server-side. When a tile needs to be send to the client,
 * it must first be converted to a DTO tile. A tile is defined by creating a rectangular grid in map coordinates based
 * on the maximum extent of the layer. This requires a transformation of this maximum extent from layer coordinates to
 * map coordinates.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface InternalTile extends Serializable {

	/**
	 * Return the rendering method used.
	 *
	 * @return content type
	 */
	VectorTileContentType getContentType();

	/**
	 * Set the rendering method used.
	 *
	 * @param contentType content type
	 */
	void setContentType(VectorTileContentType contentType);

	/**
	 * Return the rendering of a tile's features. Depending on the rendering method used, the returned string will
	 * contain an entire rendering (SVG/VML) or a URL.
	 *
	 * @return rendered features as SVG/VML or URL
	 */
	String getFeatureContent();

	/**
	 * Set the rendered content of a tile's features. Depending on the rendering method used, a different content type
	 * will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 *
	 * @param featureContent
	 *            The new value for the actual rendered feature content of this tile.
	 * @since 1.8.0
	 */
	void setFeatureContent(String featureContent);

	/**
	 * Return the rendering of a tile's labels. Depending on the rendering method used, the returned string will contain
	 * an entire rendering (SVG/VML) or a URL.
	 *
	 * @return rendered labels as SVG/VML or URL
	 */
	String getLabelContent();

	/**
	 * Set the rendered content of a tile's labels. Depending on the rendering method used, a different content type
	 * will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 *
	 * @param labelContent
	 *            The new value for the actual rendered label content of this tile.
	 * @since 1.8.0
	 */
	void setLabelContent(String labelContent);

	/**
	 * Get bounding box for the tile (in map coordinates).
	 *
	 * @return bounding box
	 */
	Envelope getBounds();

	/**
	 * Set bounding box for the tile (in map coordinates).
	 *
	 * @param bounds bounding box
	 */
	void setBounds(Envelope bounds);

	/**
	 * Get features for this tile (contained {@link com.vividsolutions.jts.geom.Geometry} objects use map coordinates).
	 *
	 * @return list of features
	 */
	List<InternalFeature> getFeatures();

	/**
	 * Set list of features for this tile (contained {@link com.vividsolutions.jts.geom.Geometry} objects use map
	 * coordinates).
	 *
	 * @param features list of features
	 */
	void setFeatures(List<InternalFeature> features);

	/**
	 * Add a feature in the tile.
	 *
	 * @param feature feature to add
	 */
	void addFeature(InternalFeature feature);

	/**
	 * Return the tile's width (in map coordinates).
	 *
	 * @return tile width
	 */
	double getTileWidth();

	/**
	 * Set the tile's width (in map coordinates).
	 *
	 * @param tileWidth
	 *            The tile's width.
	 */
	void setTileWidth(double tileWidth);

	/**
	 * Return the tile's height (in map coordinates).
	 *
	 * @return tile height
	 */
	double getTileHeight();

	/**
	 * Set the tile's height (in map coordinates).
	 *
	 * @param tileHeight
	 *            The tile's height.
	 */
	void setTileHeight(double tileHeight);

	/**
	 * Return the tile's width, expressed in client side pixels.
	 *
	 * @return tile width in client side pixels
	 */
	int getScreenWidth();

	/**
	 * Set the tile's width, expressed in client side pixels.
	 *
	 * @param screenWidth
	 *            The new value.
	 */
	void setScreenWidth(int screenWidth);

	/**
	 * Return the tile's height, expressed in client side pixels.
	 *
	 * @return tile height in client side pixels
	 */
	int getScreenHeight();

	/**
	 * Set the tile's height, expressed in client side pixels.
	 *
	 * @param screenHeight
	 *            The new value.
	 */
	void setScreenHeight(int screenHeight);

	/**
	 * Add another details (code) for another tile which should be added to the list of dependent tiles.
	 * <p/>
	 * Tile levels indicate zooming. Level 0 contains the entire extent for the layer. Level 1 contains 4 tiles ((0,0)
	 * till (1,1)), level 2 contains 16 tiles ((0,0) till (3,3)),...
	 *
	 * @param level tile level for dependent tile
	 * @param x x ordinate for dependent tile
	 * @param y y ordinate for dependent tile
	 */
	void addCode(int level, int x, int y);

	/**
	 * All tiles that have at least one feature that geographically intersects with this tile are called dependent
	 * tiles. This list returns their codes.
	 *
	 * @return Return the list of tiles that are dependent on this one.
	 */
	List<TileCode> getCodes();

	/**
	 * All tiles that have at least one feature that geographically intersects with this tile are called dependent
	 * tiles.
	 *
	 * @param codes
	 *            Set the list of tiles that are dependent on this one.
	 */
	void setCodes(List<TileCode> codes);

	/**
	 * Returns whether or not at least one feature within this tile has been clipped. This may sometimes be necessary
	 * when a feature's bounds are larger then the space allowed on the client side.
	 *
	 * @return true when at least one feature has a clipped geometry
	 */
	boolean isClipped();

	/**
	 * Set whether or not at least one feature within this tile has been clipped. This may sometimes be necessary when a
	 * feature's bounds are larger then the space allowed on the client side.
	 *
	 * @param clipped
	 *            The new value.
	 */
	void setClipped(boolean clipped);

	/**
	 * Returns the unique code for this tile. Consider this it's unique identifier within a vector layer.
	 *
	 * @return tile code
	 */
	TileCode getCode();

	/**
	 * Set the unique code for this tile. Consider this it's unique identifier within a vector layer.
	 *
	 * @param code
	 *            The tile's code.
	 */
	void setCode(TileCode code);
}
