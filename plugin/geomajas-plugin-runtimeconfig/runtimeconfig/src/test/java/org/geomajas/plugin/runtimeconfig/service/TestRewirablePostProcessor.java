/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

public class TestRewirablePostProcessor {

	@Autowired(required = false)
	private Map<String, TestRewirable> rewirables;

	@PostConstruct
	public void postProcess() {
		for (TestRewirable t : rewirables.values()) {
			t.setProcessed(true);
		}
	}
}
