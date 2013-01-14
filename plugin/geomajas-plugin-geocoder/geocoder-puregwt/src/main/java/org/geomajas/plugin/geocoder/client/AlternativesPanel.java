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

package org.geomajas.plugin.geocoder.client;

import org.geomajas.plugin.geocoder.client.event.SelectAlternativeEvent;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeHandler;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A {@link PopupPanel} extension for the {@link GetLocationForStringAlternative}s 
 * given by a {@link SelectAlternativeEvent}.
 * 
 * @author Emiel Ackermann
 */
public class AlternativesPanel extends PopupPanel implements SelectAlternativeHandler {

	private final GeocoderTextBox box;
	private VerticalPanel verticalPanel;
	
	/**
	 * Main constructor.
	 * 
	 * @param box
	 * 			the {@link GeocoderTextBox} that could give alternatives
	 */
	public AlternativesPanel(final GeocoderTextBox box) {
		super(true);
		this.box = box;
		box.setSelectAlternativeHandler(this);
		setStyleName(GeocoderGadget.GM_GEOCODER_GADGET_ALT_PANEL);
		verticalPanel = new VerticalPanel();
		add(verticalPanel);
		addCloseHandler(new CloseHandler<PopupPanel>() {
			
			public void onClose(CloseEvent<PopupPanel> event) {
				box.removeStyleName(GeocoderGadget.GM_GEOCODER_GADGET_TEXT_BOX_WITH_ALTS);
			}
		});
	}
	
	/**
	 * Always reset position in case the textbox has been moved (map resize, etc).
	 * {@inheritDoc}
	 */
	@Override
	protected void onLoad() {
		super.onLoad();
		int left = (int) box.getAbsoluteLeft();
		int top = (int) box.getAbsoluteTop() + box.getOffsetHeight();
		setWidth(String.valueOf(box.getOffsetWidth()));
		setPopupPosition(left, top);
	}
	
	/** {@inheritDoc} */
	public void onSelectAlternative(SelectAlternativeEvent event) {
		verticalPanel.clear();
		for (GetLocationForStringAlternative alternative : event.getAlternatives()) {
			final String altText = alternative.getCanonicalLocation();
			Label altLabel = new Label(altText);
			altLabel.setStyleName(GeocoderGadget.GM_GEOCODER_GADGET_ALT_LABEL);
			altLabel.addClickHandler(new ClickHandler() {
				
				public void onClick(ClickEvent event) {
					box.goToLocation(altText);
					hide();
				}
			});
			verticalPanel.add(altLabel);
		}
		show();
		box.addStyleName(GeocoderGadget.GM_GEOCODER_GADGET_TEXT_BOX_WITH_ALTS);
	}

}
