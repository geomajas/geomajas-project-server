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
package org.geomajas.plugin.deskmanager.servlet.mvc;

import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.service.common.FileRef;
import org.geomajas.plugin.deskmanager.service.common.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC controller that gets previously uploaded files through fileUpload.
 * 
 * @author Kristof Heirwegh
 * 
 */
@Controller("/fileDownload")
public class FileDownloadController {

	@Autowired
	protected FileService service;

	@RequestMapping(value = GdmLayout.FILEDOWNLOAD_URL, method = RequestMethod.GET)
	public ModelAndView getFile(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(FileDownloadView.VIEW_NAME);

		FileRef fr = service.retrieve(id);
		if (fr == null) {
			throw new FileNotFoundException("Bestand niet gevonden.");
		}

		mav.addObject(FileDownloadView.CONTENTTYPE_KEY, fr.getMimeType());
		mav.addObject(FileDownloadView.DATA_KEY, fr.getData());

		return mav;
	}

	@ExceptionHandler
	public ModelAndView exception(Exception exception, HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		if (exception instanceof FileNotFoundException) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		response.getWriter().println(exception.getLocalizedMessage());
		return new ModelAndView();
	}

}
