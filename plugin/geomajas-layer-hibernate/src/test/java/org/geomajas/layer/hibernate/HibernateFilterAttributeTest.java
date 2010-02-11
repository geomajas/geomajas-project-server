/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.layer.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;

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
		Assert.assertEquals(2, t);
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
			HibernateTestFeature expected = (HibernateTestFeature)it.next();
			Filter filter = filterCreator.createFidFilter(new String[] {layer.getFeatureModel().getId(expected)});
			Iterator<?> it2 = layer.getElements(filter, 0, 0);
			Assert.assertTrue("FidFilter should return an iterator with 1 feature.", it2.hasNext());
			HibernateTestFeature actual = (HibernateTestFeature) it2.next();
			Assert.assertEquals(expected, actual);
		}		
	}

	@Test
	public void containsFilter() {
	}

	@Test
	public void withinFilter() {
	}

	@Test
	public void intersectsFilter() {
	}

	@Test
	public void touchesFilter() {
	}

	@Test
	public void overlapsFilter() {
	}

	@Test
	public void bboxFilter() {
	}
}
