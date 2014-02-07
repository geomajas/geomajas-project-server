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

package org.geomajas.internal.service;

import static org.fest.assertions.Assertions.assertThat;
import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * Test class that test conversions between JTS and DTO geometries. It specifically tests certain methods in the
 * {@link org.geomajas.service.DtoConverterService} service.
 * </p>
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/spring/moreContext.xml" })
public class GeometryConverterTest {

	private static final int SRID = 31300;

	@Autowired
	private DtoConverterService converter;

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
	public void jtsEmptyToDto() throws GeomajasException {
		Geometry p = converter.toDto(createJtsEmpty(Point.class));
		Assert.assertEquals(Geometry.POINT, p.getGeometryType());
		Assert.assertTrue(GeometryService.isEmpty(p));
		Geometry ls = converter.toDto(createJtsEmpty(LineString.class));
		Assert.assertEquals(Geometry.LINE_STRING, ls.getGeometryType());
		Assert.assertTrue(GeometryService.isEmpty(ls));
		Geometry lr = converter.toDto(createJtsEmpty(LinearRing.class));
		Assert.assertEquals(Geometry.LINEAR_RING, lr.getGeometryType());
		Assert.assertTrue(GeometryService.isEmpty(lr));
		Geometry po = converter.toDto(createJtsEmpty(Polygon.class));
		Assert.assertEquals(Geometry.POLYGON, po.getGeometryType());
		Assert.assertTrue(GeometryService.isEmpty(po));
		assertThat(po.getGeometries()).isNull();
		Geometry mp = converter.toDto(createJtsEmpty(MultiPoint.class));
		Assert.assertEquals(Geometry.MULTI_POINT, mp.getGeometryType());
		Assert.assertTrue(GeometryService.isEmpty(mp));
		Geometry mpo = converter.toDto(createJtsEmpty(MultiPolygon.class));
		Assert.assertEquals(Geometry.MULTI_POLYGON, mpo.getGeometryType());
		Assert.assertTrue(GeometryService.isEmpty(mpo));
		Geometry mls = converter.toDto(createJtsEmpty(MultiLineString.class));
		Assert.assertEquals(Geometry.MULTI_LINE_STRING, mls.getGeometryType());
		Assert.assertTrue(GeometryService.isEmpty(mls));
	}

	@Test
	public void jtsPointToDto() throws GeomajasException {
		// Test JTS Point to DTO:
		Geometry point = converter.toDto(createJtsPoint());
		Assert.assertEquals(jtsC1.x, point.getCoordinates()[0].getX());
	}

	@Test
	public void jtsLineStringToDto() throws GeomajasException {
		// Test JTS LineString to DTO:
		Geometry lineString = converter.toDto(createJtsLineString());
		Assert.assertEquals(jtsC2.x, lineString.getCoordinates()[1].getX());
	}

	@Test
	public void jtsLinearRingToDto() throws GeomajasException {
		// Test JTS LinearRing to DTO:
		Geometry linearRing = converter.toDto(createJtsLinearRing());
		Assert.assertEquals(jtsC4.x, linearRing.getCoordinates()[3].getX());
	}

	@Test
	public void jtsPolygonToDto() throws GeomajasException {
		// Test JTS Polygon to DTO:
		Geometry polygon = converter.toDto(createJtsPolygon());
		Assert.assertEquals(jtsC6.x, polygon.getGeometries()[1].getCoordinates()[1].getX());
	}

	@Test
	public void jtsMultiPointToDto() throws GeomajasException {
		// Test JTS MultiPoint to DTO:
		Geometry multiPoint = converter.toDto(createJtsMultiPoint());
		Assert.assertEquals(jtsC3.x, multiPoint.getGeometries()[2].getCoordinates()[0].getX());
	}

	@Test
	public void jtsMultiLineStringToDto() throws GeomajasException {
		// Test JTS MultiLineString to DTO:
		Geometry multiLineString = converter.toDto(createJtsMultiLineString());
		Assert.assertEquals(jtsC7.x, multiLineString.getGeometries()[1].getCoordinates()[2].getX());
	}

