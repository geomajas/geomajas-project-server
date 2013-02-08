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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common.layertree;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.common.WidgetEditor;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;

import com.smartgwt.client.widgets.Canvas;


/**
 * Editor for the layertree. Actualy a wrapper around {@link LayerTreeSelectPanel}.
 * 
 * @author Oliver May
 *
 */
public class LayerTreeEditor implements WidgetEditor {

	private LayerTreeSelectPanel panel;

	public LayerTreeEditor() {
		panel = new LayerTreeSelectPanel();
	}
	
	@Override
	public Canvas getCanvas() {
		return panel;
	}

	@Override
	public ClientWidgetInfo getWidgetConfiguration() {
		return panel.getValues();
	}

	@Override
	public void setWidgetConfiguration(ClientWidgetInfo configuration) {
		//Do nothing, configuration is set trough setBaseGeodesk()
	}

	@Override
	public void setBaseGeodesk(BaseGeodeskDto geodesk) {
		panel.setValues(geodesk);
	}

	@Override
	public void setDisabled(boolean disabled) {
		panel.setDisabled(disabled);
	}

}
