/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package com.smartgwt.client.core;

/**
 * Equivalent of SmartGWT Function class to assure @Api is not broken.
 *
 * @author Joachim Van der Auwera
 * @deprecated in Geomajas context use {@link Runnable} (use is fine in SmartGWT context)
 */
@Deprecated
public interface Function {

	/**
	 * Execute something.
	 */
	void execute();

}
