/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.project.profiling.jmx;

import org.springframework.stereotype.Component;

/**
 * Example service which is bound for the BindJmxTest.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class ExampleService {
	
	public void doSomething(int count) {
		try {
			Thread.sleep(count * 100);
		} catch (InterruptedException ie) {
			// ignore, nothing to do
		}
	}

}
