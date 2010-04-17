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

package org.geomajas.gwt.client.widget;

import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.global.Api;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.i18n.I18nProvider;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * <p>
 * Select item that enables the user to select a locale. The choice of locales, is determined by the {@link Geomajas}
 * static method holder. Selecting a locale, will trigger a reload of the entire page, using the new locale.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Api(allMethods = true)
public class LocaleSelect extends Canvas implements ChangedHandler {

	/** The full mapping of locales and their language counterparts that Geomajas supports. */
	private Map<String, String> locales;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public LocaleSelect(String defaultLocaleLabel) {
		super();
		locales = Geomajas.getSupportedLocales();
		locales.put("default", defaultLocaleLabel);
		setHeight(28);
		setWidth(200);

		// Build a small form with a select item for locales:
		DynamicForm form = new DynamicForm();
		ComboBoxItem localeItem = new ComboBoxItem();
		localeItem.setValueMap(locales.values().toArray(new String[0]));
		localeItem.setTitle(I18nProvider.getGlobal().localeTitle());

		// Show the current locale value in the select item:
		String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
		localeItem.setValue(locales.get(currentLocale));

		// Add the changed handler, and build the widget:
		localeItem.addChangedHandler(this);
		form.setFields(localeItem);
		addChild(form);
	}

	// -------------------------------------------------------------------------
	// ChangedHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * When a new locale has been selected in the select item, this method will be called. It will automatically reload
	 * the entire page using the newly selected locale.
	 * 
	 * @param event
	 *            The changed event that contains the new value for the locale.
	 */
	public void onChanged(ChangedEvent event) {
		String selectedLocale = (String) event.getValue();
		for (Entry<String, String> entry : locales.entrySet()) {
			if (entry.getValue().equals(selectedLocale)) {
				SC.showPrompt(I18nProvider.getGlobal().localeReload() + ": " + selectedLocale);
				Window.Location.assign(buildLocaleUrl(entry.getKey()));
			}
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/** Build the correct URL for the new locale. */
	private String buildLocaleUrl(String selectedLocale) {
		UrlBuilder builder = Window.Location.createUrlBuilder();
		builder.removeParameter("locale");
		if (!"default".equals(selectedLocale)) {
			builder.setParameter("locale", selectedLocale);
		}
		return builder.buildString();
	}
}
