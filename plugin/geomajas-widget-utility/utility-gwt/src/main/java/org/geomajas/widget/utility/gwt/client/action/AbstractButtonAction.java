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
package org.geomajas.widget.utility.gwt.client.action;

import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutStyle;
import org.geomajas.widget.utility.common.client.action.RibbonColumnAware;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;

/**
 * A basic implementation of a ButtonAction.
 * 
 * @author Kristof Heirwegh
 * 
 */
public abstract class AbstractButtonAction implements ButtonAction, RibbonColumnAware {

	public static final String PARAMETER_ENABLED = "enabled";

	private String title;
	private String tooltip;
	private String icon;
	private ButtonLayoutStyle buttonLayoutStyle;
	private RibbonColumn ribbonColumn;

	/**
	 * Constructor. Set all properties through configuration.
	 */
	public AbstractButtonAction() {
	}

	/**
	 * Constructor. Set basic properties.
	 * 
	 * @param title
	 * @param tooltip
	 * @param icon
	 */
	public AbstractButtonAction(String icon, String title, String tooltip) {
		this.icon = icon;
		this.title = title;
		this.tooltip = tooltip;
	}

	public void configure(String key, String value) {
		if (PARAMETER_ENABLED.equalsIgnoreCase(key)) {
			if (getRibbonColumn() != null) {
				getRibbonColumn().setEnabled(Boolean.parseBoolean(value));
			}
		}
		// override when needed, don't forget to call super.
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public ButtonLayoutStyle getButtonLayoutStyle() {
		return buttonLayoutStyle;
	}

	public void setButtonLayoutStyle(ButtonLayoutStyle buttonLayoutStyle) {
		this.buttonLayoutStyle = buttonLayoutStyle;
	}

	public void setRibbonColumn(RibbonColumn column) {
		this.ribbonColumn = column;
	}

	public RibbonColumn getRibbonColumn() {
		return ribbonColumn;
	}
}