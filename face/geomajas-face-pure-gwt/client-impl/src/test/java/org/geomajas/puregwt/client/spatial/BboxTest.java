package org.geomajas.puregwt.client.spatial;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests the Bbox methods.
 * 
 * @author Pieter De Graef
 */
public class BboxTest {

	private Bbox empty = new BboxImpl(0, 0, 0, 0);

	private Bbox origin = new BboxImpl(0, 0, 10, 10);

	private Bbox movedEmpty = new BboxImpl(-10, -10, 0, 0);

	private Bbox movedEmpty2 = new BboxImpl(5, 5, 0, 0);

	private Bbox normal = new BboxImpl(-5, -5, 20, 20);

	private GeometryFactory factory;

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new TestConfigurationModule());
		factory = myInjector.getInstance(GeometryFactory.class);
		empty = factory.createBbox(0, 0, 0, 0);

		origin = factory.createBbox(0, 0, 10, 10);

		movedEmpty = factory.createBbox(-10, -10, 0, 0);

		movedEmpty2 = factory.createBbox(5, 5, 0, 0);

		normal = factory.createBbox(-5, -5, 20, 20);
	}

	@Test
	public void testGuice() {
		Assert.assertNotNull(factory);
	}

	@Test
	public void testGetCenterPoint() {
		Coordinate c = empty.getCenterPoint();
		Assert.assertEquals(c.getX(), 0.0);
		Assert.assertEquals(c.getY(), 0.0);

		c = origin.getCenterPoint();
		Assert.assertEquals(c.getX(), 5.0);
		Assert.assertEquals(c.getY(), 5.0);

		c = movedEmpty.getCenterPoint();
		Assert.assertEquals(c.getX(), -10.0);
		Assert.assertEquals(c.getY(), -10.0);

		c = normal.getCenterPoint();
		Assert.assertEquals(c.getX(), 5.0);
		Assert.assertEquals(c.getY(), 5.0);
	}

	@Test
	public void testUnion() {
		// Result should equal empty:
		Bbox union = empty.union(empty);
		Assert.assertTrue(union.equals(empty));

		// Result should equal origin:
		union = empty.union(origin);
		Assert.assertTrue(union.equals(origin));

		// Result should equal origin:
		union = origin.union(empty);
		Assert.assertTrue(union.equals(origin));

		// Result should equal movedEmpty:
		union = movedEmpty.union(empty);
		Assert.assertTrue(union.equals(movedEmpty));

		// Result should equal movedEmpty:
		union = empty.union(movedEmpty);
		Assert.assertTrue(union.equals(movedEmpty));

		union = movedEmpty.union(origin);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 20.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 20.0);

		union = origin.union(movedEmpty);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 20.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 20.0);

		union = movedEmpty.union(movedEmpty2);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 15.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 15.0);

		union = movedEmpty.union(normal);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 25.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 25.0);
	}

	@Test
	public void testBuffer() {
		Bbox buffer = empty.buffer(10);
		Assert.assertEquals(buffer.getX(), -10.0);
		Assert.assertEquals(buffer.getWidth(), 20.0);
		Assert.assertEquals(buffer.getY(), -10.0);
		Assert.assertEquals(buffer.getHeight(), 20.0);
	}

	@Test
	public void testIntersection() {
		Bbox intersection = empty.intersection(empty);
		Assert.assertTrue(intersection.equals(empty));

		intersection = empty.intersection(origin);
		Assert.assertTrue(intersection.equals(empty));

		intersection = origin.intersection(empty);
		Assert.assertTrue(intersection.equals(empty));

		intersection = empty.intersection(movedEmpty);
		Assert.assertNull(intersection);

		intersection = movedEmpty.intersection(empty);
		Assert.assertNull(intersection);

		intersection = normal.intersection(origin);
		Assert.assertEquals(intersection.getX(), 0.0);
		Assert.assertEquals(intersection.getWidth(), 10.0);
		Assert.assertEquals(intersection.getY(), 0.0);
		Assert.assertEquals(intersection.getHeight(), 10.0);
	}

	@Test
	public void intersects() {
		Assert.assertTrue(empty.intersects(empty));
		Assert.assertTrue(empty.intersects(origin));
		Assert.assertTrue(origin.intersects(empty));
		Assert.assertFalse(empty.intersects(movedEmpty));
		Assert.assertFalse(movedEmpty.intersects(empty));
		Assert.assertTrue(normal.intersects(origin));
	}

	@Test
	public void contains() {
		Assert.assertTrue(normal.intersects(empty));
	}
}
