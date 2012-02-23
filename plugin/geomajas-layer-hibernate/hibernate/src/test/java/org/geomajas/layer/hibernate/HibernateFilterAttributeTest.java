/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * Unit test that tests filters for the HibernateLayer.
 * 
 * @author Pieter De Graef
 */
public class HibernateFilterAttributeTest extends AbstractHibernateLayerModelTest {

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		layer.create(HibernateTestFeature.getDefaultInstance1(null));
		layer.create(HibernateTestFeature.getDefaultInstance2(null));
		layer.create(HibernateTestFeature.getDefaultInstance3(null));
		layer.create(HibernateTestFeature.getDefaultInstance4(null));
	}

	@Test
	public void betweenFilterOnInteger() throws Exception {
		Filter intFilter = filterCreator.createBetweenFilter(PARAM_INT_ATTR, "5", "25");
		Iterator<?> it = layer.getElements(intFilter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void betweenFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createBetweenFilter(PARAM_FLOAT_ATTR, "5.0f", "25.0f");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void betweenFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createBetweenFilter(PARAM_DOUBLE_ATTR, "5.0", "25.0");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void compareFilterOnInteger() throws Exception {
		Filter lt = filterCreator.createCompareFilter(PARAM_INT_ATTR, "<", "35");
		Iterator<?> it = layer.getElements(lt, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
		Filter ne = filterCreator.createCompareFilter(PARAM_INT_ATTR, "<>", "10");
		it = layer.getElements(ne, 0, 0);

		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void compareFilterOnFloat() throws Exception {
		Filter lt = filterCreator.createCompareFilter(PARAM_FLOAT_ATTR, "<", "35");
		Iterator<?> it = layer.getElements(lt, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
		Filter ne = filterCreator.createCompareFilter(PARAM_FLOAT_ATTR, "<>", "10");
		it = layer.getElements(ne, 0, 0);

		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void compareFilterOnDouble() throws Exception {
		Filter lt = filterCreator.createCompareFilter(PARAM_DOUBLE_ATTR, "<", "35");
		Iterator<?> it = layer.getElements(lt, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
		Filter ne = filterCreator.createCompareFilter(PARAM_DOUBLE_ATTR, "<>", "10");
		it = layer.getElements(ne, 0, 0);

		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void compareFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/mm/yyyy");
		Date d1;
		d1 = format.parse("12/12/2007");

		Filter filter = filterCreator.createCompareFilter(PARAM_DATE_ATTR, "<", d1);
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void compareFilterOnBoolean() throws Exception {
		Filter eq = filterCreator.createCompareFilter(PARAM_BOOLEAN_ATTR, "==", "true");
		Iterator<?> it = layer.getElements(eq, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(2, t);

		Filter ne = filterCreator.createCompareFilter(PARAM_BOOLEAN_ATTR, "<>", "true");
		it = layer.getElements(ne, 0, 0);

		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void compareFilterOnString() throws Exception {
		Filter eq = filterCreator.createCompareFilter(PARAM_TEXT_ATTR, "==", "default-name-1");
		Iterator<?> it = layer.getElements(eq, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(1, t);

		Filter ne = filterCreator.createCompareFilter(PARAM_TEXT_ATTR, "<>", "default-name-1");
		it = layer.getElements(ne, 0, 0);

		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void likeFilter() throws Exception {
		Filter filter = filterCreator.createLikeFilter(PARAM_TEXT_ATTR, "*name-_");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
		// test case-insensitive (http://jira.geomajas.org/browse/HIB-33)
		filter = filterCreator.createLikeFilter(PARAM_TEXT_ATTR, "*NAME-_");
		it = layer.getElements(filter, 0, 0);

		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void logicFilter() throws Exception {
		Filter filter1 = filterCreator.createCompareFilter(PARAM_INT_ATTR, "<", "15");
		Filter filter2 = filterCreator.createLikeFilter(PARAM_TEXT_ATTR, "default*");
		Filter filter = filterCreator.createLogicFilter(filter1, "and", filter2);

		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void fidFilter() throws Exception {
		Iterator<?> it = layer.getElements(filterCreator.createTrueFilter(), 0, 0);
		// iterate and check if they can be fetched with the fid filter
		while (it.hasNext()) {
			HibernateTestFeature expected = (HibernateTestFeature) it.next();
			Filter filter = filterCreator.createFidFilter(new String[] { layer.getFeatureModel().getId(expected) });
			Iterator<?> it2 = layer.getElements(filter, 0, 0);
			Assert.assertTrue("FidFilter should return an iterator with 1 feature.", it2.hasNext());
			HibernateTestFeature actual = (HibernateTestFeature) it2.next();
			Assert.assertEquals(expected, actual);
		}
	}

	@Test
	public void nullFilter() throws Exception {
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
		Filter filter = ff.isNull(ff.property(PARAM_DATE_ATTR));
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	/**
	 * @todo tests to write
	 * @Test public void containsFilter() { }
	 * @Test public void withinFilter() { }
	 * @Test public void intersectsFilter() { }
	 * @Test public void touchesFilter() { }
	 * @Test public void overlapsFilter() { }
	 * @Test public void bboxFilter() { }
	 */
}
