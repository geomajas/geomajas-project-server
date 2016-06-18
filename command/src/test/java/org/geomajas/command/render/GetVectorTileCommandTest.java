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

package org.geomajas.command.render;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.GetVectorTileResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Test for {@link GetVectorTileCommand}.
 *
 * @author Joachim Van der Auwera
 */
@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
@ReloadContext // @todo unclear why needed, appeared after add a test (+ related change) in SearchByLocationCommand
public class GetVectorTileCommandTest {

	private static final String LAYER_ID = "countries";
	private static final String CRS = "EPSG:4326";

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testGetVectorTile() throws Exception {
		GetVectorTileRequest request = new GetVectorTileRequest();
		request.setCrs(CRS);
		request.setLayerId(LAYER_ID);
		request.setCode(new TileCode(0, 0, 0));
		request.setPanOrigin(new Coordinate(0, 0));
		request.setRenderer(GetVectorTileRequest.PARAM_SVG_RENDERER);
		request.setScale(1.0);
		GetVectorTileResponse response = (GetVectorTileResponse) dispatcher.execute(
				GetVectorTileRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		VectorTile tile = response.getTile();
		Assert.assertNotNull(tile);
		Assert.assertNotNull(tile.getFeatureContent());
		Assert.assertEquals(
				"<g id=\"countries.features.0-0-0\">" +
						"<g style=\"fill:#995500;fill-opacity:0.6;stroke:#995500;stroke-opacity:0.3;stroke-width:1px;\" " +
						"id=\"countries.features.0-0-0.0\">" +
						"<path fill-rule=\"evenodd\" d=\"M0 1l1 0 0 -1 -1 0 0 1 Z\" id=\"4\">" +
						"</path><path fill-rule=\"evenodd\" d=\"M-1 0l1 0 0 -1 -1 0 0 1 Z\" id=\"3\">" +
						"</path><path fill-rule=\"evenodd\" d=\"M-1 1l1 0 0 -1 -1 0 0 1 Z\" id=\"2\">" +
						"</path><path fill-rule=\"evenodd\" d=\"M0 0l1 0 0 -1 -1 0 0 1 Z\" id=\"1\"/></g></g>",
				tile.getFeatureContent());
		Assert.assertEquals(171, tile.getScreenHeight());
		Assert.assertEquals(171, tile.getScreenWidth());
	}
}
