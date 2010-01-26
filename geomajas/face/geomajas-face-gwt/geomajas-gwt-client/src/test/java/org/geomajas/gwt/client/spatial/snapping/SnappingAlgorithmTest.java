package org.geomajas.gwt.client.spatial.snapping;

import com.vividsolutions.jts.util.Assert;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Test case that tests the different <code>SnappingAlgorithm</code> implementations.
 * </p>
 *
 * @author Pieter De Graef
 */
public class SnappingAlgorithmTest {

	private List<Geometry> geometries = new ArrayList<Geometry>();

	private double ruleDistance = 5;

	/**
	 * Constructor that sets up an initial list of geometries to snap to.
	 */
	public SnappingAlgorithmTest() {
		GeometryFactory factory = new GeometryFactory(4326, -1);
		LinearRing shell1 = factory.createLinearRing(new Coordinate[] {new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0), new Coordinate(10.0, 20.0),
				new Coordinate(10.0, 10.0)});
		LinearRing hole1 = factory.createLinearRing(new Coordinate[] {new Coordinate(12.0, 12.0),
				new Coordinate(18.0, 12.0), new Coordinate(18.0, 18.0), new Coordinate(12.0, 18.0),
				new Coordinate(12.0, 12.0)});
		geometries.add(factory.createPolygon(shell1, new LinearRing[] {hole1}));
		LinearRing shell2 = factory.createLinearRing(new Coordinate[] {new Coordinate(5.0, 5.0),
				new Coordinate(15.0, 5.0), new Coordinate(15.0, 25.0), new Coordinate(5.0, 25.0)});
		geometries.add(factory.createPolygon(shell2, null));
	}

	@Test
	public void testClosestPoint() {
		SnappingAlgorithm algorithm = new ClosestPointAlgorithm(geometries, ruleDistance);
		Coordinate snapped = algorithm.getSnappingPoint(new Coordinate(16, 16), Double.MAX_VALUE);
		Assert.equals(18.0, snapped.getX());
		Assert.equals(18.0, snapped.getY());
		snapped = algorithm.getSnappingPoint(new Coordinate(7, 7.5), Double.MAX_VALUE);
		Assert.equals(5.0, snapped.getX());
		Assert.equals(5.0, snapped.getY());
	}

	@Test
	public void testNearest() {
		SnappingAlgorithm algorithm = new NearestAlgorithm(geometries, ruleDistance);
		Coordinate snapped = algorithm.getSnappingPoint(new Coordinate(16, 16), Double.MAX_VALUE);
		Assert.equals(15.0, snapped.getX());
		Assert.equals(16.0, snapped.getY());
		snapped = algorithm.getSnappingPoint(new Coordinate(14, 18.9), Double.MAX_VALUE);
		Assert.equals(14.0, snapped.getX());
		Assert.equals(18.0, snapped.getY());
	}
}
