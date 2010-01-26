package org.geomajas.gwt.client.map.feature.operation;

import com.vividsolutions.jts.util.Assert;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.junit.Test;

/**
 * Test case for the {@link AddCoordinateOp} feature operation class.
 *
 * @author Pieter De Graef
 */
public class RemoveCoordinateTest {

	private final static int SRID = 4326;

	private final static int PRECISION = -1;

	private LineString lineString;

	private MultiLineString multiLineString;

	private Polygon polygon;

	private MultiPolygon multiPolygon;

	private TransactionGeomIndex index;

	private FeatureOperation op;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public RemoveCoordinateTest() {
		GeometryFactory factory = new GeometryFactory(SRID, PRECISION);
		lineString = factory.createLineString(new Coordinate[] {new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0), new Coordinate(30.0, 30.0)});
		multiLineString = factory.createMultiLineString(new LineString[] {(LineString) lineString.clone()});

		LinearRing exteriorRing = factory.createLinearRing(new Coordinate[] {new Coordinate(0.0, 0.0),
				new Coordinate(100.0, 0.0), new Coordinate(100.0, 100.0), new Coordinate(0.0, 100.0)});
		LinearRing interiorRing1 = factory.createLinearRing(new Coordinate[] {new Coordinate(20.0, 20.0),
				new Coordinate(40.0, 20.0), new Coordinate(40.0, 40.0), new Coordinate(20.0, 40.0)});
		LinearRing interiorRing2 = factory.createLinearRing(new Coordinate[] {new Coordinate(60.0, 60.0),
				new Coordinate(80.0, 60.0), new Coordinate(80.0, 80.0), new Coordinate(60.0, 80.0)});
		polygon = factory.createPolygon(exteriorRing, new LinearRing[] {interiorRing1, interiorRing2});
		multiPolygon = factory.createMultiPolygon(new Polygon[] {(Polygon) polygon.clone()});

		index = new TransactionGeomIndex();
		index.setCoordinateIndex(2);
		index.setGeometryIndex(0);
		index.setInteriorRingIndex(0);
		index.setCoordinateIndex(2);

		op = new RemoveCoordinateOp(index);
	}

	@Test
	public void testLineString() {
		Feature feature = new Feature();
		feature.setGeometry((LineString) lineString.clone());
		op.execute(feature);
		LineString l = (LineString) feature.getGeometry();
		Assert.equals(30.0, l.getCoordinateN(index.getCoordinateIndex()).getX());
		System.out.println(feature.getGeometry().toWkt());
		op.undo(feature);
		System.out.println(feature.getGeometry().toWkt());
		Assert.equals(lineString.toWkt(), feature.getGeometry().toWkt());
	}

	@Test
	public void testMultiLineString() {
		Feature feature = new Feature();
		feature.setGeometry((MultiLineString) multiLineString.clone());
		op.execute(feature);
		MultiLineString m = (MultiLineString) feature.getGeometry();
		LineString l = (LineString) m.getGeometryN(index.getGeometryIndex());
		Assert.equals(30.0, l.getCoordinateN(index.getCoordinateIndex()).getX());
		op.undo(feature);
		Assert.equals(multiLineString.toWkt(), feature.getGeometry().toWkt());
	}

	@Test
	public void testPolygon() {
		Feature feature = new Feature();
		feature.setGeometry((Polygon) polygon.clone());
		op.execute(feature);
		Polygon p = (Polygon) feature.getGeometry();
		LinearRing r = p.getInteriorRingN(index.getInteriorRingIndex());
		Assert.equals(20.0, r.getCoordinateN(index.getCoordinateIndex()).getX());
		op.undo(feature);
		Assert.equals(polygon.toWkt(), feature.getGeometry().toWkt());
	}

	@Test
	public void testMultiPolygon() {
		Feature feature = new Feature();
		feature.setGeometry((MultiPolygon) multiPolygon.clone());
		op.execute(feature);
		MultiPolygon m = (MultiPolygon) feature.getGeometry();
		Polygon p = (Polygon) m.getGeometryN(index.getGeometryIndex());
		LinearRing r = p.getInteriorRingN(index.getInteriorRingIndex());
		Assert.equals(20.0, r.getCoordinateN(index.getCoordinateIndex()).getX());
		op.undo(feature);
		Assert.equals(multiPolygon.toWkt(), feature.getGeometry().toWkt());
	}
}
