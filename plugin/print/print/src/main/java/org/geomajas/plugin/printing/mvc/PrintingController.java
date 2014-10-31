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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateExtResponse;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.document.Document.Format;
import org.geomajas.plugin.printing.service.PrintService;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

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

	//public static final String IMAGE_VIEW_NAME = "plugin.printing.mvc.ImageView";

	public static final String DOCUMENT_KEY = "document";

	public static final String DOWNLOAD_KEY = "download";

	public static final String FILENAME_KEY = "name";

	public static final String FORMAT_KEY = "format";
	
	//public static final String IMAGE_KEY = "image";

	public static final String DOWNLOAD_METHOD_BROWSER = "0";

	public static final String DOWNLOAD_METHOD_SAVE = "1";

	@Autowired
	protected PrintService printService;

	@Autowired
	protected PrintConfigurationService configurationService;
	
	@Autowired
	protected CommandDispatcher commandDispatcher; 
	
	@Autowired
	protected SecurityContext securityContext; 
	
	protected ObjectMapper objectMapper;

	@RequestMapping(value = "/printing", method = RequestMethod.GET)
	public ModelAndView printGet(
			@RequestParam("documentId") String documentId,
			@RequestParam(value = "download", defaultValue = DOWNLOAD_METHOD_SAVE) String download,
			@RequestParam(value = "name", defaultValue = "geomajas.pdf") String fileName)
			throws PrintingException {
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
			@RequestParam(value = "request") String commandRequest
			)
			throws PrintingException, JsonProcessingException, IOException {
		CommandResponse response = commandDispatcher.execute(PrintGetTemplateRequest.COMMAND, fromJson(commandRequest), securityContext.getToken(), null);
		PrintGetTemplateExtResponse pr = (PrintGetTemplateExtResponse)response;
		ModelAndView mav = new ModelAndView();
		mav.setViewName(DOCUMENT_VIEW_NAME);
		mav.addObject(DOCUMENT_KEY, printService.removeDocument(pr.getDocumentId()));
		mav.addObject(DOWNLOAD_KEY, download);
		mav.addObject(FILENAME_KEY, fileName);
		mav.addObject(FORMAT_KEY, Format.decode(fileName));
		return mav;
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
	
	private CommandRequest fromJson(String commandRequest) throws JsonProcessingException, IOException {
		ObjectReader reader = objectMapper.reader(PrintGetTemplateRequest.class);
		return reader.readValue(commandRequest);
	}
	
	@PostConstruct
	public void postConstruct() {
		objectMapper = new ObjectMapper();
		objectMapper.enableDefaultTyping();
	}

}
