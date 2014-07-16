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

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Jan Venstermans
 */
@Component(GenericFileUploadView.VIEW_NAME)
public class GenericFileUploadView extends AbstractView {

	public static final String VIEW_NAME = "TerritoryUploadView";

	public static final String MESSAGE_KEY = "message";

	public static final String FILE_ID_KEY = "fileId";

	public static final String RESPONSE_INVALID_FILE = "Invalid file";

	public static final String RESPONSE_INVALID_LAYER = "Invalid ClientLayer";

	public static final String RESPONSE_NO_RIGHTS = "No rights";

	public static final String RESPONSE_OK = "OK";

	public GenericFileUploadView() {
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String message = (String) model.get(MESSAGE_KEY);
		if (model.containsKey(FILE_ID_KEY)) {
			message = RESPONSE_OK + "[" + model.get(FILE_ID_KEY) + "]";
		}
		response.getWriter().append(message);
	}
}