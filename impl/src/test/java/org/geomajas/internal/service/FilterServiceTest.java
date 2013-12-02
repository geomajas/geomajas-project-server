package org.geomajas.internal.service;

import java.util.Stack;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.service.FilterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.PropertyName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * <p>
 * Test class for {@link org.geomajas.service.FilterService} service.
 * </p>
 * 
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/spring/moreContext.xml" })
public class FilterServiceTest {

	@Autowired
	private FilterService filterService;

	private WKTReader wkt = new WKTReader();

	@Test
	public void testAndFilter() throws GeomajasException {
		Filter left = filterService.parseFilter("a='3'");
		Filter right = filterService.parseFilter("b='4'");
		Filter and = filterService.createAndFilter(left, right);
		// test both true
		TestFeature f = new TestFeature();
		f.expectAndReturn("a", "3");
		f.expectAndReturn("b", "4");
		Assert.assertTrue(and.evaluate(f));
		// test left false
		f.clear();
		f.expectAndReturn("a", "2");
		f.expectAndReturn("b", "4");
		Assert.assertFalse(and.evaluate(f));
		// test right false
		f.clear();
		f.expectAndReturn("a", "3");
		f.expectAndReturn("b", "5");
		Assert.assertFalse(and.evaluate(f));
		// test both false
		f.clear();
		f.expectAndReturn("a", "2");
		f.expectAndReturn("b", "5");
		Assert.assertFalse(and.evaluate(f));
	}

