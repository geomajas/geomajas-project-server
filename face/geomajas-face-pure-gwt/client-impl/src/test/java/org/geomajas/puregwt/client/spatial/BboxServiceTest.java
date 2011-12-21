/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.spatial;

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.GeomajasTestModule;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests the Bbox methods.
 * 
 * @author Pieter De Graef
 */
public class BboxServiceTest {

	private Bbox empty = new Bbox(0, 0, 0, 0);

	private Bbox origin = new Bbox(0, 0, 10, 10);

	private Bbox movedEmpty = new Bbox(-10, -10, 0, 0);

	private Bbox movedEmpty2 = new Bbox(5, 5, 0, 0);

	private Bbox normal = new Bbox(-5, -5, 20, 20);

	private BboxService service;

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new GeomajasTestModule());
		service = myInjector.getInstance(BboxService.class);

		empty = new Bbox(0, 0, 0, 0);
		origin = new Bbox(0, 0, 10, 10);
		movedEmpty = new Bbox(-10, -10, 0, 0);
		movedEmpty2 = new Bbox(5, 5, 0, 0);
		normal = new Bbox(-5, -5, 20, 20);
	}

	@Test
	public void testGuice() {
		Assert.assertNotNull(service);
	}

	@Test
	public void testEquals() {
		Assert.assertTrue(service.equals(empty, empty, 0.0001));
		Assert.assertFalse(service.equals(normal, empty, 0.0001));
	}

	@Test
	public void testGetCenterPoint() {
		Coordinate c = service.getCenterPoint(empty);
		Assert.assertEquals(c.getX(), 0.0);
		Assert.assertEquals(c.getY(), 0.0);

		c = service.getCenterPoint(origin);
		Assert.assertEquals(c.getX(), 5.0);
		Assert.assertEquals(c.getY(), 5.0);

		c = service.getCenterPoint(movedEmpty);
		Assert.assertEquals(c.getX(), -10.0);
		Assert.assertEquals(c.getY(), -10.0);

		c = service.getCenterPoint(normal);
		Assert.assertEquals(c.getX(), 5.0);
		Assert.assertEquals(c.getY(), 5.0);
	}

	@Test
	public void testUnion() {
		// Result should equal empty:
		Bbox union = service.union(empty, empty);
		Assert.assertTrue(service.equals(union, empty, 0.0001));

		// Result should equal origin:
		union = service.union(empty, origin);
		Assert.assertTrue(service.equals(union, origin, 0.0001));

		// Result should equal origin:
		union = service.union(origin, empty);
		Assert.assertTrue(service.equals(union, origin, 0.0001));

		// Result should equal movedEmpty:
		union = service.union(movedEmpty, empty);
		Assert.assertTrue(service.equals(union, movedEmpty, 0.0001));

		// Result should equal movedEmpty:
		union = service.union(empty, movedEmpty);
		Assert.assertTrue(service.equals(union, movedEmpty, 0.0001));

		union = service.union(movedEmpty, origin);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 20.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 20.0);

		union = service.union(origin, movedEmpty);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 20.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 20.0);

		union = service.union(movedEmpty, movedEmpty2);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 15.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 15.0);

		union = service.union(movedEmpty, normal);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 25.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 25.0);
	}

	@Test
	public void testBuffer() {
		Bbox buffer = service.buffer(empty, 10);
		Assert.assertEquals(buffer.getX(), -10.0);
		Assert.assertEquals(buffer.getWidth(), 20.0);
		Assert.assertEquals(buffer.getY(), -10.0);
		Assert.assertEquals(buffer.getHeight(), 20.0);
	}

	@Test
	public void testIntersection() {
		Bbox intersection = service.intersection(empty, empty);
		Assert.assertTrue(service.equals(intersection, empty, 0.0001));

		intersection = service.intersection(empty, origin);
		Assert.assertTrue(service.equals(intersection, empty, 0.0001));

		intersection = service.intersection(origin, empty);
		Assert.assertTrue(service.equals(intersection, empty, 0.0001));

		intersection = service.intersection(empty, movedEmpty);
		Assert.assertNull(intersection);

		intersection = service.intersection(movedEmpty, empty);
		Assert.assertNull(intersection);

		intersection = service.intersection(normal, origin);
		Assert.assertEquals(intersection.getX(), 0.0);
		Assert.assertEquals(intersection.getWidth(), 10.0);
		Assert.assertEquals(intersection.getY(), 0.0);
		Assert.assertEquals(intersection.getHeight(), 10.0);
	}

	@Test
	public void testIntersects() {
		Assert.assertTrue(service.intersects(empty, empty));
		Assert.assertTrue(service.intersects(empty, origin));
		Assert.assertTrue(service.intersects(origin, empty));
		Assert.assertFalse(service.intersects(empty, movedEmpty));
		Assert.assertFalse(service.intersects(movedEmpty, empty));
		Assert.assertTrue(service.intersects(normal, origin));
	}

	@Test
	public void testContains() {
		Assert.assertTrue(service.contains(normal, empty));
		Assert.assertTrue(service.contains(normal, origin));
		Assert.assertTrue(service.contains(normal, normal));
		Assert.assertFalse(service.contains(origin, normal));
	}

	@Test
	public void testScale() {
		Bbox scaled = service.scale(normal, 2);
		Assert.assertEquals(service.getCenterPoint(normal).getX(), service.getCenterPoint(scaled).getX());
		Assert.assertEquals(service.getCenterPoint(normal).getY(), service.getCenterPoint(scaled).getY());
		Assert.assertEquals(scaled.getX(), -15.0);
		Assert.assertEquals(scaled.getWidth(), 40.0);
		Assert.assertEquals(scaled.getY(), -15.0);
		Assert.assertEquals(scaled.getHeight(), 40.0);
	}

	@Test
	public void testIsEmpty() {
		Assert.assertTrue(service.isEmpty(empty));
		Assert.assertFalse(service.isEmpty(normal));
	}
}