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
package org.geomajas.plugin.rasterizing.tms;

import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.rasterizing.layer.tile.TmsTileMetadata;

import com.vividsolutions.jts.geom.Envelope;

/**
 * The global-geodetic profile as defined in http://wiki.osgeo.org/wiki/Tile_Map_Service_Specification.
 * 
 * @author Jan De Moerloose
 *
 */
public class GlobalGeodeticProfile implements TmsProfile {

	private static double[] resolutions = new double[32];

	static {
		for (int i = 0; i < 32; i++) {
			resolutions[i] = 0.703125 / Math.pow(2, i);
		}
	}

	@Override
	public void prepareMetadata(TmsTileMetadata metadata) {
		metadata.setResolution(resolutions[metadata.getCode().getTileLevel()]);
		metadata.setTileOrigin(new Coordinate(-180, -90));
		metadata.setTileWidth(256);
		metadata.setTileHeight(256);
	}

	@Override
	public Envelope getBounds() {
		return new Envelope(-180, 180, -90, 90);
	}

	@Override
	public com.vividsolutions.jts.geom.Coordinate getOrigin() {
		return new com.vividsolutions.jts.geom.Coordinate(-180, -90);
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
		return ProfileType.GLOBAL_GEODETIC;
	}

}
