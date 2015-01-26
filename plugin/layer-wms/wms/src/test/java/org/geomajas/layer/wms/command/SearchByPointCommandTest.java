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

package org.geomajas.layer.wms.command;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.wms.command.dto.SearchByPointRequest;
import org.geomajas.layer.wms.command.dto.SearchByPointResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test for {@link org.geomajas.layer.wms.command.wms.SearchByPointCommand}
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml", "/wmsContext.xml"})
public class SearchByPointCommandTest {

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
		SearchByPointRequest request = new SearchByPointRequest();
		CommandResponse response = dispatcher.execute(SearchByPointRequest.COMMAND, request, null, "en");
		Assert.assertTrue(response.isError());
		List<Throwable> errors = response.getErrors();
		Assert.assertNotNull(errors);
		Assert.assertEquals(1, errors.size());
		Throwable error = errors.get(0);
		Assert.assertTrue(error instanceof GeomajasException);
		Assert.assertEquals(ExceptionCode.PARAMETER_MISSING, ((GeomajasException) error).getExceptionCode());
	}

	@Test
	public void testSearchByPoint() throws Exception {
		String layer = "layerWmsCountries";
		SearchByPointRequest request = new SearchByPointRequest();
		request.setBbox(new Bbox(-3211986.0066263545, 98246.25012821658, 1.065471024672729E7, 3365675.229452881));
		request.setCrs("EPSG:900913");
		Map<String, String> layers = new HashMap<String, String>();
		layers.put(layer, layer);
		request.setLayerMapping(layers);
		request.setLocation(new Coordinate(672238.022713162, 2554015.0948743597));
		request.setScale(1.022083167709322E-4);
		CommandResponse response = dispatcher.execute(SearchByPointRequest.COMMAND, request, null, "en");
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof SearchByPointResponse);
		SearchByPointResponse sbp = (SearchByPointResponse) response;
		Map<String, List<Feature>> map = sbp.getFeatureMap();
		Assert.assertNotNull(map);
		List<Feature> features = map.get(layer);
		Assert.assertEquals(1, features.size());
		Feature feature = features.get(0);
		Assert.assertEquals("belt.2", feature.getId());
		Assert.assertEquals("Tropical", feature.getAttributes().get("BELT").getValue());
		Assert.assertEquals("2", feature.getAttributes().get("FID").getValue());
	}

}
