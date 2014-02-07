/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command;

import org.geomajas.annotation.Api;

/**
 * Response object for {@link Command} which indicates success.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SuccessCommandResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;
	private boolean success;

	/**
	 * Was the command successful?
	 *
	 * @return true when command was successful
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Set whether the command was successful.
	 *
	 * @param success true when successful
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * String to display as command dispatcher trace.
	 *
	 * @return string representation of response
	 * @since 1.10.0
	 */
	@Override
	public String toString() {
		return "SuccessCommandResponse{" +
				super.toString() +
				", success=" + success +
				'}';
	}
}