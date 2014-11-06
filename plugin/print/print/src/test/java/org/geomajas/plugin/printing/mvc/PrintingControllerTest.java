package org.geomajas.plugin.printing.mvc;

import java.io.FileOutputStream;
import java.io.IOException;

import org.geomajas.configuration.client.BoundsLimitOption;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.MapConfigurationVisitor;
import org.geomajas.plugin.printing.document.DefaultDocument;
import org.geomajas.plugin.printing.document.Document;
import org.geomajas.plugin.printing.document.Document.Format;
import org.geomajas.plugin.printing.service.PrintService;
import org.geomajas.security.SecurityManager;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
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
		ObjectMapper mapper = printingController.getObjectMapper();
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
	
	@Test
	public void testLimitsOption() throws JsonProcessingException, IOException {
		ClientMapInfo map = new ClientMapInfo();
		map.setViewBoundsLimitOption(BoundsLimitOption.CENTER_WITHIN_MAX_BOUNDS);
		ObjectMapper objectMapper = printingController.getObjectMapper();
		String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
//		System.out.println(json);
		
		JsonNode tree = objectMapper.readTree(json);
		System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));

		ObjectReader reader = objectMapper.reader(ClientMapInfo.class);
		ClientMapInfo m = (ClientMapInfo)reader.readValue(json);
		
		m = (ClientMapInfo)reader.readValue("{\n" + 
				"  \"class\" : \"org.geomajas.configuration.client.ClientMapInfo\",\n" + 
				"  \"viewBoundsLimitOption\" : \"CENTER_WITHIN_MAX_BOUNDS\" \n" + 
				"}");
		
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
