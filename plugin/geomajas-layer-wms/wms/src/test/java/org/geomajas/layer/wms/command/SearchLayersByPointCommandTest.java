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

package org.geomajas.layer.wms.command;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchLayersByPointRequest;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Test for {@link org.geomajas.command.wms.SearchLayersByPointCommand}
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml", "/wmsContext.xml"})
public class SearchLayersByPointCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Before
	public void login() {
		securityManager.createSecurityContext("");
	}

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	@Test
	public void testInvalidRequest() throws Exception {
		SearchLayersByPointRequest request = new SearchLayersByPointRequest();
		CommandResponse response = dispatcher.execute(SearchLayersByPointRequest.COMMAND, request, null, "en");
		Assert.assertTrue(response.isError());
		List<Throwable> errors = response.getErrors();
		Assert.assertNotNull(errors);
		Assert.assertEquals(1, errors.size());
		Throwable error = errors.get(0);
		Assert.assertTrue(error instanceof GeomajasException);
		Assert.assertEquals(ExceptionCode.PARAMETER_MISSING, ((GeomajasException) error).getExceptionCode());
	}

	// @todo needs more tests, at least one test which verifies the "normal" behaviour
}
