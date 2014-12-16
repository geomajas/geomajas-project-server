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
package org.geomajas.rest.server.factory;

import org.geomajas.command.dto.GeometryAreaRequest;
import org.geomajas.command.dto.GeometryBufferRequest;
import org.geomajas.command.dto.GeometryConvexHullRequest;
import org.geomajas.command.dto.GeometryMergeRequest;
import org.geomajas.command.dto.GeometrySplitRequest;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.RefreshConfigurationRequest;
import org.geomajas.command.dto.RegisterNamedStyleInfoRequest;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.UserMaximumExtentRequest;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.UserStyleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * RequestObjectsFactory is used to create examples from all kinds of request dto's.
 *
 * @author Dosi Bingov
 */
public class RequestObjectsFactory {

	public GetConfigurationRequest generateConfigurationRequest() {
		GetConfigurationRequest configurationRequest = new GetConfigurationRequest();

		configurationRequest.setApplicationId("rest-app");
		return configurationRequest;
	}

	public GetMapConfigurationRequest generateMapConfigurationRequest() {
		GetMapConfigurationRequest configurationRequest = new GetMapConfigurationRequest();
		configurationRequest.setApplicationId("rest-app");
		configurationRequest.setMapId("mapWms");

		return configurationRequest;
	}

	public RefreshConfigurationRequest generateRefreshConfigurationRequest() {
		RefreshConfigurationRequest configurationRequest = new RefreshConfigurationRequest();
		return configurationRequest;
	}

	public UserMaximumExtentRequest generateUserMaximumExtentRequest() {
		UserMaximumExtentRequest maximumExtentRequest = new UserMaximumExtentRequest();

		return maximumExtentRequest;
	}

	public GeometryAreaRequest generateGeometryAreaRequest() {
		GeometryAreaRequest geometryAreaRequest = new GeometryAreaRequest();

		List<Geometry> geometryList = new ArrayList<Geometry>();
		geometryList.add(generateGeometry());
		geometryList.add(generateGeometry());

		geometryAreaRequest.setGeometries(geometryList);
		geometryAreaRequest.setCrs("EPSG:");

		return geometryAreaRequest;
	}

	public GeometrySplitRequest generateGeometrySplitRequest() {
		GeometrySplitRequest request = new GeometrySplitRequest();

		//TODO:
		request.setGeometry(generateGeometry());
		request.setSplitLine(generateGeometry());

		return request;
	}

	public GeometryMergeRequest generateGeometryMergeRequest() {
		GeometryMergeRequest request = new GeometryMergeRequest();

		//TODO:
		List<Geometry> geometryList = new ArrayList<Geometry>();
		geometryList.add(generateGeometry());
		geometryList.add(generateGeometry());
		request.setGeometries(geometryList);
		request.setPrecision(220);
		request.setUsePrecisionAsBuffer(true);

		return request;
	}

	public GeometryBufferRequest generateGeometryBufferRequest() {
		GeometryBufferRequest request = new GeometryBufferRequest();

		//TODO:
		List<Geometry> geometryList = new ArrayList<Geometry>();
		geometryList.add(generateGeometry());
		geometryList.add(generateGeometry());
		request.setGeometries(geometryList);

		return request;
	}

	public GeometryConvexHullRequest generateGeometryConvexHullRequest() {
		GeometryConvexHullRequest request = new GeometryConvexHullRequest();

		//TODO:
		List<Geometry> geometryList = new ArrayList<Geometry>();
		geometryList.add(generateGeometry());
		geometryList.add(generateGeometry());
		request.setGeometries(geometryList);

		return request;
	}

	public TransformGeometryRequest generateTransformGeometryRequest() {
		TransformGeometryRequest request = new TransformGeometryRequest();

		//TODO:
		request.setGeometry(generateGeometry());

		return request;
	}

	public GetRasterTilesRequest generateGetRasterTilesRequest() {
		GetRasterTilesRequest request = new GetRasterTilesRequest();

		//TODO:
		request.setBbox(generateBbox());

		return request;
	}

	public GetVectorTileRequest generateGetVectorTileRequest() {
		GetVectorTileRequest request = new GetVectorTileRequest();

		//TODO:

		return request;
	}

	public RegisterNamedStyleInfoRequest generateNamedStyleInfoRequest() {
		//TODO:
		RegisterNamedStyleInfoRequest request = new RegisterNamedStyleInfoRequest();
		request.setLayerId("test id");
		NamedStyleInfo namedStyleInfo = new NamedStyleInfo();
		FeatureStyleInfo featureStyleInfo = new FeatureStyleInfo();
		namedStyleInfo.getFeatureStyles().add(featureStyleInfo);
		namedStyleInfo.setName("test name");
		UserStyleInfo userStyleInfo = new UserStyleInfo();
		userStyleInfo.getFeatureTypeStyleList().add(new FeatureTypeStyleInfo());
		userStyleInfo.setTitle("test title");
		namedStyleInfo.setUserStyle(userStyleInfo);
		request.setNamedStyleInfo(namedStyleInfo);

		return request;
	}

	public LoginRequest generateLoginRequest() {
		LoginRequest request = new LoginRequest();
		request.setLogin("admin");
		request.setPassword("password"); //encrypt: PwofgU2BD04NlWr6clUYYA
		return request;
	}

	public Geometry generateGeometry() {
		Geometry geometry = new Geometry();
		//todo generate random coordinates
		return geometry;
	}

	public Bbox generateBbox() {
		Bbox bbox = new Bbox();
		bbox.setHeight(250);
		bbox.setWidth(250);
		bbox.setMaxY(250);
		bbox.setMaxX(250);
		bbox.setX(0);
		bbox.setY(0);

		//todo s random coordinates
		return bbox;
	}
}
