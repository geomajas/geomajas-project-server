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

package org.geomajas.internal.service;

import org.geomajas.service.TestRecorder;
import org.springframework.stereotype.Component;

/**
 * Dummy {@link org.geomajas.service.TestRecorder} implementation for use in deployed applications.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class DummyTestRecorder implements TestRecorder {

	@Override
	public void record(Object group, String message) {
		// do nothing, this is a dummy
	}

	@Override
	public void clear() {
		// nothing to clear
	}

	@Override
	public String matches(Object group, String... messages) {
		// return false to prevent accidental test success when using dummy
		return "DummyTestRecorder, cannot test";  
	}
}
