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

package org.geomajas.plugin.staticsecurity.configuration;

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
 * @since 1.6.0
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

		private FilterAuthorization() {
			super(); // for deserialization
		}

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
