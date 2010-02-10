package org.geomajas.gwt.client.samples.base;

import org.geomajas.command.CommandRequest;

public class GetResourcesRequest implements CommandRequest {

	private static final long serialVersionUID = 153L;

	private String[] javaClass;

	// Constructors:

	public GetResourcesRequest() {
	}

	public GetResourcesRequest(String[] javaClass) {
		this.javaClass = javaClass;
	}

	// Getters and setters:

	public String[] getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(String[] javaClass) {
		this.javaClass = javaClass;
	}
}
