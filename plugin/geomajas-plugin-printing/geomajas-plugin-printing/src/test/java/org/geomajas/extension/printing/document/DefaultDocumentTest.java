/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.extension.printing.document;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.geomajas.extension.printing.component.PageComponent;
import org.geomajas.extension.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vividsolutions.jts.geom.Coordinate;

public class DefaultDocumentTest {

	private ConfigurationService runtime;

	private GeoService geoService;

	private FilterService filterCreator;

	private VectorLayerService layerService;

	@Before
	public void setUp() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"org/geomajas/spring/geomajasContext.xml", "org/geomajas/testdata/layerBluemarble.xml",
				"org/geomajas/testdata/layerCountries110m.xml", "org/geomajas/testdata/simplemixedContext.xml" });
		// load the configuration (context-wide object);
		runtime = applicationContext.getBean("service.ConfigurationService", ConfigurationService.class);
		geoService = applicationContext.getBean("service.GeoService", GeoService.class);
		filterCreator = applicationContext.getBean("service.FilterService", FilterService.class);
		layerService = applicationContext.getBean("layer.VectorLayerService", VectorLayerService.class);
	}

	@Test
	public void testRender() throws Exception {
		DefaultDocument document = new DefaultDocument("A4", runtime, null, getDefaultVisitor(-31.44, -37.43, 80.83f),
				geoService, filterCreator, layerService);
		document.render();

		JAXBContext context = JAXBContext.newInstance("org.geomajas.extension.printing.component", getClass()
				.getClassLoader());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		m.marshal(document.getPage(), baos);
		baos.flush();
		Unmarshaller u = context.createUnmarshaller();
		Object result = u.unmarshal(new StringReader(baos.toString()));
		PageComponent pageCopy = (PageComponent) result;

		SinglePageDocument doc = new SinglePageDocument(pageCopy, runtime, null);
		doc.render();
		FileOutputStream fo = new FileOutputStream("target/test.pdf");
		doc.getOutputStream().writeTo(fo);
		fo.flush();
		fo.close();
	}

	/*
	 * @Test public void testWrite() throws Exception { PageComponent page = new PageComponent("A4", true);
	 * page.setTag(PrintTemplate.PAGE); MapComponent map = createMap(-570765.9534524073, 5067494.962772554, 2000000);
	 * ImageComponent northarrow = createArrow(); ScaleBarComponent bar = createBar(); LabelComponent title =
	 * createTitle();
	 * 
	 * LegendComponent legend = new LegendComponent(); map.addComponent(bar); map.addComponent(legend);
	 * map.addComponent(northarrow); page.addComponent(map); page.addComponent(title);
	 * 
	 * HibernateUtil.getInstance().beginTransaction(); if (HibernateTransactionInterceptor.isHibernateEnabled()) {
	 * PrintTemplateDAO dao = PrintConfiguration.getDAO();
	 * 
	 * PrintTemplate template = new PrintTemplate(); template.setDataSourceName("Viewports1"); template.setPage(page);
	 * template.encode(); List<String> names = dao.findAllNames(); // dao.makePersistent(template); template =
	 * PrintTemplate.createDefaultTemplate("A4"); template.setDataSourceName("Default"); template.encode(); //
	 * dao.makePersistent(template); HibernateUtil.getInstance().commitTransaction();
	 * HibernateUtil.getInstance().getCurrentSession().close(); } }
	 */

	/*
	 * @Test public void testRead() throws Exception { Application app = createApplication();
	 * HibernateUtil.getInstance().beginTransaction(); if (HibernateTransactionInterceptor.isHibernateEnabled()) {
	 * PrintTemplateDAO dao = PrintConfiguration.getDAO(); List<PrintTemplate> templates = dao.findAll();
	 * 
	 * for (PrintTemplate template : templates) { // decode the page template.decode(); // calculate the sizes (if not
	 * already calculated !) SinglePageDocument document = new SinglePageDocument(template.getPage(), app, null);
	 * document.setLayoutOnly(true); document.render(); } HibernateUtil.getInstance().commitTransaction();
	 * HibernateUtil.getInstance().getCurrentSession().close(); } }
	 */

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