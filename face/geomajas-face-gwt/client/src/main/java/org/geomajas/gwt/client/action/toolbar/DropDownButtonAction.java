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

package org.geomajas.gwt.client.action.toolbar;

import java.util.List;

import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.Layout;

/**
 * Opens a drop-down panel beneath a button.
 * 
 * @author Emiel Ackermann
 */
public class DropDownButtonAction extends ToolbarAction implements ConfigurableAction {

	private Layout dropDownPanel;
	private List<ClientToolInfo> tools;
	private int panelWidth;
	
	/**
	 * Title, icon and tool tip need to be set through {@link org.geomajas.configuration.Parameter}s.
	 */
	public DropDownButtonAction() {
		super("", "");
	}

	/**
	 * Set panel which should be displayed in the drop down.
	 *
	 * @param dropDownPanel drop down panel
	 */
	public void setDropDownPanel(Layout dropDownPanel) {
		this.dropDownPanel = dropDownPanel;
	}

	/**
	 * Get the panel which should be displayed in the drop down.
	 *
	 * @return drop down panel
	 */
	public Layout getDropDownPanel() {
		return dropDownPanel;
	}
	
	/** {@inheritDoc} */
	public void configure(String key, String value) {
		if ("panelWidth".equals(key)) {
			panelWidth = Integer.parseInt(value);
		}
	}

	public void onClick(ClickEvent event) {
		if (dropDownPanel.isVisible()) {
			dropDownPanel.hide();
		} else {
			dropDownPanel.animateShow(AnimationEffect.SLIDE);
		}
	}

	public void setTools(List<ClientToolInfo> tools) {
		this.tools = tools;
	}

	public List<ClientToolInfo> getTools() {
		return tools;
	}

	public int getPanelWidth() {
		return panelWidth;
	}

	public void setPanelWidth(int panelWidth) {
		this.panelWidth = panelWidth;
	}
}
