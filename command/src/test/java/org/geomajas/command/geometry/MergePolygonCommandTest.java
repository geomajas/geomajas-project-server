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

package org.geomajas.command.geometry;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.MergePolygonRequest;
import org.geomajas.command.dto.MergePolygonResponse;
import org.geomajas.geometry.Geometry;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Test for {@link org.geomajas.command.geometry.MergePolygonCommand}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
public class MergePolygonCommandTest {

	private static final double DOUBLE_TOLERANCE = .0000000001;

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverter;

	@Test
	public void testMergePolygon() throws Exception {
		MergePolygonRequest request = new MergePolygonRequest();
		GeometryFactory factory = new GeometryFactory();
		request.setPolygons(new Geometry[] {
				dtoConverter.toDto(geoService.createCircle(factory.createPoint(new Coordinate(0, 0)), 10, 10)),
				dtoConverter.toDto(geoService.createCircle(factory.createPoint(new Coordinate(5, 5)), 10, 10))});
		MergePolygonResponse response = (MergePolygonResponse) dispatcher.execute(
				MergePolygonRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertNotNull(response.getGeometry());
		// @todo should verify that merge is correct
	}
}
