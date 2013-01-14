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

package org.geomajas.widget.utility.gwt.client.ribbon.dropdown;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ButtonGroup;
import org.geomajas.gwt.client.action.toolbar.DropDownButtonAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.gwt.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonAction;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.Layout;

/**
 * RibbonColumn implementation that displays a button, which opens a drop-down panel with more buttons. 
 * Panel can contain {@link org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnCanvas}ses as well,
 * so everything that extends {@link com.smartgwt.client.widgets.Canvas} can be included. 
 * 
 * @author Emiel Ackermann
 */
public class DropDownRibbonButton extends RibbonButton {
	
	private DropDownPanel dropDownPanel;
		
	public DropDownRibbonButton(final DropDownButtonAction action, List<ClientToolInfo> tools, MapWidget mapWidget) {
		super(new ToolbarButtonAction(action));
		createPanel(action, tools, mapWidget);
	}
	
	public DropDownRibbonButton(final DropDownButtonAction action, int iconSize,
			TitleAlignment titleAlignment, List<ClientToolInfo> tools, MapWidget mapWidget) {
		super(new ToolbarButtonAction(action), iconSize, titleAlignment);
		createPanel(action, tools, mapWidget);
	}
	
	private void createPanel(final DropDownButtonAction action, List<ClientToolInfo> tools, MapWidget mapWidget) {
		dropDownPanel = new DropDownPanel(this);
		dropDownPanel.hide();
		setTitle(action.getTitle());
		setTooltip(action.getTooltip());
		setIcon(action.getIcon());
		action.setDropDownPanel(dropDownPanel);
		
		ButtonGroup group = null;
		List<ButtonAction> actions = new ArrayList<ButtonAction>();
		for (ClientToolInfo tool : tools) {
			ToolbarBaseAction toolbarAction = ToolbarRegistry.getToolbarAction(tool.getToolId(), mapWidget);
			if (toolbarAction != null) {
				if (toolbarAction instanceof ButtonGroup) {
					// First wrap currently found actions into a group (previous group can be null).
					if (actions.size() > 0) {
						dropDownPanel.addGroup(group, actions);
					}
					group = (ButtonGroup) toolbarAction;
					group.setTitle(tool.getTitle());
					for (Parameter parameter : tool.getParameters()) {
						group.configure(parameter.getName(), parameter.getValue());
					}
					actions = new ArrayList<ButtonAction>();
				} else {
					ButtonAction innerAction = RibbonColumnRegistry.getAction(tool, mapWidget);
					actions.add(innerAction);
				}
			}
		}
		// Always add the last actions as a group to the panel (also if group is null)
		dropDownPanel.addGroup(group, actions);
	}
	
	@Override
	public void configure(String key, String value) {
		if ("title".equals(key)) {
			setTitle(value);
		} else if ("titleAlignment".equals(key)) {
			setTitleAlignment(TitleAlignment.valueOf(value.toUpperCase()));
		} else if ("icon".equals(key)) {
			setIcon(value);
		} else if ("toolTip".equals(key)) {
			setTooltip(value);
		} else if ("panelWidth".equals(key)) {
			dropDownPanel.setWidth(Integer.parseInt(value));
		} 
	}
	
	@Override
	protected void onDraw() {
		updateGui();
		Layout outer = getOuter();
		if (GuwLayout.DropDown.showDropDownImage) {
			Img arrow = new Img("[ISOMORPHIC]/images/arrow_down.png", 9, 9);
			arrow.setLayoutAlign(Alignment.CENTER);
			outer.addMember(arrow);
		}
		addChild(outer);
	}
	
	// ------------------------------------------------------------------------
	// Class specific methods:
	// ------------------------------------------------------------------------
	@Override
	public void setButtonBaseStyle(String baseStyle) {
		this.setBaseStyle(baseStyle.replace("Button", "DropDownButton"));
		dropDownPanel.setStyleName(baseStyle.replace("Button", "Panel"));
	}

	/**
	 * Get the {@link DropDownPanel} of this button.
	 * @return dropDownPanel
	 * 				The {@link DropDownPanel} of this button.
	 */
	public DropDownPanel getPanel() {
		return dropDownPanel;
	}
}