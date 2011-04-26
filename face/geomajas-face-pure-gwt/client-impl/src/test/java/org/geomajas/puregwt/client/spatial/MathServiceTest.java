package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the MathService.
 * 
 * @author Pieter De Graef
 */
public class MathServiceTest {

	private static final double DELTA = 0.0001;

	private Coordinate c1 = new Coordinate(0, 0);

	private Coordinate c2 = new Coordinate(10, 20);

	private Coordinate c3 = new Coordinate(-10, -20);

	private Coordinate c4 = new Coordinate(-20, 10);

	private Coordinate c5 = new Coordinate(20, 10);

	private MathService service = new MathServiceImpl();

	@Test
	public void testDistanceTwoPoint() {
		Assert.assertEquals(40.0, service.distance(c4, c5), DELTA);
		Assert.assertEquals(5.0, service.distance(new Coordinate(3, 4), new Coordinate(0, 0)), DELTA);
		try {
			service.distance(null, null);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testDistancePointLine() {
		Assert.assertEquals(0.0, service.distance(c2, c3, c1), DELTA);
		Assert.assertEquals(10.0, service.distance(c4, c5, c1), DELTA);
		Assert.assertEquals(Math.sqrt(15 * 15 + 15 * 15), service.distance(c2, c5, c1), DELTA);
		try {
			service.distance(c1, c2, null);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testIntersects() {
		Assert.assertFalse(service.intersectsLineSegment(c1, c2, c3, c4));
		Assert.assertTrue(service.intersectsLineSegment(c2, c3, c4, c5));
		try {
			service.intersectsLineSegment(c1, c2, null, c4);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testIntersection() {
		Coordinate intersection = service.lineSegmentIntersection(c2, c3, c4, c5);
		Assert.assertEquals(5.0, intersection.getX(), DELTA);
		Assert.assertEquals(10.0, intersection.getY(), DELTA);

		Assert.assertNull(service.lineSegmentIntersection(c1, c2, c3, c4));
		try {
			service.lineSegmentIntersection(c1, null, c3, c4);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testNearest() {
		Coordinate nearest = service.nearest(c2, c3, new Coordinate(-20, 10));
		Assert.assertEquals(0.0, nearest.getX(), DELTA);
		Assert.assertEquals(0.0, nearest.getY(), DELTA);

		nearest = service.nearest(c2, c3, new Coordinate(100, 10));
		Assert.assertEquals(c2.getX(), nearest.getX(), DELTA);
		Assert.assertEquals(c2.getY(), nearest.getY(), DELTA);

		nearest = service.nearest(c2, c3, new Coordinate(-100, -10));
		Assert.assertEquals(c3.getX(), nearest.getX(), DELTA);
		Assert.assertEquals(c3.getY(), nearest.getY(), DELTA);
	}

	@Test
	public void testIsWithin() {
		GeometryFactory factory = new GeometryFactoryImpl();
		Point point = factory.createPoint(new Coordinate(0, 0));
		Assert.assertFalse(service.isWithin(point, new Coordinate(0, 0)));

		LinearRing ring = factory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0),
				new Coordinate(10, 10), new Coordinate(0, 10), new Coordinate(0, 0) });
		Assert.assertTrue(service.isWithin(ring, new Coordinate(8, 1)));
		Assert.assertFalse(service.isWithin(ring, new Coordinate(8, 11)));
		Assert.assertFalse(service.isWithin(ring, new Coordinate(5, 0)));

		LinearRing hole = factory.createLinearRing(new Coordinate[] { new Coordinate(0, -5), new Coordinate(5, 0),
				new Coordinate(0, 5), new Coordinate(-5, 0), new Coordinate(0, -5) });
		Polygon polygon = factory.createPolygon(ring, new LinearRing[] { hole });
		Assert.assertTrue(service.isWithin(polygon, new Coordinate(6, 6)));
		Assert.assertFalse(service.isWithin(polygon, new Coordinate(1, 2)));
		Assert.assertFalse(service.isWithin(polygon, new Coordinate(2.5, 2.5)));

		MultiPolygon mp = factory.createMultiPolygon(new Polygon[] { polygon });
		Assert.assertTrue(service.isWithin(mp, new Coordinate(6, 6)));
		Assert.assertFalse(service.isWithin(mp, new Coordinate(1, 2)));
		Assert.assertFalse(service.isWithin(mp, new Coordinate(2.5, 2.5)));
	}

	@Test
	public void testTouches() {
		GeometryFactory factory = new GeometryFactoryImpl();
		Point point = factory.createPoint(new Coordinate(0, 0));
		Assert.assertTrue(service.touches(point, new Coordinate(0, 0)));

		LinearRing ring = factory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0),
				new Coordinate(10, 10), new Coordinate(0, 10), new Coordinate(0, 0) });
		Assert.assertFalse(service.touches(ring, new Coordinate(8, 1)));
		Assert.assertFalse(service.touches(ring, new Coordinate(8, 11)));
		Assert.assertTrue(service.touches(ring, new Coordinate(5, 0)));

		LinearRing hole = factory.createLinearRing(new Coordinate[] { new Coordinate(0, -5), new Coordinate(5, 0),
				new Coordinate(0, 5), new Coordinate(-5, 0), new Coordinate(0, -5) });
		Polygon polygon = factory.createPolygon(ring, new LinearRing[] { hole });
		Assert.assertFalse(service.touches(polygon, new Coordinate(6, 6)));
		Assert.assertFalse(service.touches(polygon, new Coordinate(1, 2)));
		Assert.assertTrue(service.touches(polygon, new Coordinate(2.5, 2.5)));

		MultiPolygon mp = factory.createMultiPolygon(new Polygon[] { polygon });
		Assert.assertFalse(service.touches(mp, new Coordinate(6, 6)));
		Assert.assertFalse(service.touches(mp, new Coordinate(1, 2)));
		Assert.assertTrue(service.touches(mp, new Coordinate(2.5, 2.5)));
	}
}