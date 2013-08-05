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
package org.geomajas.smartgwt.client.i18n;

import java.util.Map;
import java.util.MissingResourceException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * Central provider for all i18n constants.
 * 
 * @author Jan De Moerloose
 */
public final class I18nProvider {

	private static final AttributeMessages ATTRIBUTE = GWT.create(AttributeMessages.class);

	private static final MenuMessages MENU = GWT.create(MenuMessages.class);

	private static final ToolbarConstants TOOLBAR = GWT.create(ToolbarConstants.class);

	private static final GlobalMessages GLOBAL = GWT.create(GlobalMessages.class);

	private static final LayerTreeMessages LAYER_TREE = GWT.create(LayerTreeMessages.class);

	private static final SearchMessages SEARCH = GWT.create(SearchMessages.class);

	private static ConstantsWithLookup lookup = new DefaultLookup();

	private I18nProvider() {
	}

	public static AttributeMessages getAttribute() {
		return ATTRIBUTE;
	}

	public static MenuMessages getMenu() {
		return MENU;
	}

	public static ToolbarConstants getToolbar() {
		return TOOLBAR;
	}

	public static GlobalMessages getGlobal() {
		return GLOBAL;
	}

	public static LayerTreeMessages getLayerTree() {
		return LAYER_TREE;
	}

	public static SearchMessages getSearch() {
		return SEARCH;
	}

	/**
	 * Return application-specific configurable constants with lookup. The instance must be set first before calling
	 * this method.
	 * 
	 * @return the
	 */
	public static ConstantsWithLookup getLookUp() {
		return lookup;
	}

	/**
	 * Set application-specific configurable constants with lookup. Call this method first at application startup.
	 * 
	 * @param newLookUp
	 *            the application specific lookup bundle
	 */
	public static void setLookUp(ConstantsWithLookup newLookUp) {
		lookup = newLookUp;
	}

	/**
	 * Converts a parameter using the application-specific constants. Only converts parameters of the form "${....}".
	 * 
	 * @param parameter
	 *            parameter to look up (must be of the form "${....}")
	 * @return the converted parameter (if conformal) or the parameter itself
	 */
	public static String lookupParameter(String parameter) {
		if (parameter.startsWith("i18n:")) {
			String methodName = parameter.substring(5).replace('.', '_');
			return lookup.getString(methodName);
		} else {
			return parameter;
		}
	}

	/**
	 * ???
	 */
	static class DefaultLookup implements ConstantsWithLookup {

		public boolean getBoolean(String s) throws MissingResourceException {
			throw new MissingResourceException(GLOBAL.missingI18n(), "Boolean", s);
		}

		public double getDouble(String s) throws MissingResourceException {
			throw new MissingResourceException(GLOBAL.missingI18n(), "Double", s);
		}

		public float getFloat(String s) throws MissingResourceException {
			throw new MissingResourceException(GLOBAL.missingI18n(), "Float", s);
		}

		public int getInt(String s) throws MissingResourceException {
			throw new MissingResourceException(GLOBAL.missingI18n(), "Int", s);
		}

		public Map<String, String> getMap(String s) throws MissingResourceException {
			throw new MissingResourceException(GLOBAL.missingI18n(), "Map<String, String>", s);
		}

		public String getString(String s) throws MissingResourceException {
			throw new MissingResourceException(GLOBAL.missingI18n(), "String", s);
		}

		public String[] getStringArray(String s) throws MissingResourceException {
			throw new MissingResourceException(GLOBAL.missingI18n(), "String[]", s);
		}
	}
}
