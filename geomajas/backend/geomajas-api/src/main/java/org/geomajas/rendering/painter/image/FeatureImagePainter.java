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

package org.geomajas.rendering.painter.image;

import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.painter.TilePaintContext;

import java.awt.Graphics2D;

/**
 * <p>
 * Painter interface that renders features onto a graphics object.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface FeatureImagePainter {

	/**
	 * Set the MapContext for this painter. Since the features have their coordinates in world space, this MapContext
	 * can help transform them to screen space.
	 *
	 * @param tileContext
	 */
	void setTileContext(TilePaintContext tileContext);

	/**
	 * Paint the feature on the graphics object.
	 *
	 * @param graphics
	 * @param feature
	 */
	void paint(Graphics2D graphics, RenderedFeature feature);
}