	@Test
	public void jtsMultiPolygonToDto() throws GeomajasException {
		// Test JTS MultiPolygon to DTO:
		Geometry multiPolygon = converter.toDto(createJtsMultiPolygon());
		Assert.assertEquals(jtsC7.x, multiPolygon.getGeometries()[1].getGeometries()[1].getCoordinates()[2].getX());
	}

	// -------------------------------------------------------------------------
	// Test geometry conversions from DTO to JTS:
	// -------------------------------------------------------------------------

	@Test
	public void dtoEmptyToJts() throws GeomajasException {
		// Test DTO Point to JTS:
		LineString ls = (LineString) converter.toInternal(createDtoEmpty(Geometry.LINE_STRING));
		Assert.assertTrue(ls.isEmpty());
		LinearRing lr = (LinearRing) converter.toInternal(createDtoEmpty(Geometry.LINEAR_RING));
		Assert.assertTrue(lr.isEmpty());
		MultiLineString mls = (MultiLineString) converter.toInternal(createDtoEmpty(Geometry.MULTI_LINE_STRING));
		Assert.assertTrue(mls.isEmpty());
		MultiPoint mp = (MultiPoint) converter.toInternal(createDtoEmpty(Geometry.MULTI_POINT));
		Assert.assertTrue(mp.isEmpty());
		MultiPolygon mpo = (MultiPolygon) converter.toInternal(createDtoEmpty(Geometry.MULTI_POLYGON));
		Assert.assertTrue(mpo.isEmpty());
		Point p = (Point) converter.toInternal(createDtoEmpty(Geometry.POINT));
		Assert.assertTrue(p.isEmpty());
		Polygon po = (Polygon) converter.toInternal(createDtoEmpty(Geometry.POLYGON));
		Assert.assertTrue(po.isEmpty());
	}

	@Test
	public void dtoPointToJts() throws GeomajasException {
		// Test DTO Point to JTS:
		Point point = (Point) converter.toInternal(createDtoPoint());
		Assert.assertEquals(dtoC1.getX(), point.getX());
	}

	@Test
	public void dtoLineStringToJts() throws GeomajasException {
		// Test DTO LineString to JTS:
		LineString lineString = (LineString) converter.toInternal(createDtoLineString());
		Assert.assertEquals(dtoC3.getX(), lineString.getCoordinateN(2).x);
	}

	@Test
	public void dtoLinearRingToJts() throws GeomajasException {
		// Test DTO LinearRing to JTS:
		LinearRing linearRing = (LinearRing) converter.toInternal(createDtoLinearRing());
		Assert.assertEquals(dtoC3.getX(), linearRing.getCoordinateN(2).x);
	}

