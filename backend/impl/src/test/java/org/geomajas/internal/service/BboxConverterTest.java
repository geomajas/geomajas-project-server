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
import org.geomajas.geometry.Bbox;
import org.geomajas.service.DtoConverterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for the DtoConverterService, specifically testing the bbox conversions. 
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/moreContext.xml"})
public class BboxConverterTest {

	private static final double ALLOWANCE = .000001;

	@Autowired
	private DtoConverterService converterService;

	@Test
	public void testToInternal() {
		Bbox bbox = new Bbox(10, 20, 30, 40);
		Envelope envelope = converterService.toInternal(bbox);
		Assert.assertEquals(10, envelope.getMinX(), ALLOWANCE);
		Assert.assertEquals(20, envelope.getMinY(), ALLOWANCE);
		Assert.assertEquals(40, envelope.getMaxX(), ALLOWANCE);
		Assert.assertEquals(60, envelope.getMaxY(), ALLOWANCE);
	}

	@Test
	public void testToDto() {
		Envelope envelope = new Envelope(10, 20, 30, 40);
		Bbox bbox = converterService.toDto(envelope);
		Assert.assertEquals(10, bbox.getX(), ALLOWANCE);
		Assert.assertEquals(30, bbox.getY(), ALLOWANCE);
		Assert.assertEquals(20, bbox.getMaxX(), ALLOWANCE);
		Assert.assertEquals(40, bbox.getMaxY(), ALLOWANCE);
	}
}
