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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.KeyNames;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.IconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.IconClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.MapWidget;
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
	private PickerIcon clearIcon;

	/**
	 * Create geocoder widget which allows searching a location from a string.
	 *
	 * @param map map to apply search results
	 * @param name widget name
	 * @param title label which is displayed left of the widget
	 */
	@Api
	public GeocoderWidget(MapWidget map, String name, String title) {
		super();
		presenter = new GeocoderPresenter(map, this);
		this.map = map;

		textItem = new TextItem(name, title);

		textItem.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent keyPressEvent) {
				if (KeyNames.ENTER.equals(keyPressEvent.getKeyName())) {
					presenter.goToLocation((String) textItem.getValue());
				}
			}
		});

		final PickerIcon findIcon = new PickerIcon(WidgetLayout.iconPickerSearch);
		if (GeocoderLayout.showClearIcon) {
			clearIcon = new PickerIcon(WidgetLayout.iconPickerClear);
			textItem.setIcons(findIcon, clearIcon);
		} else {
			textItem.setIcons(findIcon);
		}

		textItem.addIconClickHandler(new IconClickHandler() {
			public void onIconClick(IconClickEvent iconClickEvent) {
				FormItemIcon icon = iconClickEvent.getIcon();
				if (GeocoderLayout.showClearIcon && clearIcon.getSrc().equals(icon.getSrc())) {
					presenter.clearLocation();
				} else {
					presenter.goToLocation((String) textItem.getValue());
				}
			}
		});

		this.setFields(textItem);
	}

	/**
	 * Set whether the title should be shown for the widget.
	 *
	 * @param showTitle show title?
	 * @since 1.2.0
	 */
	@Api
	public void setShowTitle(boolean showTitle) {
		textItem.setShowTitle(showTitle);
	}

	/**
	 * Sets the text item value.
	 * 
	 * @param value text
	 * @since 1.1.0
	 */
	@Api
	public void setValue(String value) {
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
