/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * FlowPanel containing a customised {@link SliderBar}.
 * 
 * @author Jan Venstermans
 * 
 */
public class TransparencySliderBar extends FlowPanel {

	private SliderBar slider;
	
	public TransparencySliderBar() {
		add(makeNewSliderFlowPanel());
		this.setStyleName("transparency-SliderBar-panel");
	}
	
	private SliderBar makeNewSliderFlowPanel() {
		slider = new SliderBar(0d, 1d);
		slider.setNumLabels(5);
		slider.setNumTicks(10);
		slider.setStepSize(0.05);
		slider.setNumTicks(10);
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		slider.addMouseDownHandler(preventWeirdBehaviourHandler);
		slider.addMouseUpHandler(preventWeirdBehaviourHandler);
		slider.addClickHandler(preventWeirdBehaviourHandler);
		slider.addDoubleClickHandler(preventWeirdBehaviourHandler);
		return slider;
	}
	
	public void setCurrentValue(double value) {
		slider.setCurrentValue(value);
	}

	public double getCurrentValue() {
		return slider.getCurrentValue();
	}

	/**
	 * Combination of different handlers with a single goal: stop all the events from propagating to the map. This is
	 * meant to be used for clickable widgets.
	 * 
	 * @author Pieter De Graef
	 */
	public class StopPropagationHandler implements MouseDownHandler, MouseUpHandler, ClickHandler, DoubleClickHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			event.stopPropagation();
		}

		public void onClick(ClickEvent event) {
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			event.stopPropagation();
			event.preventDefault();
		}

		public void onMouseUp(MouseUpEvent event) {
			event.stopPropagation();
		}
	}
}
