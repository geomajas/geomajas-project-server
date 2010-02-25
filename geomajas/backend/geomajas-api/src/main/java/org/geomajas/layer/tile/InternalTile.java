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

import java.util.List;

import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * General definition for a tile that is used internally on the server-side. When a tile needs to be send to the client,
 * it must first be converted to a DTO tile.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface InternalTile {

	/**
	 * Return the rendering method used.
	 *
	 * @return content type
	 */
	VectorTileContentType getContentType();

	/**
	 * Return the rendering of a tile's features. Depending on the rendering method used, the returned string will
	 * contain an entire rendering (SVG/VML) or a URL.
	 *
	 * @return rendered features as SVG/VML or URL
	 */
	String getFeatureContent();

	/**
	 * Return the rendering of a tile's labels. Depending on the rendering method used, the returned string will contain
	 * an entire rendering (SVG/VML) or a URL.
	 *
	 * @return rendered labels as SVG/VML or URL
	 */
	String getLabelContent();

	/**
	 * Initialise tile, set layer and scale.
	 *
	 * @param layer layer for tile
	 * @param scale scale
	 */
	void init(VectorLayer layer, double scale);

	/**
	 * Get bounding box for the tile.
	 *
	 * @return bounding box
	 */
	Envelope getBbox();

	/**
	 * Get features for this tile.
	 *
	 * @return list of features
	 */
	List<InternalFeature> getFeatures();

	/**
	 * Set list of features for this tile.
	 *
	 * @param features list of features
	 */
	void setFeatures(List<InternalFeature> features);

	void addFeature(InternalFeature feature);

	double getTileWidth();

	void setTileWidth(double tileWidth);

	double getTileHeight();

	void setTileHeight(double tileHeight);

	double getScreenWidth();

	void setScreenWidth(double screenWidth);

	double getScreenHeight();

	void setScreenHeight(double screenHeight);

	void addCode(int level, int x, int y);

	List<TileCode> getCodes();

	void setCodes(List<TileCode> codes);

	boolean isClipped();

	void setClipped(boolean clipped);

	TileCode getCode();

	void setCode(TileCode code);
}
