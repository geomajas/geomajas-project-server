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
package org.geomajas.plugin.deskmanager.test.service;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geomajas.layer.LayerException;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
import org.geomajas.plugin.deskmanager.service.manager.ShapeFileService;
import org.geomajas.service.GeoService;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Oliver May
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })

public class ShapeFileServiceTest {

	@Autowired
	private ShapeFileService shapeFileService;

	@Autowired
	private GeoService geoService;

	@Test
	public void testImportShapeFile() throws Exception {
		URL url = ClassLoader.getSystemResource("shp/werken2013.shp");

		String path = url.getPath();
		path = URLDecoder.decode(path, "utf-8");
		path = new File(path).getPath();

		Assert.assertTrue(shapeFileService.importShapeFile(path, UUID.randomUUID().toString()));
	}

	@Test
	public void testCrsTransform() {
		MathTransform mt = null;
		try {
			mt = CRS.findMathTransform(geoService.getCrs2("EPSG:31370"), geoService.getCrs2("EPSG:31370"));
		} catch (FactoryException e) {
			e.printStackTrace();
		} catch (LayerException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(mt);
		mt = null;

		try {
			mt = CRS.findMathTransform(geoService.getCrs2("EPSG:31370"), geoService.getCrs2("EPSG:31300"));
		} catch (FactoryException e) {
			e.printStackTrace();
		} catch (LayerException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(mt);
		mt = null;

		try {
			mt = CRS.findMathTransform(geoService.getCrs2("EPSG:31370"), geoService.getCrs2("EPSG:3426"));
		} catch (FactoryException e) {
			e.printStackTrace();
		} catch (LayerException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(mt);
		mt = null;

		try {
			mt = CRS.findMathTransform(geoService.getCrs2("EPSG:31370"), geoService.getCrs2("EPSG:900913"));
		} catch (FactoryException e) {
			e.printStackTrace();
		} catch (LayerException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(mt);
	}

}
