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

package org.geomajas.plugin.printing.service;

import java.util.List;

import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/printing/printing.xml" })
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public class PrintServiceImplTest {

	@Autowired
	private PrintService printService;

	private final Logger log = LoggerFactory.getLogger(PrintServiceImplTest.class);

	@Before
	public void loadTemplates() throws Exception {
		PrintTemplate template = printService.createDefaultTemplate("A4", true);
		template.setName("test");
		printService.saveOrUpdateTemplate(template);
		log.info(template.getPageXml());

	}

	@Test
	public void testGetAllTemplates() throws Exception {
		List<PrintTemplate> templates = printService.getAllTemplates();
		Assert.assertEquals(11, templates.size());
	}

}
