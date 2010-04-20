/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.command.configuration;

import java.util.ArrayList;

import org.geomajas.command.Command;
import org.geomajas.command.dto.UserMaximumExtentRequest;
import org.geomajas.command.dto.UserMaximumExtentResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerType;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.layer.VectorLayerService;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
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

	public UserMaximumExtentResponse getEmptyCommandResponse() {
		return new UserMaximumExtentResponse();
	}

	public void execute(UserMaximumExtentRequest request, UserMaximumExtentResponse response) throws Exception {
		String[] layers;
		ArrayList<String> tempLayers = new ArrayList<String>();
		String includeLayers = request.getIncludeLayers();
		boolean excludeRasterLayers = request.isExcludeRasterLayers();
		if (includeLayers != null && includeLayers.length() > 0) {
			for (String layer : includeLayers.split(",")) {
				String layerId = layer.trim();
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
		CoordinateReferenceSystem targetCrs = CRS.decode(request.getCrs());

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
						MathTransform transformer = geoService.findMathTransform(layer.getCrs(), targetCrs);
						bounds = JTS.transform(bounds, transformer);
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
