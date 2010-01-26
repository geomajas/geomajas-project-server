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

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.extension.printing.component.ImageComponent;
import org.geomajas.extension.printing.component.LabelComponent;
import org.geomajas.extension.printing.component.LayoutConstraint;
import org.geomajas.extension.printing.component.MapComponent;
import org.geomajas.extension.printing.component.PageComponent;
import org.geomajas.extension.printing.component.RasterLayerComponent;
import org.geomajas.extension.printing.component.ScaleBarComponent;
import org.geomajas.extension.printing.component.VectorLayerComponent;
import org.geomajas.extension.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.extension.printing.configuration.PrintTemplate;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.BboxService;
import org.geomajas.service.FilterCreator;
import org.geomajas.service.GeoService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.net.URL;

public class DefaultDocumentTest {

	private ApplicationInfo application;

	private ApplicationService runtime;

	private GeoService geoService;

	private BboxService bboxService;

	private FilterCreator filterCreator;

	private PaintFactory paintFactory;

	@Before
	public void setUp() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] {"org/geomajas/spring/geomajasContext.xml",
						"org/geomajas/testdata/layerBluemarble.xml",
						"org/geomajas/testdata/layerCountries.xml",
						"org/geomajas/testdata/simplemixedContext.xml"});
		URL appsUrl = getClass().getResource("/org/geomajas/configuration");
		URL escapedUrl = new URL(appsUrl.getProtocol(), appsUrl.getHost(), appsUrl.getPort(), appsUrl
				.getFile().replace(" ", "%20"));
		// load the configuration (context-wide object);
		ApplicationService applicationService =
				applicationContext.getBean("service.ApplicationService", ApplicationService.class);

		application = applicationContext.getBean("application", ApplicationInfo.class);
		runtime = applicationContext.getBean("service.ApplicationService", ApplicationService.class);
		geoService = applicationContext.getBean("service.GeoService", GeoService.class);
		bboxService = applicationContext.getBean("service.BboxService", BboxService.class);
		filterCreator = applicationContext.getBean("service.FilterCreator", FilterCreator.class);
		paintFactory = applicationContext.getBean("rendering.painter.PaintFactory", PaintFactory.class);
	}

	@Test
	public void testRender() throws Exception {
		DefaultDocument document = new DefaultDocument("A4", application, runtime, null, getDefaultVisitor(-31.44,
				-37.43, 89.83f), geoService, bboxService, filterCreator, paintFactory);
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

		SinglePageDocument doc = new SinglePageDocument(pageCopy, application, runtime, null);
		doc.render();
		FileOutputStream fo = new FileOutputStream("target/test.pdf");
		doc.getOutputStream().writeTo(fo);
		fo.flush();
		fo.close();
	}

	/*
	@Test
	public void testWrite() throws Exception {
		PageComponent page = new PageComponent("A4", true);
		page.setTag(PrintTemplate.PAGE);
		MapComponent map = createMap(-570765.9534524073, 5067494.962772554, 2000000);
		ImageComponent northarrow = createArrow();
		ScaleBarComponent bar = createBar();
		LabelComponent title = createTitle();

		LegendComponent legend = new LegendComponent();
		map.addComponent(bar);
		map.addComponent(legend);
		map.addComponent(northarrow);
		page.addComponent(map);
		page.addComponent(title);

		HibernateUtil.getInstance().beginTransaction();
		if (HibernateTransactionInterceptor.isHibernateEnabled()) {
			PrintTemplateDAO dao = PrintConfiguration.getDAO();

			PrintTemplate template = new PrintTemplate();
			template.setDataSourceName("Viewports1");
			template.setPage(page);
			template.encode();
			List<String> names = dao.findAllNames();
			// dao.makePersistent(template);
			template = PrintTemplate.createDefaultTemplate("A4");
			template.setDataSourceName("Default");
			template.encode();
			// dao.makePersistent(template);
			HibernateUtil.getInstance().commitTransaction();
			HibernateUtil.getInstance().getCurrentSession().close();
		}
	}
	*/

	/*
	@Test
	public void testRead() throws Exception {
		Application app = createApplication();
		HibernateUtil.getInstance().beginTransaction();
		if (HibernateTransactionInterceptor.isHibernateEnabled()) {
			PrintTemplateDAO dao = PrintConfiguration.getDAO();
			List<PrintTemplate> templates = dao.findAll();

			for (PrintTemplate template : templates) {
				// decode the page
				template.decode();
				// calculate the sizes (if not already calculated !)
				SinglePageDocument document = new SinglePageDocument(template.getPage(), app, null);
				document.setLayoutOnly(true);
				document.render();
			}
			HibernateUtil.getInstance().commitTransaction();
			HibernateUtil.getInstance().getCurrentSession().close();
		}
	}
	*/

	private DefaultConfigurationVisitor getDefaultVisitor(double x, double y, float widthInUnits) {
		// 842, 595
		DefaultConfigurationVisitor config = new DefaultConfigurationVisitor();
		config.setMapId("mainMap");
		config.setMapRasterResolution(72);
		config.setMapPpUnit(822 / widthInUnits);
		config.setMapLocation(new Coordinate(x, y));
		config.setTitle("Africa");
		return config;
	}

	private MapComponent createMap(double x, double y, float widthInUnits) {
		MapComponent map = new MapComponent();
		map.getConstraint().setMarginX(20);
		map.getConstraint().setMarginY(20);
		map.setMapId("mainMap");
		map.setRasterResolution(72);
		map.setPpUnit(822 / widthInUnits);
		map.setLocation(new Coordinate(x, y));
		map.setTag(PrintTemplate.MAP);
		RasterLayerComponent rc = new RasterLayerComponent();
		rc.setLayerId("bluemarble");
		rc.setVisible(true);
		map.addComponent(rc);
		VectorLayerComponent vc = new VectorLayerComponent(geoService, bboxService, filterCreator, paintFactory);
		vc.setLayerId("countries");
		vc.setVisible(true);
		vc.setLabelsVisible(true);
		map.addComponent(vc);
		return map;
	}

	private ImageComponent createArrow() {
		ImageComponent northarrow = new ImageComponent();
		northarrow.setImagePath("/images/northarrow.gif");
		northarrow.getConstraint().setAlignmentX(LayoutConstraint.RIGHT);
		northarrow.getConstraint().setAlignmentY(LayoutConstraint.TOP);
		northarrow.getConstraint().setMarginX(20);
		northarrow.getConstraint().setMarginY(20);
		northarrow.getConstraint().setWidth(10);
		northarrow.setTag(PrintTemplate.ARROW);
		return northarrow;
	}

	private LabelComponent createTitle() {
		LabelComponent title = new LabelComponent();
		title.setFont(new Font("Dialog", Font.ITALIC, 14));
		title.setBackgroundColor(Color.white);
		title.setBorderColor(Color.BLACK);
		title.setFontColor(Color.BLACK);
		title.setText("France");
		title.getConstraint().setAlignmentY(LayoutConstraint.TOP);
		title.getConstraint().setAlignmentX(LayoutConstraint.CENTER);
		title.getConstraint().setMarginY(40);
		title.setTag(PrintTemplate.TITLE);
		return title;
	}

	private ScaleBarComponent createBar() {
		ScaleBarComponent bar = new ScaleBarComponent();
		bar.setTicNumber(3);
		bar.setUnit("m");
		return bar;
	}

}