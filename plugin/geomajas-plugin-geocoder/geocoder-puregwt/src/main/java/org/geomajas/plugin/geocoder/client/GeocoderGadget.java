/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.client;

import org.geomajas.puregwt.client.map.gadget.AbstractMapGadget;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map gadget that contains a {@link GeocoderTextBox} with a clickable magnifying glass.
 * <p><b>Tip</b>: To position next to the {@link org.geomajas.puregwt.client.map.gadget.ZoomToRectangleGadget}, 
 * set top to 5 and left to 90 with <code>new GeocoderGadget(5, 90)</code></p>
 * @author Emiel Ackermann
 *
 */
public class GeocoderGadget extends AbstractMapGadget {
	
	private static final String GM_GEOCODER_GADGET = "gm-GeocoderGadget";
	private static final String GM_GEOCODER_GADGET_TEXT_BOX = "gm-GeocoderGadget-textBox";
	/**
	 * Extra css to style the tip
	 */
	private static final String GM_GEOCODER_GADGET_TIP = "gm-GeocoderGadget-tip";
	private static final String FIND_PLACE_ON_MAP = "Find place on map";
	
	private FlowPanel layout;
	private GeocoderTextBox box;

	public GeocoderGadget(int top, int left) {
		setHorizontalMargin(left);
		setVerticalMargin(top);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Widget asWidget() {
		if (layout == null) {
			buildGui();
		}
		return layout;
	}
	
	private void buildGui() {
		layout = new FlowPanel();
		layout.setStyleName(GM_GEOCODER_GADGET);

		box = new GeocoderTextBox(mapPresenter);
		box.setStyleName(GM_GEOCODER_GADGET_TEXT_BOX);
		box.setValue(FIND_PLACE_ON_MAP);
		box.addStyleName(GM_GEOCODER_GADGET_TIP);
		
		// All handlers are removed from the TextBox. Text selection by dragging not reinstated.
		box.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				box.setFocus(true);
				if (FIND_PLACE_ON_MAP.equals(box.getValue())) {
					box.setValue(null);
					box.removeStyleName(GM_GEOCODER_GADGET_TIP);
				}
				event.stopPropagation();
			}
		});
		box.addBlurHandler(new BlurHandler() {
			
			public void onBlur(BlurEvent event) {
				if (box.getValue() == null || "".equals(box.getValue())) {
					box.setValue(FIND_PLACE_ON_MAP);
					box.addStyleName(GM_GEOCODER_GADGET_TIP);
				}
			}
		});
		layout.add(box);
		layout.add(createMagnifyingGlass());
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		layout.addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		// ClickEvent is needed by local widgets
		layout.addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());
	}

	private Widget createMagnifyingGlass() {
		Button glass = new Button();
		glass.setStyleName("gm-GeocoderGadget-glass");
		glass.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				box.goToLocation();
				event.stopPropagation();
			}
		});
		return glass;
	}

}
