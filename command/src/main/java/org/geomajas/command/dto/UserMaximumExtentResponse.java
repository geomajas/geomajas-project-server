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
package org.geomajas.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Bbox;

/**
 * Response object for {@link org.geomajas.command.configuration.UserMaximumExtentCommand}.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public class UserMaximumExtentResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private Bbox bounds;

	public UserMaximumExtentResponse() {
	}

	public Bbox getBounds() {
		return bounds;
	}

	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	@Override
	public String toString() {
		return "UserMaximumExtentResponse{" +
				"bounds=" + bounds +
				'}';
	}
}
