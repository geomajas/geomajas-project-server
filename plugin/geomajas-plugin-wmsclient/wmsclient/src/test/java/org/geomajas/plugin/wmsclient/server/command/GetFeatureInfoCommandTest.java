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

package org.geomajas.plugin.wmsclient.server.command;

import org.geomajas.layer.feature.Feature;
import org.geomajas.plugin.wmsclient.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsVersion;
import org.geomajas.plugin.wmsclient.server.command.dto.GetFeatureInfoRequest;
import org.geomajas.plugin.wmsclient.server.command.dto.GetFeatureInfoResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case for the GetFeatureInfoCommand.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "commandContext.xml" })
public class GetFeatureInfoCommandTest {

	@Autowired
	private GetFeatureInfoCommand command;

	@Test
	public void testTextHtml111() throws Exception {
		GetFeatureInfoResponse response = command.getEmptyCommandResponse();
		Assert.assertNotNull(response);

		GetFeatureInfoRequest request = new GetFeatureInfoRequest(getFeatureInfoUrl(WmsVersion.V1_1_1,
				GetFeatureInfoFormat.HTML));
		command.execute(request, response);
		Assert.assertNotNull(response.getWmsResponse());
		Assert.assertTrue(response.getWmsResponse().indexOf("Texas") > 0);
	}

	@Test
	public void testTextHtml130() throws Exception {
		GetFeatureInfoResponse response = command.getEmptyCommandResponse();
		Assert.assertNotNull(response);

		GetFeatureInfoRequest request = new GetFeatureInfoRequest(getFeatureInfoUrl(WmsVersion.V1_3_0,
				GetFeatureInfoFormat.HTML));
		command.execute(request, response);
		Assert.assertNotNull(response.getWmsResponse());
		Assert.assertTrue(response.getWmsResponse().indexOf("Texas") > 0);
	}

	@Test
	public void testTextGml2Wms111() throws Exception {
		GetFeatureInfoResponse response = command.getEmptyCommandResponse();
		Assert.assertNotNull(response);

		GetFeatureInfoRequest request = new GetFeatureInfoRequest(getFeatureInfoUrl(WmsVersion.V1_1_1,
				GetFeatureInfoFormat.GML2));
		command.execute(request, response);
		Assert.assertNull(response.getWmsResponse());
		Assert.assertEquals(1, response.getFeatures().size());
		Feature feature = response.getFeatures().get(0);
		Assert.assertEquals("states.15", feature.getId());
	}

	@Test
	public void testTextGml3Wms130() throws Exception {
		GetFeatureInfoResponse response = command.getEmptyCommandResponse();
		Assert.assertNotNull(response);

		GetFeatureInfoRequest request = new GetFeatureInfoRequest(getFeatureInfoUrl(WmsVersion.V1_3_0,
				GetFeatureInfoFormat.GML3));
		command.execute(request, response);
		Assert.assertNull(response.getWmsResponse());
		Assert.assertEquals(1, response.getFeatures().size());
		Feature feature = response.getFeatures().get(0);
		Assert.assertEquals("states.15", feature.getId());
	}

	@Test
	public void testTextGml3Wms111() throws Exception {
		GetFeatureInfoResponse response = command.getEmptyCommandResponse();
		Assert.assertNotNull(response);

		GetFeatureInfoRequest request = new GetFeatureInfoRequest(getFeatureInfoUrl(WmsVersion.V1_1_1,
				GetFeatureInfoFormat.GML3));
		command.execute(request, response);
		Assert.assertNull(response.getWmsResponse());
		Assert.assertEquals(1, response.getFeatures().size());
		Feature feature = response.getFeatures().get(0);
		Assert.assertEquals("states.15", feature.getId());
	}

	@Test
	public void testTextGml2Wms130() throws Exception {
		GetFeatureInfoResponse response = command.getEmptyCommandResponse();
		Assert.assertNotNull(response);

		GetFeatureInfoRequest request = new GetFeatureInfoRequest(getFeatureInfoUrl(WmsVersion.V1_3_0,
				GetFeatureInfoFormat.GML2));
		command.execute(request, response);
		Assert.assertNull(response.getWmsResponse());
		Assert.assertEquals(1, response.getFeatures().size());
		Feature feature = response.getFeatures().get(0);
		Assert.assertEquals("states.15", feature.getId());
	}

	private String getFeatureInfoUrl(WmsVersion version, GetFeatureInfoFormat format) {
		String crs = "crs";
		String x = "i";
		String y = "j";
		if (version.equals(WmsVersion.V1_1_1)) {
			crs = "srs";
			x = "x";
			y = "y";
		}
		String bbox = "-100.0,22.0,-84.0,38.0";
		if (version.equals(WmsVersion.V1_3_0)) {
			bbox = "22.0,-100.0,38.0,-84.0"; // Invert Axis order in WMS 1.3.0....
		}

		return "http://apps.geomajas.org/geoserver/wms?service=WMS&layers=states&width=256&height=256&bbox=" + bbox
				+ "&format=image/png&version=" + version.toString() + "&" + crs
				+ "=EPSG:4326&styles=&transparent=true&QUERY_LAYERS=states&request=GetFeatureInfo&" + x + "=18&" + y
				+ "=109&INFO_FORMAT=" + format.toString();
	}
}