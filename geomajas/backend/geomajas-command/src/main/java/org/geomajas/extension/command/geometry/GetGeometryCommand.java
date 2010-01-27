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

package org.geomajas.extension.command.geometry;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.command.Command;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.extension.command.dto.GetGeometryRequest;
import org.geomajas.extension.command.dto.GetGeometryResponse;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.DtoConverter;
import org.geomajas.service.FilterCreator;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Get geometry for a feature. This needed when features are loaded lazily.
 *
 * @author Pieter De Graef
 */
@Component()
public class GetGeometryCommand implements Command<GetGeometryRequest, GetGeometryResponse> {

	private final Logger log = LoggerFactory.getLogger(GetGeometryCommand.class);

	@Autowired
	private ApplicationService runtimeParameters;

	@Autowired
	private ApplicationInfo application;

	@Autowired
	private DtoConverter converter;

	@Autowired
	private FilterCreator filterCreator;

	public GetGeometryResponse getEmptyCommandResponse() {
		return new GetGeometryResponse();
	}

	public void execute(GetGeometryRequest request, GetGeometryResponse response) throws Exception {
		String layerId = request.getLayerId();
		String[] featureIds = request.getFeatureIds();
		int size = featureIds.length;
		org.geomajas.geometry.Geometry[] geometries = new org.geomajas.geometry.Geometry[size];
		response.setGeometries(geometries);
		for (int i = 0; i < size; i++) {
			String featureId = featureIds[i];
			if (layerId != null && featureIds != null) {
				Layer<?> layer;
				try {
					layer = runtimeParameters.getLayer(layerId);
				} catch (NullPointerException e) {
					layer = null;
				}
				if (layer != null && layer instanceof VectorLayer) {
					VectorLayer vectorLayer = (VectorLayer) layer;
					Object feature;
					try {
						Filter filter = filterCreator.createFidFilter(new String[] {featureId});

						Iterator<?> iterator = vectorLayer.getLayerModel().getElements(filter);
						if (iterator.hasNext()) {
							feature = iterator.next();
						} else {
							feature = null;
						}
					} catch (GeomajasException e) {
						log.error("execute() problem", e);
						feature = null;
					}
					if (feature != null) {
						Geometry geometry;
						geometry = vectorLayer.getLayerModel().getFeatureModel().getGeometry(feature);
						geometries[i] = converter.toDto(geometry);
					}
				}
			}
		}
	}

}