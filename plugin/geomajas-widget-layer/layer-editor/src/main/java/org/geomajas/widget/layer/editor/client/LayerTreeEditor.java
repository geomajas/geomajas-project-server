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
package org.geomajas.widget.layer.editor.client;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.BaseGeodeskWidgetEditor;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;


/**
 * Editor for the layertree. Actualy a wrapper around {@link LayerTreeSelectPanel}.
 * 
 * @author Oliver May
 *
 */
public class LayerTreeEditor implements BaseGeodeskWidgetEditor {

	private LayerTreeSelectPanel panel;
	private VLayout layout;

	public LayerTreeEditor() {
		
		panel = new LayerTreeSelectPanel();

		layout = new VLayout();
		layout.setPadding(10);
		layout.setIsGroup(true);
		layout.setGroupTitle(new LayerTreeEditorFactory().getName());
		layout.addMember(panel);
		layout.setOverflow(Overflow.AUTO);
	}
	
	@Override
	public Canvas getCanvas() {
		return layout;
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
