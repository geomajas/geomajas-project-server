/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.hibernate.association;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.hibernate.AbstractHibernateAssociationTest;
import org.geomajas.layer.hibernate.association.pojo.AssociationFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;

/**
 * TestCase for Hibernate's ManyToOne association. All possible filters we can imagine on such an association must be
 * put to the test here.
 * 
 * @author Pieter De Graef
 */
public class FilterManyToOneTest extends AbstractHibernateAssociationTest {

	static boolean initialised = false;
	
	private AssociationFeature f1;
	private AssociationFeature f2;
	private AssociationFeature f3;
	private AssociationFeature f4;
	

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		f1 = (AssociationFeature)layer.create(AssociationFeature.getDefaultInstance1(null));
		f2 = (AssociationFeature)layer.create(AssociationFeature.getDefaultInstance2(null));
		f3 = (AssociationFeature)layer.create(AssociationFeature.getDefaultInstance3(null));
		f4 = (AssociationFeature)layer.create(AssociationFeature.getDefaultInstance4(null));
	}
	
	@Test
	public void eqFilterOnId() throws Exception {
		Filter filter = filterCreator.parseFilter(MTO_ID + "= " + f1.getManyToOne().getId());
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void betweenFilterOnInteger() throws Exception {
		Filter filter = filterCreator.createBetweenFilter(MTO_INT, "50", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void betweenFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createBetweenFilter(MTO_FLOAT, "50", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void betweenFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createBetweenFilter(MTO_DOUBLE, "50", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void ltFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_INT, "<", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void leFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_INT, "<=", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void gtFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_INT, ">", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void geFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_INT, ">=", "300");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void eqFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_INT, "==", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void neFilterOnInt() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_INT, "<>", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void ltFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_FLOAT, "<", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void leFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_FLOAT, "<=", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void gtFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_FLOAT, ">", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void geFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_FLOAT, ">=", "300");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void eqFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_FLOAT, "==", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void neFilterOnFloat() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_FLOAT, "<>", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void ltFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_DOUBLE, "<", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void leFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_DOUBLE, "<=", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void gtFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_DOUBLE, ">", "250");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void geFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_DOUBLE, ">=", "300");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void eqFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_DOUBLE, "==", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void neFilterOnDouble() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_DOUBLE, "<>", "200");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void ltFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2008");
		Filter filter = filterCreator.createCompareFilter(MTO_DATE, "<", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void leFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2007");
		Filter filter = filterCreator.createCompareFilter(MTO_DATE, "<=", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void gtFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2007");
		Filter filter = filterCreator.createCompareFilter(MTO_DATE, ">", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void geFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2007");
		Filter filter = filterCreator.createCompareFilter(MTO_DATE, ">=", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void eqFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2008");
		Filter filter = filterCreator.createCompareFilter(MTO_DATE, "==", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void neFilterOnDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2008");
		Filter filter = filterCreator.createCompareFilter(MTO_DATE, "<>", date);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void eqFilterOnBoolean() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_BOOLEAN, "==", "true");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void neFilterOnBoolean() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_BOOLEAN, "<>", "true");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void eqFilterOnString() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_TEXT, "==", "manyToOne-1");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void neFilterOnString() throws Exception {
		Filter filter = filterCreator.createCompareFilter(MTO_TEXT, "<>", "manyToOne-1");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void likeFilter() throws Exception {
		Filter wildCardFilter = filterCreator.createLikeFilter(MTO_TEXT, "*-1");
		Iterator<?> it = layer.getElements(wildCardFilter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(1, t);

		Filter singleCharFilter = filterCreator.createLikeFilter(MTO_TEXT, "manyToOne-?");
		it = layer.getElements(singleCharFilter, 0, 0);
		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void andFilter() throws Exception {
		Filter textFilter = filterCreator.createCompareFilter(MTO_TEXT, "==", "manyToOne-1");
		Filter intFilter = filterCreator.createCompareFilter(MTO_INT, "<", "250");
		Filter filter = filterCreator.createLogicFilter(textFilter, "and", intFilter);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void orFilter() throws Exception {
		Filter textFilter = filterCreator.createCompareFilter(MTO_TEXT, "==", "manyToOne-1");
		Filter intFilter = filterCreator.createCompareFilter(MTO_INT, ">", "250");
		Filter filter = filterCreator.createLogicFilter(textFilter, "or", intFilter);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void notFilter() throws Exception {
		Filter textFilter = filterCreator.createCompareFilter(MTO_TEXT, "==", "manyToOne-1");
		Filter filter = filterCreator.createLogicFilter(textFilter, "NOT", null);
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue("Returned object must be a AssociationFeature", it.next() instanceof AssociationFeature);
			t++;
		}
		Assert.assertEquals(3, t);
	}
	
	
}