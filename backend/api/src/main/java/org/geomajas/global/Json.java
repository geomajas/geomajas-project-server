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
package org.geomajas.global;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.geomajas.annotation.Api;

/**
 * Indicates that the property should be serialized by JSON.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Api(allMethods = true)
public @interface Json {

	/**
	 * Should field be serialized?
	 */
	boolean serialize();
}
