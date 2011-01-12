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
package org.geomajas.plugin.printing.document;

import java.io.FileOutputStream;

import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.MapConfigurationVisitor;
import org.geomajas.plugin.printing.document.DefaultDocument;
import org.geomajas.plugin.printing.service.PrintService;
import org.geomajas.security.SecurityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Coordinate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/printing/printing.xml", "/org/geomajas/testdata/layerBluemarble.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplemixedContext.xml" })
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public class DefaultDocumentTest {

	@Autowired
	private PrintConfigurationService configurationService;

	@Autowired
	private PrintDtoConverterService printDtoService;

	@Autowired
	private PrintService printService;

	@Autowired
	private SecurityManager securityManager;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testRender() throws Exception {
		DefaultDocument document = new DefaultDocument(printService.createDefaultTemplate("A4", true), null,
				getDefaultVisitor(-31.44, -37.43, 80.83f), new MapConfigurationVisitor(configurationService,
						printDtoService));
		FileOutputStream fo = new FileOutputStream("target/test.pdf");
		document.render(fo);
		fo.flush();
		fo.close();
	}

	private DefaultConfigurationVisitor getDefaultVisitor(double x, double y, float widthInUnits) {
		// 842, 595
		DefaultConfigurationVisitor config = new DefaultConfigurationVisitor();
		config.setApplicationId("application");
		config.setMapId("mainMap");
		config.setMapRasterResolution(72);
		config.setMapPpUnit(822 / widthInUnits);
		config.setMapLocation(new Coordinate(x, y));
		config.setTitle("Africa");
		return config;
	}

}