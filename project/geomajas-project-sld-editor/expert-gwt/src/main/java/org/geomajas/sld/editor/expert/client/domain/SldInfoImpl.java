package org.geomajas.sld.editor.expert.client.domain;

import java.io.Serializable;

public class SldInfoImpl implements SldInfo, Serializable {

	private static final long serialVersionUID = 1L;

	protected String name;
	protected String title;

	public SldInfoImpl() {
	}

	public SldInfoImpl(String name, String title) {
		this.name = name;
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
