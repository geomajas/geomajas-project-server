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

package org.geomajas.global;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Copyright information for a plug-in and the dependencies.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 */
@Api(allMethods = true)
public class CopyrightInfo implements Serializable {

	private static final long serialVersionUID = 170L;

	@NotNull
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
