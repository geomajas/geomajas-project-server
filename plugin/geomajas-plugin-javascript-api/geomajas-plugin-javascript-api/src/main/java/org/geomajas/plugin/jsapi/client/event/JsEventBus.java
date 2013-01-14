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

package org.geomajas.plugin.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

/**
 * Definition of a map-centric event bus. If defines methods for all supported event types that apply on a map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
public interface JsEventBus extends Exportable {

	/** {@inheritDoc} */
	JsHandlerRegistration addLayersModelChangedHandler(LayersModelChangedHandler handler);

	/** {@inheritDoc} */
	JsHandlerRegistration addFeatureSelectionHandler(FeatureSelectedHandler selectedHandler,
			FeatureDeselectedHandler deselectedHandler);
}