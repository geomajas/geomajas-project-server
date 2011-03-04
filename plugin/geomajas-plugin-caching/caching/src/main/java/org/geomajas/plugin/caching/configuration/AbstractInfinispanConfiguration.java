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

package org.geomajas.plugin.caching.configuration;

/**
 * ...
 *
 * @author Joachim Van der Auwera
 */
public abstract class AbstractInfinispanConfiguration implements InfinispanConfiguration {

	private boolean cacheEnabled = true;
	private String baseConfigurationName;

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

}
