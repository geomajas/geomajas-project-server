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

import org.geomajas.puregwt.client.map.event.LayerAddedEvent;
import org.geomajas.puregwt.client.map.event.LayerHideEvent;
import org.geomajas.puregwt.client.map.event.LayerOrderChangedEvent;
import org.geomajas.puregwt.client.map.event.LayerRemovedEvent;
import org.geomajas.puregwt.client.map.event.LayerShowEvent;
import org.geomajas.puregwt.client.map.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.map.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.map.event.MapResizedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.gfx.HtmlGroup;
import org.geomajas.puregwt.client.map.gfx.HtmlObject;
import org.geomajas.puregwt.client.map.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.gfx.VectorGroup;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterLayer;
import org.geomajas.puregwt.client.map.layer.VectorLayer;
import org.geomajas.puregwt.client.spatial.Matrix;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * <p>
 * Renderer for the map that simply delegates to all layers individually.
 * </p>
 * Doesn't listen to layer add events, or layer re-order events or....yet.
 * 
 * @author Pieter De Graef
 */
public class DelegatingMapRenderer implements MapRenderer {

	private LayersModel layersModel;

	private ViewPort viewPort;

	private HtmlContainer htmlContainer;

	private Map<Layer<?>, HtmlContainer> layerContainers;

	private VectorContainer vectorContainer;

	private Map<Layer<?>, VectorContainer> vectorLayerContainers;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	protected DelegatingMapRenderer(LayersModel layersModel, ViewPort viewPort) {
		this.layersModel = layersModel;
		this.viewPort = viewPort;
		layerContainers = new HashMap<Layer<?>, HtmlContainer>();
		vectorLayerContainers = new HashMap<Layer<?>, VectorContainer>();
	}

