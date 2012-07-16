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
package org.geomajas.plugin.admin.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class TestRewirable {

	@Autowired(required = false)
	private Map<String, AutowiredBean> allBeans;

	private boolean processed;

	public Map<String, AutowiredBean> getBeans() {
		return allBeans;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

}
