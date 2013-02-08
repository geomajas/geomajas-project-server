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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.annotation.Api;

/**
 * Registry where different Widget Editor factories can be registered. The key is often the
 * {@link org.geomajas.configuration.client.ClientWidgetInfo} identifier.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public final class WidgetEditorFactoryRegistry {

	private static final WidgetEditorFactoryRegistry INSTANCE = new WidgetEditorFactoryRegistry();

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
	 * Get the only instance of the widget editor registry.
	 * 
	 * @return the registry.
	 */
	public static WidgetEditorFactoryRegistry getInstance() {
		return INSTANCE;
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
