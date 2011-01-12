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
package org.geomajas.dojo.example;

import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * Verifications for the demo application
 *
 * @author Joachim Van der Auwera
 */
public class DemoTestInt extends SeleneseTestCase {

	public void setUp() throws Exception {
		setUp("http://localhost:9080/", "*firefox");
	}

	/**
	 * Simple test which verifies that the demo starts
	 * @throws Exception oops
	 */
	public void testDemoLoadsProperly() throws Exception {
		selenium.open("/");
		selenium.click("//div[@id='sample1']/table/tbody/tr[7]/td[1]/a/img");
		//selenium.waitForPageToLoad("30000"); don't use as this may fail when invoked after page already loaded
		// when this is available, the page is loaded and dojo did it's job...
		selenium.waitForCondition(
   			"selenium.browserbot.getCurrentWindow().document.getElementById('sampleTechToolbar.ZoomIn')",
   			"30000"
			);
	}
}
