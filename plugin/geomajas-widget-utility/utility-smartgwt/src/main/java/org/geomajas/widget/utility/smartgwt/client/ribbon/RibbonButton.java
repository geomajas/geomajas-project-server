/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.utility.smartgwt.client.ribbon;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.widget.utility.client.action.ButtonAction;
import org.geomajas.widget.utility.client.action.RadioAction;
import org.geomajas.widget.utility.client.action.RibbonColumnAware;
import org.geomajas.widget.utility.client.ribbon.RibbonColumn;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.StatefulCanvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Implementation of the RibbonColumn interface that displays a single button. Instances of this class are initialized
 * using a {@link ButtonAction} that determines the icon, title, tool-tip and click action.
 * 
 * @author Pieter De Graef
 */
public class RibbonButton extends StatefulCanvas implements RibbonColumn {

	public static final String PARAMETER_RADIOGROUP = "radioGroup";
	public static final String PARAMETER_SELECTED = "selected";
	public static final String PARAMETER_SHOWDISABLEDICON = "showDisabledIcon";

	private static final String DISABLEDMARKER = "-disabled";

	private boolean showTitles = true;

	private boolean showDisabledIcon;

	private TitleAlignment titleAlignment;

	private int iconSize;

	private ButtonAction buttonAction;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Initialize this object using a {@link ButtonAction}. This button will use the icon, title, tool-tip and click
	 * action that this {@link ButtonAction} provides. By default, an icon size of 24px will be used in combination with
	 * a BOTTOM title alignment.
	 * 
	 * @param buttonAction
	 *            The {@link ButtonAction} to use as template.
	 */
	public RibbonButton(final ButtonAction buttonAction) {
		this(buttonAction, 24, TitleAlignment.BOTTOM);
	}