	@Test
	public void dtoPolygonToJts() throws GeomajasException {
		Geometry shell = new Geometry(Geometry.LINEAR_RING, SRID, -1);
		shell.setCoordinates(new Coordinate[] { dtoC1, dtoC2, dtoC3, dtoC4, dtoC1 });

		Geometry hole = new Geometry(Geometry.LINEAR_RING, SRID, -1);
		hole.setCoordinates(new Coordinate[] { dtoC5, dtoC6, dtoC7, dtoC8, dtoC5 });

		Geometry polygon1 = new Geometry(Geometry.POLYGON, SRID, -1);
		polygon1.setGeometries(new Geometry[] { shell, hole });
		Geometry polygon2 = new Geometry(Geometry.POLYGON, SRID, -1);
		polygon2.setGeometries(new Geometry[] { shell });
		Geometry polygon3 = new Geometry(Geometry.POLYGON, SRID, -1);
		polygon3.setGeometries(new Geometry[] {});
		Geometry polygon4 = new Geometry(Geometry.POLYGON, SRID, -1);

		// Test DTO Polygon to JTS:
		Polygon polygon;
		polygon = (Polygon) converter.toInternal(polygon1);
		assertThat(polygon).isNotNull();
		assertThat(polygon.isEmpty()).isFalse();
		assertThat(polygon.getExteriorRing()).isNotNull();
		Assert.assertEquals(dtoC1.getX(), polygon.getExteriorRing().getCoordinateN(0).x);
		Assert.assertEquals(dtoC1.getY(), polygon.getExteriorRing().getCoordinateN(0).y);
		Assert.assertEquals(dtoC2.getX(), polygon.getExteriorRing().getCoordinateN(1).x);
		Assert.assertEquals(dtoC2.getY(), polygon.getExteriorRing().getCoordinateN(1).y);
		Assert.assertEquals(dtoC3.getX(), polygon.getExteriorRing().getCoordinateN(2).x);
		Assert.assertEquals(dtoC3.getY(), polygon.getExteriorRing().getCoordinateN(2).y);
		Assert.assertEquals(dtoC4.getX(), polygon.getExteriorRing().getCoordinateN(3).x);
		Assert.assertEquals(dtoC4.getY(), polygon.getExteriorRing().getCoordinateN(3).y);
		Assert.assertEquals(dtoC1.getX(), polygon.getExteriorRing().getCoordinateN(4).x);
		Assert.assertEquals(dtoC1.getY(), polygon.getExteriorRing().getCoordinateN(4).y);
		assertThat(polygon.getNumInteriorRing()).isEqualTo(1);
		Assert.assertEquals(dtoC5.getX(), polygon.getInteriorRingN(0).getCoordinateN(0).x);
		Assert.assertEquals(dtoC5.getY(), polygon.getInteriorRingN(0).getCoordinateN(0).y);
		Assert.assertEquals(dtoC6.getX(), polygon.getInteriorRingN(0).getCoordinateN(1).x);
		Assert.assertEquals(dtoC6.getY(), polygon.getInteriorRingN(0).getCoordinateN(1).y);
		Assert.assertEquals(dtoC7.getX(), polygon.getInteriorRingN(0).getCoordinateN(2).x);
		Assert.assertEquals(dtoC7.getY(), polygon.getInteriorRingN(0).getCoordinateN(2).y);
		Assert.assertEquals(dtoC8.getX(), polygon.getInteriorRingN(0).getCoordinateN(3).x);
		Assert.assertEquals(dtoC8.getY(), polygon.getInteriorRingN(0).getCoordinateN(3).y);
		Assert.assertEquals(dtoC5.getX(), polygon.getInteriorRingN(0).getCoordinateN(4).x);
		Assert.assertEquals(dtoC5.getY(), polygon.getInteriorRingN(0).getCoordinateN(4).y);

		polygon = (Polygon) converter.toInternal(polygon2);
		assertThat(polygon).isNotNull();
		assertThat(polygon.isEmpty()).isFalse();
		assertThat(polygon.getExteriorRing()).isNotNull();
		Assert.assertEquals(dtoC1.getX(), polygon.getExteriorRing().getCoordinateN(0).x);
		Assert.assertEquals(dtoC1.getY(), polygon.getExteriorRing().getCoordinateN(0).y);
		Assert.assertEquals(dtoC2.getX(), polygon.getExteriorRing().getCoordinateN(1).x);
		Assert.assertEquals(dtoC2.getY(), polygon.getExteriorRing().getCoordinateN(1).y);
		Assert.assertEquals(dtoC3.getX(), polygon.getExteriorRing().getCoordinateN(2).x);
		Assert.assertEquals(dtoC3.getY(), polygon.getExteriorRing().getCoordinateN(2).y);
		Assert.assertEquals(dtoC4.getX(), polygon.getExteriorRing().getCoordinateN(3).x);
		Assert.assertEquals(dtoC4.getY(), polygon.getExteriorRing().getCoordinateN(3).y);
		Assert.assertEquals(dtoC1.getX(), polygon.getExteriorRing().getCoordinateN(4).x);
		Assert.assertEquals(dtoC1.getY(), polygon.getExteriorRing().getCoordinateN(4).y);
		assertThat(polygon.getNumInteriorRing()).isEqualTo(0);

		polygon = (Polygon) converter.toInternal(polygon3);
		assertThat(polygon).isNotNull();
		assertThat(polygon.isEmpty()).isTrue();
		assertThat(polygon.getExteriorRing()).isNotNull();
		assertThat(polygon.getNumInteriorRing()).isEqualTo(0);

		polygon = (Polygon) converter.toInternal(polygon4);
		assertThat(polygon).isNotNull();
		assertThat(polygon.isEmpty()).isTrue();
		assertThat(polygon.getExteriorRing()).isNotNull();
		assertThat(polygon.getNumInteriorRing()).isEqualTo(0);
	}

