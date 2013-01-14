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
package org.geomajas.command.configuration;

import java.util.ArrayList;

import org.geomajas.command.Command;
import org.geomajas.command.dto.UserMaximumExtentRequest;
import org.geomajas.command.dto.UserMaximumExtentResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Calculate the maximum extent a user can see (based on a set of layers).
 * 
 * @author Kristof Heirwegh
 * @author Joachim Van der Auwera
 */
@Component()
public class UserMaximumExtentCommand implements Command<UserMaximumExtentRequest, UserMaximumExtentResponse> {

	private final Logger log = LoggerFactory.getLogger(UserMaximumExtentCommand.class);

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	private SecurityContext securityContext;

	/** {@inheritDoc} */
	public UserMaximumExtentResponse getEmptyCommandResponse() {
		return new UserMaximumExtentResponse();
	}

	/** {@inheritDoc} */
	public void execute(UserMaximumExtentRequest request, UserMaximumExtentResponse response) throws Exception {
		String[] layers;
		ArrayList<String> tempLayers = new ArrayList<String>();
		String[] includeLayers = request.getLayerIds();
		boolean excludeRasterLayers = request.isExcludeRasterLayers();
		if (includeLayers != null && includeLayers.length > 0) {
			for (String layerId : includeLayers) {
				if (!securityContext.isLayerVisible(layerId)) {
					throw new GeomajasSecurityException(ExceptionCode.LAYER_NOT_VISIBLE, layerId);
				}
				Layer<?> l = configurationService.getLayer(layerId);
				if (null == l) {
					throw new GeomajasException(ExceptionCode.LAYER_NOT_FOUND, layerId);
				}
				if (!excludeRasterLayers || l.getLayerInfo().getLayerType() != LayerType.RASTER) {
					tempLayers.add(l.getId());
				}
			}
		}
		layers = tempLayers.toArray(new String[tempLayers.size()]);

		Layer<?> layer;
		Crs targetCrs = geoService.getCrs2(request.getCrs());

		if (layers.length == 0) {
			// return empty bbox
			response.setBounds(new Bbox());
		} else {
			Envelope extent = new Envelope();
			for (String layerId : layers) {
				layer = configurationService.getLayer(layerId);
				if (layer != null) {
					Envelope bounds;
					if (layer.getLayerInfo().getLayerType() == LayerType.RASTER) {
						bounds = securityContext.getVisibleArea(layerId).getEnvelopeInternal();
						CrsTransform transform = geoService.getCrsTransform(layer.getCrs(), targetCrs);
						bounds = geoService.transform(bounds, transform);
					} else {
						bounds = layerService.getBounds(layerId, targetCrs, null);
					}
					extent.expandToInclude(bounds);
				} else {
					log.warn("layer not found ?! " + layerId);
				}
			}
			response.setBounds(converterService.toDto(extent));
		}
	}
}
