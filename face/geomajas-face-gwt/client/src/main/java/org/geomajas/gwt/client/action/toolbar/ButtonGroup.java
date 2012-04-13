/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutParameter;
import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutParameter.Layout;
/**
 * {@link ConfigurableAction} implementation that fetches a title and a {@link Layout} from the parameters.
 * 
 * @author Emiel Ackermann
 */
public class ButtonGroup extends ToolbarBaseAction implements ConfigurableAction {

	private String buttonLayout;
	
	private Layout layout;

	public ButtonGroup() {
		super("", "", "");
	}

	public void configure(String key, String value) {
		if ("title".equals(key)) {
			setTitle(value);
		} else if (ButtonLayoutParameter.NAME.equals(key)) {
			setButtonLayout(value);
			setLayout(Layout.valueOf(value));
		}
	}
	
	public String getButtonLayout() {
		return buttonLayout;
	}

	public void setButtonLayout(String buttonLayout) {
		this.buttonLayout = buttonLayout;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}
}
