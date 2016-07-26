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

package org.geomajas.internal.security;

import org.geomajas.security.AuthorizationNeedsWiring;
import org.geomajas.service.TestRecorder;
import org.springframework.context.ApplicationContext;

/**
 * Test authorization for {@link AuthorizationNeedsWiring} use.
 *
 * @author Joachim Van der Auwera
 */
public class AllowNoneWiredAuthorization extends AllowNoneAuthorization implements AuthorizationNeedsWiring {

	public static final String GROUP = "wiring";
	public static final String VALUE = "done";

	public void wire(ApplicationContext applicationContext) {
		TestRecorder recorder = applicationContext.getBean(TestRecorder.class);
		recorder.record(GROUP, VALUE);
	}
}
