package org.geomajas.internal.service;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import junit.framework.Assert;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.service.GeometryConverter;
import org.junit.Test;

/**
 * <p>
 * Test class that test conversions between JTS and DTO geometries. It specifically tests the {@link org.geomajas.internal.service.GeometryConverterImpl}
 * class, which executes these tests.
 * </p>
 *
 * @author Pieter De Graef
 */
public class GeometryConverterTest {

	private static final int SRID = 31300;

	private GeometryConverter geometryConverter;

	private GeometryFactory factory;

	private com.vividsolutions.jts.geom.Coordinate jtsC1;

	private com.vividsolutions.jts.geom.Coordinate jtsC2;

	private com.vividsolutions.jts.geom.Coordinate jtsC3;

	private com.vividsolutions.jts.geom.Coordinate jtsC4;

	private com.vividsolutions.jts.geom.Coordinate jtsC5;

	private com.vividsolutions.jts.geom.Coordinate jtsC6;

	private com.vividsolutions.jts.geom.Coordinate jtsC7;

	private com.vividsolutions.jts.geom.Coordinate jtsC8;

	private Coordinate dtoC1;

	private Coordinate dtoC2;

	private Coordinate dtoC3;

	private Coordinate dtoC4;

	private Coordinate dtoC5;

	private Coordinate dtoC6;

	private Coordinate dtoC7;

	private Coordinate dtoC8;

	// -------------------------------------------------------------------------
	// Constructor, initializes all variables:
	// -------------------------------------------------------------------------

