/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.osm;

import java.util.List;

/**
 * Class which determines how to choose the URL to use from a list of URLs.
 * <p/>
 * Implementations of this interface need to be thread-safe.
 *
 * @author Joachim Van der Auwera
 */
public interface UrlSelectionStrategy {

	/**
	 * Set the list of URLs this strategy has to choose from.
	 *
	 * @param urls list of URLs to choose from.
	 */
	void setUrls(List<String> urls);

	/**
	 * Get the next URL to use.
	 *
	 * @return URL to use
	 */
	String next();
}
