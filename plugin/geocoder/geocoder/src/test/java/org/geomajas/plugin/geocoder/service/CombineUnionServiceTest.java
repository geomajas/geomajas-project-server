/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
 * Test for CombineUnionService.
 *
 * @author Joachim Van der Auwera
 */
public class CombineUnionServiceTest {

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

		CombineResultService cis = new CombineUnionService();
		Envelope res = cis.combine(list);
		Assert.assertEquals(10.0, res.getMinX(), DELTA);
		Assert.assertEquals(10.0, res.getMinY(), DELTA);
		Assert.assertEquals(40.0, res.getMaxX(), DELTA);
		Assert.assertEquals(50.0, res.getMaxY(), DELTA);
	}
}
