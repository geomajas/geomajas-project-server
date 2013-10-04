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

package org.geomajas.gwt2.client.map.render;

import org.geomajas.gwt2.client.gfx.HtmlContainer;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.render.MapRenderer;
import org.geomajas.gwt2.client.map.render.MapRendererFactory;

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