package org.geomajas.command.geometry;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GeometryMergeRequest;
import org.geomajas.command.dto.GeometryMergeResponse;
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
public class GeometryMergeCommandTest {
	
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
	public void withPrecisionAsBufferTest() throws GeomajasException {
		GeometryMergeRequest request = new GeometryMergeRequest();
		request.setGeometries(geometries);
		request.setPrecision(0);
		request.setUsePrecisionAsBuffer(true);
		GeometryMergeResponse response = (GeometryMergeResponse) dispatcher.execute(
				GeometryMergeRequest.COMMAND, request, null, "en");
		Assert.assertEquals(3, response.getGeometry().getGeometries().length);
	}
	
	@Test
	public void withoutPrecisionAsBufferTest() {
		GeometryMergeRequest request = new GeometryMergeRequest();
		request.setGeometries(geometries);
		request.setPrecision(0);
		request.setUsePrecisionAsBuffer(false);
		GeometryMergeResponse response = (GeometryMergeResponse) dispatcher.execute(
				GeometryMergeRequest.COMMAND, request, null, "en");
		// Line and point are ignored due to the fact that buffer = 0.
		Assert.assertEquals(1, response.getGeometry().getGeometries().length);
	}

}
