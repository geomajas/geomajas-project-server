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
package org.geomajas.plugin.deskmanager.servlet.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @author Kristof Heirwegh
 */
@Component(FileUploadView.VIEW_NAME)
public class FileUploadView extends AbstractView {

	public static final String VIEW_NAME = "FileUploadView";

	public static final String MESSAGE_KEY = "message";

	public static final String RESPONSE_ERROR = "ERROR";

	public static final String RESPONSE_OK = "OK";

	public FileUploadView() {
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String message = (String) model.get(MESSAGE_KEY);
		response.getWriter().append(message);
	}
}
