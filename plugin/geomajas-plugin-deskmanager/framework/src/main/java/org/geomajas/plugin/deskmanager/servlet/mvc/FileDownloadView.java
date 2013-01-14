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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @author Kristof Heirwegh
 */
@Component("fileDownloadView")
public class FileDownloadView extends AbstractView {

	public static final String VIEW_NAME = "fileDownloadView";

	public static final String CONTENTTYPE_KEY = "contentType";

	public static final String DATA_KEY = "data";

	public FileDownloadView() {
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setContentType((String) model.get(CONTENTTYPE_KEY));
		byte[] data = (byte[]) model.get(DATA_KEY);

		// write the data
		ServletOutputStream out = response.getOutputStream();
		response.setContentType(getContentType());
		response.setContentLength(data.length);
		out.write(data);
		out.flush();
	}
}
