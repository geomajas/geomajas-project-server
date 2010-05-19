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

package org.geomajas.global;

import java.io.Serializable;

/**
 * Copyright information for a plug-in and the dependencies.
 *
 * @author Joachim Van der Auwera
 */
public class CopyrightInfo implements Serializable {

	private static final long serialVersionUID = 170L;
	
	private String key;

	private String copyright;

	private String licenseName;

	private String licenseUrl;

	/**
	 * Get the key which is used to determine the copyright info. This is typically the library name.
	 *
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the key which is used to determine the copyright info. This is typically the library name.
	 *
	 * @param key key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Get the copyright message.
	 *
	 * @return copyright message
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * Set the copyright message.
	 *
	 * @param copyright copyright message
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * Get the license name.
	 *
	 * @return license name
	 */
	public String getLicenseName() {
		return licenseName;
	}

	/**
	 * Set the license name.
	 *
	 * @param licenseName license name
	 */
	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	/**
	 * Get the URL for the license text.
	 *
	 * @return license url
	 */
	public String getLicenseUrl() {
		return licenseUrl;
	}

	/**
	 * Set the URL to the license text.
	 *
	 * @param license license text URL
	 */
	public void setLicenseUrl(String license) {
		this.licenseUrl = license;
	}
}
