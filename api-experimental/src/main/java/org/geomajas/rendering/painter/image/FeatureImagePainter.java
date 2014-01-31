/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.rendering.painter.image;

import java.awt.Graphics2D;

import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.painter.TilePaintContext;

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
	void paint(Graphics2D graphics, InternalFeature feature);
}
