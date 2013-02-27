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
package org.geomajas.puregwt.client.map;

import org.geomajas.puregwt.client.event.FeatureDeselectedEvent;
import org.geomajas.puregwt.client.event.FeatureSelectedEvent;
import org.geomajas.puregwt.client.event.FeatureSelectionHandler;
import org.geomajas.puregwt.client.event.LayerDeselectedEvent;
import org.geomajas.puregwt.client.event.LayerHideEvent;
import org.geomajas.puregwt.client.event.LayerLabelHideEvent;
import org.geomajas.puregwt.client.event.LayerLabelMarkedEvent;
import org.geomajas.puregwt.client.event.LayerLabelShowEvent;
import org.geomajas.puregwt.client.event.LayerLabeledHandler;
import org.geomajas.puregwt.client.event.LayerOrderChangedHandler;
import org.geomajas.puregwt.client.event.LayerRefreshedEvent;
import org.geomajas.puregwt.client.event.LayerRefreshedHandler;
import org.geomajas.puregwt.client.event.LayerSelectedEvent;
import org.geomajas.puregwt.client.event.LayerSelectionHandler;
import org.geomajas.puregwt.client.event.LayerShowEvent;
import org.geomajas.puregwt.client.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.event.LayerStyleChangedHandler;
import org.geomajas.puregwt.client.event.LayerVisibilityHandler;
import org.geomajas.puregwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.event.MapCompositionHandler;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.event.MapResizedHandler;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Default implementation of {@link MapEventBus}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MapEventBusImpl implements MapEventBus {

	private final EventBus eventBus;

	private final Object source;

	public MapEventBusImpl(final Object source, final EventBus eventBus) {
		this.eventBus = eventBus;
		this.source = source;
	}

	public HandlerRegistration addFeatureSelectionHandler(FeatureSelectionHandler handler) {
		return eventBus.addHandlerToSource(FeatureSelectionHandler.TYPE, source, handler);
	}

	public HandlerRegistration addFeatureSelectionHandler(FeatureSelectionHandler handler, Layer layer) {
		return eventBus.addHandlerToSource(FeatureSelectionHandler.TYPE, source, new FeatureSelectionWrapper(handler,
				layer));
	}

	public HandlerRegistration addLayerLabeledHandler(LayerLabeledHandler handler) {
		return eventBus.addHandlerToSource(LayerLabeledHandler.TYPE, source, handler);
	}

	public HandlerRegistration addLayerLabeledHandler(LayerLabeledHandler handler, Layer layer) {
		return eventBus.addHandlerToSource(LayerLabeledHandler.TYPE, source, new LayerLabeledWrapper(handler, layer));
	}

	public HandlerRegistration addLayerOrderChangedHandler(LayerOrderChangedHandler handler) {
		return eventBus.addHandlerToSource(LayerOrderChangedHandler.TYPE, source, handler);
	}

	public HandlerRegistration addLayerRefreshedHandler(LayerRefreshedHandler handler) {
		return eventBus.addHandlerToSource(LayerRefreshedHandler.TYPE, source, handler);
	}

	public HandlerRegistration addLayerRefreshedHandler(LayerRefreshedHandler handler, Layer layer) {
		return eventBus.addHandlerToSource(LayerRefreshedHandler.TYPE, source,
				new LayerRefreshedWrapper(handler, layer));
	}

	public HandlerRegistration addLayerSelectionHandler(LayerSelectionHandler handler) {
		return eventBus.addHandlerToSource(LayerSelectionHandler.TYPE, source, handler);
	}

	public HandlerRegistration addLayerSelectionHandler(LayerSelectionHandler handler, Layer layer) {
		return eventBus.addHandlerToSource(LayerSelectionHandler.TYPE, source,
				new LayerSelectionWrapper(handler, layer));
	}

	public HandlerRegistration addLayerStyleChangedHandler(LayerStyleChangedHandler handler) {
		return eventBus.addHandlerToSource(LayerStyleChangedHandler.TYPE, source, handler);
	}

	public HandlerRegistration addLayerStyleChangedHandler(LayerStyleChangedHandler handler, Layer layer) {
		return eventBus.addHandlerToSource(LayerStyleChangedHandler.TYPE, source, new LayerStyleChangedWrapper(handler,
				layer));
	}

	public HandlerRegistration addLayerVisibilityHandler(LayerVisibilityHandler handler) {
		return eventBus.addHandlerToSource(LayerVisibilityHandler.TYPE, source, handler);
	}

	public HandlerRegistration addLayerVisibilityHandler(LayerVisibilityHandler handler, Layer layer) {
		return eventBus.addHandlerToSource(LayerVisibilityHandler.TYPE, source, new LayerVisibilityWrapper(handler,
				layer));
	}

	public HandlerRegistration addMapCompositionHandler(MapCompositionHandler handler) {
		return eventBus.addHandlerToSource(MapCompositionHandler.TYPE, source, handler);
	}

	public HandlerRegistration addMapInitializationHandler(MapInitializationHandler handler) {
		return eventBus.addHandlerToSource(MapInitializationHandler.TYPE, source, handler);
	}

	public HandlerRegistration addMapResizedHandler(MapResizedHandler handler) {
		return eventBus.addHandlerToSource(MapResizedHandler.TYPE, source, handler);
	}

	public HandlerRegistration addViewPortChangedHandler(ViewPortChangedHandler handler) {
		return eventBus.addHandlerToSource(ViewPortChangedHandler.TYPE, source, handler);
	}

	public <H> void fireEvent(Event<H> event) {
		eventBus.fireEventFromSource(event, source);
	}

	public <H> HandlerRegistration addHandler(Type<H> type, H handler) {
		return eventBus.addHandlerToSource(type, source, handler);
	}

	/**
	 * Wraps {@link FeatureSelectionHandler} for layer filtering.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class FeatureSelectionWrapper implements FeatureSelectionHandler {

		private Layer layer;

		private FeatureSelectionHandler handler;

		public FeatureSelectionWrapper(FeatureSelectionHandler handler, Layer layer) {
			this.layer = layer;
			this.handler = handler;
		}

		public void onFeatureSelected(FeatureSelectedEvent event) {
			if (layer == event.getLayer()) {
				handler.onFeatureSelected(event);
			}
		}

		public void onFeatureDeselected(FeatureDeselectedEvent event) {
			if (layer == event.getLayer()) {
				handler.onFeatureDeselected(event);
			}
		}
	}

	/**
	 * Wraps {@link LayerLabeledHandler} for layer filtering.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class LayerLabeledWrapper implements LayerLabeledHandler {

		private Layer layer;

		private LayerLabeledHandler handler;

		public LayerLabeledWrapper(LayerLabeledHandler handler, Layer layer) {
			this.layer = layer;
			this.handler = handler;
		}

		public void onLabelShow(LayerLabelShowEvent event) {
			if (layer == event.getLayer()) {
				handler.onLabelShow(event);
			}
		}

		public void onLabelHide(LayerLabelHideEvent event) {
			if (layer == event.getLayer()) {
				handler.onLabelHide(event);
			}
		}

		public void onLabelMarked(LayerLabelMarkedEvent event) {
			if (layer == event.getLayer()) {
				handler.onLabelMarked(event);
			}
		}

	}

	/**
	 * Wraps {@link LayerRefreshedHandler} for layer filtering.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class LayerRefreshedWrapper implements LayerRefreshedHandler {

		private Layer layer;

		private LayerRefreshedHandler handler;

		public LayerRefreshedWrapper(LayerRefreshedHandler handler, Layer layer) {
			this.layer = layer;
			this.handler = handler;
		}

		public void onLayerRefreshed(LayerRefreshedEvent event) {
			if (layer == event.getLayer()) {
				handler.onLayerRefreshed(event);
			}
		}

	}

	/**
	 * Wraps {@link LayerSelectionHandler} for layer filtering.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class LayerSelectionWrapper implements LayerSelectionHandler {

		private Layer layer;

		private LayerSelectionHandler handler;

		public LayerSelectionWrapper(LayerSelectionHandler handler, Layer layer) {
			this.layer = layer;
			this.handler = handler;
		}

		public void onSelectLayer(LayerSelectedEvent event) {
			if (layer == event.getLayer()) {
				handler.onSelectLayer(event);
			}
		}

		public void onDeselectLayer(LayerDeselectedEvent event) {
			if (layer == event.getLayer()) {
				handler.onDeselectLayer(event);
			}
		}
	}

	/**
	 * Wraps {@link LayerStyleChangedHandler} for layer filtering.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class LayerStyleChangedWrapper implements LayerStyleChangedHandler {

		private Layer layer;

		private LayerStyleChangedHandler handler;

		public LayerStyleChangedWrapper(LayerStyleChangedHandler handler, Layer layer) {
			this.layer = layer;
			this.handler = handler;
		}

		public void onLayerStyleChanged(LayerStyleChangedEvent event) {
			if (layer == event.getLayer()) {
				handler.onLayerStyleChanged(event);
			}
		}

	}

	/**
	 * Wraps {@link LayerVisibilityHandler} for layer filtering.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class LayerVisibilityWrapper implements LayerVisibilityHandler {

		private Layer layer;

		private LayerVisibilityHandler handler;

		public LayerVisibilityWrapper(LayerVisibilityHandler handler, Layer layer) {
			this.layer = layer;
			this.handler = handler;
		}

		public void onShow(LayerShowEvent event) {
			if (layer == event.getLayer()) {
				handler.onShow(event);
			}
		}

		public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
			if (layer == event.getLayer()) {
				handler.onVisibilityMarked(event);
			}
		}

		public void onHide(LayerHideEvent event) {
			if (layer == event.getLayer()) {
				handler.onHide(event);
			}
		}
	}

}
