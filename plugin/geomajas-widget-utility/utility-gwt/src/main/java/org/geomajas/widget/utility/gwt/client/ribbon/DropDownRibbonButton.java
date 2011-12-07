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

package org.geomajas.widget.utility.gwt.client.ribbon;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.widget.utility.common.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.action.DropDownButtonAction;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * RibbonColumn implementation that displays a button, which opens a drop-down panel with more buttons.
 * 
 * @author Emiel Ackermann
 */
public class DropDownRibbonButton extends RibbonButton {

	private DropDownButtonAction action;
	private List<RibbonButton> buttons;
	private VStack dropDownPanel;
	
	public DropDownRibbonButton(DropDownButtonAction action) {
		super(action);
		this.action = action;
	}
	
	public DropDownRibbonButton(final DropDownButtonAction action, int iconSize, TitleAlignment titleAlignment) {
		super(action, iconSize, titleAlignment);
		this.action = action;
	}
	
	/**
	 * Converts the given actions into {@link RibbonButton}s.
	 * 
	 * @param actions
	 * @param closeAfterClick if true the {@link DropDownPanel} will fade after clicking an action.
	 * @param iconSize
	 * @param titleAlignment
	 */
	public void prepareButtons(List<ButtonAction> actions, boolean closeAfterClick, 
			int iconSize, TitleAlignment titleAlignment) {
		buttons = new ArrayList<RibbonButton>();
		for (ButtonAction action : actions) {
			RibbonButton button = new RibbonButton(action, iconSize, titleAlignment);
			button.setHeight(20);
			button.setMargin(3);
			if (closeAfterClick) {
				button.addClickHandler(new ClickHandler() {
	
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						dropDownPanel.animateHide(AnimationEffect.WIPE);
					}
					
				});
			}
			buttons.add(button);
		}
		createButtonsPanel();
	}
	
	/**
	 * Converts the given actions into {@link RibbonButton}s.
	 * 
	 * @param actions
	 */
	public void prepareButtons(List<ButtonAction> actions) {
		prepareButtons(actions, true, 16, TitleAlignment.RIGHT);
	}

	/**
	 * Creates a {@link DropDownPanel} and fills it with the prepared buttons 
	 * (see {@link DropDownRibbonButton#prepareButtons(List)}).
	 */
	public void createButtonsPanel() {
		dropDownPanel = new DropDownPanel(this);
		dropDownPanel.setOverflow(Overflow.VISIBLE);
		dropDownPanel.setAutoHeight();
		dropDownPanel.setAutoWidth();
		dropDownPanel.setPosition(Positioning.RELATIVE);
		dropDownPanel.hide();
		for (RibbonButton button : buttons) {
			dropDownPanel.addMember(button);
		}
		action.setDropDownPanel(dropDownPanel);
	}

	// ------------------------------------------------------------------------
	// Class specific methods:
	// ------------------------------------------------------------------------

	public void setButtonBaseStyle(String buttonBaseStyle) {
		this.setBaseStyle(buttonBaseStyle);
		dropDownPanel.setStyleName(this.getStyleName().replace("Button", "DropDownPanel"));
		for (RibbonButton button : buttons) {
			button.setBaseStyle(buttonBaseStyle);
		}
	}

	// ------------------------------------------------------------------------
	// RibbonColumn implementation:
	// ------------------------------------------------------------------------

	/**
	 * Returns the vertical layout that holds the drop-down button.
	 * 
	 * @return The vertical layout that holds the drop-down button.
	 */
	public Widget asWidget() {
		return this;
	}

	/**
	 * Determine whether or not to display all titles on all buttons.
	 * 
	 * @param showTitles
	 *            The new value. Applying this new value will immediately trigger the GUI to redraw.
	 */
	public void setShowTitles(boolean showTitles) {
		this.setShowTitles(showTitles);
		for (RibbonButton button : buttons) {
			button.setShowTitles(showTitles);
		}
	}

	/**
	 * See whether or not the titles on the buttons are currently visible.
	 * 
	 * @return Return whether or not the titles on the buttons are currently visible.
	 */
	public boolean isShowTitles() {
		if (buttons.size() > 0) {
			return buttons.get(0).isShowTitles();
		}
		return false;
	}

	/**
	 * Determine the alignment (BOTTOM, RIGHT) for the titles on all buttons.
	 * 
	 * @param titleAlignment
	 *            The new value. Applying this new value will immediately trigger the GUI to redraw.
	 */
	public void setTitleAlignment(TitleAlignment titleAlignment) {
		for (RibbonButton button : buttons) {
			button.setTitleAlignment(titleAlignment);
		}
	}

	/**
	 * Get the current value for the title alignment (BOTTOM, RIGHT).
	 * 
	 * @return The current value for the title alignment (BOTTOM, RIGHT).
	 */
	public TitleAlignment getTitleAlignment() {
		if (buttons.size() > 0) {
			return buttons.get(0).getTitleAlignment();
		}
		return TitleAlignment.BOTTOM;
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
	 * Add configuration key/value pair. This pair will be applied on all actions within this list.
	 * 
	 * @param key
	 *            parameter key
	 * @param value
	 *            parameter value
	 */
	public void configure(String key, String value) {
		for (RibbonButton button : buttons) {
			button.configure(key, value);
		}
	}
	/**
	 * Extension needed for aligning this panel 'just-in-time' with its button. 
	 * 
	 * @author Emiel Ackermann
	 */
	private class DropDownPanel extends VStack {
		
		private final DropDownRibbonButton button;

		public DropDownPanel(DropDownRibbonButton button) {
			super();
			this.button = button;
		}

		@Override
		public void animateShow(AnimationEffect effect) {
			int absoluteLeft = button.getAbsoluteLeft();
			int left = button.getLeft();
			int pageLeft = button.getPageLeft();
			int absoluteTop = button.getAbsoluteTop();
			int top = button.getTop();
			int pageTop = button.getPageTop();
			Integer height = button.getHeight();
			int innerHeight = button.getInnerHeight();
			int innerContentHeight = button.getInnerContentHeight();
			int bottom = button.getBottom();
			int pageBottom = button.getPageBottom();
			this.moveTo(button.getPageLeft() - GuwLayout.ribbonGroupInternalMargin + 1, 
					button.getPageTop() + button.getInnerContentHeight());
			super.animateShow(effect);
		}
		
	}
}