	@Test
	public void dtoMultiPointToJts() throws GeomajasException {
		// Test DTO MultiPoint to JTS:
		MultiPoint multiPoint = (MultiPoint) converter.toInternal(createDtoMultiPoint());
		Assert.assertEquals(dtoC2.getX(), multiPoint.getGeometryN(1).getCoordinate().x);
	}

	@Test
	public void dtoMultiLineStringToJts() throws GeomajasException {
		// Test DTO MultiLineString to JTS:
		MultiLineString multiLineString = (MultiLineString) converter.toInternal(createDtoMultiLineString());
		Assert.assertEquals(dtoC7.getX(), multiLineString.getGeometryN(1).getCoordinates()[2].x);
	}

	@Test
	public void dtoMultiPolygonToJts() throws GeomajasException {
		// Test DTO MultiPolygon to JTS:
		MultiPolygon multiPolygon = (MultiPolygon) converter.toInternal(createDtoMultiPolygon());
		Polygon polygon = (Polygon) multiPolygon.getGeometryN(1);
		Assert.assertEquals(dtoC6.getX(), polygon.getInteriorRingN(0).getCoordinateN(1).x);
	}

	// -------------------------------------------------------------------------
	// Private methods for creating JTS geometries:
	// -------------------------------------------------------------------------

	private com.vividsolutions.jts.geom.Geometry createJtsEmpty(Class<?> clazz) {
		if (Point.class.equals(clazz)) {
			return factory.createPoint((com.vividsolutions.jts.geom.Coordinate) null);
		} else if (LineString.class.equals(clazz)) {
			return factory.createLineString((com.vividsolutions.jts.geom.Coordinate[]) null);
		} else if (LinearRing.class.equals(clazz)) {
			return factory.createLinearRing((com.vividsolutions.jts.geom.Coordinate[]) null);
		} else if (Polygon.class.equals(clazz)) {
			return factory.createPolygon(null, null);
		} else if (MultiPoint.class.equals(clazz)) {
			return factory.createMultiPoint((Point[]) null);
		} else if (MultiLineString.class.equals(clazz)) {
			return factory.createMultiLineString((LineString[]) null);
		} else if (MultiPolygon.class.equals(clazz)) {
			return factory.createMultiPolygon((Polygon[]) null);
		} else {
			return null;
		}
	}

	private Point createJtsPoint() {
		return factory.createPoint(jtsC1);
	}

