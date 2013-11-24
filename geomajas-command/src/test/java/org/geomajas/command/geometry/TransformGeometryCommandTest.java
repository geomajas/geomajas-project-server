package org.geomajas.command.geometry;

import java.util.List;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.TransformGeometryResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.service.DtoConverterService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.io.WKTReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
public class TransformGeometryCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private DtoConverterService converterService;

	private static final String MERCATOR = "EPSG:900913";

	private static final String LONLAT = "EPSG:4326";

	private static final double DELTA = 1e-20;

	@Test
	public void testTransformBounds() throws Exception {
		TransformGeometryRequest request = new TransformGeometryRequest();
		Bbox origin = new Bbox(10, 30, 10, 10);
		request.setBounds(origin);
		request.setSourceCrs(MERCATOR);
		request.setTargetCrs(LONLAT);
		// execute
		TransformGeometryResponse response = (TransformGeometryResponse) dispatcher.execute(
				TransformGeometryRequest.COMMAND, request, null, "en");
		Bbox transformed = response.getBounds();
		Assert.assertEquals(8.983152841195215E-5, transformed.getX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, transformed.getY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, transformed.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, transformed.getMaxY(), DELTA);
	}

	@Test
	public void testTransformGeometry() throws Exception {
		TransformGeometryRequest request = new TransformGeometryRequest();
		WKTReader reader = new WKTReader();
		Geometry origin = converterService.toDto(reader.read("POLYGON((10 30, 20 30,20 40,10 40,10 30))"));
		request.setGeometry(origin);
		request.setSourceCrs(MERCATOR);
		request.setTargetCrs(LONLAT);
		// execute
		TransformGeometryResponse response = (TransformGeometryResponse) dispatcher.execute(
				TransformGeometryRequest.COMMAND, request, null, "en");
		Geometry transformed = response.getGeometry();
		Envelope bounds = converterService.toInternal(transformed).getEnvelopeInternal();
		Assert.assertEquals(8.983152841195215E-5, bounds.getMinX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, bounds.getMinY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, bounds.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, bounds.getMaxY(), DELTA);
	}

	@Test
	public void testTransformGeometryCollection() throws Exception {
		TransformGeometryRequest request = new TransformGeometryRequest();
		WKTReader reader = new WKTReader();
		Geometry origin = converterService.toDto(reader.read("POLYGON((10 30, 20 30,20 40,10 40,10 30))"));
		List<Geometry> geometries = request.getGeometryCollection(); // assure not-null
		geometries.add(origin);
		request.setGeometryCollection(geometries); // assure setter is called/tested
		request.setSourceCrs(MERCATOR);
		request.setTargetCrs(LONLAT);
		// execute
		TransformGeometryResponse response = (TransformGeometryResponse) dispatcher.execute(
				TransformGeometryRequest.COMMAND, request, null, "en");
		List<Geometry> transformed = response.getGeometryCollection();
		Assert.assertEquals(1, transformed.size());
		Envelope bounds = converterService.toInternal(transformed.get(0)).getEnvelopeInternal();
		Assert.assertEquals(8.983152841195215E-5, bounds.getMinX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, bounds.getMinY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, bounds.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, bounds.getMaxY(), DELTA);
	}
}
