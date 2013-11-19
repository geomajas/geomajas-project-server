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

package org.geomajas.plugin.reporting.command.reporting;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.command.Command;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.command.dto.ClientGeometryLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.plugin.reporting.command.dto.PrepareReportingRequest;
import org.geomajas.plugin.reporting.command.dto.PrepareReportingResponse;
import org.geomajas.plugin.reporting.data.ReportingCacheContainer;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Prepares data for the report, the actual report is retrieved by a URL.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Component
//@Api don't know about api, all this caching stuff causes problems cfr lazy loading etc
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class PrepareReportingCommand implements Command<PrepareReportingRequest, PrepareReportingResponse> {

	private static final int MAP_BUFFER_SIZE = 1024 * 10; // 10 kB buffer // NOSONAR

	@Autowired
	private ImageService imageService;

	@Autowired
	private CacheManagerService cacheManagerService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private GeoService geoService;

	@Override
	public PrepareReportingResponse getEmptyCommandResponse() {
		return new PrepareReportingResponse();
	}

	@Override
	public void execute(PrepareReportingRequest request, PrepareReportingResponse response) throws Exception {
		String layerId = request.getLayerId();
		if (null == layerId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layerId");
		}
		ClientMapInfo clientMapInfo = request.getClientMapInfo();
		if (null == clientMapInfo) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "clientMapInfo");
		}

		MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) clientMapInfo
				.getWidgetInfo(MapRasterizingInfo.WIDGET_KEY);
		ByteArrayOutputStream stream = new ByteArrayOutputStream(MAP_BUFFER_SIZE);
		ReportingCacheContainer container = new ReportingCacheContainer();
		// change font for dpi
		for (ClientLayerInfo clientLayerInfo : clientMapInfo.getLayers()) {
			if (clientLayerInfo instanceof ClientVectorLayerInfo) {
				VectorLayerRasterizingInfo vectorRasterizingInfo = (VectorLayerRasterizingInfo) clientLayerInfo
						.getWidgetInfo(VectorLayerRasterizingInfo.WIDGET_KEY);
				int origSize = vectorRasterizingInfo.getStyle().getLabelStyle().getFontStyle().getSize();
				vectorRasterizingInfo.getStyle().getLabelStyle().getFontStyle()
						.setSize(origSize * request.getDpi() / 96);
			}
		}
		// write features and calculate bounds
		Envelope bounds = new Envelope();
		List<Feature> containerFeatures = container.getFeatures();
		for (ClientLayerInfo clientLayerInfo : clientMapInfo.getLayers()) {
			if (layerId.equals(clientLayerInfo.getServerLayerId())) {
				List<InternalFeature> features = vectorLayerService.getFeatures(layerId,
						geoService.getCrs2(clientMapInfo.getCrs()),
						getFilter(request.getFilter(), request.getFeatureIds()), null,
						VectorLayerService.FEATURE_INCLUDE_ALL);
				for (InternalFeature internalFeature : features) {
					containerFeatures.add(dtoConverterService.toDto(internalFeature));
					bounds.expandToInclude(internalFeature.getBounds());
				}
			}
		}
		// add the buffer to the bounds
		for (ClientLayerInfo clientLayerInfo : mapRasterizingInfo.getExtraLayers()) {
			if (clientLayerInfo instanceof ClientGeometryLayerInfo) {
				for (Geometry geometry : ((ClientGeometryLayerInfo) clientLayerInfo).getGeometries()) {
					bounds.expandToInclude(dtoConverterService.toInternal(geometry).getEnvelopeInternal());
				}
			}
		}
		// assure bounds have a minimum size
		double minGeometrySize = request.getMinimumGeometrySize();
		if (minGeometrySize > 0) {
			if (bounds.getWidth() < minGeometrySize || bounds.getHeight() < minGeometrySize) {
				bounds.expandBy(Math.max(0, (minGeometrySize - bounds.getWidth()) / 2),
						Math.max(0, (minGeometrySize - bounds.getHeight()) / 2));
			}
		}
		// finalize bounds and scale
		double imageWidthPx = (double) request.getImageWidth();
		double imageHeightPx = (double) request.getImageHeight();
		// resize to same ratio
		double ratio = imageWidthPx / imageHeightPx;
		if (bounds.getWidth() / bounds.getHeight() > ratio) {
			double newHeight = bounds.getWidth() / ratio;
			// too wide, expand height
			bounds.expandBy(0, 0.5 * (newHeight - bounds.getHeight()));
		} else {
			double newWidth = bounds.getHeight() * ratio;
			// too high, expand width
			bounds.expandBy(0.5 * (newWidth - bounds.getWidth()), 0);
		}
		// add margin
		bounds.expandBy(request.getMargin() / imageWidthPx * bounds.getWidth(), request.getMargin()
				/ imageHeightPx * bounds.getHeight());
		// recalculate scale
		double scale = (imageWidthPx + 2 * request.getMargin()) / bounds.getWidth();
		mapRasterizingInfo.setBounds(dtoConverterService.toDto(bounds));
		mapRasterizingInfo.setScale(scale);
		imageService.writeMap(stream, request.getClientMapInfo());
		container.setMapImageData(stream.toByteArray());
		stream.reset();

		mapRasterizingInfo.getLegendRasterizingInfo().setWidth(request.getLegendWidth());
		mapRasterizingInfo.getLegendRasterizingInfo().setHeight(request.getLegendHeight());
		imageService.writeLegend(stream, clientMapInfo);
		container.setLegendImageData(stream.toByteArray());
		String key = UUID.randomUUID().toString();
		cacheManagerService.put(null, CacheCategory.RASTER, key, container,
				dtoConverterService.toInternal(mapRasterizingInfo.getBounds()));
		response.setKey(key);
		response.setCacheCategory(CacheCategory.RASTER.toString());
		response.setRelativeUrl("reporting/c/" + layerId + "/" + PrepareReportingResponse.REPORT_NAME + "." +
				PrepareReportingResponse.FORMAT + "?key=" + response.getKey());
	}

	/**
	 * Build filter for the request.
	 *
	 * @param layerFilter layer filter
	 * @param featureIds features to include in report (null for all)
	 * @return filter
	 * @throws GeomajasException filter could not be parsed/created
	 */
	private Filter getFilter(String layerFilter, String[] featureIds) throws GeomajasException {
		Filter filter = null;
		if (null != layerFilter) {
			filter = filterService.parseFilter(layerFilter);
		}
		if (null != featureIds) {
			Filter fidFilter = filterService.createFidFilter(featureIds);
			if (null == filter) {
				filter = fidFilter;
			} else {
				filter = filterService.createAndFilter(filter, fidFilter);
			}
		}
		return filter;
	}

}