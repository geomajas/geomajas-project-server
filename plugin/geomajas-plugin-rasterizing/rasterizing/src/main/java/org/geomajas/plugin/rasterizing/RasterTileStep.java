/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.rasterizing;

import java.io.ByteArrayOutputStream;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.api.RasterizingService;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Pipeline step which rasterizes vector tiles.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class RasterTileStep implements PipelineStep<GetTileContainer> {

	private final Logger log = LoggerFactory.getLogger(RasterTileStep.class);

	@Autowired
	private RasterizingService rasterizingService;

	@Autowired
	private TestRecorder recorder;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer tileContainer) throws GeomajasException {
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		TileMetadata tileMetadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
		// put the image in a raster container
		RasterizingContainer rasterizingContainer = new RasterizingContainer();
		NamedStyleInfo style = tileMetadata.getStyleInfo();
		if (style == null) {
			// no style specified, take the first
			style = layer.getLayerInfo().getNamedStyleInfos().get(0);
		} else if (style.getFeatureStyles().isEmpty()) {
			// only name specified, find it
			style = layer.getLayerInfo().getNamedStyleInfo(style.getName());
		}
		ByteArrayOutputStream imageStream = new ByteArrayOutputStream(50000);
		try {
			rasterizingService.rasterize(imageStream, layer, style, tileMetadata, tileContainer.getTile());
			recorder.record(CacheCategory.RASTER, "Rasterization success");
		} catch (Exception ex) {
			recorder.record(CacheCategory.RASTER, "Rasterization failed");
			log.error("Problem while rasterizing tile, image will be zero-length.", ex);
		}

		byte[] image = imageStream.toByteArray();
		rasterizingContainer.setImage(image);
		context.put(RasterizingPipelineCode.CONTAINER_KEY, rasterizingContainer);
	}
}
