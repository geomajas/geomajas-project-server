package org.geomajas.puregwt.client.map.render;

import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.Layer;


public class TestMapScalesRendererFactory implements MapScalesRendererFactory {

	@Override
	public MapScalesRenderer create(ViewPort viewPort, Layer<?> layer, HtmlContainer htmlContainer) {
		return new LayerScalesRenderer(viewPort, layer, htmlContainer);
	}

}
