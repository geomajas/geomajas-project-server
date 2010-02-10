package org.geomajas.gwt.client.samples.base;

import java.util.Map;

import org.geomajas.command.CommandResponse;

public class GetResourcesResponse extends CommandResponse {

	private static final long serialVersionUID = 153L;

	private Map<String, String> resources;

	// Constructors:

	public GetResourcesResponse() {
	}

	public GetResourcesResponse(Map<String, String> resources) {
		this.resources = resources;
	}

	// Getters and setters:

	public Map<String, String> getResources() {
		return resources;
	}

	public void setResources(Map<String, String> javaSource) {
		this.resources = javaSource;
	}
}
