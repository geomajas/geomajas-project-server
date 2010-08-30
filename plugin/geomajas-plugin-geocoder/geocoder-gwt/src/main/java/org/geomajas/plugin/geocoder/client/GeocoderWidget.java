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

package org.geomajas.plugin.geocoder.client;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.IconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.IconClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeEvent;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeHandler;
import org.geomajas.plugin.geocoder.client.event.SelectLocationEvent;
import org.geomajas.plugin.geocoder.client.event.SelectLocationHandler;

/**
 * Widget for starting a geocoder location search.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class GeocoderWidget extends DynamicForm {

	private TextItem textItem;
	private GeocoderPresenter presenter;
	private MapWidget map;

	/**
	 * Create geocoder widget which allows searching a location from a string.
	 *
	 * @param map map to apply search results
	 * @param name widget name
	 * @param title label which is displayed left of the widget
	 */
	@Api
	public GeocoderWidget(MapWidget map, String name, String title) {
		presenter = new GeocoderPresenter(map, this);
		this.map = map;

		textItem = new TextItem(name, title);

		textItem.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent keyPressEvent) {
				if ("enter".equalsIgnoreCase(keyPressEvent.getKeyName())) {
					presenter.goToLocation((String) textItem.getValue());
				}
			}
		});

		final PickerIcon findIcon = new PickerIcon(PickerIcon.SEARCH);
		final PickerIcon clearIcon = new PickerIcon(PickerIcon.CLEAR);
		textItem.setIcons(findIcon, clearIcon);

		textItem.addIconClickHandler(new IconClickHandler() {
			public void onIconClick(IconClickEvent iconClickEvent) {
				FormItemIcon icon = iconClickEvent.getIcon();
				if (clearIcon.getSrc().equals(icon.getSrc())) {
					presenter.clearLocation();
				} else {
					presenter.goToLocation((String) textItem.getValue());
				}
			}
		});

		this.setFields(textItem);
	}

	void setValue(String value) {
		textItem.setValue(value);
	}

	/**
	 * Get the regular expression which is used to select which geocoder services to use.
	 *
	 * @return geocoder selection regular expression
	 */
	@Api
	public String getServicePattern() {
		return presenter.getServicePattern();
	}

	/**
	 * Set the regular expression which is used to select which geocoder services to use.
	 *
	 * @param servicePattern geocoder selection regular expression
	 */
	@Api
	public void setServicePattern(String servicePattern) {
		presenter.setServicePattern(servicePattern);
	}

	/**
	 * Set the select alternative handler.
	 * <p/>
	 * There can only be one handler, the default displays the alternatives in a window on the map widget.
	 *
	 * @param handler select alternative handler
	 * @return handler registration.
	 */
	public HandlerRegistration setSelectAlternativeHandler(SelectAlternativeHandler handler) {
		return presenter.setSelectAlternativeHandler(handler);
	}

	/**
	 * Set the select location handler.
	 * <p/>
	 * There can only be one handler, the default zooms the map widget to the selected location.
	 *
	 * @param handler select location handler
	 * @return handler registration.
	 */
	public HandlerRegistration setSelectLocationHandler(SelectLocationHandler handler) {
		return presenter.setSelectLocationHandler(handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		if (event instanceof SelectLocationEvent || event instanceof SelectAlternativeEvent) {
			presenter.fireEvent(event);
		} else {
			super.fireEvent(event);
		}
	}

	/**
	 * Get the map on which this geocoder widget applies.
	 *
	 * @return map widget
	 */
	@Api
	public MapWidget getMap() {
		return map;
	}
}
