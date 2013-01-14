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

package org.geomajas.plugin.jsapi.gwt.client.exporter.event;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.plugin.jsapi.client.event.FeatureDeselectedHandler;
import org.geomajas.plugin.jsapi.client.event.FeatureSelectedHandler;
import org.geomajas.plugin.jsapi.client.event.JsEventBus;
import org.geomajas.plugin.jsapi.client.event.JsHandlerRegistration;
import org.geomajas.plugin.jsapi.client.event.LayersModelChangedEvent;
import org.geomajas.plugin.jsapi.client.event.LayersModelChangedHandler;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.feature.Feature;
import org.geomajas.plugin.jsapi.client.map.layer.FeaturesSupported;
import org.geomajas.plugin.jsapi.gwt.client.exporter.map.MapImpl;
import org.geomajas.plugin.jsapi.gwt.client.exporter.map.feature.FeatureImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Central event bus for handler registration and event firing.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("JsEventBus")
@ExportPackage("org.geomajas.jsapi.event")
@Api(allMethods = true)
public class JsEventBusImpl implements Exportable, JsEventBus {

	private Map map;

	/** No-args constructor for GWT. */
	public JsEventBusImpl() {
	}

	/** Construct event bus for specific map. */
	public JsEventBusImpl(Map map) {
		this.map = map;
	}

	/**
	 * Add a handler to change events in the layer configuration from the map. This event is fired for example when the
	 * map gets it's configuration from the server. Only then can it know what layers it has available.
	 */
	public JsHandlerRegistration addLayersModelChangedHandler(final LayersModelChangedHandler handler) {
		HandlerRegistration registration = ((MapImpl) map).getMapWidget().getMapModel()
				.addMapModelChangedHandler(new MapModelChangedHandler() {

					public void onMapModelChanged(MapModelChangedEvent event) {
						handler.onLayersModelChanged(new LayersModelChangedEvent(map.getLayersModel()));
					}
				});
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Add a handler for feature selection.
	 */
	public JsHandlerRegistration addFeatureSelectionHandler(final FeatureSelectedHandler selectedHandler,
			final FeatureDeselectedHandler deselectedHandler) {
		if (((MapImpl) map).getMapWidget().getMapModel().isInitialized()) {
			return addFeatureSelectionHandler2(selectedHandler, deselectedHandler);
		}
		final CallbackHandlerRegistration callbackRegistration = new CallbackHandlerRegistration();
		((MapImpl) map).getMapWidget().getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {

			public void onMapModelChanged(MapModelChangedEvent event) {
				JsHandlerRegistration temp = addFeatureSelectionHandler2(selectedHandler, deselectedHandler);
				callbackRegistration.setRegistration(temp);
			}
		});
		return callbackRegistration;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private JsHandlerRegistration addFeatureSelectionHandler2(final FeatureSelectedHandler selectedHandler,
			final FeatureDeselectedHandler deselectedHandler) {

		final List<VectorLayer> layers = ((MapImpl) map).getMapWidget().getMapModel().getVectorLayers();
		final HandlerRegistration[] registrations = new HandlerRegistration[layers.size()];

		for (int i = 0; i < layers.size(); i++) {
			final FeaturesSupported fs = (FeaturesSupported) map.getLayersModel().getLayer(layers.get(i).getId());

			registrations[i] = layers.get(i).addFeatureSelectionHandler(new FeatureSelectionHandler() {

				public void onFeatureSelected(FeatureSelectedEvent event) {
					Feature feature = new FeatureImpl(event.getFeature().toDto(), fs);
					selectedHandler.onFeatureSelected(new org.geomajas.plugin.jsapi.client.event.FeatureSelectedEvent(
							feature));
				}

				public void onFeatureDeselected(FeatureDeselectedEvent event) {
					Feature feature = new FeatureImpl(event.getFeature().toDto(), fs);
					deselectedHandler
							.onFeatureDeselected(new org.geomajas.plugin.jsapi.client.event.FeatureDeselectedEvent(
									feature));
				}
			});
		}

		return new JsHandlerRegistration(registrations);
	}

	/**
	 * Handler registration extension used when the actual handles are known (and set) at a later time. This class is
	 * usually used when we can't immediately add handlers but still need to return a registration.
	 * 
	 * @author Pieter De Graef
	 */
	@Export
	@ExportPackage("org.geomajas.jsapi.event")
	private class CallbackHandlerRegistration extends JsHandlerRegistration implements Exportable {

		private JsHandlerRegistration reg;

		public CallbackHandlerRegistration() {
			super(null);
		}

		public void removeHandler() {
			if (reg != null) {
				reg.removeHandler();
			}
		}

		public void setRegistration(JsHandlerRegistration reg) {
			this.reg = reg;
		}
	}
}