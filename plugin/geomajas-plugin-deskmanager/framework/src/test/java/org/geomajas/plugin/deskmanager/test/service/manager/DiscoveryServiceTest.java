/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.test.service.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Oliver May
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
public class DiscoveryServiceTest {

	@Autowired
	private DiscoveryService discoveryService;

	@Test
	public void testGetRasterCapabilities() throws Exception {
		Map<String, String> props = new HashMap<String, String>();
		props.put(GetWmsCapabilitiesRequest.GET_CAPABILITIES_URL,
				"http://apps.geomajas.org/geoserver/geosparc/wms?service=WMS&version=1.1.0&request=GetCapabilities");
		List<RasterCapabilitiesInfo> layers = discoveryService.getRasterCapabilities(props);
		Assert.assertTrue(layers.size() > 0);
	}
}
