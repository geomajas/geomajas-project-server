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
 * Implementation which cyclically selects the next url from the list.
 *
 * @author Joachim Van der Auwera
 */
public class RoundRobinUrlSelectionStrategy implements UrlSelectionStrategy {

	private List<String> urls;
	private int urlSize;
	private int index;

	public void setUrls(List<String> urls) {
		this.urls = urls;
		urlSize = urls.size();
	}

	public String next() {
		return urls.get((index++) % urlSize);
	}
}
