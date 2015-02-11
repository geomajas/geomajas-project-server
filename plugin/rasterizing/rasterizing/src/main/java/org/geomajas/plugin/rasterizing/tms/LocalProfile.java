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

import org.geomajas.plugin.rasterizing.layer.tile.TmsTileMetadata;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * A custom profile based on maximum bounds for level 0 and factor 2 scaling. The minimum tile size defines the length
 * of the smallest side of the tile. The other side gets calculated from the ratio of the layer bounds.
 * 
 * @author Jan De Moerloose
 *
 */
public class LocalProfile implements TmsProfile {

	private Envelope bounds;

	private Coordinate origin;

	private int tileWidth;

	private int tileHeight;

	private double maxResolution;

	private double[] resolutions = new double[30];

	public LocalProfile(Envelope bounds, int minTileSize) {
		this.origin = new Coordinate(bounds.getMinX(), bounds.getMinY());
		this.bounds = bounds;
		tileWidth = minTileSize;
		tileHeight = minTileSize;
		double ratio = bounds.getWidth() / bounds.getHeight();
		if (ratio >= 1) {
			tileWidth = (int) Math.ceil(tileHeight * ratio);
			maxResolution = bounds.getHeight() / tileHeight;
		} else {
			tileHeight = (int) Math.ceil(tileWidth / ratio);
			maxResolution = bounds.getWidth() / tileWidth;
		}
		for (int i = 0; i < 30; i++) {
			resolutions[i] = getResolution(i);
		}
	}

	@Override
	public void prepareMetadata(TmsTileMetadata metadata) {
		int tileLevel = metadata.getCode().getTileLevel();
		metadata.setTileHeight(tileHeight);
		metadata.setTileWidth(tileWidth);
		metadata.setResolution(getResolution(tileLevel));
		metadata.setTileOrigin(new org.geomajas.geometry.Coordinate(origin.x, origin.y));
	}

	private double getResolution(int level) {
		double resolution = maxResolution / Math.pow(2, level);
		return resolution;
	}

	@Override
	public Envelope getBounds() {
		return bounds;
	}

	@Override
	public Coordinate getOrigin() {
		return origin;
	}

	@Override
	public int getTileWidth() {
		return tileWidth;
	}

	@Override
	public int getTileHeight() {
		return tileHeight;
	}

	@Override
	public double[] getResolutions() {
		return resolutions;
	}

	@Override
	public ProfileType getProfile() {
		return ProfileType.LOCAL;
	}

}
