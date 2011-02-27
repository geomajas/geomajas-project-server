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

package org.geomajas.puregwt.client.map;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortDraggedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterLayer;

/**
 * <p>
 * Renderer for the map that simply delegates to all layers individually.
 * </p>
 * Doesn't listen to layer add events, or layer re-order events or....yet.
 * 
 * @author Pieter De Graef
 */
public class DelegatingMapRenderer implements MapRenderer {

	private HtmlContainer htmlContainer;

	private Map<Layer<?>, HtmlContainer> layerContainers;

	private MapModel mapModel;

	public DelegatingMapRenderer(MapModel mapModel) {
		this.mapModel = mapModel;
		layerContainers = new HashMap<Layer<?>, HtmlContainer>();
	}

	public void onViewPortChanged(ViewPortChangedEvent event) {
		Coordinate translation = mapModel.getViewPort().getPanToViewTranslation();
		htmlContainer.setTop((int) Math.round(translation.getY()));
		htmlContainer.setLeft((int) Math.round(translation.getX()));
		for (int i = 0; i < mapModel.getLayerCount(); i++) {
			Layer<?> layer = mapModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().onViewPortChanged(event);
			}
		}
	}

	public void onViewPortScaled(ViewPortScaledEvent event) {
		Coordinate translation = mapModel.getViewPort().getPanToViewTranslation();
		htmlContainer.setTop((int) Math.round(translation.getY()));
		htmlContainer.setLeft((int) Math.round(translation.getX()));
		for (int i = 0; i < mapModel.getLayerCount(); i++) {
			Layer<?> layer = mapModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().onViewPortScaled(event);
			}
		}
	}

	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		Coordinate translation = mapModel.getViewPort().getPanToViewTranslation();
		htmlContainer.setTop((int) Math.round(translation.getY()));
		htmlContainer.setLeft((int) Math.round(translation.getX()));
		for (int i = 0; i < mapModel.getLayerCount(); i++) {
			Layer<?> layer = mapModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().onViewPortTranslated(event);
			}
		}
	}

	public void onViewPortDragged(ViewPortDraggedEvent event) {
		Coordinate translation = mapModel.getViewPort().getPanToViewTranslation();
		htmlContainer.setTop((int) Math.round(translation.getY()));
		htmlContainer.setLeft((int) Math.round(translation.getX()));
		for (int i = 0; i < mapModel.getLayerCount(); i++) {
			Layer<?> layer = mapModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().onViewPortDragged(event);
			}
		}
	}

	public void clear() {
		for (int i = 0; i < mapModel.getLayerCount(); i++) {
			Layer<?> layer = mapModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().clear();
			}
		}
	}

	public void redraw() {
		for (int i = 0; i < mapModel.getLayerCount(); i++) {
			Layer<?> layer = mapModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().redraw();
			}
		}
	}

	public void setMapExentScaleAtFetch(double scale) {
		for (int i = 0; i < mapModel.getLayerCount(); i++) {
			Layer<?> layer = mapModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().setMapExentScaleAtFetch(scale);
			}
		}
	}

	public void setHtmlContainer(HtmlContainer htmlContainer) {
		this.htmlContainer = htmlContainer;
	}

	private HtmlContainer getHtmlContainer(Layer<?> layer) {
		if (layerContainers.containsKey(layer)) {
			return layerContainers.get(layer);
		}
		HtmlContainer layerContainer = new HtmlContainer(htmlContainer.getWidth(), htmlContainer.getHeight());
		htmlContainer.add(layerContainer);
		layerContainers.put(layer, layerContainer);
		return layerContainer;
	}
}