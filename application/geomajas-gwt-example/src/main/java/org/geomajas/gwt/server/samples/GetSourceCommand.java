package org.geomajas.gwt.server.samples;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.command.Command;
import org.geomajas.gwt.client.samples.base.GetResourcesRequest;
import org.geomajas.gwt.client.samples.base.GetResourcesResponse;
import org.springframework.stereotype.Component;

@Component
public class GetSourceCommand implements Command<GetResourcesRequest, GetResourcesResponse> {

	public void execute(GetResourcesRequest request, GetResourcesResponse response) throws Exception {
		if (request != null && request.getJavaClass() != null) {
			Map<String, String> resources = new HashMap<String, String>();

			for (int i = 0; i < request.getJavaClass().length; i++) {
				InputStream in = Class.class.getResourceAsStream(request.getJavaClass()[i]);
				if (in != null) {
					String content = new String(read(in));
					resources.put(request.getJavaClass()[i], content);
				} else {
					throw new IllegalArgumentException("Resource file " + request.getJavaClass()[i]
							+ " could not be found.");
				}
			}
			response.setResources(resources);
		}
	}

	public GetResourcesResponse getEmptyCommandResponse() {
		return new GetResourcesResponse();
	}

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
