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

package org.geomajas.internal.layer;

import java.util.List;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetAttributesContainer;
import org.geomajas.layer.pipeline.GetBoundsContainer;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Implementation of {@link org.geomajas.layer.VectorLayerService}, a service which allows accessing data from a
 * vector layer.
 * <p/>
 * All access to vector layers should be done through this service, not by accessing the layer directly as this
 * adds possible caching, security etc. These services are implemented using pipelines (see
 * {@link org.geomajas.service.pipeline.PipelineService}) to make them configurable.
 * 
 * @author Joachim Van der Auwera
 */
@Transactional(rollbackFor = { Exception.class })
@Component
public class VectorLayerServiceImpl extends LayerServiceImpl implements VectorLayerService {

	private final Logger log = LoggerFactory.getLogger(VectorLayerServiceImpl.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private PipelineService pipelineService;

	private VectorLayer getVectorLayer(String layerId) throws GeomajasException {
		if (!securityContext.isLayerVisible(layerId)) {
			throw new GeomajasSecurityException(ExceptionCode.LAYER_NOT_VISIBLE, layerId, securityContext.getUserId());
		}
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		if (null == layer) {
			throw new GeomajasException(ExceptionCode.VECTOR_LAYER_NOT_FOUND, layerId);
		}
		return layer;
	}

	@SuppressWarnings("unchecked")
	public void saveOrUpdate(String layerId, CoordinateReferenceSystem crs, List<InternalFeature> oldFeatures,
			List<InternalFeature> newFeatures) throws GeomajasException {
		log.debug("saveOrUpdate start on layer {}", layerId);
		long ts = System.currentTimeMillis();
		VectorLayer layer = getVectorLayer(layerId);
		CrsTransform mapToLayer = geoService.getCrsTransform(crs, layer.getCrs());
		CrsTransform layerToMap = geoService.getCrsTransform(layer.getCrs(), crs);
		PipelineContext context = pipelineService.createContext();
		context.put(PipelineCode.LAYER_ID_KEY, layerId);
		context.put(PipelineCode.LAYER_KEY, layer);
		context.put(PipelineCode.CRS_TRANSFORM_KEY, mapToLayer);
		context.put(PipelineCode.CRS_BACK_TRANSFORM_KEY, layerToMap);
		context.put(PipelineCode.OLD_FEATURES_KEY, oldFeatures);
		context.put(PipelineCode.NEW_FEATURES_KEY, newFeatures);
		context.put(PipelineCode.CRS_KEY, crs);
		pipelineService.execute(PipelineCode.PIPELINE_SAVE_OR_UPDATE, layerId, context, null);
		log.debug("saveOrUpdate done on layer {}, time {}s", layerId, (System.currentTimeMillis() - ts) / 1000.0);
	}

	public List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter queryFilter,
			NamedStyleInfo style, int featureIncludes, int offset, int maxResultSize) throws GeomajasException {
		return getFeatures(layerId, crs, queryFilter, style, featureIncludes, offset, maxResultSize, false);
	}
	@SuppressWarnings("unchecked")
	public List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter queryFilter,
			NamedStyleInfo style, int featureIncludes, int offset, int maxResultSize, boolean forcePaging)
			throws GeomajasException {
		log.debug("getFeatures start on layer {}", layerId);
		long ts = System.currentTimeMillis();
		VectorLayer layer = getVectorLayer(layerId);
		CrsTransform transformation = null;
		if ((featureIncludes & FEATURE_INCLUDE_GEOMETRY) != 0 && crs != null && !crs.equals(layer.getCrs())) {
			transformation = geoService.getCrsTransform(layer.getCrs(), crs);
		}
		GetFeaturesContainer container = new GetFeaturesContainer();
		PipelineContext context = pipelineService.createContext();
		context.put(PipelineCode.LAYER_ID_KEY, layerId);
		context.put(PipelineCode.LAYER_KEY, layer);
		context.put(PipelineCode.CRS_TRANSFORM_KEY, transformation);
		context.put(PipelineCode.CRS_KEY, crs);
		context.put(PipelineCode.FILTER_KEY, queryFilter);
		context.put(PipelineCode.STYLE_KEY, style);
		context.put(PipelineCode.FEATURE_INCLUDES_KEY, featureIncludes);
		context.put(PipelineCode.OFFSET_KEY, offset);
		context.put(PipelineCode.MAX_RESULT_SIZE_KEY, maxResultSize);
		context.put(PipelineCode.FORCE_PAGING_KEY, forcePaging);
		pipelineService.execute(PipelineCode.PIPELINE_GET_FEATURES, layerId, context, container);
		log.debug("getFeatures done on layer {}, time {}s", layerId, (System.currentTimeMillis() - ts) / 1000.0);
		return container.getFeatures();
	}

	public List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter filter,
			NamedStyleInfo style, int featureIncludes) throws GeomajasException {
		return getFeatures(layerId, crs, filter, style, featureIncludes, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public Envelope getBounds(String layerId, CoordinateReferenceSystem crs, Filter queryFilter)
			throws GeomajasException {
		log.debug("getBounds start on layer {}", layerId);
		long ts = System.currentTimeMillis();
		VectorLayer layer = getVectorLayer(layerId);
		GetBoundsContainer container = new GetBoundsContainer();
		PipelineContext context = pipelineService.createContext();
		context.put(PipelineCode.LAYER_ID_KEY, layerId);
		context.put(PipelineCode.LAYER_KEY, layer);
		context.put(PipelineCode.CRS_KEY, crs);
		context.put(PipelineCode.CRS_TRANSFORM_KEY, geoService.getCrsTransform(layer.getCrs(), crs));
		context.put(PipelineCode.FILTER_KEY, queryFilter);
		pipelineService.execute(PipelineCode.PIPELINE_GET_BOUNDS, layerId, context, container);
		log.debug("getBounds done on layer {}, time {}s", layerId, (System.currentTimeMillis() - ts) / 1000.0);
		return container.getEnvelope();
	}

	@SuppressWarnings("unchecked")
	public InternalTile getTile(TileMetadata tileMetadata) throws GeomajasException {
		log.debug("getTile start tileMetadata {}", tileMetadata);
		long ts = System.currentTimeMillis();
		String layerId = tileMetadata.getLayerId();
		VectorLayer layer = getVectorLayer(layerId);
		PipelineContext context = pipelineService.createContext();
		context.put(PipelineCode.LAYER_ID_KEY, layerId);
		context.put(PipelineCode.LAYER_KEY, layer);
		context.put(PipelineCode.TILE_METADATA_KEY, tileMetadata);
		Crs crs = geoService.getCrs2(tileMetadata.getCrs());
		context.put(PipelineCode.CRS_KEY, crs);
		CrsTransform layerToMap = geoService.getCrsTransform(layer.getCrs(), crs);
		context.put(PipelineCode.CRS_TRANSFORM_KEY, layerToMap);
		Envelope layerExtent = dtoConverterService.toInternal(layer.getLayerInfo().getMaxExtent());
		Envelope tileExtent = geoService.transform(layerExtent, layerToMap);
		context.put(PipelineCode.TILE_MAX_EXTENT_KEY, tileExtent);
		InternalTile tile = new InternalTileImpl(tileMetadata.getCode(), tileExtent, tileMetadata.getScale());
		GetTileContainer response = new GetTileContainer();
		response.setTile(tile);
		pipelineService.execute(PipelineCode.PIPELINE_GET_VECTOR_TILE, layerId, context, response);
		log.debug("getTile response InternalTile {}", response);
		log.debug("getTile done on layer {}, time {}s", layerId, (System.currentTimeMillis() - ts) / 1000.0);
		return response.getTile();
	}

	@SuppressWarnings("unchecked")
	public List<Attribute<?>> getAttributes(String layerId, String attributeName, Filter filter)
			throws GeomajasException {
		log.debug("saveOrUpdate start on layer {}", layerId);
		long ts = System.currentTimeMillis();
		VectorLayer layer = getVectorLayer(layerId);
		PipelineContext context = pipelineService.createContext();
		context.put(PipelineCode.LAYER_ID_KEY, layerId);
		context.put(PipelineCode.LAYER_KEY, layer);
		context.put(PipelineCode.FILTER_KEY, filter);
		context.put(PipelineCode.ATTRIBUTE_NAME_KEY, attributeName);
		GetAttributesContainer container = new GetAttributesContainer();
		pipelineService.execute(PipelineCode.PIPELINE_GET_ATTRIBUTES, layerId, context, container);
		log.debug("saveOrUpdate done on layer {}, time {}s", layerId, (System.currentTimeMillis() - ts) / 1000.0);
		return container.getAttributes();
	}
}
