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

package org.geomajas.layer.tms.mvc;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases to test the TmsController static methods.
 * 
 * @author Kristof Heirwegh
 */
public class TmsControllerTest {

	@Test
	public void testParseLayerId() {
		String expected = "BliepBlap";
		String uri = "http://hierzo.daarzo:8080/mijnApplicatie/d/tms-proxy/BliepBlap/123/5/7.jpg";
		Assert.assertEquals(expected, TmsProxyController.parseLayerId(uri));
	}
	
	@Test
	public void testParseRelativeUrl() {
		String uri = "http://hierzo.daarzo:8080/mijnApplicatie/d/tms-proxy/BliepBlap/123/5/7.jpg";
		String layerId = "BliepBlap";
		String expected = "123/5/7.jpg";
		Assert.assertEquals(expected, TmsProxyController.parseRelativeUrl(uri, layerId));
	}
}