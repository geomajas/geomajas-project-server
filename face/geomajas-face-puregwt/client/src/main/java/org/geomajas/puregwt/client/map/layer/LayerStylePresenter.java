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

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.annotation.FutureApi;

/**
 * Presenter for a layer style. It points to an image URL and a label that is associated with that image. This
 * combination can for example be used within a legend widget.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public class LayerStylePresenter {

	private final int index;

	private final String url;

	private final String label;

	protected LayerStylePresenter(int index, String url, String label) {
		this.index = index;
		this.url = url;
		this.label = label;
	}

	/**
	 * Get the URL that points to an image representing this particular style.
	 * 
	 * @return The URL that points to an image representing this particular style.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get the label for this style.
	 * 
	 * @return The label for this style.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Get the index for this style within the layer.
	 * 
	 * @return The index for this style within the layer.
	 */
	public int getIndex() {
		return index;
	}
}