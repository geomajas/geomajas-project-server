package org.geomajas.plugin.editing.server.command;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.editing.client.merge.GeometryMergeException;
import org.geomajas.plugin.editing.dto.GeometryConvexHullRequest;
import org.geomajas.plugin.editing.dto.GeometryConvexHullResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
public class GeomertryConvexHullCommandTest {
	
	@Autowired
	private CommandDispatcher dispatcher;
	
	@Test
	public void doesConvexTest() throws GeometryMergeException, GeomajasException {
		List<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(createComplexPoly());
		geometries.add(createThreePointsLine());
		GeometryConvexHullRequest request = new GeometryConvexHullRequest();
		request.setGeometries(geometries);
		GeometryConvexHullResponse response = (GeometryConvexHullResponse) dispatcher.execute(
				GeometryConvexHullRequest.COMMAND, request, null, "en");
		List<Geometry> result = response.getGeometries();
		Assert.assertEquals(2, result.size());
		// From 6 to 5 coordinates
		Assert.assertEquals(geometries.get(0).getGeometries()[0].getCoordinates().length, 
							result.get(0).getGeometries()[0].getCoordinates().length + 1);
		// From 3 to 4 coordinates; from Line to Polygon
		Assert.assertEquals(geometries.get(1).getCoordinates().length,
							result.get(1).getGeometries()[0].getCoordinates().length - 1);
	}
	
	@Test
	public void doesntConvexTest() throws GeometryMergeException, GeomajasException {
		List<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(createSimplePoly());
		geometries.add(createTwoPointsLine());
		geometries.add(createPoint());
		GeometryConvexHullRequest request = new GeometryConvexHullRequest();
		request.setGeometries(geometries);
		GeometryConvexHullResponse response = (GeometryConvexHullResponse) dispatcher.execute(
				GeometryConvexHullRequest.COMMAND, request, null, "en");
		List<Geometry> result = response.getGeometries();
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(geometries.get(0).getGeometries()[0].getCoordinates().length, 
							result.get(0).getGeometries()[0].getCoordinates().length);
		Assert.assertEquals(geometries.get(1).getCoordinates().length, 
							result.get(1).getCoordinates().length);
		Assert.assertEquals(geometries.get(2).getCoordinates().length, 
							result.get(2).getCoordinates().length);
	}
	
	public Geometry createSimplePoly() {
		Geometry polygon = new Geometry(Geometry.POLYGON, 1, 0);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 1, 0);
		linearRing.setCoordinates(new Coordinate[]{
				new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(5, 10), new Coordinate(0, 0)		
		});
		polygon.setGeometries(new Geometry[]{linearRing});
		return polygon;
	}
	
	public Geometry createComplexPoly() {
		Geometry polygon = new Geometry(Geometry.POLYGON, 1, 0);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 1, 0);
		linearRing.setCoordinates(new Coordinate[]{
				new Coordinate(0, 0), new Coordinate(0, 30), new Coordinate(5, 10), new Coordinate(15, 30), new Coordinate(30, 0), new Coordinate(0, 0)	
		});
		polygon.setGeometries(new Geometry[]{linearRing});
		return polygon;
	}
	
	public Geometry createThreePointsLine() {
		Geometry line = new Geometry(Geometry.LINE_STRING, 2, 0);
		line.setCoordinates(new Coordinate[]{
				new Coordinate(200, 200), new Coordinate(400, 400), new Coordinate(200, 600)				
		});
		return line;
	}
	
	public Geometry createTwoPointsLine() {
		Geometry line = new Geometry(Geometry.LINE_STRING, 2, 0);
		line.setCoordinates(new Coordinate[]{
				new Coordinate(200, 200), new Coordinate(400, 400)				
		});
		return line;
	}
	
	public Geometry createPoint() {
		Geometry point = new Geometry(Geometry.POINT, 3, 0);
		point.setCoordinates(new Coordinate[]{
				new Coordinate(5000, 5000)				
		});
		return point;
	}
}
