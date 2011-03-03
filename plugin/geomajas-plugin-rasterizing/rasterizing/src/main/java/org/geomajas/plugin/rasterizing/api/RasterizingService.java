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

package org.geomajas.plugin.rasterizing.api;

import java.io.OutputStream;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;

/**
 * Service interface for doing the actual rasterizing.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface RasterizingService {

	/**
	 * Call to convert a vector tile into a raster image.
	 *
	 * @param stream stream to write the image to
	 * @param layer layer for which the image needs to be produced
	 * @param style style for the rendering
	 * @param metadata tile metadata
	 * @param tile tile itself (containing the features to draw)
	 * @throws GeomajasException problem during rendering
	 */
	void rasterize(OutputStream stream, VectorLayer layer, NamedStyleInfo style, TileMetadata metadata,
			InternalTile tile) throws GeomajasException;	

}
