package org.geomajas.plugin.editing.server.command;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.editing.dto.GeometryAreaRequest;
import org.geomajas.plugin.editing.dto.GeometryAreaResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/testdata/layerCountries.xml",
		"/org/geomajas/testdata/simplevectorsContext.xml" })
public class GeometryAreaCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	private List<Geometry> geometries;

	private double DELTA = 1E-5;

	@Before
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
			double i = j/100.0;
			coords.add(new Coordinate((1 - i) * start.getX() + i * stop.getX(), (1 - i) * start.getY() + i
					* stop.getY()));
		}
		start = new Coordinate(180, 0);
		stop = new Coordinate(180, 90);
		for (int j = 0; j <= 100; j++) {
			double i = j/100.0;
			coords.add(new Coordinate((1 - i) * start.getX() + i * stop.getX(), (1 - i) * start.getY() + i
					* stop.getY()));
		}
		start = new Coordinate(180, 90);
		stop = new Coordinate(0, 90);
		for (int j = 0; j <= 100; j++) {
			double i = j/100.0;
			coords.add(new Coordinate((1 - i) * start.getX() + i * stop.getX(), (1 - i) * start.getY() + i
					* stop.getY()));
		}
		start = new Coordinate(0, 90);
		stop = new Coordinate(0, 0);
		for (int j = 0; j <= 100; j++) {
			double i = j/100.0;
			coords.add(new Coordinate((1 - i) * start.getX() + i * stop.getX(), (1 - i) * start.getY() + i
					* stop.getY()));
		}
		earthRing.setCoordinates(coords.toArray(new Coordinate[0]));
		earthQuadrant.setGeometries(new Geometry[] { earthRing });
		geometries.add(earthQuadrant);
	}

	@Test
	public void areaTestNoCrs() throws GeomajasException {
		GeometryAreaRequest request = new GeometryAreaRequest();
		request.setGeometries(geometries);
		GeometryAreaResponse response = (GeometryAreaResponse) dispatcher.execute(GeometryAreaRequest.COMMAND, request,
				null, "en");
		List<Double> areas = response.getAreas();
		Assert.assertEquals(4, areas.size());
		Assert.assertEquals(25, areas.get(0), DELTA);
		Assert.assertEquals(0, areas.get(1), DELTA);
		Assert.assertEquals(0, areas.get(2), DELTA);
		Assert.assertEquals(90 * 180, areas.get(3), DELTA);
	}

	@Test
	public void areaTestCrs() throws GeomajasException {
		GeometryAreaRequest request = new GeometryAreaRequest();
		request.setGeometries(geometries);
		request.setGeometries(geometries);
		request.setCrs("EPSG:4326");
		GeometryAreaResponse response = (GeometryAreaResponse) dispatcher.execute(GeometryAreaRequest.COMMAND, request,
				null, "en");
		List<Double> areas = response.getAreas();
		Assert.assertEquals(4, areas.size());
		Assert.assertEquals(3.05094E11, areas.get(0), DELTA * 3.05094E11);
		Assert.assertEquals(0, areas.get(1), DELTA);
		Assert.assertEquals(0, areas.get(2), DELTA);
		Assert.assertEquals(1.278E14, areas.get(3), DELTA * 1.278E14);
	}

}
