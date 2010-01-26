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
package org.geomajas.extension.command.configuration;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.command.Command;
import org.geomajas.extension.command.dto.UserMaximumExtentRequest;
import org.geomajas.extension.command.dto.UserMaximumExtentResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.BboxService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * ???
 *
 * @author check subversion
 */
@Component()
public class UserMaximumExtentCommand implements Command<UserMaximumExtentRequest, UserMaximumExtentResponse> {

	private final Logger log = LoggerFactory.getLogger(UserMaximumExtentCommand.class);

	@Autowired
	private BboxService bboxService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private ApplicationService runtimeParameters;

	public UserMaximumExtentResponse getEmptyCommandResponse() {
		return new UserMaximumExtentResponse();
	}

	public void execute(UserMaximumExtentRequest request, UserMaximumExtentResponse response) throws Exception {
		Filter[] filters;
		String[] layers;
		ArrayList<String> tempLayers = new ArrayList<String>();
		String includeLayers = request.getIncludeLayers();
		boolean excludeRasterLayers = request.isExcludeRasterLayers();
		if (includeLayers != null && includeLayers.length() > 0) {
			for (String layer : includeLayers.split("\\, ")) {
				Layer l = runtimeParameters.getLayer(layer.trim());
				if (!excludeRasterLayers || l.getLayerInfo().getLayerType() != LayerType.RASTER) {
					tempLayers.add(l.getLayerInfo().getId());
				}
			}
		}
		layers = tempLayers.toArray(new String[tempLayers.size()]);
		filters = new Filter[tempLayers.size()];
		for (int i = 0; i < filters.length; i++) {
			filters[i] = Filter.INCLUDE;
		}

		Layer layer;
		CoordinateReferenceSystem mapCrs = CRS.decode(request.getCrs());

		if (layers.length == 0) {
			// return empty bbox
			response.setBounds(new Bbox());
		} else {
			Envelope extent = new Envelope();
			for (int i = 0; i < layers.length; i++) {
				layer = runtimeParameters.getLayer(layers[i]);
				if (layer != null) {
					if (filters[i] != null) {
						MathTransform transformer = geoService.findMathTransform(layer.getCrs(), mapCrs);
						Bbox bounds;
						if (layer.getLayerInfo().getLayerType() == LayerType.RASTER) {
							// no features so nothing to filter
							extent.expandToInclude(
									JTS.transform(bboxService.toEnvelope(layer.getLayerInfo().getMaxExtent()),
											transformer));
						} else {
							if (Filter.INCLUDE.equals(filters[i])) {
								// a shortcut, might not be the same though
								bounds = layer.getLayerInfo().getMaxExtent();

								extent.expandToInclude(
										JTS.transform(bboxService.toEnvelope(layer.getLayerInfo().getMaxExtent()),
												transformer));
							} else {
								log.info("need to filter: " + filters[i].toString());
								if (layer instanceof VectorLayer) {
									VectorLayer vLayer = (VectorLayer) layer;
									log.info("getting bounds for layer: " + layer.getLayerInfo().getId() +
											" - layermodel: " + vLayer.getLayerModel().getClass().getName());
									bounds = vLayer.getLayerModel().getBounds(filters[i]);
									if (layer.getCrs() != null && !mapCrs.equals(layer.getCrs())) {
										log.info("     **** bounds before transforming: " + layers[i] + ": "
												+ bounds.toString());
										extent.expandToInclude(
												JTS.transform(bboxService.toEnvelope(bounds), transformer));
									}
								} else {
									throw new IllegalStateException("Cannot filter features of non-vectorlayer: "
											+ layers[i]);
								}
							}
							log.info("     **** bounds layer " + layers[i] + ": " + bounds.toString());
						}
					}
				} else {
					log.warn("layer not found ?! " + layers[i]);
				}
			}
			response.setBounds(bboxService.fromEnvelope(extent));
		}
	}
}
