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

package org.geomajas.gwt.example.base.command.resource;

import org.geomajas.command.Command;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt.example.base.command.dto.GetResourceRequest;
import org.geomajas.gwt.example.base.command.dto.GetResourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 * Retrieve the contents of one or more resources on the class-path.
 * </p>
 * <p>If the resource filename has a "§panel" as extension, then the file is processed to retrieve only</p>
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
@Component
public class GetResourceCommand implements Command<GetResourceRequest, GetResourceResponse> {

	private static final String PROCESS_START_STRING = "\tpublic Canvas getViewPanel";
	private static final String PROCESS_END_STRING = "\n\t}";
	private static final String PROCESS_SUFFIX = "§panel";
	private static final String FILE_ENCODING = "UTF-8";
	private static final int READ_BUFFER_SIZE = 1024;
	private static final int READ_FILE_BASE_SIZE = 1024;

	@Autowired
	private ApplicationContext context;

	public void execute(GetResourceRequest request, GetResourceResponse response) throws Exception {
		String[] resources = request.getResources();
		if (null == resources) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "resources");
		}
		Map<String, String> result = new HashMap<String, String>();

		for (String resourceName : resources) {
			String url = resourceName;
			boolean process = false;
			if (url.endsWith(PROCESS_SUFFIX)) {
				url = url.substring(0, url.length() - PROCESS_SUFFIX.length());
				process = true;
			}
			Resource resource = context.getResource(url);
			String content;
			if (null != resource && resource.exists()) {
				content = new String(read(resource.getInputStream()), FILE_ENCODING);
				if (process) {
					content = process(content);
				}
				content = Pattern.compile("^.*@extract.*$", Pattern.MULTILINE).matcher(content).replaceAll("");
			} else {
				content = "*** File " + url + " not found.";
			}
			result.put(resourceName, content);
		}
		response.setResources(result);
	}

	public GetResourceResponse getEmptyCommandResponse() {
		return new GetResourceResponse();
	}

	// Private methods:

	private byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(READ_FILE_BASE_SIZE);
		byte[] buffer = new byte[READ_BUFFER_SIZE];
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

	private String process(String javaContent) {
		String result = "";
		int position = javaContent.indexOf(PROCESS_START_STRING);
		if (position > 0) {
			result = javaContent.substring(position);
			position = result.indexOf(PROCESS_END_STRING);
			if (position > 0) {
				result = result.substring(0, position + 3);
			}
		}
		return result;
	}
}