	public GeometryConverterTest() {
		geometryConverter = new GeometryConverterImpl();
		factory = new GeometryFactory(new PrecisionModel(), SRID);
		jtsC1 = new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0);
		jtsC2 = new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0);
		jtsC3 = new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0);
		jtsC4 = new com.vividsolutions.jts.geom.Coordinate(10.0, 20.0);
		jtsC5 = new com.vividsolutions.jts.geom.Coordinate(12.0, 12.0);
		jtsC6 = new com.vividsolutions.jts.geom.Coordinate(12.0, 18.0);
		jtsC7 = new com.vividsolutions.jts.geom.Coordinate(18.0, 18.0);
		jtsC8 = new com.vividsolutions.jts.geom.Coordinate(18.0, 12.0);

		dtoC1 = new Coordinate(10.0, 10.0);
		dtoC2 = new Coordinate(20.0, 10.0);
		dtoC3 = new Coordinate(20.0, 20.0);
		dtoC4 = new Coordinate(10.0, 20.0);
		dtoC5 = new Coordinate(12.0, 12.0);
		dtoC6 = new Coordinate(12.0, 18.0);
		dtoC7 = new Coordinate(18.0, 18.0);
		dtoC8 = new Coordinate(18.0, 12.0);
	}

	// -------------------------------------------------------------------------
	// Test geometry conversions from JTS to DTO:
	// -------------------------------------------------------------------------

	@Test
	public void jtsPointToDto() {
		// Test JTS Point to DTO:
		Geometry point = geometryConverter.toDto(createJtsPoint());
		Assert.assertEquals(jtsC1.x, point.getCoordinates()[0].getX());
	}

	@Test
	public void jtsLineStringToDto() {
		// Test JTS LineString to DTO:
		Geometry lineString = geometryConverter.toDto(createJtsLineString());
		Assert.assertEquals(jtsC2.x, lineString.getCoordinates()[1].getX());
	}

	@Test
	public void jtsLinearRingToDto() {
		// Test JTS LinearRing to DTO:
		Geometry linearRing = geometryConverter.toDto(createJtsLinearRing());
		Assert.assertEquals(jtsC4.x, linearRing.getCoordinates()[3].getX());
	}

	@Test
	public void jtsPolygonToDto() {
		// Test JTS Polygon to DTO:
		Geometry polygon = geometryConverter.toDto(createJtsPolygon());
		Assert.assertEquals(jtsC6.x, polygon.getGeometries()[1].getCoordinates()[1].getX());
	}

	@Test
	public void jtsMultiPointToDto() {
		// Test JTS MultiPoint to DTO:
		Geometry multiPoint = geometryConverter.toDto(createJtsMultiPoint());
		Assert.assertEquals(jtsC3.x, multiPoint.getGeometries()[2].getCoordinates()[0].getX());
	}

	@Test
	public void jtsMultiLineStringToDto() {
		// Test JTS MultiLineString to DTO:
		Geometry multiLineString = geometryConverter.toDto(createJtsMultiLineString());
		Assert.assertEquals(jtsC7.x, multiLineString.getGeometries()[1].getCoordinates()[2].getX());
	}

	@Test
	public void jtsMultiPolygonToDto() {
		// Test JTS MultiPolygon to DTO:
		Geometry multiPolygon = geometryConverter.toDto(createJtsMultiPolygon());
		Assert.assertEquals(jtsC7.x, multiPolygon.getGeometries()[1].getGeometries()[1].getCoordinates()[2].getX());
	}

	// -------------------------------------------------------------------------
	// Test geometry conversions from DTO to JTS:
	// -------------------------------------------------------------------------

	@Test
	public void dtoPointToJts() {
		// Test DTO Point to JTS:
		Point point = (Point) geometryConverter.toJts(createDtoPoint());
		Assert.assertEquals(dtoC1.getX(), point.getX());
	}

	@Test
	public void dtoLineStringToJts() {
		// Test DTO LineString to JTS:
		LineString lineString = (LineString) geometryConverter.toJts(createDtoLineString());
		Assert.assertEquals(dtoC3.getX(), lineString.getCoordinateN(2).x);
	}

	@Test
	public void dtoLinearRingToJts() {
		// Test DTO LinearRing to JTS:
		LinearRing linearRing = (LinearRing) geometryConverter.toJts(createDtoLinearRing());
		Assert.assertEquals(dtoC3.getX(), linearRing.getCoordinateN(2).x);
	}

	@Test
	public void dtoPolygonToJts() {
		// Test DTO Polygon to JTS:
		Polygon polygon = (Polygon) geometryConverter.toJts(createDtoPolygon());
		Assert.assertEquals(dtoC6.getX(), polygon.getInteriorRingN(0).getCoordinateN(1).x);
	}

	@Test
	public void dtoMultiPointToJts() {
		// Test DTO MultiPoint to JTS:
		MultiPoint multiPoint = (MultiPoint) geometryConverter.toJts(createDtoMultiPoint());
		Assert.assertEquals(dtoC2.getX(), multiPoint.getGeometryN(1).getCoordinate().x);
	}

	@Test
	public void dtoMultiLineStringToJts() {
		// Test DTO MultiLineString to JTS:
		MultiLineString multiLineString = (MultiLineString) geometryConverter.toJts(createDtoMultiLineString());
		Assert.assertEquals(dtoC7.getX(), multiLineString.getGeometryN(1).getCoordinates()[2].x);
	}

	@Test
	public void dtoMultiPolygonToJts() {
		// Test DTO MultiPolygon to JTS:
		MultiPolygon multiPolygon = (MultiPolygon) geometryConverter.toJts(createDtoMultiPolygon());
		Polygon polygon = (Polygon) multiPolygon.getGeometryN(1);
		Assert.assertEquals(dtoC6.getX(), polygon.getInteriorRingN(0).getCoordinateN(1).x);
	}

	// -------------------------------------------------------------------------
	// Private methods for creating JTS geometries:
	// -------------------------------------------------------------------------

	private Point createJtsPoint() {
		return factory.createPoint(jtsC1);
	}

	private LineString createJtsLineString() {
		return factory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {jtsC1, jtsC2, jtsC3, jtsC4});
	}

	private LinearRing createJtsLinearRing() {
		return factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {jtsC1, jtsC2, jtsC3, jtsC4,
				jtsC1});
	}

	private Polygon createJtsPolygon() {
		LinearRing shell = factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {jtsC1, jtsC2, jtsC3,
				jtsC4, jtsC1});
		LinearRing hole = factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {jtsC5, jtsC6, jtsC7,
				jtsC8, jtsC5});
		return factory.createPolygon(shell, new LinearRing[] {hole});
	}

	private MultiPoint createJtsMultiPoint() {
		return factory.createMultiPoint(new com.vividsolutions.jts.geom.Coordinate[] {jtsC1, jtsC2, jtsC3});
	}

	private MultiLineString createJtsMultiLineString() {
		LineString l1 = factory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {jtsC1, jtsC2, jtsC3,
				jtsC4});
		LineString l2 = factory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {jtsC5, jtsC6, jtsC7,
				jtsC8});
		return factory.createMultiLineString(new LineString[] {l1, l2});
	}

	private MultiPolygon createJtsMultiPolygon() {
		LinearRing shell = factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {jtsC1, jtsC2, jtsC3,
				jtsC4, jtsC1});
		LinearRing hole = factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {jtsC5, jtsC6, jtsC7,
				jtsC8, jtsC5});
		Polygon polygon1 = factory.createPolygon(shell, new LinearRing[] {});
		Polygon polygon2 = factory.createPolygon(shell, new LinearRing[] {hole});
		return factory.createMultiPolygon(new Polygon[] {polygon1, polygon2});
	}

	// -------------------------------------------------------------------------
	// Private methods for creating DTO geometries:
	// -------------------------------------------------------------------------

	private Geometry createDtoPoint() {
		Geometry geometry = new Geometry("Point", SRID, -1);
		geometry.setCoordinates(new Coordinate[] {dtoC1});
		return geometry;
	}

	private Geometry createDtoLineString() {
		Geometry geometry = new Geometry("LineString", SRID, -1);
		geometry.setCoordinates(new Coordinate[] {dtoC1, dtoC2, dtoC3, dtoC4});
		return geometry;
	}

	private Geometry createDtoLinearRing() {
		Geometry geometry = new Geometry("LinearRing", SRID, -1);
		geometry.setCoordinates(new Coordinate[] {dtoC1, dtoC2, dtoC3, dtoC4, dtoC1});
		return geometry;
	}

	private Geometry createDtoPolygon() {
		Geometry shell = new Geometry("LinearRing", SRID, -1);
		shell.setCoordinates(new Coordinate[] {dtoC1, dtoC2, dtoC3, dtoC4, dtoC1});

		Geometry hole = new Geometry("LinearRing", SRID, -1);
		hole.setCoordinates(new Coordinate[] {dtoC5, dtoC6, dtoC7, dtoC8, dtoC5});

		Geometry geometry = new Geometry("Polygon", SRID, -1);
		geometry.setGeometries(new Geometry[] {shell, hole});
		return geometry;
	}

	private Geometry createDtoMultiPoint() {
		Geometry point1 = new Geometry("Point", SRID, -1);
		point1.setCoordinates(new Coordinate[] {dtoC1});

		Geometry point2 = new Geometry("Point", SRID, -1);
		point2.setCoordinates(new Coordinate[] {dtoC2});

		Geometry geometry = new Geometry("MultiPoint", SRID, -1);
		geometry.setGeometries(new Geometry[] {point1, point2});
		return geometry;
	}

	private Geometry createDtoMultiLineString() {
		Geometry lineString1 = new Geometry("LineString", SRID, -1);
		lineString1.setCoordinates(new Coordinate[] {dtoC1, dtoC2, dtoC3, dtoC4});

		Geometry lineString2 = new Geometry("LineString", SRID, -1);
		lineString2.setCoordinates(new Coordinate[] {dtoC5, dtoC6, dtoC7, dtoC8});

		Geometry geometry = new Geometry("MultiLineString", SRID, -1);
		geometry.setGeometries(new Geometry[] {lineString1, lineString2});
		return geometry;
	}

	private Geometry createDtoMultiPolygon() {
		Geometry geometry = new Geometry("MultiPolygon", SRID, -1);
		geometry.setGeometries(new Geometry[] {createDtoPolygon(), createDtoPolygon()});
		return geometry;
	}
}
