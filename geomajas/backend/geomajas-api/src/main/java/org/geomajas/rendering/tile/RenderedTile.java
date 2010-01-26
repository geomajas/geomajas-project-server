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
import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.RenderedFeature;

import java.util.List;

/**
 * ???
 * 
 * @author check subversion
 */
public interface RenderedTile {

	void init(VectorLayer layer, double scale);

	Bbox getBbox(Layer layer);

	String codeAsString();

	List<RenderedFeature> getFeatures();

	void setFeatures(List<RenderedFeature> features);

	void addFeature(RenderedFeature feature);

	double getTileWidth();

	void setTileWidth(double tileWidth);

	double getTileHeight();

	void setTileHeight(double tileHeight);

	int getScreenWidth();

	void setScreenWidth(int screenWidth);

	int getScreenHeight();

	void setScreenHeight(int screenHeight);

	void addCode(int level, int x, int y);

	List<TileCode> getCodes();

	void setCodes(List<TileCode> codes);

	boolean isClipped();

	void setClipped(boolean clipped);

	TileCode getCode();

	void setCode(TileCode code);
}
