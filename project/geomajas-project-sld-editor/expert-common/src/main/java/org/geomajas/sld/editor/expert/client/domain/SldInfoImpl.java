/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.expert.client.domain;

import java.io.Serializable;

/**
 * Base implementation of {@link SldInfo}.
 * 
 * @author Kristof Heirwegh
 */
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
