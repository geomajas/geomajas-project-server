/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.testdata;

import org.geomajas.configuration.client.ClientUserDataInfo;

/**
 * Test object that for the GetClientUserDataCommand test case.
 * 
 * @author Pieter De Graef
 */
public class ClientUserDataObject implements ClientUserDataInfo {

	private static final long serialVersionUID = 1100L;

	private String something;

	public String getSomething() {
		return something;
	}

	public void setSomething(String something) {
		this.something = something;
	}
}