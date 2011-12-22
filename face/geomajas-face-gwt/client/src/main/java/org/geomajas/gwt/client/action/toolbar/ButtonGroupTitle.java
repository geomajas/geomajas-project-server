package org.geomajas.gwt.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
/**
 * Basic {@link ConfigurableAction} implementation that fetches a title and a buttonLayout from the parameters.
 * 
 * @author Emiel Ackermann
 */
public class ButtonGroupTitle extends ToolbarBaseAction implements ConfigurableAction{

	private String buttonLayout;

	public ButtonGroupTitle() {
		super("", "", "");
	}

	public void configure(String key, String value) {
		if ("title".equals(key)) {
			setTitle(value);
		} else if ("buttonLayout".equals(key)) {
			setButtonLayout(value);
		}
	}
	
	public String getButtonLayout() {
		return buttonLayout;
	}

	private void setButtonLayout(String buttonLayout) {
		this.buttonLayout = buttonLayout;
	}
}
