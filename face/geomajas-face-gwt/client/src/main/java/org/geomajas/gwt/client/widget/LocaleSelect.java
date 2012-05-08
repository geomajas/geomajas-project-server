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

package org.geomajas.gwt.client.widget;

import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.annotation.Api;
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
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LocaleSelect extends Canvas implements ChangedHandler {

	/** The full mapping of locales and their language counterparts that Geomajas supports. */
	private Map<String, String> locales;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Constructor.
	 *
	 * @param defaultLocaleLabel label for the default locale
	 */
	public LocaleSelect(String defaultLocaleLabel) {
		super();
		locales = Geomajas.getSupportedLocales();
		locales.put("default", defaultLocaleLabel);
		setHeight(28);
		setWidth(200);

		// Build a small form with a select item for locales:
		DynamicForm form = new DynamicForm();
		ComboBoxItem localeItem = new ComboBoxItem();
		localeItem.setValueMap(locales.values().toArray(new String[locales.size()]));
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
