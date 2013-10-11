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

package org.geomajas.puregwt.client.map.render;

import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.MapConfiguration;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.LayersModel;

/**
 * Test implementation of the {@link MapRendererFactory}.
 * 
 * @author Jan De Moerloose
 */
public class MockMapRendererFactory implements MapRendererFactory {

	public MapRenderer create(LayersModel layersModel, ViewPort viewPort, MapConfiguration configuration,
			HtmlContainer htmlContainer) {
		return new MockMapRenderer();
	}
}