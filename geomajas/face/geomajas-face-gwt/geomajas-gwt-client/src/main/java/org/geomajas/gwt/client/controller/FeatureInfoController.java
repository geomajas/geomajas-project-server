/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.controller;

import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Shows information about the clicked feature.
 * 
 * @author Frank Wynants
 */
public class FeatureInfoController extends AbstractGraphicsController {

	public FeatureInfoController(MapWidget mapWidget) {
		super(mapWidget);
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		Layer<?> selectedLayer = mapWidget.getMapModel().getSelectedLayer();
		if (selectedLayer instanceof VectorLayer) {
			// TODO This should show info about the feature the user has clicked using
			// clickedLayer.getFeatureStore().getFeatures()
		}
	}
}
