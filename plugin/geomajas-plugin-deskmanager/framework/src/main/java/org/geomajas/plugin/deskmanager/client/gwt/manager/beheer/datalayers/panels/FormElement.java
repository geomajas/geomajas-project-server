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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.panels;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class FormElement {

	private String name;

	private String title;

	private String tooltip;

	private boolean required;

	private int width;

	private String defaultValue;

	/**
	 * Supports TextItem & PasswordItem. (others can/will be added when needed) default is TextItem.
	 */
	private String itemType;

	public FormElement(String name, String title) {
		this(name, title, null, false, 0, null, null);
	}

	public FormElement(String name, String title, String itemType) {
		this(name, title, itemType, false, 0, null, null);
	}

	public FormElement(String name, String title, boolean required) {
		this(name, title, null, required, 0, null, null);
	}

	public FormElement(String name, String title, boolean required, String defaultValue) {
		this(name, title, null, required, 0, null, defaultValue);
	}

	public FormElement(String name, String title, int width) {
		this(name, title, null, false, width, null, null);
	}

	public FormElement(String name, String title, String itemType, boolean required, int width, String tooltip,
			String defaultValue) {
		this.setName(name);
		this.setTitle(title);
		this.setTooltip(tooltip);
		this.setRequired(required);
		this.setWidth(width);
		this.setItemType(itemType);
		this.setDefaultValue(defaultValue);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param tooltip the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
}