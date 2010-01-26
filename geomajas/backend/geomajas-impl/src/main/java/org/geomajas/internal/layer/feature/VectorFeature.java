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

package org.geomajas.internal.layer.feature;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.global.Json;
import org.geomajas.service.BboxService;

/**
 * <p>
 * Extension of the <code>RenderedFeature</code> interface that is used only in
 * the {@link org.geomajas.internal.rendering.strategy.VectorRendering} rendering strategy. This feature type has
 * support for clipped geometries. This is sometimes necessary when features are huge,
 * and the map is zoomed in. In SVG and VML you would still be bothered by the
 * useless extra points. So in some situations clipped geometries provide faster
 * SVG/VML rendering.
 * </p>
 *
 * @author Pieter De Graef
 */
public class VectorFeature extends RenderedFeatureImpl {

	/**
	 * Has this feature's geometry been clipped?
	 */
	private boolean clipped;

	/**
	 * If the feature's geometry has been clipped, store it here.
	 */
	private Geometry clippedGeometry;

	public VectorFeature(BboxService bboxService) {
		super(bboxService);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public boolean isClipped() {
		return clipped;
	}

	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}

	@Json(serialize = false)
	public Geometry getClippedGeometry() {
		return clippedGeometry;
	}

	@Json(serialize = false)
	public void setClippedGeometry(Geometry clippedGeometry) {
		this.clippedGeometry = clippedGeometry;
	}
}