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

package org.geomajas.plugin.wmsclient.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wmsclient.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.puregwt.client.controller.AbstractMapController;
import org.geomajas.puregwt.client.map.feature.Feature;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Default map controller that executes WMS GetFeatureInfo requests on the registered layers.
 * 
 * @author Pieter De Graef
 */
public class WmsGetFeatureInfoController extends AbstractMapController {

	private final List<FeaturesSupportedWmsLayer> layers;

	private Callback<List<Feature>, String> gmlCallback;

	private Callback<String, String> htmlCallback;

	private GetFeatureInfoFormat format = GetFeatureInfoFormat.GML;

	public WmsGetFeatureInfoController() {
		this(null);
	}

	public WmsGetFeatureInfoController(FeaturesSupportedWmsLayer layer) {
		super(false);
		this.layers = new ArrayList<FeaturesSupportedWmsLayer>();
		if (layer != null) {
			addLayer(layer);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		// Do not interfere with default behaviour:
		super.onMouseUp(event);

		// Get the event location in world space:
		Coordinate worldLocation = getLocation(event, RenderSpace.WORLD);

		// Now execute the GetFeatureInfo for each layer:
		for (FeaturesSupportedWmsLayer layer : layers) {
			switch (format) {
				case GML:
					layer.getFeatureInfo(worldLocation, gmlCallback);
					break;
				default:
					layer.getFeatureInfo(worldLocation, format, htmlCallback);
			}
		}
	}

	/**
	 * Add a layer for which a GetFeatureRequest should be fired on click.
	 * 
	 * @param layer
	 *            The layer to add.
	 */
	public void addLayer(FeaturesSupportedWmsLayer layer) {
		layers.add(layer);
	}

	/**
	 * Remove a layer for which a GetFeatureInfoRequest should no longer be fired on click.
	 * 
	 * @param layer
	 *            The layer to remove again.
	 */
	public void removeLayer(FeaturesSupportedWmsLayer layer) {
		layers.remove(layer);
	}

	/**
	 * Get the default GetFeatureInfoFormat. By default this is {@link GetFeatureInfoFormat#GML}.
	 * 
	 * @return the GetFeatureInfoFormat used.
	 */
	public GetFeatureInfoFormat getFormat() {
		return format;
	}

	/**
	 * Set a new GetFeatureInfoFormat to use in the GetFeatureInfoRequest.
	 * 
	 * @param format
	 *            The new GetFeatureInfoFormat.
	 */
	public void setFormat(GetFeatureInfoFormat format) {
		this.format = format;
	}

	/**
	 * Set the callback to use in case the GetFeatureInfoFormat is {@link GetFeatureInfoFormat#GML}.
	 * 
	 * @param gmlCallback
	 *            The callback to execute when the response returns. This response already contains {@link Feature}
	 *            objects, and should not be parsed anymore.
	 */
	public void setGmlCallback(Callback<List<Feature>, String> gmlCallback) {
		this.gmlCallback = gmlCallback;
	}

	/**
	 * Set the callback to use in case the GetFeatureInfoFormat is NOT {@link GetFeatureInfoFormat#GML}.
	 * 
	 * @param gmlCallback
	 *            The callback to execute when the response returns. Note that the response is the bare boned WMS
	 *            GetFeatureInfo. It is up to you to parse it.
	 */
	public void setHtmlCallback(Callback<String, String> htmlCallback) {
		this.htmlCallback = htmlCallback;
	}
}