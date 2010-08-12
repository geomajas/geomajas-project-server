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

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.plugin.geocoder.api.CombineResultService;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for CombineIntersectionService.
 *
 * @author Joachim Van der Auwera
 */
public class CombineIntersectionServiceTest {

	private static final double DELTA = 1e-20;

	@Test
	public void testCombine() throws Exception {
		Envelope e1 = new Envelope(10, 40, 10, 30);
		GetLocationResult glr1 = new GetLocationResult();
		glr1.setEnvelope(e1);
		Envelope e2 = new Envelope(20, 35, 25, 50);
		GetLocationResult glr2 = new GetLocationResult();
		glr2.setEnvelope(e2);
		List<GetLocationResult> list = new ArrayList<GetLocationResult>();
		list.add(glr1);
		list.add(glr2);

		CombineResultService cis = new CombineIntersectionService();
		Envelope res = cis.combine(list);
		Assert.assertEquals(20.0, res.getMinX(), DELTA);
		Assert.assertEquals(25.0, res.getMinY(), DELTA);
		Assert.assertEquals(35.0, res.getMaxX(), DELTA);
		Assert.assertEquals(30.0, res.getMaxY(), DELTA);
	}
}
