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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;
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

	private final transient Logger log = LoggerFactory.getLogger(LayerFilterAuthorizationInfo.class);

	private Map<String, String> filters;

	@Override
	public BaseAuthorization getAuthorization() {
		return new FilterAuthorization(this);
	}

	/**
	 * Get the map of filters, key is the layer id, value is the filter.
	 *
	 * @return filters
	 */
	public Map<String, String> getFilters() {
		return filters;
	}

	/**
	 * Set the map of filters, key is the layer id, value is the filter.
	 *
	 * @param filters filters map
	 */
	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}

	/**
	 * Authorization including layer filter.
	 *
	 * @author Joachim Van der Auwera
	 */
	public static final class FilterAuthorization extends LayerAuthorization
			implements VectorLayerSelectFilterAuthorization {

		private LayerFilterAuthorizationInfo info; // NOSONAR

		/**
		 * Create a {@link FilterAuthorization} for deserialization.
		 */
		private FilterAuthorization() {
			super(); // for deserialization
		}

		/**
		 * Create a {@link FilterAuthorization} object for a specific configuration.
		 *
		 * @param info authorization info
		 */
		private FilterAuthorization(LayerFilterAuthorizationInfo info) {
			super(info);
			this.info = info;
		}

		@Override
		public String getId() {
			return "LayerFilterAuthorizationInfo." + Integer.toString(info.hashCode());
		}

		@Override
		public Filter getFeatureFilter(String layerId) {
			try {
				String filter = info.getFilters().get(layerId);
				if (filter == null) {
					return Filter.INCLUDE;
				}
				return CQL.toFilter(filter);
			} catch (CQLException cqlException) {
				info.log.error(cqlException.getMessage(), cqlException);
				return Filter.EXCLUDE;
			}
		}
	}
}
