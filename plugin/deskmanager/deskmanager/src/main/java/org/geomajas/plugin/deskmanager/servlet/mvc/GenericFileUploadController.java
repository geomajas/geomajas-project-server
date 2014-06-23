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

import org.geomajas.plugin.deskmanager.service.manager.GenericFileService;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Spring MVC controller for uploading a file to the memory.
 * It will return an id that enables retrieving the file (content) later on.
 * For now, only Administrators can add a file.
 * 
 * @author Jan Venstermans
 */
@Controller("/genericFileUpload")
public class GenericFileUploadController {

	private final Logger log = LoggerFactory.getLogger(GenericFileUploadController.class);

	@Autowired
	protected GenericFileService genericFileService;

	@Autowired
	private SecurityContext securityContext;

	@RequestMapping(value = "/genericFileUpload", method = RequestMethod.POST)
	public ModelAndView handleUpload(@RequestParam("uploadFormElement") MultipartFile file) throws IOException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(GenericFileUploadView.VIEW_NAME);
		String message;

		if (file.isEmpty()) {
			message = GenericFileUploadView.RESPONSE_INVALID_FILE;
		} else {

			// possible security check: is administrator
			//if (((DeskmanagerSecurityContext) securityContext).getRole().equals(Role.ADMINISTRATOR)) {
				String uuid = genericFileService.saveFile(file);
				mav.addObject(GenericFileUploadView.FILE_ID_KEY, uuid);
				message = GenericFileUploadView.RESPONSE_OK;
			/*} else {
				message = GenericFileUploadView.RESPONSE_NO_RIGHTS;
			} */
		}

		mav.addObject(GenericFileUploadView.MESSAGE_KEY, message);
		return mav;
	}


}
