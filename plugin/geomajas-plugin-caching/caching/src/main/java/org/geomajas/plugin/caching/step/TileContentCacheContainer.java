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

package org.geomajas.plugin.caching.step;

/**
 * Container for the objects which need to be stored in on of the tile content (string) caches.
 *
 * @author Joachim Van der Auwera
 */
public class TileContentCacheContainer extends CacheContainer {

	private static final long serialVersionUID = 100L;

	private String featureContent;
	private String labelContent;

	/**
	 * Create for feature content.
	 *
	 * @param featureContent tile feature content
	 * @param labelContent tile label content
	 */
	public TileContentCacheContainer(String featureContent, String labelContent) {
		super();
		this.featureContent = featureContent;
		this.labelContent = labelContent;
	}

	/**
	 * Get the tile feature content.
	 *
	 * @return tile feature content
	 */
	public String getFeatureContent() {
		return featureContent;
	}

	/**
	 * Get the tile label content.
	 *
	 * @return tile label content
	 */
	public String getLabelContent() {
		return labelContent;
	}
}
