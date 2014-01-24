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

package org.geomajas.command.configuration;

import org.geomajas.service.TestRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Simple component which records when the post-construct method was called for testing the refresh configuration.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class RefreshTestRecorder {

	@Autowired
	private TestRecorder recorder;

	@PostConstruct
	public void postConstruct() {
		recorder.record("test", "postConstruct");
	}
}
