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
package org.geomajas.widget.advancedviews.editor.client;

import org.geomajas.plugin.deskmanager.client.gwt.manager.WidgetEditorFactoryRegistry;

import com.google.gwt.core.client.EntryPoint;


/**
 * @author Oliver May
 *
 */
public class AdvancedviewsEditor implements EntryPoint {

	@Override
	public void onModuleLoad() {
		WidgetEditorFactoryRegistry.getInstance().register(new ThemeConfigurationEditorFactory());
	}

}
