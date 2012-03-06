package org.geomajas.puregwt.client.map.render;

import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.LayersModel;
import org.geomajas.puregwt.client.map.ViewPort;

public class TestMapRendererFactory implements MapRendererFactory {

	@Override
	public MapRenderer create(LayersModel layersModel, ViewPort viewPort, HtmlContainer htmlContainer) {
		return new MapRendererImpl(layersModel, viewPort, htmlContainer);
	}

}