	// ------------------------------------------------------------------------
	// LayerOrderChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerOrderChanged(LayerOrderChangedEvent event) {
		if (event.getFromIndex() < htmlContainer.getChildCount()) {
			// Source is rasterized layer:
			int toIndex = event.getToIndex() > htmlContainer.getChildCount() ? htmlContainer.getChildCount() : event
					.getToIndex();
			HtmlObject layerContainer = htmlContainer.getChild(event.getFromIndex());
			if (layerContainer != null) {
				htmlContainer.remove(layerContainer);
				htmlContainer.insert(layerContainer, toIndex);
			}
		} else {
			// Source is vector layer:
			int fromIndex = event.getFromIndex() - htmlContainer.getChildCount();
			int toIndex = event.getToIndex() - htmlContainer.getChildCount();
			VectorObject layerContainer = vectorContainer.getVectorObject(fromIndex);
			if (layerContainer != null) {
				vectorContainer.remove(layerContainer);
				vectorContainer.insert(layerContainer, toIndex);
			}
		}
	}

	// ------------------------------------------------------------------------
	// LayerVisibleHandler implementation:
	// ------------------------------------------------------------------------

	public void onShow(LayerShowEvent event) {
		Layer<?> layer = event.getLayer();
		if (layer instanceof RasterLayer) {
			((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
			((RasterLayer) layer).getRenderer().onShow(event);
		} else if (layer instanceof VectorLayer) {
			((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
			((VectorLayer) layer).getRenderer().onShow(event);
		}
	}

	public void onHide(LayerHideEvent event) {
		Layer<?> layer = event.getLayer();
		if (layer instanceof RasterLayer) {
			((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
			((RasterLayer) layer).getRenderer().onHide(event);
		} else if (layer instanceof VectorLayer) {
			((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
			((VectorLayer) layer).getRenderer().onHide(event);
		}
	}

	public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
	}

	// ------------------------------------------------------------------------
	// LayerStyleChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerStyleChanged(LayerStyleChangedEvent event) {
		Layer<?> layer = event.getLayer();
		if (layer instanceof RasterLayer) {
			((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
			((RasterLayer) layer).getRenderer().onLayerStyleChanged(event);
		} else if (layer instanceof VectorLayer) {
			((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
			((VectorLayer) layer).getRenderer().onLayerStyleChanged(event);
		}
	}

	// ------------------------------------------------------------------------
	// MapResizedHandler implementation:
	// ------------------------------------------------------------------------

	public void onMapResized(MapResizedEvent event) {
		Matrix translation = viewPort.getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		int dx = (int) Math.round(translation.getDx());
		int dy = (int) Math.round(translation.getDy());
		htmlContainer.setTop(dy);
		htmlContainer.setLeft(dx);
		vectorContainer.transform(translation);

		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer<?> layer = layersModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().onMapResized(event);
			} else if (layer instanceof VectorLayer) {
				((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
				((VectorLayer) layer).getRenderer().onMapResized(event);
			}
		}
	}

	// ------------------------------------------------------------------------
	// MapCompositionHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerAdded(LayerAddedEvent event) {
		Layer<?> layer = event.getLayer();
		if (layer instanceof RasterLayer) {
			((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
			((RasterLayer) layer).getRenderer().onLayerAdded(event);
		} else if (layer instanceof VectorLayer) {
			((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
			((VectorLayer) layer).getRenderer().onLayerAdded(event);
		}
	}

	public void onLayerRemoved(LayerRemovedEvent event) {
	}

	// ------------------------------------------------------------------------
	// ViewPortChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onViewPortChanged(ViewPortChangedEvent event) {
		Matrix translation = viewPort.getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		int dx = (int) Math.round(translation.getDx());
		int dy = (int) Math.round(translation.getDy());
		htmlContainer.setTop(dy);
		htmlContainer.setLeft(dx);
		vectorContainer.transform(translation);

		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer<?> layer = layersModel.getLayer(i);
			if (layer.isShowing()) {
				if (layer instanceof RasterLayer) {
					((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
					((RasterLayer) layer).getRenderer().onViewPortChanged(event);
				} else if (layer instanceof VectorLayer) {
					((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
					((VectorLayer) layer).getRenderer().onViewPortChanged(event);
				}
			}
		}
	}

	public void onViewPortScaled(ViewPortScaledEvent event) {
		Matrix translation = viewPort.getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		int dx = (int) Math.round(translation.getDx());
		int dy = (int) Math.round(translation.getDy());
		htmlContainer.setTop(dy);
		htmlContainer.setLeft(dx);
		vectorContainer.transform(translation);

		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer<?> layer = layersModel.getLayer(i);
			if (layer.isShowing()) {
				if (layer instanceof RasterLayer) {
					((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
					((RasterLayer) layer).getRenderer().onViewPortScaled(event);
				} else if (layer instanceof VectorLayer) {
					((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
					((VectorLayer) layer).getRenderer().onViewPortScaled(event);
				}
			}
		}
	}

	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		Matrix translation = viewPort.getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		int dx = (int) Math.round(translation.getDx());
		int dy = (int) Math.round(translation.getDy());
		htmlContainer.setTop(dy);
		htmlContainer.setLeft(dx);
		vectorContainer.transform(translation);

		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer<?> layer = layersModel.getLayer(i);
			if (layer.isShowing()) {
				if (layer instanceof RasterLayer) {
					((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
					((RasterLayer) layer).getRenderer().onViewPortTranslated(event);
				} else if (layer instanceof VectorLayer) {
					((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
					((VectorLayer) layer).getRenderer().onViewPortTranslated(event);
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// MapRenderer public methods:
	// ------------------------------------------------------------------------

	public void clear() {
		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer<?> layer = layersModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().clear();
			} else if (layer instanceof VectorLayer) {
				((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
				((VectorLayer) layer).getRenderer().clear();
			}
		}
	}

	public void setMapExentScaleAtFetch(double scale) {
		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer<?> layer = layersModel.getLayer(i);
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).getRenderer().setHtmlContainer(getHtmlContainer(layer));
				((RasterLayer) layer).getRenderer().setMapExentScaleAtFetch(scale);
			} else if (layer instanceof VectorLayer) {
				((VectorLayer) layer).getRenderer().setVectorContainer(getVectorContainer(layer));
				((VectorLayer) layer).getRenderer().setMapExentScaleAtFetch(scale);
			}
		}
	}

	public void setHtmlContainer(HtmlContainer htmlContainer) {
		this.htmlContainer = htmlContainer;
	}

	public void setVectorContainer(VectorContainer vectorContainer) {
		this.vectorContainer = vectorContainer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private HtmlContainer getHtmlContainer(Layer<?> layer) {
		if (layerContainers.containsKey(layer)) {
			return layerContainers.get(layer);
		}
		HtmlGroup layerContainer = new HtmlGroup(htmlContainer.getWidth(), htmlContainer.getHeight());
		htmlContainer.add(layerContainer);
		layerContainers.put(layer, layerContainer);
		return layerContainer;
	}

	private VectorContainer getVectorContainer(Layer<?> layer) {
		if (vectorLayerContainers.containsKey(layer)) {
			return vectorLayerContainers.get(layer);
		}
		VectorGroup container = new VectorGroup();
		vectorContainer.add(container);
		vectorLayerContainers.put(layer, container);
		return container;
	}
}