	/**
	 * Initialize this object using a {@link ButtonAction}. This button will use the icon, title, tool-tip and click
	 * action that this {@link ButtonAction} provides.
	 * 
	 * @param buttonAction
	 *            The {@link ButtonAction} to use as template.
	 * @param iconSize
	 *            The size (width & height) for the icon - in pixels.
	 * @param titleAlignment
	 *            The alignment for the title (BOTTOM, RIGHT).
	 */
	public RibbonButton(final ButtonAction buttonAction, int iconSize, TitleAlignment titleAlignment) {
		this.buttonAction = buttonAction;
		this.iconSize = iconSize;
		this.titleAlignment = titleAlignment;

		setBaseStyle("ribbonButton");
		setWidth(50);
		setCursor(Cursor.HAND);

		setCanHover(true);
		setShowHover(true);
		setHoverWrap(false);
		setHoverWidth(1);
		setTooltip(buttonAction.getTooltip());

		setShowRollOver(true);
		setShowDown(true);
		setShowDisabled(true);
		setShowDisabledIcon(false);

		if (buttonAction instanceof RadioAction) {
			final RadioAction radioAction = (RadioAction) buttonAction;
			setActionType(SelectionType.CHECKBOX);
			setRadioGroup(radioAction.getRadioGroup());
			addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					radioAction.setSelected(isSelected());
				}
			});
		} else {
			addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					buttonAction.onClick(null);
				}
			});
		}

		if (buttonAction instanceof RibbonColumnAware) {
			((RibbonColumnAware) buttonAction).setRibbonColumn(this);
		}

		updateGui();
	}

	// ------------------------------------------------------------------------
	// RibbonColumn implementation:
	// ------------------------------------------------------------------------

	/**
	 * The text title to display in this button. Set the title.
	 * 
	 * @param title
	 *            new title.
	 */
	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		buttonAction.setTitle(title);
		updateGui();
	}

	/**
	 * Icon to be shown with the button title text.
	 * 
	 * @param icon
	 *            URL of new icon. Default value is null
	 */
	@Override
	public void setIcon(String icon) {
		super.setIcon(icon);
		buttonAction.setIcon(icon);
		updateGui();
	}

	@Override
	public void setDisabled(boolean disabled) {
		super.setDisabled(disabled);
		updateGui();
	}

	/**
	 * Sets the base CSS class for this button.
	 * 
	 * @param The
	 *            new base CSS class for this button.
	 */
	public void setButtonBaseStyle(String buttonBaseStyle) {
		setBaseStyle(buttonBaseStyle);
	}

	/**
	 * Determine whether or not to display the title on this button.
	 * 
	 * @param showTitles
	 *            The new value. Applying this new value will immediately trigger the GUI to redraw.
	 */
	public void setShowTitles(boolean showTitles) {
		this.showTitles = showTitles;
		updateGui();
	}

	/**
	 * See whether or not the title on this button is currently visible.
	 * 
	 * @return Return whether or not the title on this button is currently visible.
	 */
	public boolean isShowTitles() {
		return showTitles;
	}

	/**
	 * Determine the alignment (BOTTOM, RIGHT) for the title on this button.
	 * 
	 * @param titleAlignment
	 *            The new value. Applying this new value will immediately trigger the GUI to redraw.
	 */
	public void setTitleAlignment(TitleAlignment titleAlignment) {
		this.titleAlignment = titleAlignment;
		updateGui();
	}

	/**
	 * Get the current value for the title alignment (BOTTOM, RIGHT).
	 * 
	 * @return The current value for the title alignment (BOTTOM, RIGHT).
	 */
	public TitleAlignment getTitleAlignment() {
		return titleAlignment;
	}

	/**
	 * Is the ribbonColumn enabled?
	 * 
	 * @return true if column is enabled
	 */
	public boolean isEnabled() {
		return !isDisabled();
	}

	/**
	 * Set the enabled state of the RibbonColumn.
	 * 
	 * @param enabled
	 *            The enabled state
	 */
	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}

	/**
	 * Add configuration key/value pair.
	 * 
	 * @param key
	 *            parameter key
	 * @param value
	 *            parameter value
	 */
	public void configure(String key, String value) {
		if (PARAMETER_SELECTED.equalsIgnoreCase(key)) {
			if (buttonAction instanceof RadioAction) {
				boolean selected = Boolean.parseBoolean(value);
				setSelected(selected);
				RadioAction ra = (RadioAction) buttonAction;
				ra.setSelected(selected);
			}
		} else if (PARAMETER_RADIOGROUP.equalsIgnoreCase(key)) {
			if (buttonAction instanceof RadioAction) {
				addToRadioGroup(value);
				RadioAction ra = (RadioAction) buttonAction;
				ra.setRadioGroup(value);
			}
		} else if (PARAMETER_SHOWDISABLEDICON.equalsIgnoreCase(key)) {
			showDisabledIcon = Boolean.parseBoolean(value);
			updateGui();
		} else {
			buttonAction.configure(key, value);
		}
	}

	// ------------------------------------------------------------------------
	// Class specific getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the current icon size in pixels.
	 * 
	 * @return The current icon size in pixels.
	 */
	public int getIconSize() {
		return iconSize;
	}

	/**
	 * Set a new icon size in pixels.
	 * 
	 * @param iconSize
	 *            The new value. Applying this new value will immediately trigger the GUI to redraw.
	 */
	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
		updateGui();
	}

	/**
	 * Return the button action that is used as a template for this button.
	 * 
	 * @return The button action that is used as a template for this button.
	 */
	public ButtonAction getButtonAction() {
		return buttonAction;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Update the GUI to reflect the settings.
	 */
	private void updateGui() {
		String title = buttonAction.getTitle() == null ? buttonAction.getTooltip() : buttonAction.getTitle();
		if (title == null) {
			title = "??";
		} else {
			title = title.trim();
		}

		if (titleAlignment.equals(TitleAlignment.BOTTOM)) {
			String titleContent = "";
			if (showTitles) {
				titleContent = "<div style='text-align:center; margin-top: 10px; " + getTitleTextStyle() + "'>" + title
						+ "</div>";
			}
			setContents("<div style='text-align:center;'><img src='" + getIconUrl() + "' width='" + iconSize
					+ "' height='" + iconSize + "' /></div>" + titleContent);
		} else {
			setWidth100();
			String titleContent = "";
			if (showTitles) {
				titleContent = "<td style='text-align:left; padding-left: 8px; font-size: 11px; white-space:nowrap; "
						+ getTitleTextStyle() + "'>" + title + "</td>";
			}
			setContents("<table style='border-spacing: 0px;' cellpadding='0px'><tr><td style='text-align:center;'>"
					+ "<img src='" + getIconUrl() + "' width='" + iconSize + "' height='" + iconSize + "' />" + "</td>"
					+ titleContent + "</tr></table>");
		}
	}

	private String getTitleTextStyle() {
		if (isDisabled()) {
			return "color: #777777;";
		} else {
			return "";
		}
	}

	private String getIconUrl() {
		String icon = buttonAction.getIcon().replaceFirst("\\[ISOMORPHIC\\]", Geomajas.getIsomorphicDir());
		if (isDisabled() && showDisabledIcon) {
			int dot = icon.lastIndexOf(".");
			if (dot > -1) {
				icon = icon.substring(0, dot) + DISABLEDMARKER + icon.substring(dot, icon.length());
			}
		}
		return icon;
	}
}