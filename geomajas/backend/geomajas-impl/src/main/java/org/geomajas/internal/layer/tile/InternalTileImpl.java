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
package org.geomajas.internal.layer.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.InternalTileRendering;
import org.geomajas.layer.tile.TileCode;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.service.DtoConverterService;

/**
 * RenderedTile implementation.
 * 
 * @author Pieter De Graef
 */
public class InternalTileImpl implements InternalTile {

	/**
	 * features in the tile
	 */
	private List<InternalFeature> features = new ArrayList<InternalFeature>();

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

	private InternalTileRendering tileRendering;

	private DtoConverterService converterService;

	// Constructors:

	public InternalTileImpl(TileCode code, VectorLayer layer, double scale, DtoConverterService converterService) {
		super();
		this.code = code;
		this.converterService = converterService;
		init(layer, scale);
	}

	public InternalTileImpl(int x, int y, int tileLevel, VectorLayer layer, double scale,
			DtoConverterService converterService) {
		this(new TileCode(tileLevel, x, y), layer, scale, converterService);
	}

	// General functions:

	public void init(VectorLayer layer, double scale) {
		Envelope max = converterService.toInternal(layer.getLayerInfo().getMaxExtent());
		double div = Math.pow(2, code.getTileLevel());
		tileWidth = Math.ceil(scale * (max.getMaxX() - max.getMinX()) / div) / scale;
		tileHeight = Math.ceil(scale * (max.getMaxY() - max.getMinY()) / div) / scale;
		screenWidth = (int) Math.ceil(scale * tileWidth);
		screenHeight = (int) Math.ceil(scale * tileHeight);
	}

	public Envelope getBbox(Layer<?> layer) {
		if (tileWidth == 0) {
			return null;
		}
		Envelope max = converterService.toInternal(layer.getLayerInfo().getMaxExtent());
		double cX = max.getMinX() + code.getX() * tileWidth;
		double cY = max.getMinY() + code.getY() * tileHeight;
		return new Envelope(cX, cX + tileWidth, cY, cY + tileHeight);
	}

	public String codeAsString() {
		return (code.getX() == -1 ? "super" : code.getTileLevel() + "-" + code.getX() + "-" + code.getY());
	}

	// Getters and setters:

	public List<InternalFeature> getFeatures() {
		Collections.sort(features);
		return features;
	}

	public void setFeatures(List<InternalFeature> features) {
		this.features = features;
	}

	public void addFeature(InternalFeature feature) {
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

	public InternalTileRendering getTileRendering() {
		return tileRendering;
	}

	public void setTileRendering(InternalTileRendering tileRendering) {
		this.tileRendering = tileRendering;
	}
}
