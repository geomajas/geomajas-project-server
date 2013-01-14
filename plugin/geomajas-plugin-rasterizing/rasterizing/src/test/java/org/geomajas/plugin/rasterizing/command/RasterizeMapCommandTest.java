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

package org.geomajas.plugin.rasterizing.command;

import java.io.IOException;

import junit.framework.Assert;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapRequest;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapResponse;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.security.SecurityManager;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for GetLocationForStringCommand command, with explicit combiner.
 * 
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/command/rasterizing-info.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml",
		"/org/geomajas/testdata/layerBluemarble.xml" })
public class RasterizeMapCommandTest {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	@Qualifier("bean")
	private ClientApplicationInfo clientApplicationInfo;

	@Autowired
	@Qualifier("rasterizingInfo")
	private MapRasterizingInfo mapRasterizingInfo;

	@Before
	public void login() {
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testMissingMapInfo() throws Exception {
		RasterizeMapRequest request = new RasterizeMapRequest();
		ClientMapInfo mapInfo = clientApplicationInfo.getMaps().get(0);
		request.setClientMapInfo(mapInfo);
		CommandResponse response = commandDispatcher.execute(RasterizeMapRequest.COMMAND, request, null, "en");
		Assert.assertNotNull(response);
		Assert.assertTrue(response instanceof RasterizeMapResponse);
		RasterizeMapResponse rasterizeMapResponse = (RasterizeMapResponse) response;
		Assert.assertTrue(rasterizeMapResponse.isError());
		Assert.assertEquals(ExceptionCode.PARAMETER_MISSING, rasterizeMapResponse.getExceptions().get(0)
				.getExceptionCode());
	}

	@Test
	public void testKeysAndUrls() throws Exception {
		RasterizeMapRequest request = new RasterizeMapRequest();
		ClientMapInfo orig = clientApplicationInfo.getMaps().get(0); 
		// clone to keep context clean
		ClientMapInfo mapInfo = cloneInfo(orig);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		request.setClientMapInfo(mapInfo);
		for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
			if(layerInfo instanceof ClientVectorLayerInfo){
				VectorLayerRasterizingInfo rasterizingInfo = new VectorLayerRasterizingInfo();
				rasterizingInfo.setShowing(true);
				rasterizingInfo.setPaintGeometries(true);
				rasterizingInfo.setPaintLabels(true);
				rasterizingInfo.setStyle(((ClientVectorLayerInfo) layerInfo).getNamedStyleInfo());
				layerInfo.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, rasterizingInfo);
			} else {
				RasterLayerRasterizingInfo rasterizingInfo = new RasterLayerRasterizingInfo();
				rasterizingInfo.setShowing(true);
				layerInfo.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterizingInfo);
			}
		}
		CommandResponse response = commandDispatcher.execute(RasterizeMapRequest.COMMAND, request, null, "en");
		Assert.assertNotNull(response);
		Assert.assertTrue(response instanceof RasterizeMapResponse);
		RasterizeMapResponse rasterizeMapResponse = (RasterizeMapResponse) response;
		Assert.assertFalse(rasterizeMapResponse.isError());
		Assert.assertNotNull(rasterizeMapResponse.getLegendKey());
		Assert.assertEquals("http://test/rasterizing/image/"+rasterizeMapResponse.getLegendKey()+".png", rasterizeMapResponse.getLegendUrl());
		Assert.assertNotNull(rasterizeMapResponse.getMapKey());
		Assert.assertEquals("http://test/rasterizing/image/"+rasterizeMapResponse.getMapKey()+".png", rasterizeMapResponse.getMapUrl());
	}

	@After
	public void logout() {
		securityManager.clearSecurityContext();
	}
	
	private ClientMapInfo cloneInfo(ClientMapInfo input) throws GeomajasException {
		try {
			JBossObjectOutputStream jbossSerializer = new JBossObjectOutputStream(null);
			Object obj = jbossSerializer.smartClone(input);
			return (ClientMapInfo) obj;
		} catch (IOException e) {
			// should not happen
			throw new GeomajasException(e, ExceptionCode.UNEXPECTED_PROBLEM);
		}
	}

}
