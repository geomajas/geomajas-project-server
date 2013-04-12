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


/**
 * Contains the SLD data.
 * 
 * @author Kristof Heirwegh
 */
public class RawSld extends SldInfoImpl {

	private static final long serialVersionUID = 1L;

	private String version;
	private String xml;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) {	return false; }
		
		RawSld other = (RawSld) obj;
		if (name == null) {
			if (other.name != null) { return false; }
		} else if (!name.equals(other.name)) { 
			return false; 
		}
		if (title == null) {
			if (other.title != null) { return false; }
		} else if (!title.equals(other.title)) {
			return false; 
		}
		if (version == null) {
			if (other.version != null) { return false; }
		} else if (!version.equals(other.version)) {
			return false;
		}
		return true;
	}
}
