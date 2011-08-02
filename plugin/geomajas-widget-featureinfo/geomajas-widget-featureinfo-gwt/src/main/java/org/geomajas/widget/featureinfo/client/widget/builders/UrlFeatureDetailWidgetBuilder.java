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
package org.geomajas.widget.featureinfo.client.widget.builders;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetBuilder;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
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

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);

	private String windowWidth = "75%";
	private String windowHeight = "75%";
	private String urlPattern;
	private String urlAttributeName;

	public Window createFeatureDetailWindow(Feature feature, boolean editingAllowed) {
		Window w = new Window();
		w.setWidth(windowWidth);
		w.setHeight(windowHeight);
		w.setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(feature.getLabel()));
		w.setCanDragReposition(true);
		w.setCanDragResize(true);
		w.setAutoCenter(true);
		w.setKeepInParentRect(true);

		String url = buildUrl(feature);
		if (url == null) {
			Label l = new Label(messages.urlFeatureDetailWidgetBuilderNoValue());
			l.setWidth100();
			l.setHeight100();
			l.setValign(VerticalAlignment.CENTER);
			l.setAlign(Alignment.CENTER);
			w.addItem(l);

		} else {
			HTMLPane htmlPane = new HTMLPane();
			htmlPane.setWidth100();
			htmlPane.setHeight100();
			htmlPane.setContentsURL(url);
			htmlPane.setContentsType(ContentsType.PAGE);
			w.addItem(htmlPane);
		}
		return w;
	}

	public Canvas createWidget() {
		return null; // cannot be used this way
	}

	public void configure(String key, String value) {
		try {
			if ("windowWidth".equals(key)) {
				windowWidth = value;
			} else if ("windowHeight".equals(key)) {
				windowHeight = value;
			} else if ("urlPattern".equals(key)) {
				urlPattern = value;
			} else if ("urlAttributeName".equals(key)) {
				urlAttributeName = value;
			}
		} catch (Exception e) {
			SC.logWarn("Error parsing parameters for " + IDENTIFIER + " - " + e.getMessage());
		}
	}

	// ----------------------------------------------------------

	private String buildUrl(Feature feature) {
		Object o = feature.getAttributeValue(urlAttributeName);
		if (o == null || o.toString() == null || "".equals(o.toString())) {
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
