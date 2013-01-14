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

package org.geomajas.layer.tile;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Coordinate;

/**
 * <p>
 * The collected meta-data that uniquely define a tile's rendering.
 * </p>
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface TileMetadata {

	/**
	 * Render the tile as svg vector data for display in non-IE browsers.
	 */
	String PARAM_SVG_RENDERER = "SVG";

	/**
	 * The the tile as vml vector data for display in IE browsers.
	 */
	String PARAM_VML_RENDERER = "VML";

	/**
	 * Get layer id.
	 *
	 * @return layer id
	 */
	String getLayerId();

	/**
	 * Set layer id.
	 *
	 * @param layerId layer id
	 */
	void setLayerId(String layerId);

	/**
	 * Get crs code.
	 *
	 * @return crs
	 */
	String getCrs();

	/**
	 * Set crs code.
	 *
	 * @param crs crs
	 */
	void setCrs(String crs);

	/**
	 * Get tile code, which is the tile id.
	 *
	 * @return tile code
	 */
	TileCode getCode();

	/**
	 * Set tile code.
	 *
	 * @param code tile code
	 */
	void setCode(TileCode code);

	/**
	 * Get scale for tile.
	 *
	 * @return scale in pix/map unit (at map center)
	 */
	double getScale();

	/**
	 * Set the scale (in pixels per map unit at the center of the map).
	 *
	 * @param scale scale in pix/map unit (at map center)
	 */
	void setScale(double scale);

	/**
	 * Get the pan origin.
	 *
	 * @return pan origin
	 */
	Coordinate getPanOrigin();

	/**
	 * Set the pan origin.
	 *
	 * @param panOrigin pan origin
	 */
	void setPanOrigin(Coordinate panOrigin);

	/**
	 * Get the renderer to use. This indicates the kind of data for the tile content.
	 *
	 * @return renderer type
	 */
	String getRenderer();

	/**
	 * Set the renderer to use. This indicates the kind of data for the tile content.
	 *
	 * @param renderer renderer type
	 */
	void setRenderer(String renderer);

	/**
	 * Get filter which needs to be applied for in the tile (apart from security and layer specific filters).
	 *
	 * @return filter which needs to be applied for the tile
	 */
	String getFilter();

	/**
	 * Set the filter which needs to be applied for in the tile (apart from security and layer specific filters).
	 *
	 * @param filter filter which needs to be applied for the tile
	 */
	void setFilter(String filter);

	/**
	 * Get the style info which needs to be applied when rendering the tile content.
	 *
	 * @return style info for rendering the tile
	 */
	NamedStyleInfo getStyleInfo();

	/**
	 * Set the style info which needs to be applied when rendering the tile content.
	 *
	 * @param styleInfo style info for rendering the tile
	 */
	void setStyleInfo(NamedStyleInfo styleInfo);

	/**
	 * Should the geometries be painted?
	 *
	 * @return true when geometries should be rendered
	 */
	boolean isPaintGeometries();

	/**
	 * Set whether geometries should be rendered.
	 *
	 * @param paintGeometries include geometries when rendering?
	 */
	void setPaintGeometries(boolean paintGeometries);

	/**
	 * Should the labels be painted?
	 *
	 * @return true when labels should be rendered
	 */
	boolean isPaintLabels();

	/**
	 * Set whether labels should be rendered.
	 *
	 * @param paintLabels include labels when rendering?
	 */
	void setPaintLabels(boolean paintLabels);

	/**
	 * Get which data should be included in the features. For possible values, see
	 * {@link org.geomajas.layer.VectorLayerService}.
	 *
	 * @return what to include
	 * @deprecated features no longer included in tile
	 */
	@Deprecated
	int getFeatureIncludes();

	/**
	 * Set the data to include in the features which are returned. For possible values, see
	 * {@link org.geomajas.layer.VectorLayerService}.
	 *
	 * @param featureIncludes what the include
	 * @deprecated features no longer included in tile
	 */
	@Deprecated
	void setFeatureIncludes(int featureIncludes);

}
