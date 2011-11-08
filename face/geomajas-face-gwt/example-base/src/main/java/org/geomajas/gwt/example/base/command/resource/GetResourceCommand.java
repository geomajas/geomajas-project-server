/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.example.base.command.resource;

import org.geomajas.command.Command;
import org.geomajas.gwt.example.base.command.dto.GetResourceRequest;
import org.geomajas.gwt.example.base.command.dto.GetResourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Retrieve the contents of one or more resources on the class-path.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public class GetResourceCommand implements Command<GetResourceRequest, GetResourceResponse> {

	@Autowired
	private ApplicationContext context;

	public void execute(GetResourceRequest request, GetResourceResponse response) throws Exception {
		if (request != null && request.getResources() != null) {
			Map<String, String> resources = new HashMap<String, String>();

			for (int i = 0; i < request.getResources().length; i++) {
				File file = ResourceUtils.getFile(request.getResources()[i]);
				if (!file.exists()) {
					file = context.getResource(request.getResources()[i]).getFile();
				}

				InputStream in = new FileInputStream(file);
				String content = new String(read(in), "UTF-8");
				resources.put(request.getResources()[i], content);
			}
			response.setResources(resources);
		}
	}

	public GetResourceResponse getEmptyCommandResponse() {
		return new GetResourceResponse();
	}

	// Private methods:

	private byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(32768);
		byte[] buffer = new byte[1024];
		int count = in.read(buffer);
		while (count != -1) {
			if (count != 0) {
				out.write(buffer, 0, count);
			}
			count = in.read(buffer);
		}
		in.close();
		return out.toByteArray();
	}
}
