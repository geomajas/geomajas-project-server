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

package org.geomajas.internal.rendering.strategy;

import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.strategy.RenderingStrategy;

/**
 * This class does exactly the same than ImageRendering. The only difference is
 * that this renderer will not return (Multi)Polygons to the client, as
 * complex geometries block the browser. Instead of the complex geometry, a
 * simple bounding box is returned.
 *
 * @author Oliver May
 * @author Pieter De Graef
 */
public class SimplifiedImageRendering implements RenderingStrategy {

	/* Rendering strategy to delegate to */
	private ImageRendering rendering = new ImageRendering();

	/**
	 *
	 * @param metadata
	 *            The command that holds all the spatial and styling
	 *            information.
	 * @param application
	 *            The application in which this tile is defined.
	 * @return Returns a completely rendered <code>RasterTile</code>.
	 */
	public InternalTile paint(TileMetadata metadata, ApplicationInfo application) throws RenderException {

		InternalTile paintedTile = rendering.paint(metadata, application);

		// Remove loads of geometries from features.
		for (InternalFeature feature : paintedTile.getFeatures()) {
			String geometryType = feature.getGeometry().getGeometryType();
			if ("MultiPolygon".equals(geometryType) || "Polygon".equals(geometryType)) {
				//feature.setGeometry(feature.getGeometry().getBbox());
				feature.setGeometry(null);
			}
		}
		return paintedTile;
	}

}