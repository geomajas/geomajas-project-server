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

package org.geomajas.rendering.strategy;

import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.global.ExpectAlternatives;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.tile.InternalTile;
import org.geomajas.rendering.tile.TileMetadata;

/**
 * <p>
 * Interface for a strategy that determines how a VectorLayer should be rendered. In essence a VectorLayer may be
 * rendered as pure vector based HTML element such as SVG or VML, but it might also be desirable to render images
 * instead.
 * </p>
 * <p>
 * Since there is a limit to what browser can handle (and that limit is easily reached), often you don't want layers to
 * be displayed as full vector layers.
 * </p>
 * <p>
 * A rendering strategy will determine in what format the resulting tiles are rendered. Note that extra tiling
 * information, such as the list of available features in a tile, and the tile's positioning meta-data will not be
 * affected. It is only the rendering result that changes, whether this be SVG, VML, PNG, JPG, .... Your layer will
 * remain a VectorLayer!
 * </p>
 *
 * @author Pieter De Graef
 */
@ExpectAlternatives
public interface RenderingStrategy {

	/**
	 * Paint a tile! This is what this interface is all about.
	 *
	 * @param metadata The object that holds all the spatial and styling information for a tile.
	 * @param application The application in which this tile is to be rendered.
	 * @return The fully rendered tile! The different implementations of this <code>RenderedTile</code> will contain
	 *         different rendering formats.
	 * @throws RenderException rendering failed
	 */
	InternalTile paint(TileMetadata metadata, ApplicationInfo application) throws RenderException;
}
