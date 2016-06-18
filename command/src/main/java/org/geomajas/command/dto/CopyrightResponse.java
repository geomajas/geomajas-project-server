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
package org.geomajas.command.dto;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.global.CopyrightInfo;

/**
 * Response class for {@link org.geomajas.command.general.CopyrightCommand}.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.1
 */
@Api(allMethods = true)
public class CopyrightResponse extends CommandResponse {

	private static final long serialVersionUID = 170L;

	private List<CopyrightInfo> copyrights;

	/**
	 * Get the copyright information.
	 *
	 * @return copyrights
	 */
	public List<CopyrightInfo> getCopyrights() {
		return copyrights;
	}

	/**
	 * Set the copyright information.
	 *
	 * @param copyrights copyrights
	 */
	public void setCopyrights(List<CopyrightInfo> copyrights) {
		this.copyrights = copyrights;
	}

	@Override
	public String toString() {
		return "CopyrightResponse{" +
				"copyrights=" + copyrights +
				'}';
	}
}