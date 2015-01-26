/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.command.configuration;

import org.geomajas.configuration.ServerSideOnlyInfo;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Dummy server-side only data container.
 *
 * @author Joachim Van der Auwera
 */
public class DummyServerSideOnlyInfo implements ClientWidgetInfo, ClientUserDataInfo, ServerSideOnlyInfo {

	private String dummy;

	/**
	 * Get dummy data.
	 *
	 * @return dummy data
	 */
	public String getDummy() {
		return dummy;
	}

	/**
	 * Set dummy data.
	 *
	 * @param dummy dummy data
	 */
	public void setDummy(String dummy) {
		this.dummy = dummy;
	}
}
