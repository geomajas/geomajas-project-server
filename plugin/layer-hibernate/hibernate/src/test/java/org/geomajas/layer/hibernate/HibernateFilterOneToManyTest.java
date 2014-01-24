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
package org.geomajas.layer.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.geomajas.layer.hibernate.pojo.HibernateTestOneToMany;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;

/**
 * TestCase for Hibernate's OneToMany association. All possible filters we can imagine on such an association must be
 * put to the test here.
 * 
 * @author Pieter De Graef
 */
public class HibernateFilterOneToManyTest extends AbstractHibernateLayerModelTest {

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		HibernateTestFeature f1 = HibernateTestFeature.getDefaultInstance1(null);
		HibernateTestFeature f2 = HibernateTestFeature.getDefaultInstance2(null);
		HibernateTestFeature f3 = HibernateTestFeature.getDefaultInstance3(null);
		HibernateTestFeature f4 = HibernateTestFeature.getDefaultInstance4(null);

		f1.addOneToMany(HibernateTestOneToMany.getDefaultInstance1(null));
		f1.addOneToMany(HibernateTestOneToMany.getDefaultInstance2(null));

		f2.addOneToMany(HibernateTestOneToMany.getDefaultInstance3(null));
		f2.addOneToMany(HibernateTestOneToMany.getDefaultInstance4(null));

		f3.addOneToMany(HibernateTestOneToMany.getDefaultInstance1(null));
		f3.addOneToMany(HibernateTestOneToMany.getDefaultInstance3(null));

		f4.addOneToMany(HibernateTestOneToMany.getDefaultInstance1(null));
		f4.addOneToMany(HibernateTestOneToMany.getDefaultInstance2(null));
		f4.addOneToMany(HibernateTestOneToMany.getDefaultInstance3(null));
		f4.addOneToMany(HibernateTestOneToMany.getDefaultInstance4(null));

		layer.create(f1);
		layer.create(f2);
		layer.create(f3);
		layer.create(f4);
	}

	/*
	 * Uses inner join: If ANY of the oneToMany objects are evaluated by the filter, then the HibernateTestFeature is
	 * returned.
	 */
	@Test
	public void betweenFilterOnInteger() throws Exception {
		Filter filter = filterCreator.createBetweenFilter(ATTR__ONE_TO_MANY__DOT__INT, "500", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void betweenFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createBetweenFilter(ATTR__ONE_TO_MANY__DOT__FLOAT, "500", "2500");
		try {
			Iterator<?> it = layer.getElements(filter, 0, 0);
			int t = 0;
			while (it.hasNext()) {
				Assert.assertTrue("Returned object must be a HibernateTestFeature",
						it.next() instanceof HibernateTestFeature);
				t++;
			}
			Assert.assertEquals(3, t);
		} catch (Exception e) {
			Assert.fail("Test resulted in error: " + e.getMessage());
		}
	}

	@Test
	public void betweenFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createBetweenFilter(ATTR__ONE_TO_MANY__DOT__DOUBLE, "500", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void ltFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__INT, "<", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void leFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__INT, "<=", "2000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void gtFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__INT, ">", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void geFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__INT, ">=", "3000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void eqFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__INT, "==", "2000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	/**
	 * Watch out here! If ANY of the oneToMany object's intAttr is not equal to 2000, it is evaluated by the filter!
	 * Logical but not really handy.....we're still looking for a way to search for features that do not contain a
	 * oneToMany equal to 2000. In other words: "if ALL of the...."
	 */
	@Test
	public void neFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__INT, "<>", "2000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void ltFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__FLOAT, "<", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void leFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__FLOAT, "<=", "2000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void gtFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__FLOAT, ">", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void geFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__FLOAT, ">=", "3000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void eqFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__FLOAT, "==", "2000");
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
	public void neFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__FLOAT, "<>", "2000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void ltFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DOUBLE, "<", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void leFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DOUBLE, "<=", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void gtFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DOUBLE, ">", "2500");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void geFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DOUBLE, ">=", "3000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void eqFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DOUBLE, "==", "2000");
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
	public void neFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DOUBLE, "<>", "2000");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void ltFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2008");
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DATE, "<", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void leFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2007");
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DATE, "<=", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void gtFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2007");
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DATE, ">", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void geFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2007");
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DATE, ">=", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void eqFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2008");
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DATE, "==", date);
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
	public void neFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2008");
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__DATE, "<>", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void eqFilterOnBoolean() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__BOOLEAN, "==", "true");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void neFilterOnBoolean() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__BOOLEAN, "<>", "true");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void eqFilterOnString() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__TEXT, "==", "oneToMany-1");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void neFilterOnString() throws Exception {
		Filter filter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__TEXT, "<>", "oneToMany-1");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void likeFilter() throws Exception {
		Filter wildCardFilter = filterCreator.createLikeFilter(ATTR__ONE_TO_MANY__DOT__TEXT, "*-1");
		Iterator<?> it = layer.getElements(wildCardFilter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);

		Filter singleCharFilter = filterCreator.createLikeFilter(ATTR__ONE_TO_MANY__DOT__TEXT, "oneToMany-?");
		it = layer.getElements(singleCharFilter, 0, 0);
		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void andFilter() throws Exception {
		Filter textFilter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__TEXT, "==", "oneToMany-1");
		Filter intFilter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__INT, "<", "2500");
		Filter filter = filterCreator.createLogicFilter(textFilter, "and", intFilter);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void orFilter() throws Exception {
		Filter textFilter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__TEXT, "==", "oneToMany-1");
		Filter intFilter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__INT, ">", "2500");
		Filter filter = filterCreator.createLogicFilter(textFilter, "or", intFilter);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	/*
	 * Interesting case: Find all the features where the textAttr="oneToMany-1" is not in the list.
	 */
	@Test
	public void notFilter() throws Exception {
		Filter textFilter = filterCreator.createCompareFilter(ATTR__ONE_TO_MANY__DOT__TEXT, "<>", "oneToMany-1");
		Filter filter = filterCreator.createLogicFilter(textFilter, "NOT", null);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a HibernateTestFeature",
					it.next() instanceof HibernateTestFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}
}
