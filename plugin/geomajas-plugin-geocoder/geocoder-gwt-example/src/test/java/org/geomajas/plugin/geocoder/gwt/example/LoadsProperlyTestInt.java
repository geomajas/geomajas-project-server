/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.gwt.example;

import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * Verify that the application loads properly.
 *
 * @author Joachim Van der Auwera
 */
public class LoadsProperlyTestInt extends SeleneseTestCase {

	public void setUp() throws Exception {
		setUp("http://localhost:9080/", "*firefox");
	}

	/**
	 * Simple test which verifies that the demo starts
	 * @throws Exception oops
	 */
	public void testDemoLoadsProperly() throws Exception {
		selenium.open("/");
		selenium.waitForCondition(
   			"selenium.browserbot.getCurrentWindow().document.getElementById('isc_M')",
   			"90000"
			);
	}
}
