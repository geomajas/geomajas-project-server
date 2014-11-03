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
package org.geomajas.plugin.printing.mvc;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.configuration.IsInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateExtRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateExtResponse;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.command.print.LayoutAsSinglePageDoc;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.document.Document.Format;
import org.geomajas.plugin.printing.service.PrintService;
import org.geomajas.security.SecurityContext;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.expression.ExpressionTypeInfo;
import org.geomajas.sld.filter.ComparisonOpsTypeInfo;
import org.geomajas.sld.filter.LogicOpsTypeInfo;
import org.geomajas.sld.filter.SpatialOpsTypeInfo;
import org.geomajas.sld.geometry.AbstractGeometryCollectionInfo;
import org.geomajas.sld.geometry.AbstractGeometryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Spring MVC controller that maps the pdf request to a document. The document should be available in the print service.
 * The view is referenced by name.
 * 
 * @author Jan De Moerloose
 * 
 */
@Controller("/printing/**")
public class PrintingController {

	public static final String DOCUMENT_VIEW_NAME = "plugin.printing.mvc.DocumentView";

	// public static final String IMAGE_VIEW_NAME = "plugin.printing.mvc.ImageView";

	public static final String DOCUMENT_KEY = "document";

	public static final String DOWNLOAD_KEY = "download";

	public static final String FILENAME_KEY = "name";

	public static final String FORMAT_KEY = "format";

	// public static final String IMAGE_KEY = "image";

	public static final String DOWNLOAD_METHOD_BROWSER = "0";

	public static final String DOWNLOAD_METHOD_SAVE = "1";

	@Autowired
	protected PrintService printService;

	@Autowired
	protected PrintConfigurationService configurationService;

	@Autowired
	private PrintDtoConverterService converterService;

	@Autowired
	protected CommandDispatcher commandDispatcher;

	@Autowired
	protected SecurityContext securityContext;

	protected ObjectMapper objectMapper;

	@RequestMapping(value = "/printing", method = RequestMethod.GET)
	public ModelAndView printGet(@RequestParam("documentId") String documentId,
			@RequestParam(value = "download", defaultValue = DOWNLOAD_METHOD_SAVE) String download,
			@RequestParam(value = "name", defaultValue = "geomajas.pdf") String fileName) throws PrintingException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(DOCUMENT_VIEW_NAME);
		mav.addObject(DOCUMENT_KEY, printService.removeDocument(documentId));
		mav.addObject(DOWNLOAD_KEY, download);
		mav.addObject(FILENAME_KEY, fileName);
		mav.addObject(FORMAT_KEY, Format.decode(fileName));
		return mav;
	}

	@RequestMapping(value = "/printing", method = RequestMethod.POST)
	public ModelAndView printPost(
			@RequestParam(value = "download", defaultValue = DOWNLOAD_METHOD_SAVE) String download,
			@RequestParam(value = "name", defaultValue = "geomajas.pdf") String fileName,
			@RequestParam(value = "template") String templateJson,
			@RequestParam(value = "pageSize", required = false) String pageSize) throws GeomajasException,
			JsonProcessingException, IOException {
		// parse as a generic tree, sufficient to remove the nulls first
		JsonNode tree = objectMapper.readTree(templateJson);
		// remove the nulls first, some SLD classes fail when setting null
		// preferably the client should not sent nulls, but gwt-jackson has no support for this
		removeNulls(tree);
		// reuse the null-safe tree to do the actual parsing
		PrintTemplateInfo template = fromJson(tree);
		PrintGetTemplateExtRequest request = new PrintGetTemplateExtRequest();
		request.setOutputFormat(Format.decode(fileName).getExtension());
		request.setPageSize(pageSize);
		request.setTemplate(template);
		PrintGetTemplateExtResponse response = new PrintGetTemplateExtResponse();
		LayoutAsSinglePageDoc.execute(request, response, converterService, printService);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(DOCUMENT_VIEW_NAME);
		mav.addObject(DOCUMENT_KEY, printService.removeDocument(response.getDocumentId()));
		mav.addObject(DOWNLOAD_KEY, download);
		mav.addObject(FILENAME_KEY, fileName);
		mav.addObject(FORMAT_KEY, Format.decode(fileName));
		return mav;
	}

	private static void removeNulls(JsonNode tree) {
		if (tree instanceof ArrayNode) {
			ArrayNode array = (ArrayNode) tree;
			for (int i = array.size() - 1; i >= 0; i--) {
				if (array.get(i).isNull()) {
					array.remove(i);
				} else {
					removeNulls(array.get(i));
				}
			}
		} else if (tree instanceof ObjectNode) {
			ObjectNode object = (ObjectNode) tree;
			Set<String> nulls = new HashSet<String>();
			for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
				String name = it.next();
				if (object.get(name).isNull()) {
					nulls.add(name);
				} else {
					removeNulls(object.get(name));
				}
			}
			object.remove(nulls);
		}
	}

	@ExceptionHandler
	public ModelAndView exception(PrintingException exception, HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		switch (exception.getExceptionCode()) {
			case PrintingException.DOCUMENT_NOT_FOUND:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response.getWriter().println(exception.getLocalizedMessage());
		return new ModelAndView();
	}

	private PrintTemplateInfo fromJson(JsonNode template) throws JsonProcessingException, IOException {
		ObjectReader reader = objectMapper.reader(PrintTemplateInfo.class);
		return reader.readValue(template);
	}

	@PostConstruct
	public void postConstruct() {
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// abstract info classes
		objectMapper.addMixInAnnotations(IsInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(PrintComponentInfo.class, TypeInfoMixin.class);
		// abstract SLD classes
		objectMapper.addMixInAnnotations(ExpressionInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(ExpressionTypeInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(ComparisonOpsTypeInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(LogicOpsTypeInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(SpatialOpsTypeInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(AbstractGeometryCollectionInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(AbstractGeometryInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(SymbolizerTypeInfo.class, TypeInfoMixin.class);
	}

}