	@Test
	public void testBboxFilter() throws GeomajasException, ParseException {
		Filter bbox = filterService.createBboxFilter("EPSG:4326", new Envelope(0, 1, 0, 1), "geometry");
		TestFeature f = new TestFeature();
		f.expectAndReturn("geometry", wkt.read("POINT(0.5 0.5)"));
		Assert.assertTrue(bbox.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", wkt.read("POINT(1.0001 1.0001)"));
		Assert.assertFalse(bbox.evaluate(f));
	}

	@Test
	public void testBetweenFilter() throws GeomajasException {
		Filter between = filterService.createBetweenFilter("a", "1", "10");
		TestFeature f = new TestFeature();
		f.expectAndReturn("a", 1.56);
		Assert.assertTrue(between.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 0.5);
		Assert.assertFalse(between.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 11);
		Assert.assertFalse(between.evaluate(f));
	}

	@Test
	public void testComparatorFilter() throws GeomajasException {
		Filter lt = filterService.createCompareFilter("a", "<", "10");
		TestFeature f = new TestFeature();
		f.expectAndReturn("a", 9);
		Assert.assertTrue(lt.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 11);
		Assert.assertFalse(lt.evaluate(f));

		Filter lte = filterService.createCompareFilter("a", "<=", "10");
		f.clear();
		f.expectAndReturn("a", 9);
		Assert.assertTrue(lte.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 10);
		Assert.assertTrue(lte.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 11);
		Assert.assertFalse(lte.evaluate(f));

		Filter gt = filterService.createCompareFilter("a", ">", "10");
		f.clear();
		f.expectAndReturn("a", 9);
		Assert.assertFalse(gt.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 11);
		Assert.assertTrue(gt.evaluate(f));

		Filter gte = filterService.createCompareFilter("a", ">=", "10");
		f.clear();
		f.expectAndReturn("a", 9);
		Assert.assertFalse(gte.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 10);
		Assert.assertTrue(gte.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 11);
		Assert.assertTrue(gte.evaluate(f));

		Filter ne = filterService.createCompareFilter("a", "<>", "10");
		f.clear();
		f.expectAndReturn("a", 9);
		Assert.assertTrue(ne.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 10);
		Assert.assertFalse(ne.evaluate(f));
		f.clear();
		f.expectAndReturn("a", 11);
		Assert.assertTrue(ne.evaluate(f));
	}

	@Test
	public void testContainsFilter() throws GeomajasException, ParseException {
		Polygon poly1 = (Polygon) wkt.read("POLYGON((0 0,1 0,1 1,0 1,0 0))");
		Polygon touching = (Polygon) wkt.read("POLYGON((1 1,2 1,2 2,1 2,1 1))");
		Polygon disjoint = (Polygon) wkt.read("POLYGON((2 2,3 2,3 3,2 3,2 2))");
		Polygon overlapping = (Polygon) wkt.read("POLYGON((0.5 0.5,1.5 0.5,1.5 1.5,0.5 1.5,0.5 0.5))");
		Polygon within = (Polygon) wkt.read("POLYGON((0.1 0.1,0.9 0.1,0.9 0.9,0.1 0.9,0.1 0.1))");
		Polygon contains = (Polygon) wkt.read("POLYGON((-0.1 -0.1,1.1 -0.1,1.1 1.1,-0.1 1.1,-0.1 -0.1))");
		Filter filter = filterService.createContainsFilter(poly1, "geometry");
		TestFeature f = new TestFeature();
		f.expectAndReturn("geometry", touching);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", disjoint);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", overlapping);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", within);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", contains);
		Assert.assertTrue(filter.evaluate(f));
	}

	@Test
	public void testFidFilter() throws GeomajasException {
		Filter fid = filterService.createFidFilter(new String[] { "1" });
		TestFeature f = new TestFeature();
		f.expectAndReturn("@id", "1");
		Assert.assertTrue(fid.evaluate(f));
	}

	@Test
	public void testGeometryTypeFilter() throws GeomajasException, ParseException {
		Polygon poly = (Polygon) wkt.read("POLYGON((0 0,1 0,1 1,0 1,0 0))");
		Filter fid = filterService.createGeometryTypeFilter("geometry", "Polygon");
		TestFeature f = new TestFeature();
		f.expectAndReturn("geometry", poly);
		Assert.assertTrue(fid.evaluate(f));
	}

	@Test
	public void testDefaultGeometryTypeFilter() throws GeomajasException, ParseException {
		Polygon poly = (Polygon) wkt.read("POLYGON((0 0,1 0,1 1,0 1,0 0))");
		Filter isPoly = filterService.createGeometryTypeFilter("", "Polygon");
		TestFeature f = new TestFeature();
		f.expectAndReturn("", poly);
		Assert.assertTrue(isPoly.evaluate(f));
		Filter isPoint = filterService.createGeometryTypeFilter("", "Point");
		f.expectAndReturn("", poly);
		Assert.assertFalse(isPoint.evaluate(f));
	}

	@Test
	public void testLikeFilter() throws GeomajasException, ParseException {
		Filter fid = filterService.createLikeFilter("a", "a*b");
		TestFeature f = new TestFeature();
		f.expectAndReturn("a", "adgxgxggb");
		Assert.assertTrue(fid.evaluate(f));
		f.expectAndReturn("a", "ac");
		Assert.assertFalse(fid.evaluate(f));
		f.expectAndReturn("a", "a123b");
		Assert.assertTrue(fid.evaluate(f));
		fid = filterService.createLikeFilter("a", "12??A*f");
		f.expectAndReturn("a", "1234Af");
		Assert.assertTrue(fid.evaluate(f));
		f.expectAndReturn("a", "12345Af");
		Assert.assertFalse(fid.evaluate(f));
		f.expectAndReturn("a", "12cdAkhkgkf");
		Assert.assertTrue(fid.evaluate(f));
	}

	@Test
	public void testIntersectsFilter() throws GeomajasException, ParseException {
		Polygon poly1 = (Polygon) wkt.read("POLYGON((0 0,1 0,1 1,0 1,0 0))");
		Polygon touching = (Polygon) wkt.read("POLYGON((1 1,2 1,2 2,1 2,1 1))");
		Polygon disjoint = (Polygon) wkt.read("POLYGON((2 2,3 2,3 3,2 3,2 2))");
		Polygon overlapping = (Polygon) wkt.read("POLYGON((0.5 0.5,1.5 0.5,1.5 1.5,0.5 1.5,0.5 0.5))");
		Polygon within = (Polygon) wkt.read("POLYGON((0.1 0.1,0.9 0.1,0.9 0.9,0.1 0.9,0.1 0.1))");
		Polygon contains = (Polygon) wkt.read("POLYGON((-0.1 -0.1,1.1 -0.1,1.1 1.1,-0.1 1.1,-0.1 -0.1))");
		Filter filter = filterService.createIntersectsFilter(poly1, "geometry");
		TestFeature f = new TestFeature();
		f.expectAndReturn("geometry", touching);
		Assert.assertTrue(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", disjoint);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", overlapping);
		Assert.assertTrue(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", within);
		Assert.assertTrue(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", contains);
		Assert.assertTrue(filter.evaluate(f));
	}

	@Test
	public void testOverlapsFilter() throws GeomajasException, ParseException {
		Polygon poly1 = (Polygon) wkt.read("POLYGON((0 0,1 0,1 1,0 1,0 0))");
		Polygon touching = (Polygon) wkt.read("POLYGON((1 1,2 1,2 2,1 2,1 1))");
		Polygon disjoint = (Polygon) wkt.read("POLYGON((2 2,3 2,3 3,2 3,2 2))");
		Polygon overlapping = (Polygon) wkt.read("POLYGON((0.5 0.5,1.5 0.5,1.5 1.5,0.5 1.5,0.5 0.5))");
		Polygon within = (Polygon) wkt.read("POLYGON((0.1 0.1,0.9 0.1,0.9 0.9,0.1 0.9,0.1 0.1))");
		Polygon contains = (Polygon) wkt.read("POLYGON((-0.1 -0.1,1.1 -0.1,1.1 1.1,-0.1 1.1,-0.1 -0.1))");
		Filter filter = filterService.createOverlapsFilter(poly1, "geometry");
		TestFeature f = new TestFeature();
		f.expectAndReturn("geometry", touching);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", disjoint);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", overlapping);
		Assert.assertTrue(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", within);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", contains);
		Assert.assertFalse(filter.evaluate(f));
	}

	@Test
	public void testTouchesFilter() throws GeomajasException, ParseException {
		Polygon poly1 = (Polygon) wkt.read("POLYGON((0 0,1 0,1 1,0 1,0 0))");
		Polygon touching = (Polygon) wkt.read("POLYGON((1 1,2 1,2 2,1 2,1 1))");
		Polygon disjoint = (Polygon) wkt.read("POLYGON((2 2,3 2,3 3,2 3,2 2))");
		Polygon overlapping = (Polygon) wkt.read("POLYGON((0.5 0.5,1.5 0.5,1.5 1.5,0.5 1.5,0.5 0.5))");
		Polygon within = (Polygon) wkt.read("POLYGON((0.1 0.1,0.9 0.1,0.9 0.9,0.1 0.9,0.1 0.1))");
		Polygon contains = (Polygon) wkt.read("POLYGON((-0.1 -0.1,1.1 -0.1,1.1 1.1,-0.1 1.1,-0.1 -0.1))");
		Filter filter = filterService.createTouchesFilter(poly1, "geometry");
		TestFeature f = new TestFeature();
		f.expectAndReturn("geometry", touching);
		Assert.assertTrue(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", disjoint);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", overlapping);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", within);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", contains);
		Assert.assertFalse(filter.evaluate(f));
	}

	@Test
	public void testWithinFilter() throws GeomajasException, ParseException {
		Polygon poly1 = (Polygon) wkt.read("POLYGON((0 0,1 0,1 1,0 1,0 0))");
		Polygon touching = (Polygon) wkt.read("POLYGON((1 1,2 1,2 2,1 2,1 1))");
		Polygon disjoint = (Polygon) wkt.read("POLYGON((2 2,3 2,3 3,2 3,2 2))");
		Polygon overlapping = (Polygon) wkt.read("POLYGON((0.5 0.5,1.5 0.5,1.5 1.5,0.5 1.5,0.5 0.5))");
		Polygon within = (Polygon) wkt.read("POLYGON((0.1 0.1,0.9 0.1,0.9 0.9,0.1 0.9,0.1 0.1))");
		Polygon contains = (Polygon) wkt.read("POLYGON((-0.1 -0.1,1.1 -0.1,1.1 1.1,-0.1 1.1,-0.1 -0.1))");
		Filter filter = filterService.createWithinFilter(poly1, "geometry");
		TestFeature f = new TestFeature();
		f.expectAndReturn("geometry", touching);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", disjoint);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", overlapping);
		Assert.assertFalse(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", within);
		Assert.assertTrue(filter.evaluate(f));
		f.clear();
		f.expectAndReturn("geometry", contains);
		Assert.assertFalse(filter.evaluate(f));
	}
	
	@Test
	public void testParseFilter() throws GeomajasException {
		Filter filter = filterService.parseFilter("a.b = 1 or a.b = 2 or a.b = 2");
		Filter exclude = filterService.parseFilter("EXCLUDE");
	}

	@Test
	public void testParseFidFilter() throws GeomajasException {
		Filter f1 = filterService.parseFilter("IN( 1 )");
		Filter f2 = filterService.parseFilter("a.@id = 1");
		Assert.assertTrue(f1 instanceof Id);
		Assert.assertTrue(f2 instanceof PropertyIsEqualTo);
		PropertyIsEqualTo piet = (PropertyIsEqualTo)f2;
		Assert.assertTrue(piet.getExpression1() instanceof PropertyName);
		Assert.assertEquals("a/@id",((PropertyName)piet.getExpression1()).getPropertyName());
		
	}

	/**
	 * Test feature that expects properties being accessed in a certain order.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class TestFeature implements TestPropertyAccess {

		private Stack<PathAndValue> expected = new Stack<PathAndValue>();

		public Object get(String xpath, Class target) {
			PathAndValue pav = expected.pop();
			// fail if wrong property was accessed
			Assert.assertEquals(pav.path, xpath);
			return pav.value;
		}

		/**
		 * Expect the property with the given path to be accessed and return the specified value.
		 * 
		 * @param path
		 * @param value
		 */
		public void expectAndReturn(String path, Object value) {
			PathAndValue pav = new PathAndValue();
			pav.path = path;
			pav.value = value;
			expected.add(0, pav);
		}

		public void clear() {
			expected.clear();
		}

		class PathAndValue {

			public String path;

			public Object value;
		}

	}

}
