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

package org.geomajas.internal.configuration;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

/**
 * Dummy raster layer for testing
 *
 * @author Joachim Van der Auwera
 */
public class DummyRasterLayer implements RasterLayer {

	String id;
	RasterLayerInfo layerInfo;

	@Override
	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws GeomajasException {
		return null;
	}

	/**
	 * Set raster layer descriptor.
	 *
	 * @param info raster layer descriptor
	 */
	public void setLayerInfo(RasterLayerInfo info) {
		layerInfo = info;
	}

	@Override
	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	@Override
	public CoordinateReferenceSystem getCrs() {
		return null;
	}

	/**
	 * Set layer id.
	 *
	 * @param id layer id
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}
}
