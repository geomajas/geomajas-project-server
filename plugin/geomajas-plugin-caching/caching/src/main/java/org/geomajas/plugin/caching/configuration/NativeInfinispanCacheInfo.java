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

package org.geomajas.plugin.caching.configuration;

import org.geomajas.annotation.Api;
import org.infinispan.config.Configuration;

/**
 * Use the Infinispan built-in configuration for configuring a cache.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class NativeInfinispanCacheInfo extends Configuration implements InfinispanConfiguration {

	private static final long serialVersionUID = 190L;

	private static final int PRIME = 31;

	private boolean cacheEnabled = true;
	private String baseConfigurationName;

	/** No-arguments constructor. */
	@Api
	public NativeInfinispanCacheInfo() {
		super(); // NOSONAR
	}

	/** {@inheritDoc} */
	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	/**
	 * Set whether this cache is enabled or not.
	 *
	 * @param cacheEnabled cache enabled?
	 */
	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

	/** {@inheritDoc} */
	public String getConfigurationName() {
		return baseConfigurationName;
	}

	/**
	 * Set the name of the base configuration from the Infinispan XML configuration file.
	 *
	 * @param baseConfigurationName name of base configuration
	 */
	public void setBaseConfigurationName(String baseConfigurationName) {
		this.baseConfigurationName = baseConfigurationName;
	}

	/** {@inheritDoc} */
	public Configuration getInfinispanConfiguration() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof NativeInfinispanCacheInfo)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		NativeInfinispanCacheInfo that = (NativeInfinispanCacheInfo) o;

		if (cacheEnabled != that.cacheEnabled) {
			return false;
		}
		if (baseConfigurationName != null ? !baseConfigurationName.equals(that.baseConfigurationName) :
				that.baseConfigurationName != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = PRIME * result + (cacheEnabled ? 1 : 0);
		result = PRIME * result + (baseConfigurationName != null ? baseConfigurationName.hashCode() : 0);
		return result;
	}
}
