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
package org.geomajas.widget.featureinfo.client.widget.factory;

import java.util.HashMap;
import java.util.Map;

import com.smartgwt.client.widgets.Canvas;

/**
 * A factory for building widgets.
 * Add builders to this factory through EntryPoint.onModuleLoad()
 * 
 * @see CustomCountriesFeatureInfoCanvasBuilder in the sample application on how you can use this factory
 * @author Kristof Heirwegh
 *
 */
public final class WidgetFactory {
	
	private static final Map<String, WidgetBuilder> WIDGETBUILDERS;

	static {
		WIDGETBUILDERS = new HashMap<String, WidgetBuilder>();
	}
	
	private WidgetFactory() {
		// utility class, hide constructor
	}

	/**
	 * Add a builder to the factory.
	 * @param key
	 * @param builder
	 */
	public static void put(String key, WidgetBuilder builder) {
		if (null != key && null != builder) {
			WIDGETBUILDERS.put(key, builder);
		}
	}

	public static WidgetBuilder get(String key) {
		return WIDGETBUILDERS.get(key);
	}
	
	/**
	 * Create a widget of given type.
	 * 
	 * @param key
	 *            key of widgettype
	 * @return widget (canvas) or null when key not found
	 */
	public static Canvas createWidget(String key) {
		WidgetBuilder builder = WIDGETBUILDERS.get(key);
		if (null == builder) {
			return null;
		}
		return builder.createWidget();
	}

}
