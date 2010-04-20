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

package org.geomajas.internal.layer.raster;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

/**
 * Main step for the {@link org.geomajas.layer.RasterLayerService} getTiles method.
 * <p/>
 * Actually gets the data from the layer.
 *
 * @author Joachim Van der Auwera
 */
public class GetTilesGetStep implements PipelineStep<List<RasterTile>> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, List<RasterTile> response) throws GeomajasException {
		RasterLayer layer = context.get(PipelineCode.LAYER_KEY, RasterLayer.class);
		Envelope bounds = context.get(PipelineCode.BOUNDS_KEY, Envelope.class);
		double scale = context.get(PipelineCode.SCALE_KEY, Double.class);
		CoordinateReferenceSystem crs = context.get(PipelineCode.CRS_KEY, CoordinateReferenceSystem.class);
		List<RasterTile> images = layer.paint(crs, bounds, scale);
		response.addAll(images);
	}

}
