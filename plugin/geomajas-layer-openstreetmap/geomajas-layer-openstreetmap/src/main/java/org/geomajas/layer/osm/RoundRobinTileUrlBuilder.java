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
package org.geomajas.layer.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Builds tile URLs by round robin iteration over a set of base URLs.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RoundRobinTileUrlBuilder implements TileUrlBuilder {

	private List<String> baseUrls;

	private ThreadLocal<Iterator<String>> iteratorHolder;

	private String extension = "png";

	/**
	 * Constructs a new, thread-safe RoundRobinTileUrlBuilder that uses the Mapnik OSM rendering.
	 */
	public RoundRobinTileUrlBuilder() {
		baseUrls = new ArrayList<String>();
		baseUrls.add("http://a.tile.openstreetmap.org");
		baseUrls.add("http://b.tile.openstreetmap.org");
		baseUrls.add("http://c.tile.openstreetmap.org");
		
		iteratorHolder = new ThreadLocal<Iterator<String>>() {

			@Override
			protected Iterator<String> initialValue() {
				List<String> emptyList = Collections.emptyList();
				return emptyList.iterator();
			}

		};
	}

	/**
	 * Returns a new URL by iterating over the base URLS.
	 */
	public String buildUrl(int level, int x, int y) {
		if (!iteratorHolder.get().hasNext()) {
			iteratorHolder.set(baseUrls.iterator());
		}
		return iteratorHolder.get().next() + "/" + level + "/" + x + "/" + y + "." + extension;
	}

	public List<String> getBaseUrls() {
		return baseUrls;
	}

	public void setBaseUrls(List<String> urls) {
		this.baseUrls = urls;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
