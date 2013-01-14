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

package test;

import org.geomajas.global.Api;

/**
 * Should cause problems because the @since is not set on newMethod() and because getSomething() has changed signature.
 *
 * @author Joachim Van der Auwera
 * @since 1.2.3
 */
@Api(allMethods = true)
public class AllMethodsUsed {

	public String getSomething() {
		return null;
	}

	public void doSomething() {
	}

	/**
	 * Changed version.
	 *
	 * @since 1.4.4
	 */
	public void oldMethod() {
	}

	public void newMethod() {
	}

}
