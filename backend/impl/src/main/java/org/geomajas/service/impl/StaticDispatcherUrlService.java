/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.service.impl;

import javax.validation.constraints.NotNull;

import org.geomajas.annotations.Api;
import org.geomajas.service.DispatcherUrlService;

/**
 * {@link DispatcherUrlService} which allows you to statically configure the URL for the dispatcher service.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public class StaticDispatcherUrlService implements DispatcherUrlService {

	@NotNull
	private String dispatcherUrl;

	/**
	 * Set the URL for the dispatcher service. All controllers which are linked in using the dispatcher service have
	 * a URL starting with this string.
	 *
	 * @param dispatcherUrl dispatcher service URL
	 */
	public void setDispatcherUrl(String dispatcherUrl) {
		this.dispatcherUrl = dispatcherUrl;
	}

	public String getDispatcherUrl() {
		return dispatcherUrl;
	}
}
