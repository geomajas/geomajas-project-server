/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.servlet.mvc;

import java.io.IOException;
import java.io.InputStream;

import org.geomajas.plugin.deskmanager.service.common.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC controller for uploading files.
 * 
 * @author Kristof Heirwegh
 */
@Controller("/fileUpload")
public class FileUploadController {

	private final Logger log = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	protected FileService service;

	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public ModelAndView handleFileUpload(@RequestParam("uploadFormElement") MultipartFile binaryFile)
			throws IOException {

		ModelAndView mav = new ModelAndView();
		mav.setViewName(FileUploadView.VIEW_NAME);
		String message;

		if (binaryFile.isEmpty()) {
			message = FileUploadView.RESPONSE_ERROR;
		} else {
			message = handleFile(binaryFile);
		}

		mav.addObject(FileUploadView.MESSAGE_KEY, message);
		return mav;
	}

	private String handleFile(MultipartFile binaryFile) {
		InputStream is = null;
		try {
			// it may be more efficient to use .transferto(file) if the store is filebased
			is = binaryFile.getInputStream();
			String mimeType = binaryFile.getContentType();
			if (mimeType == null || "".equals(mimeType)) {
				mimeType = "application/octet-stream";
			}

			String id = service.persist(is, mimeType);
			return FileUploadView.RESPONSE_OK + "[" + id + "]";

		} catch (Exception e) {
			log.warn("Exception while processing binary file", e);
			return FileUploadView.RESPONSE_ERROR + " - " + e.getMessage();

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.warn("Fail²: " + e.getMessage());
				}
			}
		}
	}
}
