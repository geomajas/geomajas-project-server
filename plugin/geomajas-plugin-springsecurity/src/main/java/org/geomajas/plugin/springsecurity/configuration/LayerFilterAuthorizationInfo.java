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

package org.geomajas.plugin.springsecurity.configuration;

import org.geomajas.global.Api;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.VectorLayerSelectFilterAuthorization;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Allow configuring authorizations including a layer filter.
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class LayerFilterAuthorizationInfo extends LayerAuthorizationInfo {

	private final Logger log = LoggerFactory.getLogger(LayerFilterAuthorizationInfo.class);

	private Map<String, String> filters;

	public BaseAuthorization getAuthorization() {
		return new FilterAuthorization(this);
	}

	public Map<String, String> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}

	/**
	 * Authorization including layer filter.
	 */
	private final class FilterAuthorization extends LayerAuthorization implements VectorLayerSelectFilterAuthorization {

		private LayerFilterAuthorizationInfo info;

		private FilterAuthorization(LayerFilterAuthorizationInfo info) {
			super(info);
			this.info = info;
		}

		public String getId() {
			return "LayerFilterAuthorizationInfo." + Integer.toString(info.hashCode());
		}

		public Filter getFeatureFilter(String layerId) {
			try {
				String filter = filters.get(layerId);
				if (filter == null) {
					return Filter.INCLUDE;
				}
				return CQL.toFilter(filter);
			} catch (CQLException cqlException) {
				log.error(cqlException.getMessage(), cqlException);
				return Filter.EXCLUDE;
			}
		}
	}
}
