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

package org.geomajas.spring;

/**
 * Example extra (xml) contribution
 *
 * @author Joachim Van der Auwera
 */
public class ConfiguredExample implements ExampleInterface {

	public static final String HELLO_WORLD = "Hello from Belgium :-)";

	public String helloWorld() {
		return HELLO_WORLD;
	}
}
