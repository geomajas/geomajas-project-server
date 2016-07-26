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
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link GetRasterTilesCommand}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
public class GetRasterTilesCommandTest {

	private static final double DOUBLE_TOLERANCE = .0000000001;
	private static final String CRS = "EPSG:4326";

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverter;

	// @todo need to add a dummy raster layer in the context to do a normal test run

	@Test
	public void testNoLayerIdRequest() throws Exception {
		GetRasterTilesRequest request = new GetRasterTilesRequest();
		request.setCrs(CRS);
		GetRasterTilesResponse response = (GetRasterTilesResponse) dispatcher.execute(
				GetRasterTilesRequest.COMMAND, request, null, "en");
		Assert.assertTrue(response.isError());
		Assert.assertTrue(response.getErrors().get(0) instanceof GeomajasException);
		Assert.assertEquals(ExceptionCode.PARAMETER_MISSING,
				((GeomajasException) response.getErrors().get(0)).getExceptionCode());
	}

}
