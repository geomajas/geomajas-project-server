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

package org.geomajas.layer.osm;

import java.util.List;

/**
 * Implementation which always uses the last url from the list.
 *
 * @author Joachim Van der Auwera
 */
public class LastUrlSelectionStrategy implements UrlSelectionStrategy {

	private List<String> urls;
	private int urlSize;

	public void setUrls(List<String> urls) {
		this.urls = urls;
		urlSize = urls.size();
	}

	public String next() {
		return urls.get(urlSize - 1);
	}
}
