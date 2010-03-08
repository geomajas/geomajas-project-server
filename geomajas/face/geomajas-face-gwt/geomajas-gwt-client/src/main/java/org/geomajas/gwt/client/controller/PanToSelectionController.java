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

import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Allows zooming to a selection.
 * 
 * @author Frank Wynants
 */
public class PanToSelectionController extends RectangleController {

	// TODO This needs to be reimplemented to do a pan to the current selection 
	// Don't use a controller and modelAction... Base yourself on ZoomPreviousAction for example
	// This class need to be removed
	public PanToSelectionController(MapWidget mapWidget) {
		super(mapWidget);
	}

	@Override
	protected void selectRectangle(Bbox worldBounds) {
		mapWidget.getMapModel().getMapView().applyBounds(worldBounds, MapView.ZoomOption.LEVEL_CHANGE);
	}
}
