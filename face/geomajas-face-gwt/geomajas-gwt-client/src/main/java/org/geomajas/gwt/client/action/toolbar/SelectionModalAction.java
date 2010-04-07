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

package org.geomajas.gwt.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.controller.SelectionController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * <p>
 * Selection tool. You can either select individual features by indicating them, or drag a rectangle and select all
 * features which fall (for a minimum percentage) inside this rectangle.
 * </p>
 * <p>
 * Possible configuration options (server-side configuration) are:
 * <ul>
 * <li><b>clickTimeout</b>: Timeout in milliseconds for handling as click versus dragging.</li>
 * <li><b>coverageRatio</b>: Coverage percentage which is used to determine a feature as selected. This is only used
 * when dragging a rectangle to select in. Must be a floating value between 0 and 1.</li>
 * <li><b>priorityToSelectedLayer</b>: Activate or disable priority to the selected layer. This works only if there is a
 * selected layer, and that selected layer is a <code>VectorLayer</code>. In all other cases, the selection toggle will
 * occur on the first object that is encountered. In other words it will depend on the layer drawing order, starting at
 * the top.</li>
 * <li><b>pixelTolerance</b>: Number of pixels that describes the tolerance allowed when trying to select features.</li>
 * </ul>
 * </p>
 * 
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
public class SelectionModalAction extends ToolbarModalAction implements ConfigurableAction {

	private MapWidget map;

	private int clickTimeout = 1000; // milliseconds to determine whether it was a click or drag event

	private float coverageRatio = .7f; // percentage of area which needs to be inside selected area for selection

	private boolean priorityToSelectedLayer;

	private int pixelTolerance = 5;

	public SelectionModalAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/select.png", I18nProvider.getToolbar().selectionSelect());
		this.map = mapWidget;
	}

	public void onSelect(ClickEvent event) {
		map.setController(new SelectionController(map, clickTimeout, coverageRatio, priorityToSelectedLayer,
				pixelTolerance));
	}

	public void onDeselect(ClickEvent event) {
		map.setController(null);
	}

	public void configure(String key, String value) {
		if ("clickTimeout".equals(key)) {
			clickTimeout = Integer.parseInt(value);
		}
		if ("coverageRatio".equals(key)) {
			coverageRatio = Float.parseFloat(value);
		}
		if ("priorityToSelectedLayer".equals(key)) {
			priorityToSelectedLayer = Boolean.parseBoolean(value);
		}
		if ("pixelTolerance".equals(key)) {
			pixelTolerance = Integer.parseInt(value);
		}
	}

	// Getters:

	public int getClickTimeout() {
		return clickTimeout;
	}

	public float getCoverageRatio() {
		return coverageRatio;
	}

	public boolean isPriorityToSelectedLayer() {
		return priorityToSelectedLayer;
	}

	public int getPixelTolerance() {
		return pixelTolerance;
	}
}
