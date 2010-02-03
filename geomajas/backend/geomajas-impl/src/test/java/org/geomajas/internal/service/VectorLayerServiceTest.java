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

package org.geomajas.internal.service;

import com.vividsolutions.jts.geom.Envelope;
import junit.framework.Assert;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for VectorLayerService.
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml"})
public class VectorLayerServiceTest {

	private static final String LAYER_ID = "beans";
	private static final double ALLOWANCE = .00000001;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	@Qualifier("beans")
	private BeanLayer beanLayer;

	@Autowired
	private FilterService filterService;

	@Test
	public void testSaveOrUpdate() {
		// @todo
	}

	@Test
	public void testGetFeatures() {
		// @todo
	}

	@Test
	public void testGetBoundsAll() throws Exception {
		Envelope bounds = layerService.getBounds(LAYER_ID, CRS.decode(beanLayer.getLayerInfo().getCrs()), null);
		System.out.println("bound "+bounds.getMinX()+","+bounds.getMinY()+" - "+bounds.getMaxX()+","+bounds.getMaxY());
		Assert.assertEquals(0, bounds.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, bounds.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, bounds.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, bounds.getMaxY(), ALLOWANCE);
	}

	@Test
	public void testGetBoundsFiltered() throws Exception {
		Filter filter = filterService.createFidFilter(new String[] {"2","3"});
		Envelope bounds = layerService.getBounds(LAYER_ID, CRS.decode(beanLayer.getLayerInfo().getCrs()), filter);
		System.out.println("bound "+bounds.getMinX()+","+bounds.getMinY()+" - "+bounds.getMaxX()+","+bounds.getMaxY());
		Assert.assertEquals(2, bounds.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, bounds.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, bounds.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, bounds.getMaxY(), ALLOWANCE);
	}

	// @todo should also test the getObjects() method.

}
