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
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.MapInfo;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * This command fetches, and returns the initial application configuration for a specific MapWidget.
 *
 * @author Pieter De Graef
 */
@Component()
public class GetMapConfigurationCommand implements Command<GetMapConfigurationRequest, GetMapConfigurationResponse> {

	@Autowired
	private ApplicationInfo application;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	private static double METER_PER_INCH = 0.0254;

	public GetMapConfigurationResponse getEmptyCommandResponse() {
		return new GetMapConfigurationResponse();
	}

	public void execute(GetMapConfigurationRequest request, GetMapConfigurationResponse response) throws Exception {
		// @todo security, data should be filtered
		String mapId = request.getMapId();
		if (null == mapId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "mapId");
		}
		for (MapInfo mapInfo : application.getMaps()) {
			if (mapId.equals(mapInfo.getId())) {
				response.setMapInfo(getClientMapInfo(mapInfo));
			}
		}
	}

	public MapInfo getClientMapInfo(MapInfo server) throws LayerException {
		MapInfo client = server.clone();
		client.setLayers(getClientLayers(server.getCrs(), server.getLayers()));
		client.setUnitLength(getUnitLength(server.getCrs(), server.getInitialBounds()));
		client.setPixelLength(METER_PER_INCH / client.getUnitLength() / application.getScreenDpi());
		return client;
	}

	public List<LayerInfo> getClientLayers(String mapCrs, List<LayerInfo> serverLayers) throws LayerException {
		List<LayerInfo> clientLayers = new ArrayList<LayerInfo>();
		for (LayerInfo serverLayer : serverLayers) {
			clientLayers.add(getClientLayer(mapCrs, serverLayer));
		}
		return clientLayers;
	}

	public LayerInfo getClientLayer(String mapCrs, LayerInfo server) throws LayerException {
		LayerInfo client = server.clone();
		client.setMaxExtent(getClientMaxExtent(mapCrs, server.getCrs(), server.getMaxExtent()));
		return client;
	}

	public Bbox getClientMaxExtent(String mapCrsKey, String layerCrsKey, Bbox serverBbox) throws LayerException {
		if (mapCrsKey.equals(layerCrsKey)) {
			return serverBbox;
		}
		try {
			CoordinateReferenceSystem mapCrs = CRS.decode(mapCrsKey);
			CoordinateReferenceSystem layerCrs = CRS.decode(layerCrsKey);
			Envelope serverEnvelope = converterService.toInternal(serverBbox);
			MathTransform transformer = geoService.findMathTransform(layerCrs, mapCrs);
			return converterService.toDto(JTS.transform(serverEnvelope, transformer));
		} catch (FactoryException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_INIT_PROBLEM);
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_INIT_PROBLEM);
		}
	}

	private double getUnitLength(String mapCrsKey, Bbox mapBounds) throws LayerException {
		try {
			if (null == mapBounds) {
				throw new LayerException(ExceptionCode.MAP_MAX_EXTENT_MISSING);
			}
			CoordinateReferenceSystem crs = CRS.decode(mapCrsKey);
			GeodeticCalculator calculator = new GeodeticCalculator(crs);
			calculator.setStartingPosition(new DirectPosition2D(crs, mapBounds.getX(), mapBounds.getY()));
			calculator.setDestinationPosition(new DirectPosition2D(crs, mapBounds.getMaxX(), mapBounds.getY()));
			double distance = calculator.getOrthodromicDistance();
			return distance / mapBounds.getWidth();
		} catch (FactoryException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_INIT_PROBLEM);
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_INIT_PROBLEM);
		}
	}

}
