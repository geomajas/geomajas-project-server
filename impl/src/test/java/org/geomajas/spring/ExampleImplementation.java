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

package org.geomajas.spring;

import org.springframework.stereotype.Component;

/**
 * Example interface which shows spring (with auto discovery) in action.
 *
 * @author Joachim Van der Auwera
 */
@Component("ExampleInterface")
public class ExampleImplementation implements ExampleInterface {

	public static final String HELLO_WORLD = "Hello World!";

	public String helloWorld() {
		return HELLO_WORLD; // who would have guessed
	}
}
