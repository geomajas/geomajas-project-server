package org.geomajas.gwt.client.action.toolbar.parameter;

import org.geomajas.configuration.Parameter;


public class ButtonLayoutParameter extends Parameter {
	
	public enum Layout {
		/**
		 * Button layout, which is the same as a RibbonButton 
		 * in a ToolbarActionList; icon (16px) on the left and title on the right.
		 */
		ICON_AND_TITLE,
		/**
		 * Button layout consisting of an icon (24px) on the 
		 * left and the title and description on the right, the title on top of the description.
		 */
		ICON_TITLE_AND_DESCRIPTION
	}

	private static final long serialVersionUID = 1L;
	public static final String NAME = "buttonLayout";
	private Layout layout;
	
	public ButtonLayoutParameter() {
		super.setName(NAME);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		throw new IllegalArgumentException("Public naming is not allowed for LayoutParameter (its name is always 'buttonLayout')");
	}
	
	@Override
	public void setValue(String value) {
		this.layout = Layout.valueOf(value);
		super.setValue(layout.toString());
	}
	
	public void setValue(Layout layout) {
		this.layout = layout;
		super.setValue(layout.toString());
	}
	
	public Layout getLayoutValue() {
		return layout;
	}

}
