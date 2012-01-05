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

package org.geomajas.widget.utility.gwt.client.ribbon.dropdown;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.action.toolbar.ButtonGroup;
import org.geomajas.widget.utility.common.client.action.ButtonAction;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn.TitleAlignment;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButtonDescribed;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * This class aligns itself with its button in the animateShow override. 
 * Furthermore a {@link NativePreviewHandler} is added to {@link Event} in the override as well, 
 * that will close this panel when clicked outside this panel and button.
 * 
 * @author Emiel Ackermann
 */
public class DropDownPanel extends VStack {
	
	private final DropDownRibbonButton button;
	private HandlerRegistration registration;
	
	private List<VStack> groups = new ArrayList<VStack>();
	private List<Label> groupTitles = new ArrayList<Label>();
	private List<VStack> bodies = new ArrayList<VStack>();
	private List<RibbonButton> buttons = new ArrayList<RibbonButton>();

	public DropDownPanel(final DropDownRibbonButton button) {
		super();
		this.button = button;
		setPosition(Positioning.ABSOLUTE);
		setOverflow(Overflow.VISIBLE);
		setAutoHeight();
	}
	
	public void addGroup(ButtonGroup title, List<ButtonAction> actions) {
		VStack group = new VStack();
		group.setOverflow(Overflow.VISIBLE);
		group.setAutoHeight();
		group.setWidth100();
		
		String buttonLayout = GuwLayout.DropDown.ICON_AND_TITLE;
		if (null != title) {
			Label groupTitle = new Label(title.getTitle());
			groupTitle.setOverflow(Overflow.VISIBLE);
			groupTitle.setAutoHeight();
			groupTitle.setWidth100();
			group.addMember(groupTitle);
			groupTitles.add(groupTitle);
			buttonLayout = null == title.getButtonLayout() ? 
					GuwLayout.DropDown.ICON_AND_TITLE : title.getButtonLayout();
		}
		VStack body = new VStack();
		body.setOverflow(Overflow.VISIBLE);
		body.setAutoHeight();
		body.setWidth100();
		for (ButtonAction action : actions) {
			RibbonButton button = getButton(action, buttonLayout);
			body.addMember(button);
			buttons.add(button);
		}
		group.addMember(body);
		bodies.add(body);
		
		addMember(group);
		groups.add(group);
	}

	/**
	 * Converts the given action into a {@link RibbonButton}.
	 * 
	 * @param action ButtonAction
	 * @param buttonLayout determines the layout of the button.
	 * @return button RibbonButton
	 */
	private RibbonButton getButton(ButtonAction action, String buttonLayout) {
		RibbonButton button = null;
		if (buttonLayout.equals(GuwLayout.DropDown.ICON_AND_TITLE)) {
			button = new RibbonButton(action, 16, TitleAlignment.RIGHT);
		} else if (buttonLayout.equals(GuwLayout.DropDown.ICON_TITLE_AND_DESCRIPTION)) {
			button = new RibbonButtonDescribed(action);
		}
		button.setOverflow(Overflow.VISIBLE);
		button.setAutoHeight();
		button.setWidth100();
		button.setMargin(2);
		button.addClickHandler(new ClickHandler() {

			public void onClick(
					com.smartgwt.client.widgets.events.ClickEvent event) {
				hide();
			}
			
		});
		return button;
	}
	
	@Override
	public void setStyleName(String styleName) {
		super.setStyleName(styleName);
		for (VStack group : groups) {
			group.setStyleName(styleName + "Group");
		}
		for (Label groupTitle : groupTitles) {
			groupTitle.setStyleName(styleName + "GroupTitle");
		}
		for (VStack body : bodies) {
			body.setStyleName(styleName + "GroupBody");
		}
		for (RibbonButton button : buttons) {
			button.setButtonBaseStyle(styleName + "Button");
		}
	}

	@Override
	public void animateShow(final AnimationEffect effect) {
		this.moveTo(button.getPageLeft(), 
				button.getPageTop() + button.getInnerContentHeight());
		button.setSelected(true);
		registration = previewMouseUpHandler();
		setWidth(getWidth());
		super.animateShow(effect);
	}

	/**
	 * Through a {@link NativePreviewHandler} and its {@link NativePreviewEvent} 
	 * all mouse events can be caught before they are processed. 
	 * If the event is of type {@link Event#ONMOUSEUP} and the click was outside 
	 * the button or this drop-down panel, the panel is closed.
	 * @return
	 */
	
	private HandlerRegistration previewMouseUpHandler() {
		return Event.addNativePreviewHandler(new NativePreviewHandler() {
			public void onPreviewNativeEvent(NativePreviewEvent preview) {
				int typeInt = preview.getTypeInt();
				if (typeInt == Event.ONMOUSEUP) {
					Event event = Event.as(preview.getNativeEvent());
					// Can not retrieve the clicked widget from the Event, so here goes...
					int clientX = event.getClientX();
					int clientY = event.getClientY();
					// Was the button clicked?
					int left = button.getPageLeft();
					int right = left + button.getInnerContentWidth();
					int top = button.getPageTop();
					int bottom = top + button.getInnerContentHeight();
					boolean clickIsOutside = true;
					if (clientX > left && clientX < right && clientY > top && clientY < bottom) {
						clickIsOutside = false;
					}
					if (clickIsOutside) {
						// Was this panel clicked?
						right = getPageRight();
						top = getPageTop();
						bottom = getPageBottom();
						if (clientX > left && clientX < right && clientY > top && clientY < bottom) {
							clickIsOutside = false;
						}
					}
					if (clickIsOutside) {
						hide();
					}
				}
			}
		});
	}
	
	/**
	 * hide() instead of animateHide(), to make sure 
	 * the panel hides immediately (when navigating away from the ribbon).
	 */
	@Override
	public void hide() {
		button.setSelected(false);
		if (null != registration) {
			registration.removeHandler();
		}
		super.hide();
	}
}
