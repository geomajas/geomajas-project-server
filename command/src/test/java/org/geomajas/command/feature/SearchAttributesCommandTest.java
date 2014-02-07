/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.command.feature;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.SearchAttributesRequest;
import org.geomajas.command.dto.SearchAttributesResponse;
import org.geomajas.layer.feature.Attribute;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Test for {@link SearchAttributesCommand}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class SearchAttributesCommandTest {

	private static final String LAYER_ID = "beans";

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testSearchAttributes() throws Exception {
		SearchAttributesRequest request = new SearchAttributesRequest();
		request.setLayerId(LAYER_ID);
		request.setAttributePath("manyToOneAttr");
		SearchAttributesResponse response = (SearchAttributesResponse) dispatcher.execute(
				SearchAttributesRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		List<Attribute<?>> attributes = response.getAttributes();
		Assert.assertNotNull(attributes);
		Assert.assertEquals(2, attributes.size());
	}

}
