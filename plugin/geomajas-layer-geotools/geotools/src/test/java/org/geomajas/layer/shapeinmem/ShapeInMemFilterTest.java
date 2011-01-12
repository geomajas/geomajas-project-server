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
package org.geomajas.layer.shapeinmem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Test for filters in the shape-in-memory layer.
 *
 * @author Pieter De Graef
 */
public class ShapeInMemFilterTest extends AbstractFilterTest {

	@Test
	public void betweenFilterOnInteger() {
		Filter intFilter = filterService.createBetweenFilter(PARAM_INT_ATTR, "2", "8");
		try {
			Iterator<?> it = layer.getElements(intFilter, 0, 0);
			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(2, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void betweenFilterOnFloat() {
		Filter filter = filterService.createBetweenFilter(PARAM_FLOAT_ATTR, "2", "8");
		try {
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(2, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void betweenFilterOnDouble() {
		Filter filter = filterService.createBetweenFilter(PARAM_DOUBLE_ATTR, "2", "8");
		try {
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(2, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnInteger() {
		Filter lt = filterService.createCompareFilter(PARAM_INT_ATTR, "<", "15");
		try {
			Iterator<?> it = layer.getElements(lt, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
		Filter ne = filterService.createCompareFilter(PARAM_INT_ATTR, "<>", "10");
		try {
			Iterator<?> it = layer.getElements(ne, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(7, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnFloat() {
		Filter lt = filterService.createCompareFilter(PARAM_FLOAT_ATTR, "<", "15");
		try {
			Iterator<?> it = layer.getElements(lt, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
		Filter ne = filterService.createCompareFilter(PARAM_FLOAT_ATTR, "<>", "10");
		try {
			Iterator<?> it = layer.getElements(ne, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(7, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnDouble() {
		Filter lt = filterService.createCompareFilter(PARAM_DOUBLE_ATTR, "<", "15");
		try {
			Iterator<?> it = layer.getElements(lt, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
		Filter ne = filterService.createCompareFilter(PARAM_DOUBLE_ATTR, "<>", "10");
		try {
			Iterator<?> it = layer.getElements(ne, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(7, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnDate() {
		DateFormat format = new SimpleDateFormat("dd/mm/yyyy");
		Date d1;
		try {
			d1 = format.parse("12/12/2005");
		} catch (ParseException e1) {
			d1 = new Date();
		}

		Filter filter = filterService.createCompareFilter(PARAM_DATE_ATTR, "<", d1);
		try {
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnBoolean() {
		Filter eq = filterService.createCompareFilter(PARAM_BOOLEAN_ATTR, "==", "true");
		try {
			Iterator<?> it = layer.getElements(eq, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}

		Filter ne = filterService.createCompareFilter(PARAM_BOOLEAN_ATTR, "<>", "true");
		try {
			Iterator<?> it = layer.getElements(ne, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnString() {
		Filter eq = filterService.createCompareFilter(PARAM_TEXT_ATTR, "==", "inside");
		try {
			Iterator<?> it = layer.getElements(eq, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(1, t);
		} catch (Exception e) {
		}

		Filter ne = filterService.createCompareFilter(PARAM_TEXT_ATTR, "<>", "inside");
		try {
			Iterator<?> it = layer.getElements(ne, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(7, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void likeFilter() {
		Filter filter = filterService.createLikeFilter(PARAM_TEXT_ATTR, "*sid*");
		try {
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(2, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void logicFilter() {
		Filter filter1 = filterService.createCompareFilter(PARAM_INT_ATTR, "<", "15");
		Filter filter2 = filterService.createLikeFilter(PARAM_TEXT_ATTR, "over*");
		Filter filter = filterService.createLogicFilter(filter1, "and", filter2);

		try {
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(1, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void fidFilter() {
		Filter filter = filterService.createFidFilter(new String[] { "1", "2" });
		try {
			Iterator<?> it = layer.getElements(filter, 0, 0);
			SimpleFeature f = (SimpleFeature) it.next();
			f = (SimpleFeature) it.next();
			Assert.assertEquals("inside", f.getAttribute(PARAM_TEXT_ATTR));
		} catch (Exception e) {
		}
	}

	@Test
	public void containsFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layer.read("1");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterService.createContainsFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(3, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void withinFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layer.read("7");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterService.createWithinFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void intersectsFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layer.read("7");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterService.createIntersectsFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(6, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void touchesFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layer.read("7");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterService.createTouchesFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(1, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void bboxFilter() {
		try {
			Envelope bbox = new Envelope(-0.4d, -0.2d, -0.3d, 0.1d);
			Filter filter = filterService.createBboxFilter("EPSG:4326", bbox, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(3, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void overlapsFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layer.read("2");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterService.createOverlapsFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layer.getElements(filter, 0, 0);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(1, t);
		} catch (Exception e) {
		}
	}
}