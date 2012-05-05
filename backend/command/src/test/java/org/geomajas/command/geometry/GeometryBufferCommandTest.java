package org.geomajas.command.geometry;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GeometryBufferRequest;
import org.geomajas.command.dto.GeometryBufferResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
public class GeometryBufferCommandTest {
	
	@Autowired
	private CommandDispatcher dispatcher;
	
	private List<Geometry> geometries;
	
	@Before
	public void createGeometries() {
		geometries = new ArrayList<Geometry>();
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
	}
	
	@Test
	public void bufferTest() throws GeomajasException {
		GeometryBufferRequest request = new GeometryBufferRequest();
		request.setGeometries(geometries);
		request.setBufferDistance(50);
		GeometryBufferResponse response = (GeometryBufferResponse) dispatcher.execute(
				GeometryBufferRequest.COMMAND, request, null, "en");
		List<Geometry> buffered = response.getGeometries();
		Assert.assertEquals(3, buffered.size());
		Assert.assertTrue(buffered.get(0).getGeometries()[0].getCoordinates().length > 4);
		Assert.assertTrue(buffered.get(1).getGeometries()[0].getCoordinates().length > 2);
		Assert.assertTrue(buffered.get(2).getGeometries()[0].getCoordinates().length > 1);
	}
	
	@Test
	public void bufferZeroTest() throws GeomajasException {
		GeometryBufferRequest request = new GeometryBufferRequest();
		request.setGeometries(geometries);
		request.setBufferDistance(0);
		GeometryBufferResponse response = (GeometryBufferResponse) dispatcher.execute(
				GeometryBufferRequest.COMMAND, request, null, "en");
		List<Geometry> buffered = response.getGeometries();
		Assert.assertEquals(3, buffered.size());
		Assert.assertTrue(buffered.get(0).getGeometries()[0].getCoordinates().length == 4);
		Assert.assertTrue(buffered.get(1).getGeometryType().equals(Geometry.POLYGON));
		Assert.assertTrue(buffered.get(1).getCoordinates() == null);
		Assert.assertTrue(buffered.get(2).getGeometryType().equals(Geometry.POLYGON));
		Assert.assertTrue(buffered.get(2).getCoordinates() == null);
	}
	
	@Test
	public void quadrantTest() throws GeomajasException {
		Geometry point = new Geometry(Geometry.POINT, 3, 0);
		point.setCoordinates(new Coordinate[]{
				new Coordinate(5000, 5000)				
		});
		GeometryBufferRequest request = new GeometryBufferRequest();
		List<Geometry> g = new ArrayList<Geometry>();
		g.add(point);
		request.setGeometries(g);
		request.setBufferDistance(900);
		request.setQuadrantSegments(4);
		GeometryBufferResponse response = (GeometryBufferResponse) dispatcher.execute(
				GeometryBufferRequest.COMMAND, request, null, "en");
		g = response.getGeometries();
		// 4 * 4 lines with 17 coordinates (first is the same as last)
		Assert.assertTrue(g.get(0).getGeometries()[0].getCoordinates().length == 17);
	}
}
