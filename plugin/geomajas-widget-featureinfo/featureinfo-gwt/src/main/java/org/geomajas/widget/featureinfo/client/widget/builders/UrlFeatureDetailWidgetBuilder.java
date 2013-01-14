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
package org.geomajas.widget.featureinfo.client.widget.builders;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetBuilder;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetFactory;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;

/**
 * Sample implementation of a FeatureDetailWidgetBuilder.
 * 
 * @author Kristof Heirwegh
 * 
 */
public class UrlFeatureDetailWidgetBuilder implements FeatureDetailWidgetBuilder {

	private static final long serialVersionUID = 100L;

	public static final String IDENTIFIER = "UrlFeatureDetailWidgetBuilder";

	private static final String ATTRIBUTE_KEY = "${attributeValue}";
	private static final String ATTRIBUTE_KEY_REGEX = "\\$\\{attributeValue\\}";

	private static final FeatureInfoMessages MESSAGES = GWT.create(FeatureInfoMessages.class);

	private String windowWidth = "75%";
	private String windowHeight = "75%";
	private String urlPattern;
	private String urlAttributeName;
	private boolean showDefaultIfNull = true;

	public Window createFeatureDetailWindow(Feature feature, boolean editingAllowed) {
		String url = buildUrl(feature);
		if (url == null) {
			if (showDefaultIfNull) {
				return FeatureDetailWidgetFactory.createDefaultFeatureDetailWindow(feature, feature.getLayer(),
						editingAllowed);
			} else {
				Label l = new Label(MESSAGES.urlFeatureDetailWidgetBuilderNoValue());
				l.setWidth100();
				l.setHeight100();
				l.setValign(VerticalAlignment.CENTER);
				l.setAlign(Alignment.CENTER);
				Window w = createWindow(feature.getLabel());
				w.addItem(l);
				return w;
			}
		} else {
			HTMLPane htmlPane = new HTMLPane();
			htmlPane.setWidth100();
			htmlPane.setHeight100();
			htmlPane.setContentsURL(url);
			htmlPane.setContentsType(ContentsType.PAGE);
			Window w = createWindow(feature.getLabel());
			w.addItem(htmlPane);
			return w;
		}
	}

	public Canvas createWidget() {
		return null; // cannot be used this way
	}

	public void configure(String key, String value) {
		try {
			if ("windowWidth".equalsIgnoreCase(key)) {
				windowWidth = value;
			} else if ("windowHeight".equalsIgnoreCase(key)) {
				windowHeight = value;
			} else if ("urlPattern".equalsIgnoreCase(key)) {
				urlPattern = value;
			} else if ("urlAttributeName".equalsIgnoreCase(key)) {
				urlAttributeName = value;
			} else if ("showDefaultIfNull".equalsIgnoreCase(key)) {
				showDefaultIfNull = Boolean.parseBoolean(value);
			}
		} catch (Exception e) { // NOSONAR
			Log.logWarn("Error parsing parameters for " + IDENTIFIER + " - " + e.getMessage());
		}
	}

	// ----------------------------------------------------------

	private Window createWindow(String subtitle) {
		Window w = new Window();
		w.setWidth(windowWidth);
		w.setHeight(windowHeight);
		w.setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(subtitle));
		w.setCanDragReposition(true);
		w.setCanDragResize(true);
		w.setAutoCenter(true);
		w.setKeepInParentRect(true);
		return w;
	}

	private String buildUrl(Feature feature) {
		Object o = feature.getAttributeValue(urlAttributeName);
		if (o == null || o.toString() == null || "".equals(o.toString().trim())) {
			return null;
		} else {
			String value = o.toString();
			if (urlPattern == null) {
				return value;
			} else {
				if (urlPattern.contains(ATTRIBUTE_KEY)) {
					return urlPattern.replaceAll(ATTRIBUTE_KEY_REGEX, value);
				} else {
					return urlPattern + value;
				}
			}
		}
	}
}
