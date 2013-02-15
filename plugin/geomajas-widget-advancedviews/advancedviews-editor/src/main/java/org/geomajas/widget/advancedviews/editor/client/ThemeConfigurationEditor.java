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

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.WidgetEditor;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;


/**
 * Editor for the theme configuration. Actualy a wrapper around {@link ThemeConfigPanel}.
 * 
 * @author Oliver May
 *
 */
public class ThemeConfigurationEditor implements WidgetEditor {

	private ThemeConfigurationPanel panel;
	private VLayout layout;
	
	public ThemeConfigurationEditor() {
		panel = new ThemeConfigurationPanel();

		layout = new VLayout();
		layout.addMember(panel);
		layout.setOverflow(Overflow.AUTO);
	}
	
	@Override
	public Canvas getCanvas() {
		return layout;
	}

	@Override
	public ClientWidgetInfo getWidgetConfiguration() {
		return panel.getThemeConfig();
	}

	@Override
	public void setWidgetConfiguration(ClientWidgetInfo configuration) {
		if (configuration == null) {
			panel.setThemeConfig(new ThemesInfo());
		} else if (configuration instanceof ThemesInfo) {
			panel.setThemeConfig((ThemesInfo) configuration);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void setBaseGeodesk(BaseGeodeskDto geodesk) {
		//Do nothing, configuration is set trough setWidgetConfiguration()
	}

	@Override
	public void setDisabled(boolean disabled) {
		panel.setDisabled(disabled);
	}

}
