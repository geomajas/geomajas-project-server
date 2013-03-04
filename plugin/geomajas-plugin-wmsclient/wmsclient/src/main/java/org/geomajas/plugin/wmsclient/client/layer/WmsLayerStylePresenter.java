/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wmsclient.client.layer;

import org.geomajas.puregwt.client.map.layer.LayerStylePresenter;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Style presenter for WMS layers. Uses a URL that should point to the WMS GetLegendGraphic URL.
 * 
 * @author Pieter De Graef
 */
public class WmsLayerStylePresenter implements LayerStylePresenter {

	private final Image image;

	public WmsLayerStylePresenter(String url) {
		image = new Image(url);
	}

	/** Returns an Image widget that points to the WMS GetLegendGraphic URL. */
	public Widget asWidget() {
		return image;
	}

	/** Always returns 0. There will be only one LayerStylePresenter per WMS layer. */
	public int getIndex() {
		return 0;
	}
}