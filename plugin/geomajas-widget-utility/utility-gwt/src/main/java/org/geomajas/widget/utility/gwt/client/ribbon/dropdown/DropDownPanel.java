package org.geomajas.widget.utility.gwt.client.ribbon.dropdown;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.action.toolbar.ButtonGroupTitle;
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
	
	/**
	 * Same as a RibbonButton in a ToolbarActionList; icon (16px) on the left and title on the right
	 */
	public static final String ICON_AND_TITLE = "iconAndTitle";
	/**
	 * Icon (24px) on the left and the title and description on the right, the title on top of the description
	 */
	public static final String ICON_TITLE_AND_DESCRIPTION = "iconTitleAndDescription";

	private final DropDownRibbonButton button;
	private HandlerRegistration registration;
	
	List<VStack> groups = new ArrayList<VStack>();
	List<Label> groupTitles = new ArrayList<Label>();
	List<RibbonButton> buttons = new ArrayList<RibbonButton>();

	public DropDownPanel(final DropDownRibbonButton button) {
		super();
		this.button = button;
	}
	
	public void addGroup(ButtonGroupTitle title, List<ButtonAction> actions) {
		VStack group = new VStack();
		
		Label groupTitle = new Label(title.getTitle());
		groupTitle.setHeight(25);
		group.addMember(groupTitle);
		for (ButtonAction action : actions) {
			RibbonButton button = getButton(action, title.getButtonLayout());
			group.addMember(button);
		}
		addMember(group);
	}

	/**
	 * Converts the given action into a {@link RibbonButton}.
	 * 
	 * @param actions
	 * @param buttonLayout determines the layout of the button.
	 */
	private RibbonButton getButton(ButtonAction action, String buttonLayout) {
		RibbonButton button = null;
		if (buttonLayout.equals(ICON_AND_TITLE)) {
			button = new RibbonButton(action, 16, TitleAlignment.RIGHT);
		} else if (buttonLayout.equals(ICON_TITLE_AND_DESCRIPTION)) {
			button = new RibbonButtonDescribed(action, 32);
		}
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
			group.setStyleName(getStyleName() + "Group");
		}
		for (Label groupTitle : groupTitles) {
			groupTitle.setStyleName(getStyleName() + "GroupTitle");
		}
		for (RibbonButton button : buttons) {
			if (button instanceof RibbonButtonDescribed) {
				button.setStyleName(getStyleName() + "DescribedButton"); 
			}
			button.setStyleName(getStyleName() + "Button");
		}
	}

	@Override
	public void animateShow(final AnimationEffect effect) {
		this.moveTo(button.getPageLeft() - GuwLayout.ribbonGroupInternalMargin + 2, 
				button.getPageTop() + button.getInnerContentHeight() - 4);
		button.setSelected(true);
		registration = Event.addNativePreviewHandler(new NativePreviewHandler() {
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
		super.animateShow(effect);
	}
	
	/**
	 * hide() instead of animateHide(), to make sure that the panel hides immediately (when navigating away from the ribbon).
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
