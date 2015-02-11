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
package org.geomajas.plugin.rasterizing.layer.tile;

import java.io.Serializable;

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.CacheableObject;
import org.geomajas.internal.layer.tile.TileMetadataImpl;
import org.geomajas.layer.tile.TileMetadata;

/**
 * Adds tile origin and size to {@link TileMetadata} and makes it cacheable.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TmsTileMetadata extends TileMetadataImpl implements TileMetadata, CacheableObject, Serializable {

	private static final long serialVersionUID = 1160L;

	private Coordinate tileOrigin;

	private int tileWidth;

	private int tileHeight;

	public Coordinate getTileOrigin() {
		return tileOrigin;
	}

	public void setTileOrigin(Coordinate tileOrigin) {
		this.tileOrigin = tileOrigin;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public double getResolution() {
		return 1 / getScale();
	}

	public void setResolution(double resolution) {
		setScale(1 / resolution);
	}

	@Override
	public String getCacheId() {
		return hashCode() + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
		result = prime * result + ((getCrs() == null) ? 0 : getCrs().hashCode());
		result = prime * result + ((getFilter() == null) ? 0 : getFilter().hashCode());
		result = prime * result + ((getLayerId() == null) ? 0 : getLayerId().hashCode());
		result = prime * result + (isPaintGeometries() ? 1231 : 1237);
		result = prime * result + (isPaintLabels() ? 1231 : 1237);
		result = prime * result + ((getPanOrigin() == null) ? 0 : getPanOrigin().hashCode());
		result = prime * result + ((getRenderer() == null) ? 0 : getRenderer().hashCode());
		long temp;
		temp = Double.doubleToLongBits(getScale());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((getStyleInfo() == null) ? 0 : getStyleInfo().hashCode());
		result = prime * result + tileHeight;
		result = prime * result + ((tileOrigin == null) ? 0 : tileOrigin.hashCode());
		result = prime * result + tileWidth;
		return result;
	}

}
