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
package org.geomajas.widget.featureinfo.client.controller;

import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.action.toolbar.MultiLayerFeatureInfoModalAction;
import org.geomajas.widget.featureinfo.client.action.toolbar.TooltipOnMouseoverModalAction;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Combination of Pan, MultiLayerFeatureInfo and TooltipOnMouseover controllers.
 * Dragging == pan, hovering == tooltip, clicking == multiFinfo.
 * 
 * @author Kristof Heirwegh
 */
public class CombinedFeatureInfoController extends AbstractGraphicsController {

	private static final String TOOLTIP_KEY = "TooltipOnMouseOverMode";
	private static final String MULTIFO_KEY = "MultilayerFeatureInfoMode";

	private PanController panC;
	private TooltipOnMouseoverController tomC;
	private MultiLayerFeatureInfoController mlfC;

	public CombinedFeatureInfoController(MapWidget mapWidget) {
		super(mapWidget);

		// -- going roundabout to get configuration
		TooltipOnMouseoverModalAction toolAction = (TooltipOnMouseoverModalAction) ToolbarRegistry.getToolbarAction(
				TOOLTIP_KEY, mapWidget);
		MultiLayerFeatureInfoModalAction multiAction = (MultiLayerFeatureInfoModalAction) ToolbarRegistry
				.getToolbarAction(MULTIFO_KEY, mapWidget);
		panC = new PanController(mapWidget);
		tomC = toolAction.getController();
		mlfC = multiAction.getController();
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		panC.onMouseDown(event);
		tomC.onMouseDown(event);
		mlfC.onMouseDown(event);
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		panC.onMouseUp(event);
		tomC.onMouseUp(event);
		mlfC.onMouseUp(event);
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		panC.onMouseMove(event);
		tomC.onMouseMove(event);
		mlfC.onMouseMove(event);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		panC.onMouseOut(event);
		tomC.onMouseOut(event);
		mlfC.onMouseOut(event);
	}

	@Override
	public void onDeactivate() {
		super.onDeactivate();
		panC.onDeactivate();
		tomC.onDeactivate();
		mlfC.onDeactivate();
	}
}
