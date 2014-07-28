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
package org.geomajas.plugin.deskmanager.service.manager;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.service.GeoService;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.OperationType;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.opengis.geometry.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link DtoFactoryService}.
 *
 * @author Jan Venstermans
 */
@Component
public class DtoFactoryServiceImpl implements DtoFactoryService {

	@Autowired
	private GeoService geoService;

	private static final String LATLON_EPSG = "EPSG:4326";

	@Override
	public RasterCapabilitiesInfo buildRasterCapabilitesInfoFromWms(WebMapServer wmsMapServer,
																	Layer owsLayer, String toCrs)
			throws LayerException {
		RasterCapabilitiesInfo info = new RasterCapabilitiesInfo();
		Crs crs = geoService.getCrs2(toCrs);
		info.setCrs(geoService.getCodeFromCrs(crs));
		info.setName(owsLayer.getName());
		info.setDescription(owsLayer.getTitle());
		OperationType operationTypeFeatureInfo = wmsMapServer.getCapabilities().getRequest().getGetFeatureInfo();
		// GetFeatureInfo is optional, not part of WMS standard. So it can be null.
		if (operationTypeFeatureInfo != null) {
			info.setGetFeatureInfoFormats(operationTypeFeatureInfo.getFormats());
		}

		//extend
		Bbox extendBbox;
		CRSEnvelope crsEnvelope;
		try {
			extendBbox = getBbox(owsLayer, crs);
			crsEnvelope = new CRSEnvelope(crs.getId(), extendBbox.getX(), extendBbox.getY(),
					extendBbox.getMaxX(), extendBbox.getMaxY());
		} catch (GeomajasException e) {
			// this will return hybrid envelope when transformation fails.
			// Only use if other transformations fail
			Envelope generalEnvelope = owsLayer.getEnvelope(crs);
			extendBbox = toBbox(generalEnvelope);
			crsEnvelope = new CRSEnvelope(generalEnvelope);
		}
		info.setExtent(extendBbox);

		// create a sample request
		GetMapRequest request = wmsMapServer.createGetMapRequest();
		request.setFormat("image/png");
		request.setTransparent(true);
		request.setSRS(info.getCrs());
		request.setBBox(crsEnvelope);
		request.addLayer(owsLayer);
		info.setPreviewUrl(request.getFinalURL().toExternalForm());

		GetMapRequest baseRequest = wmsMapServer.createGetMapRequest();
		info.setBaseUrl(baseRequest.getFinalURL().toExternalForm().replaceFirst("\\?.*", ""));
		return info;
	}

	private Bbox getBbox(org.geotools.data.ows.Layer owsLayer, Crs toCrs) throws GeomajasException {
		Envelope envelope = null;
		String crs = null;
		if (owsLayer.getBoundingBoxes() != null && owsLayer.getBoundingBoxes().containsKey(toCrs.getId())) {
			envelope = owsLayer.getBoundingBoxes().get(toCrs);
			crs = toCrs.getId();
		}
		if (envelope != null && crs != null) {
			return toBbox(envelope);
		}
		// if no predefined bounding box: transform from latLon
		Envelope latLonEnvelope = owsLayer.getLatLonBoundingBox();
		return geoService.transform(toBbox(latLonEnvelope), LATLON_EPSG, toCrs.getId());
	}

	private Bbox toBbox(Envelope envelope) {
		return new Bbox(envelope.getMinimum(0), envelope.getMinimum(1), envelope.getSpan(0), envelope.getSpan(1));
	}
}