/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.raster;

import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

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