	private LineString createJtsLineString() {
		return factory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] { jtsC1, jtsC2, jtsC3, jtsC4 });
	}

	private LinearRing createJtsLinearRing() {
		return factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] { jtsC1, jtsC2, jtsC3, jtsC4,
				jtsC1 });
	}

	private Polygon createJtsPolygon() {
		LinearRing shell = factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] { jtsC1, jtsC2, jtsC3,
				jtsC4, jtsC1 });
		LinearRing hole = factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] { jtsC5, jtsC6, jtsC7,
				jtsC8, jtsC5 });
		return factory.createPolygon(shell, new LinearRing[] { hole });
	}

	private MultiPoint createJtsMultiPoint() {
		return factory.createMultiPoint(new com.vividsolutions.jts.geom.Coordinate[] { jtsC1, jtsC2, jtsC3 });
	}

	private MultiLineString createJtsMultiLineString() {
		LineString l1 = factory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] { jtsC1, jtsC2, jtsC3,
				jtsC4 });
		LineString l2 = factory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] { jtsC5, jtsC6, jtsC7,
				jtsC8 });
		return factory.createMultiLineString(new LineString[] { l1, l2 });
	}

	private MultiPolygon createJtsMultiPolygon() {
		LinearRing shell = factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] { jtsC1, jtsC2, jtsC3,
				jtsC4, jtsC1 });
		LinearRing hole = factory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] { jtsC5, jtsC6, jtsC7,
				jtsC8, jtsC5 });
		Polygon polygon1 = factory.createPolygon(shell, new LinearRing[] {});
		Polygon polygon2 = factory.createPolygon(shell, new LinearRing[] { hole });
		return factory.createMultiPolygon(new Polygon[] { polygon1, polygon2 });
	}

	// -------------------------------------------------------------------------
	// Private methods for creating DTO geometries:
	// -------------------------------------------------------------------------

	private Geometry createDtoEmpty(String geometryType) {
		Geometry geometry = new Geometry(geometryType, SRID, -1);
		return geometry;
	}

	private Geometry createDtoPoint() {
		Geometry geometry = new Geometry(Geometry.POINT, SRID, -1);
		geometry.setCoordinates(new Coordinate[] { dtoC1 });
		return geometry;
	}

	private Geometry createDtoLineString() {
		Geometry geometry = new Geometry(Geometry.LINE_STRING, SRID, -1);
		geometry.setCoordinates(new Coordinate[] { dtoC1, dtoC2, dtoC3, dtoC4 });
		return geometry;
	}

	private Geometry createDtoLinearRing() {
		Geometry geometry = new Geometry(Geometry.LINEAR_RING, SRID, -1);
		geometry.setCoordinates(new Coordinate[] { dtoC1, dtoC2, dtoC3, dtoC4, dtoC1 });
		return geometry;
	}

	private Geometry createDtoPolygon() {
		Geometry shell = new Geometry(Geometry.LINEAR_RING, SRID, -1);
		shell.setCoordinates(new Coordinate[] { dtoC1, dtoC2, dtoC3, dtoC4, dtoC1 });

		Geometry hole = new Geometry(Geometry.LINEAR_RING, SRID, -1);
		hole.setCoordinates(new Coordinate[] { dtoC5, dtoC6, dtoC7, dtoC8, dtoC5 });

		Geometry geometry = new Geometry("Polygon", SRID, -1);
		geometry.setGeometries(new Geometry[] { shell, hole });
		return geometry;
	}

	private Geometry createDtoMultiPoint() {
		Geometry point1 = new Geometry(Geometry.POINT, SRID, -1);
		point1.setCoordinates(new Coordinate[] { dtoC1 });

		Geometry point2 = new Geometry(Geometry.POINT, SRID, -1);
		point2.setCoordinates(new Coordinate[] { dtoC2 });

		Geometry geometry = new Geometry(Geometry.MULTI_POINT, SRID, -1);
		geometry.setGeometries(new Geometry[] { point1, point2 });
		return geometry;
	}

	private Geometry createDtoMultiLineString() {
		Geometry lineString1 = new Geometry(Geometry.LINE_STRING, SRID, -1);
		lineString1.setCoordinates(new Coordinate[] { dtoC1, dtoC2, dtoC3, dtoC4 });

		Geometry lineString2 = new Geometry(Geometry.LINE_STRING, SRID, -1);
		lineString2.setCoordinates(new Coordinate[] { dtoC5, dtoC6, dtoC7, dtoC8 });

		Geometry geometry = new Geometry(Geometry.MULTI_LINE_STRING, SRID, -1);
		geometry.setGeometries(new Geometry[] { lineString1, lineString2 });
		return geometry;
	}

	private Geometry createDtoMultiPolygon() {
		Geometry geometry = new Geometry(Geometry.MULTI_POLYGON, SRID, -1);
		geometry.setGeometries(new Geometry[] { createDtoPolygon(), createDtoPolygon() });
		return geometry;
	}
}
