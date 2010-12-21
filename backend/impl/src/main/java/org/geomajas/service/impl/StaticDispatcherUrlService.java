/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.service.impl;

import org.geomajas.global.Api;
import org.geomajas.service.DispatcherUrlService;

import javax.validation.constraints.NotNull;

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
