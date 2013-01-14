/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
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

import org.geomajas.layer.LayerException;
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

	protected static final String LAYER_NAME = "inmemfiltertest";

	@Test
	public void betweenFilterOnInteger() throws LayerException {
		Filter intFilter = filterService.createBetweenFilter(PARAM_INT_ATTR, "2", "8");
		Iterator<?> it = layer.getElements(intFilter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void betweenFilterOnFloat() throws LayerException {
		Filter filter = filterService.createBetweenFilter(PARAM_FLOAT_ATTR, "2", "8");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void betweenFilterOnDouble() throws LayerException {
		Filter filter = filterService.createBetweenFilter(PARAM_DOUBLE_ATTR, "2", "8");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void compareFilterOnInteger() throws LayerException {
		Filter lt = filterService.createCompareFilter(PARAM_INT_ATTR, "<", "15");
		Iterator<?> it = layer.getElements(lt, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
		Filter ne = filterService.createCompareFilter(PARAM_INT_ATTR, "<>", "10");
		Iterator<?> it2 = layer.getElements(ne, 0, 0);

		int t2 = 0;
		while (it2.hasNext()) {
			it2.next();
			t2++;
		}
		Assert.assertEquals(7, t2);
	}

	@Test
	public void compareFilterOnFloat() throws LayerException {
		Filter lt = filterService.createCompareFilter(PARAM_FLOAT_ATTR, "<", "15");
		Iterator<?> it = layer.getElements(lt, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
		Filter ne = filterService.createCompareFilter(PARAM_FLOAT_ATTR, "<>", "10");
		it = layer.getElements(ne, 0, 0);
		t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(7, t);
	}

	@Test
	public void compareFilterOnDouble() throws LayerException {
		Filter lt = filterService.createCompareFilter(PARAM_DOUBLE_ATTR, "<", "15");
		Iterator<?> it = layer.getElements(lt, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
		Filter ne = filterService.createCompareFilter(PARAM_DOUBLE_ATTR, "<>", "10");
		it = layer.getElements(ne, 0, 0);

		t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(7, t);
	}

	@Test
	public void compareFilterOnDate() throws ParseException, LayerException {
		DateFormat format = new SimpleDateFormat("dd/mm/yyyy");
		Date d1;
		d1 = format.parse("12/12/2005");

		Filter filter = filterService.createCompareFilter(PARAM_DATE_ATTR, "<", d1);
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void compareFilterOnBoolean() throws LayerException {
		Filter eq = filterService.createCompareFilter(PARAM_BOOLEAN_ATTR, "==", "true");
		Iterator<?> it = layer.getElements(eq, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);

		Filter ne = filterService.createCompareFilter(PARAM_BOOLEAN_ATTR, "<>", "true");
		it = layer.getElements(ne, 0, 0);

		t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void compareFilterOnString() throws LayerException {
		Filter eq = filterService.createCompareFilter(PARAM_TEXT_ATTR, "==", "inside");
		Iterator<?> it = layer.getElements(eq, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);

		Filter ne = filterService.createCompareFilter(PARAM_TEXT_ATTR, "<>", "inside");
		it = layer.getElements(ne, 0, 0);

		t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(7, t);
	}

	@Test
	public void likeFilter() throws LayerException {
		Filter filter = filterService.createLikeFilter(PARAM_TEXT_ATTR, "*sid*");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void logicFilter() throws LayerException {
		Filter filter1 = filterService.createCompareFilter(PARAM_INT_ATTR, "<", "15");
		Filter filter2 = filterService.createLikeFilter(PARAM_TEXT_ATTR, "over*");
		Filter filter = filterService.createLogicFilter(filter1, "and", filter2);

		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void fidFilter() throws LayerException {
		Filter filter = filterService.createFidFilter(new String[] { LAYER_NAME + ".1", LAYER_NAME + ".2" });
		Iterator<?> it = layer.getElements(filter, 0, 0);
		SimpleFeature f = (SimpleFeature) it.next();
		f = (SimpleFeature) it.next();
		Assert.assertEquals("inside", f.getAttribute(PARAM_TEXT_ATTR));
	}

	@Test
	public void containsFilter() throws LayerException {
		SimpleFeature feature = (SimpleFeature) layer.read(LAYER_NAME+".1");
		Geometry geom = (Geometry) feature.getDefaultGeometry();
		Filter filter = filterService.createContainsFilter(geom, PARAM_GEOMETRY_ATTR);
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void withinFilter() throws LayerException {
		SimpleFeature feature = (SimpleFeature) layer.read(LAYER_NAME+".7");
		Geometry geom = (Geometry) feature.getDefaultGeometry();
		Filter filter = filterService.createWithinFilter(geom, PARAM_GEOMETRY_ATTR);
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void intersectsFilter() throws LayerException {
		SimpleFeature feature = (SimpleFeature) layer.read(LAYER_NAME+".7");
		Geometry geom = (Geometry) feature.getDefaultGeometry();
		Filter filter = filterService.createIntersectsFilter(geom, PARAM_GEOMETRY_ATTR);
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(6, t);
	}

	@Test
	public void touchesFilter() throws LayerException {
		SimpleFeature feature = (SimpleFeature) layer.read(LAYER_NAME+".7");
		Geometry geom = (Geometry) feature.getDefaultGeometry();
		Filter filter = filterService.createTouchesFilter(geom, PARAM_GEOMETRY_ATTR);
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void bboxFilter() throws LayerException {
		Envelope bbox = new Envelope(-0.4d, -0.2d, -0.3d, 0.1d);
		Filter filter = filterService.createBboxFilter("EPSG:4326", bbox, PARAM_GEOMETRY_ATTR);
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void overlapsFilter() throws LayerException {
		SimpleFeature feature = (SimpleFeature) layer.read(LAYER_NAME+".2");
		Geometry geom = (Geometry) feature.getDefaultGeometry();
		Filter filter = filterService.createOverlapsFilter(geom, PARAM_GEOMETRY_ATTR);
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}
}