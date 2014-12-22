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

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
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
import org.geomajas.command.dto.SearchAttributesRequest;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.UserMaximumExtentRequest;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.service.DtoConverterServiceImpl;
import org.geomajas.layer.feature.SearchCriterion;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.service.DtoConverterService;
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
	private static final String LAYER_ID = "countries";
	private static final String REGION_ATTRIBUTE = "region";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String ID_ATTRIBUTE = "id";

	private static final String MERCATOR = "EPSG:900913";
	private static final String LONLAT = "EPSG:4326";

	private List<Geometry> geometries;


	public GetConfigurationRequest generateConfigurationRequest() {
		GetConfigurationRequest configurationRequest = new GetConfigurationRequest();

		configurationRequest.setApplicationId("rest-app");
		return configurationRequest;
	}

	public GetMapConfigurationRequest generateMapConfigurationRequest() {
		GetMapConfigurationRequest configurationRequest = new GetMapConfigurationRequest();
		configurationRequest.setApplicationId("rest-app");
		configurationRequest.setMapId("mapCountries");

		return configurationRequest;
	}

	public RefreshConfigurationRequest generateRefreshConfigurationRequest() {
		RefreshConfigurationRequest configurationRequest = new RefreshConfigurationRequest();
		return configurationRequest;
	}

	public UserMaximumExtentRequest generateUserMaximumExtentRequest() {
		UserMaximumExtentRequest maximumExtentRequest = new UserMaximumExtentRequest();
		String[] layerIds = new String[1];
		layerIds[0] = "layerWmsBluemarble"; //configured in the backend
		maximumExtentRequest.setLayerIds(layerIds);
		maximumExtentRequest.setCrs("EPSG:4326");
		return maximumExtentRequest;
	}

	public GeometrySplitRequest generateGeometrySplitRequest() {
		GeometrySplitRequest request = new GeometrySplitRequest();

		request.setGeometry(generateGeometry());
		request.setSplitLine(generateGeometry());

		return request;
	}

	public GeometryMergeRequest generateGeometryMergeRequest() {
		GeometryMergeRequest request = new GeometryMergeRequest();
		request.setGeometries(create());
		request.setPrecision(0);
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

		List<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(createComplexPoly());
		geometries.add(createThreePointsLine());
		GeometryConvexHullRequest request = new GeometryConvexHullRequest();
		request.setGeometries(geometries);

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

	public Geometry createThreePointsLine() {
		Geometry line = new Geometry(Geometry.LINE_STRING, 2, 0);
		line.setCoordinates(new Coordinate[]{
				new Coordinate(200, 200), new Coordinate(400, 400), new Coordinate(200, 600)
		});
		return line;
	}

	public TransformGeometryRequest generateTransformGeometryRequest() {
		TransformGeometryRequest request = new TransformGeometryRequest();
		Bbox origin = new Bbox(10, 30, 10, 10);
		request.setBounds(origin);
		request.setSourceCrs(MERCATOR);
		request.setTargetCrs(LONLAT);
		return request;
	}

	public SearchAttributesRequest generateSearchAttributesRequest() {
		SearchAttributesRequest request = new SearchAttributesRequest();
		request.setLayerId("beans"); //configured bean in test data project
		request.setAttributePath("manyToOneAttr");

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

	public GeometryAreaRequest generateGeometryAreaRequest() {
		GeometryAreaRequest request = new GeometryAreaRequest();
		request.setGeometries(geometries);
		request.setCrs("EPSG:4326");

		return request;
	}

	public SearchByLocationRequest generateSearchByLocationService() throws GeomajasException {
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setLayerIds(new String[] { LAYER_ID });

		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new com.vividsolutions.jts.geom.Coordinate[]
				{new com.vividsolutions.jts.geom.Coordinate(0, 0),
				new com.vividsolutions.jts.geom.Coordinate(-180, 180)});
		DtoConverterService converterService = new DtoConverterServiceImpl();

		request.setLocation(converterService.toDto(equator));

		return request;
	}

	public SearchFeatureRequest generateSearchFeatureRequest() {
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setLayerId(LAYER_ID);
		request.setCrs("EPSG:4326");
		SearchCriterion searchCriterion1 = new SearchCriterion();
		searchCriterion1.setAttributeName(REGION_ATTRIBUTE);
		searchCriterion1.setOperator("like");
		searchCriterion1.setValue("'%egion 1'");
		SearchCriterion searchCriterion2 = new SearchCriterion();
		searchCriterion2.setAttributeName(REGION_ATTRIBUTE);
		searchCriterion2.setOperator("like");
		searchCriterion2.setValue("'%egion 2'");

		request.setCriteria(new SearchCriterion[] {searchCriterion1, searchCriterion2});
		request.setBooleanOperator("or");

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

	private List<Geometry> create() {
		List<Geometry> geometries = new ArrayList<Geometry>();
		Geometry polygon = new Geometry(Geometry.POLYGON, 1, 0);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 1, 0);
		linearRing.setCoordinates(new Coordinate[]{
				new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(5, 10), new Coordinate(0, 0)
		});
		polygon.setGeometries(new Geometry[]{linearRing});
		geometries.add(polygon);
		Geometry line = new Geometry(Geometry.LINE_STRING, 2, 0);
		line.setCoordinates(new Coordinate[]{
				new Coordinate(200, 200), new Coordinate(400, 400)
		});
		geometries.add(line);
		Geometry point = new Geometry(Geometry.POINT, 3, 0);
		point.setCoordinates(new Coordinate[]{
				new Coordinate(5000, 5000)
		});
		geometries.add(point);

		return geometries;
	}

	private Geometry createComplexPoly() {
		Geometry polygon = new Geometry(Geometry.POLYGON, 1, 0);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 1, 0);
		linearRing.setCoordinates(new Coordinate[]{
				new Coordinate(0, 0), new Coordinate(0, 30), new Coordinate(5, 10), new Coordinate(15, 30),
				new Coordinate(30, 0), new Coordinate(0, 0)
		});
		polygon.setGeometries(new Geometry[]{linearRing});
		return polygon;
	}

	public void createGeometries() {
		geometries = new ArrayList<Geometry>();
		Geometry polygon = new Geometry(Geometry.POLYGON, 1, 0);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 1, 0);
		linearRing.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 10),
				new Coordinate(5, 10), new Coordinate(0, 0) });
		polygon.setGeometries(new Geometry[] { linearRing });
		geometries.add(polygon);
		Geometry line = new Geometry(Geometry.LINE_STRING, 2, 0);
		line.setCoordinates(new Coordinate[] { new Coordinate(200, 200), new Coordinate(400, 400) });
		geometries.add(line);
		Geometry point = new Geometry(Geometry.POINT, 3, 0);
		point.setCoordinates(new Coordinate[] { new Coordinate(5000, 5000) });
		geometries.add(point);
		Geometry earthQuadrant = new Geometry(Geometry.POLYGON, 1, 0);
		Geometry earthRing = new Geometry(Geometry.LINEAR_RING, 1, 0);
		List<Coordinate> coords = new ArrayList<Coordinate>();
		Coordinate start = new Coordinate(0, 0);
		Coordinate stop = new Coordinate(180, 0);
		for (int j = 0; j <= 100; j++) {
			double i = j / 100.0;
			coords.add(new Coordinate((1 - i) * start.getX() + i * stop.getX(), (1 - i) * start.getY() + i
					* stop.getY()));
		}
		start = new Coordinate(180, 0);
		stop = new Coordinate(180, 90);
		for (int j = 0; j <= 100; j++) {
			double i = j / 100.0;
			coords.add(new Coordinate((1 - i) * start.getX() + i * stop.getX(), (1 - i) * start.getY() + i
					* stop.getY()));
		}
		start = new Coordinate(180, 90);
		stop = new Coordinate(0, 90);
		for (int j = 0; j <= 100; j++) {
			double i = j / 100.0;
			coords.add(new Coordinate((1 - i) * start.getX() + i * stop.getX(), (1 - i) * start.getY() + i
					* stop.getY()));
		}
		start = new Coordinate(0, 90);
		stop = new Coordinate(0, 0);
		for (int j = 0; j <= 100; j++) {
			double i = j / 100.0;
			coords.add(new Coordinate((1 - i) * start.getX() + i * stop.getX(), (1 - i) * start.getY() + i
					* stop.getY()));
		}
		earthRing.setCoordinates(coords.toArray(new Coordinate[0]));
		earthQuadrant.setGeometries(new Geometry[] { earthRing });
		geometries.add(earthQuadrant);
	}
}
