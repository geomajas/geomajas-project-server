/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.service;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.LayerExtraInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Utility service which is used to get some global data.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface ConfigurationService {

	/**
	 * Get the vector layer with given id.
	 *
	 * @param id layer id
	 * @return layer
	 */
	VectorLayer getVectorLayer(String id);

	/**
	 * Get the raster layer with given id.
	 *
	 * @param id layer id
	 * @return layer
	 */
	RasterLayer getRasterLayer(String id);

	/**
	 * Get the vector layer with given id.
	 *
	 * @param id vector layer id
	 * @return vector layer
	 */
	Layer<?> getLayer(String id);

	/**
	 * Invalidate a layer. This should be called whenever significant changes to the layer have occurred. These
	 * include deleting the layer, changes in the style, data structure, security, authorizations,...
	 *
	 * @param layerId id of layer which needs to be invalidated
	 * @throws GeomajasException oops (should not happen, steps should just log and not (re)throw exceptions)
	 * @since 1.8.0
	 */
	void invalidateLayer(String layerId) throws GeomajasException;

	/**
	 * Invalidate all layers. This should be called whenever significant changes to the layer have occurred. These
	 * include deleting the layer, changes in the style, data structure, security, authorizations,...
	 *
	 * @throws GeomajasException oops (should not happen, steps should just log and not (re)throw exceptions)
	 * @since 1.8.0
	 */
	void invalidateAllLayers() throws GeomajasException;

	/**
	 * Get information about the map with specified mapId in the application with the specified applicationId.
	 *
	 * @param mapId map Id
	 * @param applicationId application id
	 * @return {@link org.geomajas.configuration.client.ClientMapInfo}
	 */
	ClientMapInfo getMap(String mapId, String applicationId);

	/**
	 * Get the {@link org.opengis.referencing.crs.CoordinateReferenceSystem} with given code.
	 *
	 * @param crs Coordinate reference system code. (EPSG:xxxx)
	 * @return {@link org.opengis.referencing.crs.CoordinateReferenceSystem}
	 * @throws LayerException CRS code not found
	 * @deprecated Use {@link GeoService#getCrs2(String crs)}
	 */
	@Deprecated
	CoordinateReferenceSystem getCrs(String crs) throws LayerException;

	/**
	 * Get a specific entry from the extraInfo for the given type on {@link org.geomajas.configuration.LayerInfo}. This
	 * assumes the fully qualified class name of the object is used as key.
	 *
	 * @param layerInfo layerInfo object to query
	 * @param type type to get
	 * @param <TYPE> type to get
	 * @return value for key if exists
	 * @since 1.9.0
	 */
	<TYPE extends LayerExtraInfo> TYPE getLayerExtraInfo(LayerInfo layerInfo, Class<TYPE> type);

	/**
	 * Get a specific entry from the extraInfo for key and with type checking.
	 *
	 * @param layerInfo layerInfo object to query
	 * @param key key from extraInfo
	 * @param type type to get
	 * @param <TYPE> type to get
	 * @return value for key if exists
	 * @since 1.9.0
	 */
	<TYPE extends LayerExtraInfo> TYPE getLayerExtraInfo(LayerInfo layerInfo, String key, Class<TYPE> type);

}
