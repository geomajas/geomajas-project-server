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
package org.geomajas.plugin.deskmanager.client.gwt.manager.editor;

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.annotation.Api;

/**
 * Registry where different Widget Editor factories can be registered. The key is often the
 * {@link org.geomajas.configuration.client.ClientWidgetInfo} identifier. This registry contains
 * all widget editors that can be used on applications, maps and layers. 
 * 
 * To access the registry for layers, use {@link WidgetEditorFactoryRegistry#getLayersRegistry()}
 * To access the registry for applications, use {@link WidgetEditorFactoryRegistry#getApplicationRegistry()}
 * To access the registry for maps, use {@link WidgetEditorFactoryRegistry#getMapsRegistry()}
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api (allMethods = true)
public final class WidgetEditorFactoryRegistry {

	private static final WidgetEditorFactoryRegistry MAP_INSTANCE = new WidgetEditorFactoryRegistry();
	private static final WidgetEditorFactoryRegistry APPLICATION_INSTANCE = new WidgetEditorFactoryRegistry();
	private static final WidgetEditorFactoryRegistry LAYER_INSTANCE = new WidgetEditorFactoryRegistry();

	private Map<String, WidgetEditorFactory> widgetEditorFactories = new LinkedHashMap<String, WidgetEditorFactory>();

	private WidgetEditorFactoryRegistry() {
	}

	/**
	 * Register a WidgetEditorFactory.
	 * 
	 * @param key
	 * @param editor
	 */
	public void register(WidgetEditorFactory editor) {
		if (editor != null) {
			widgetEditorFactories.put(editor.getKey(), editor);
		}
	}

	/**
	 * Register a WidgetEditorFactory. It can later be retrieved using the key.
	 * 
	 * @param key
	 * @param editor
	 */
	public void register(String key, WidgetEditorFactory editor) {
		widgetEditorFactories.put(key, editor);
	}

	/**
	 * Fetch a widget editor for the given key.
	 * 
	 * @param key
	 *            the key
	 * @return the WidgetEditorFactory
	 */
	public WidgetEditorFactory get(String key) {
		return widgetEditorFactories.get(key);
	}

	/**
	 * Get the only instance of the widget editor registry for Maps.
	 * 
	 * @return the registry.
	 */
	public static WidgetEditorFactoryRegistry getMapRegistry() {
		return MAP_INSTANCE;
	}

	/**
	 * Get the only instance of the widget editor registry for Applications.
	 * 
	 * @return the registry.
	 */
	public static WidgetEditorFactoryRegistry getApplicationRegistry() {
		return APPLICATION_INSTANCE;
	}

	/**
	 * Get the only instance of the widget editor registry for Layers.
	 * 
	 * @return the registry.
	 */
	public static WidgetEditorFactoryRegistry getLayerRegistry() {
		return LAYER_INSTANCE;
	}

	/**
	 * Get a map of keys mapped to the widget editors.
	 * 
	 * @return the widget editors.
	 */
	public Map<String, WidgetEditorFactory> getWidgetEditors() {
		return widgetEditorFactories;
	}

}
