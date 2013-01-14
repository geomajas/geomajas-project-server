/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.vector;

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.rendering.painter.tile.StringContentTilePainter;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.service.GeoService;
import org.geomajas.service.TextService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Set the string content in the tile.
 *
 * @author Joachim Van der Auwera
 */
public class GetTileStringContentStep implements PipelineStep<GetTileContainer> {

	private final Logger log = LoggerFactory.getLogger(GetTileStringContentStep.class);

	private String id;

	@Autowired
	private GeoService geoService;

	@Autowired
	private TextService textService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		InternalTile tile = response.getTile();
		if (null == tile.getFeatureContent()) {
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			TileMetadata metadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);

			tile.setContentType(VectorTile.VectorTileContentType.STRING_CONTENT);
			Coordinate panOrigin = new Coordinate(metadata.getPanOrigin().getX(), metadata.getPanOrigin().getY());
			TilePainter tilePainter = new StringContentTilePainter(layer, metadata.getStyleInfo(), metadata
					.getRenderer(), metadata.getScale(), panOrigin, geoService, textService);
			tilePainter.setPaintGeometries(metadata.isPaintGeometries());
			tilePainter.setPaintLabels(metadata.isPaintLabels());
			log.debug("Going to paint features {}", tile.getFeatures());

			tilePainter.paint(tile);
		}
	}
}
