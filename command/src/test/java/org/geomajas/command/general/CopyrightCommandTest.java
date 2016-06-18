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

package org.geomajas.command.general;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.CopyrightRequest;
import org.geomajas.command.dto.CopyrightResponse;
import org.geomajas.global.CopyrightInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Test for for {@link CopyrightCommand}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
public class CopyrightCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testCopyright() throws Exception {
		CopyrightRequest request = new CopyrightRequest();
		CopyrightResponse response = (CopyrightResponse) dispatcher.execute(
				CopyrightRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		List<CopyrightInfo> copyrights = response.getCopyrights();
		Assert.assertNotNull(copyrights);
		Assert.assertTrue(copyrights.size() > 12); // this number is somewhat arbitrary
		contains("Geomajas", copyrights);
		contains("JTS", copyrights);
		contains("Spring Framework", copyrights);
		contains("Apache commons", copyrights);
		contains("slf4j", copyrights);
		contains("javax.annotation", copyrights);
		contains("javax.validation", copyrights);
		contains("GeoTools", copyrights);
	}

	private void contains(String key, List<CopyrightInfo> copyrights) {
		for (CopyrightInfo ci : copyrights) {
			if (key.equals(ci.getKey())) {
				return;
			}
		}
		Assert.fail("Copyright with key " + key + " not found");
	}

}
