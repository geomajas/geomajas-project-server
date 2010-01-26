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
package org.geomajas.internal.application.tile;

import org.geomajas.geometry.Bbox;
import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.tile.RenderedTile;
import org.geomajas.rendering.tile.TileCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RenderedTile implementation.
 * 
 * @author check subversion
 */
public class RenderedTileJG implements RenderedTile {

	/**
	 * features in the tile
	 */
	private List<RenderedFeature> features = new ArrayList<RenderedFeature>();

	/**
	 * tile codes of dependent tiles
	 */
	private List<TileCode> codes = new ArrayList<TileCode>();

	/**
	 * The tile's unique code.
	 */
	private TileCode code;

	/**
	 * tile width in world coordinates
	 */
	private double tileWidth;

	/**
	 * tile height in world coordinates
	 */
	private double tileHeight;

	/**
	 * tile width in screen pixels
	 */
	private int screenWidth;

	/**
	 * tile height in screen pixels
	 */
	private int screenHeight;

	/**
	 * clipped
	 */
	private boolean clipped;

	// Constructors:

	public RenderedTileJG(TileCode code, VectorLayer layer, double scale) {
		super();
		this.code = code;
		init(layer, scale);
	}

	public RenderedTileJG(int x, int y, int tileLevel, VectorLayer layer, double scale) {
		this(new TileCode(tileLevel, x, y), layer, scale);
	}

	// General functions:

	public void init(VectorLayer layer, double scale) {
		Bbox max = layer.getLayerInfo().getMaxExtent(); // @todo used to be getMaxBbox
		double div = Math.pow(2, code.getTileLevel());
		tileWidth = Math.ceil(scale * (max.getMaxX() - max.getX()) / div) / scale;
		tileHeight = Math.ceil(scale * (max.getMaxY() - max.getY()) / div) / scale;
		screenWidth = (int) Math.ceil(scale * tileWidth);
		screenHeight = (int) Math.ceil(scale * tileHeight);
	}

	public Bbox getBbox(Layer layer) {
		if (tileWidth == 0) {
			return null;
		}
		Bbox max = layer.getLayerInfo().getMaxExtent(); // @todo used to be getMaxBbox
		double cX = max.getX() + code.getX() * tileWidth;
		double cY = max.getY() + code.getY() * tileHeight;
		return new Bbox(cX, cY, tileWidth, tileHeight);
	}

	public String codeAsString() {
		return (code.getX() == -1 ? "super" : code.getTileLevel() + "-" + code.getX() + "-" + code.getY());
	}

	// Getters and setters:

	public List<RenderedFeature> getFeatures() {
		Collections.sort(features);
		return features;
	}

	public void setFeatures(List<RenderedFeature> features) {
		this.features = features;
	}

	public void addFeature(RenderedFeature feature) {
		features.add(feature);
	}

	public double getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(double tileWidth) {
		this.tileWidth = tileWidth;
	}

	public double getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(double tileHeight) {
		this.tileHeight = tileHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public void addCode(int level, int x, int y) {
		TileCode c = new TileCode(level, x, y);
		if (!codes.contains(c)) {
			codes.add(c);
		}
	}

	public List<TileCode> getCodes() {
		return codes;
	}

	public void setCodes(List<TileCode> codes) {
		this.codes = codes;
	}

	public boolean isClipped() {
		return clipped;
	}

	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}

	public TileCode getCode() {
		return code;
	}

	public void setCode(TileCode code) {
		this.code = code;
	}

}
