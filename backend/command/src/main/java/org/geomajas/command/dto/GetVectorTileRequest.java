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

package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.LayerIdCommandRequest;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.CacheableObject;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.global.Json;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;

/**
 * Request object for {@link org.geomajas.command.render.GetVectorTileCommand}.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GetVectorTileRequest extends LayerIdCommandRequest implements TileMetadata, CacheableObject {

	private static final long serialVersionUID = 190L;
	private static final int HASH_FACTOR = 31;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 */
	public static final String COMMAND = "command.render.GetVectorTile";

	private static final double EQUALS_DELTA = 1e-12;

	private TileCode code;

	private double scale;

	private Coordinate panOrigin;

	private String filter;

	private String crs;

	private String renderer;

	private NamedStyleInfo styleInfo;

	private boolean paintGeometries = true;

	private boolean paintLabels;

	public TileCode getCode() {
		return code;
	}

	public void setCode(TileCode code) {
		this.code = code;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public Coordinate getPanOrigin() {
		return panOrigin;
	}

	public void setPanOrigin(Coordinate panOrigin) {
		this.panOrigin = panOrigin;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get the coordinate reference space which should be used for the returned geometries.
	 *
	 * @return crs
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference space which should be used for the returned geometries.
	 *
	 * @param crs crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}
	
	public NamedStyleInfo getStyleInfo() {
		return styleInfo;
	}

	
	public void setStyleInfo(NamedStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public boolean isPaintGeometries() {
		return paintGeometries;
	}

	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	public boolean isPaintLabels() {
		return paintLabels;
	}

	public void setPaintLabels(boolean paintLabels) {
		this.paintLabels = paintLabels;
	}

	/**
	 * Get which data should be included in the features. For possible values, see
	 * {@link org.geomajas.layer.VectorLayerService}.
	 *
	 * @return what to include
	 * @deprecated features are no longer included in response
	 */
	@Deprecated
	@Json(serialize = false)
	public int getFeatureIncludes() {
		return GeomajasConstant.FEATURE_INCLUDE_ALL; // for backward compatibility
	}

	/**
	 * Set the data to include in the features which are returned. For possible values, see
	 * {@link org.geomajas.layer.VectorLayerService}.
	 *
	 * @param featureIncludes what the include
	 * @deprecated features are no longer included in response
	 */
	@Deprecated
	public void setFeatureIncludes(int featureIncludes) {
		// nothing to do, just available for backwards compatibility
	}

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash
	 * codes (like MD5, SHA1, SHA2 etc) of (collections of) objects.
	 *
	 * @return cacheId
	 * @since 1.8.0
	 */
	public String getCacheId() {
		return "GetVectorTileRequest{" +
				"code=" + code +
				", layerId=" + getLayerId() +
				", crs=" + getCrs() +
				", scale=" + scale +
				", panOrigin=" + panOrigin +
				", filter='" + filter + '\'' +
				", crs='" + crs + '\'' +
				", renderer='" + renderer + '\'' +
				", styleInfo=" + styleInfo +
				", paintGeometries=" + paintGeometries +
				", paintLabels=" + paintLabels +
				'}';
	}

	@Override
	public String toString() {
		return getCacheId();
	}

	/**
	 * Are the two objects equal?
	 *
	 * @param o object to compare
	 * @return true when objects are equal
	 * @since 1.8.0
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (!(o instanceof GetVectorTileRequest)) { return false; }

		GetVectorTileRequest that = (GetVectorTileRequest) o;

		if (paintGeometries != that.paintGeometries) { return false; }
		if (paintLabels != that.paintLabels) { return false; }
		if (Math.abs(that.scale - scale) > EQUALS_DELTA) { return false; }
		if (code != null ? !code.equals(that.code) : that.code != null) { return false; }
		if (crs != null ? !crs.equals(that.crs) : that.crs != null) { return false; }
		if (filter != null ? !filter.equals(that.filter) : that.filter != null) { return false; }
		if (panOrigin != null ? !panOrigin.equals(that.panOrigin) : that.panOrigin != null) { return false; }
		if (renderer != null ? !renderer.equals(that.renderer) : that.renderer != null) { return false; }
		if (styleInfo != null ? !styleInfo.equals(that.styleInfo) : that.styleInfo != null) { return false; }

		return true;
	}

	/**
	 * Calculate object hash code.
	 *
	 * @return hash code
	 * @since 1.8.0
	 */
	@Override
	public int hashCode() {
		int result;
		long temp;
		result = code != null ? code.hashCode() : 0;
		temp = scale != +0.0d ? Double.valueOf(scale).hashCode() : 0L;
		result = HASH_FACTOR * result + (int) (temp ^ (temp >>> 32));
		result = HASH_FACTOR * result + (panOrigin != null ? panOrigin.hashCode() : 0);
		result = HASH_FACTOR * result + (filter != null ? filter.hashCode() : 0);
		result = HASH_FACTOR * result + (crs != null ? crs.hashCode() : 0);
		result = HASH_FACTOR * result + (renderer != null ? renderer.hashCode() : 0);
		result = HASH_FACTOR * result + (styleInfo != null ? styleInfo.hashCode() : 0);
		result = HASH_FACTOR * result + (paintGeometries ? 1 : 0);
		result = HASH_FACTOR * result + (paintLabels ? 1 : 0);
		return result;
	}
}
