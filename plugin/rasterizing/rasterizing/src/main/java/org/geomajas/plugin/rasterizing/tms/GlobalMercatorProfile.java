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
package org.geomajas.plugin.rasterizing.tms;

import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.rasterizing.layer.tile.TmsTileMetadata;

import com.vividsolutions.jts.geom.Envelope;

/**
 * The global-mercator profile. Differs from http://wiki.osgeo.org/wiki/Tile_Map_Service_Specification in name of CRS
 * (EPSG:3857 instead of equivalent but outdated OSGEO:41001) and upper tile level (1 tile of 256x256 instead of 4).
 * This is more logical and consistent with other TMS servers like geoserver.
 * 
 * @author Jan De Moerloose
 *
 */
public class GlobalMercatorProfile implements TmsProfile {

	private static final double WIDTH = 20037508.342789244;

	private static double[] resolutions = new double[32];

	static {
		for (int i = 0; i < 32; i++) {
			resolutions[i] = 156543.03392804062 / Math.pow(2, i);
		}
	}

	@Override
	public void prepareMetadata(TmsTileMetadata metadata) {
		metadata.setResolution(resolutions[metadata.getCode().getTileLevel()]);
		metadata.setTileOrigin(new Coordinate(-WIDTH, -WIDTH));
		metadata.setTileWidth(256);
		metadata.setTileHeight(256);
	}

	@Override
	public Envelope getBounds() {
		return new Envelope(-WIDTH, WIDTH, -WIDTH, WIDTH);
	}

	@Override
	public com.vividsolutions.jts.geom.Coordinate getOrigin() {
		return new com.vividsolutions.jts.geom.Coordinate(-WIDTH, -WIDTH);
	}

	@Override
	public int getTileWidth() {
		return 256;
	}

	@Override
	public int getTileHeight() {
		return 256;
	}

	@Override
	public double[] getResolutions() {
		return resolutions;
	}

	@Override
	public ProfileType getProfile() {
		return ProfileType.GLOBAL_MERCATOR;
	}

}
