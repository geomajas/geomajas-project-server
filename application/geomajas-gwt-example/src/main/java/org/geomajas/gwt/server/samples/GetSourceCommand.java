/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.server.samples;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.command.Command;
import org.geomajas.gwt.client.samples.base.GetResourcesRequest;
import org.geomajas.gwt.client.samples.base.GetResourcesResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * <p>
 * Retrieve the contents of one or more resources on the class-path.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public class GetSourceCommand implements Command<GetResourcesRequest, GetResourcesResponse> {

	public void execute(GetResourcesRequest request, GetResourcesResponse response) throws Exception {
		if (request != null && request.getResources() != null) {
			Map<String, String> resources = new HashMap<String, String>();

			for (int i = 0; i < request.getResources().length; i++) {
				File file = ResourceUtils.getFile(request.getResources()[i]);
				InputStream in = new FileInputStream(file);
				if (in != null) {
					String content = new String(read(in));
					resources.put(request.getResources()[i], content);
				} else {
					throw new IllegalArgumentException("Resource file " + request.getResources()[i]
							+ " could not be found.");
				}
			}
			response.setResources(resources);
		}
	}

	public GetResourcesResponse getEmptyCommandResponse() {
		return new GetResourcesResponse();
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
