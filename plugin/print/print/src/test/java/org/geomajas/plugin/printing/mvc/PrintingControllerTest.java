package org.geomajas.plugin.printing.mvc;

import java.io.FileOutputStream;

import org.geomajas.configuration.IsInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.MapConfigurationVisitor;
import org.geomajas.plugin.printing.document.DefaultDocument;
import org.geomajas.plugin.printing.document.Document;
import org.geomajas.plugin.printing.document.Document.Format;
import org.geomajas.plugin.printing.service.PrintService;
import org.geomajas.security.SecurityManager;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.expression.ExpressionTypeInfo;
import org.geomajas.sld.filter.ComparisonOpsTypeInfo;
import org.geomajas.sld.filter.LogicOpsTypeInfo;
import org.geomajas.sld.filter.SpatialOpsTypeInfo;
import org.geomajas.sld.geometry.AbstractGeometryCollectionInfo;
import org.geomajas.sld.geometry.AbstractGeometryInfo;
import org.geomajas.testdata.LoggingContextLoader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.PageSize;
import com.vividsolutions.jts.geom.Coordinate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerBluemarble.xml", "/org/geomajas/testdata/layerCountries.xml",
		"/org/geomajas/testdata/simplemixedContext.xml", "/org/geomajas/plugin/printing/printing.xml" }, loader = LoggingContextLoader.class)
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public class PrintingControllerTest {

	@Autowired
	private PrintConfigurationService configurationService;

	@Autowired
	private PrintDtoConverterService printDtoService;

	@Autowired
	private PrintService printService;

	@Autowired
	private PrintingController printingController;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	@Qualifier("application")
	private ClientApplicationInfo app;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testGet() throws Exception {
		DefaultDocument document = new DefaultDocument(printService.createDefaultTemplate("A4", true), null,
				getDefaultVisitor(-31.44, -37.43, 80.83f), new MapConfigurationVisitor(configurationService,
						printDtoService));
		String documentId = printService.putDocument(document);
		ModelAndView mv = printingController.printGet(documentId, PrintingController.DOWNLOAD_METHOD_SAVE, "mydoc.pdf");
		Assert.assertSame(document, mv.getModel().get(PrintingController.DOCUMENT_KEY));
		Assert.assertEquals("mydoc.pdf", mv.getModel().get(PrintingController.FILENAME_KEY));
		Assert.assertEquals(Format.PDF, mv.getModel().get(PrintingController.FORMAT_KEY));
	}

	@Test
	public void testPost() throws Exception {
		TestTemplateBuilder builder = new TestTemplateBuilder();
		builder.setApplicationId(app.getId());
		builder.setBounds(new Bbox(-31.44, -37.43, 80.83f, 80.83f));
		builder.setMapInfo(app.getMaps().get(0));
		builder.setMarginX(10);
		builder.setMarginY(10);
		builder.setPageHeight(PageSize.A4.getWidth());
		builder.setPageWidth(PageSize.A4.getHeight());
		builder.setRasterDpi(96);
		builder.setTitleText("My map");
		builder.setWithArrow(true);
		builder.setWithScaleBar(true);
		PrintTemplateInfo templateInfo = builder.buildTemplate();
		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixInAnnotations(IsInfo.class, TypeInfoMixin.class);
		mapper.addMixInAnnotations(PrintComponentInfo.class, TypeInfoMixin.class);
		// abstract SLD classes
		mapper.addMixInAnnotations(ExpressionInfo.class, TypeInfoMixin.class);
		mapper.addMixInAnnotations(ExpressionTypeInfo.class, TypeInfoMixin.class);
		mapper.addMixInAnnotations(ComparisonOpsTypeInfo.class, TypeInfoMixin.class);
		mapper.addMixInAnnotations(LogicOpsTypeInfo.class, TypeInfoMixin.class);
		mapper.addMixInAnnotations(SpatialOpsTypeInfo.class, TypeInfoMixin.class);
		mapper.addMixInAnnotations(AbstractGeometryCollectionInfo.class, TypeInfoMixin.class);
		mapper.addMixInAnnotations(AbstractGeometryInfo.class, TypeInfoMixin.class);
		mapper.addMixInAnnotations(SymbolizerTypeInfo.class, TypeInfoMixin.class);
		String json = mapper.writer().writeValueAsString(templateInfo);
		ModelAndView mv = printingController.printPost(PrintingController.DOWNLOAD_METHOD_SAVE, "mydoc.pdf",json,"A4");
		Document document = (Document) mv.getModel().get(PrintingController.DOCUMENT_KEY);
		Assert.assertNotNull(document);
		FileOutputStream fo = new FileOutputStream("target/mydoc.pdf");
		document.render(fo, Format.PDF);
		fo.flush();
		fo.close();
		Assert.assertEquals("mydoc.pdf", mv.getModel().get(PrintingController.FILENAME_KEY));
		Assert.assertEquals(Format.PDF, mv.getModel().get(PrintingController.FORMAT_KEY));
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

	@After
	public void logout() {
		securityManager.clearSecurityContext();
	}

}
