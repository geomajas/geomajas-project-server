/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.infinispan.configuration;

/**
 * Abstract base class for {@link InfinispanConfiguration}.
 *
 * @author Joachim Van der Auwera
 */
public abstract class AbstractInfinispanConfiguration implements InfinispanConfiguration {

	private boolean cacheEnabled = true;
	private String configurationName;

	@Override
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

	@Override
	public String getConfigurationName() {
		return configurationName;
	}

	/**
	 * Set the name of the configuration from the Infinispan XML configuration file.
	 *
	 * @param configurationName name of configuration
	 */
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

}
