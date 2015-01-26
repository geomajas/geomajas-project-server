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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.TileCodeComparator;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * Step for the {@link org.geomajas.layer.RasterLayerService}.
 * 
 * Sorts the tiles so they get rendered from the center out.
 * 
 * @author Oliver May
 * 
 */
public class SortTilesStep implements PipelineStep<List<RasterTile>> {

	private String id;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void execute(PipelineContext context, List<RasterTile> response) throws GeomajasException {
		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;

		for (RasterTile tile : response) {
			maxX = maxX < tile.getCode().getX() ? tile.getCode().getX() : maxX;
			maxY = maxY < tile.getCode().getY() ? tile.getCode().getY() : maxY;

			minX = minX > tile.getCode().getX() ? tile.getCode().getX() : minX;
			minY = minY > tile.getCode().getY() ? tile.getCode().getY() : minY;
		}

		int dX = (maxX + minX) / 2;
		int dY = (maxY + minY) / 2;
		final TileCodeComparator comp = new TileCodeComparator(dX, dY);
		
		Collections.sort(response, new Comparator<RasterTile>() {

			@Override
			public int compare(RasterTile o1, RasterTile o2) {
				return comp.compare(o1.getCode(), o2.getCode());
			}

		});
		
	}
	
}
