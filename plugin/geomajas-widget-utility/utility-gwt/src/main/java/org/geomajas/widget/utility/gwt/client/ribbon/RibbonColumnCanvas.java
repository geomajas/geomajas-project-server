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

package org.geomajas.widget.utility.gwt.client.ribbon;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarCanvas;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonCanvas;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;

/**
 * Wrapper class for {@link ToolbarButtonCanvas}, 
 * which contains a custom build {@link Canvas} that is fetched and 
 * vertically positioned in the {@link RibbonColumn#asWidget()} implementation 
 * using the configuration parameter "verticalAlignment".
 * 
 * @author Emiel Ackermann
 */
public class RibbonColumnCanvas implements RibbonColumn {
	
	private final ToolbarButtonCanvas toolbarButtonCanvas;
	private Canvas canvas;

	/**
	 * Constructor.
	 */
	public RibbonColumnCanvas(ToolbarButtonCanvas toolbarButtonCanvas) {
		this.toolbarButtonCanvas = toolbarButtonCanvas;
		canvas = ((ToolbarCanvas) toolbarButtonCanvas.getToolbarAction()).getCanvas();
	}

	/** {@inheritDoc} */
	public Widget asWidget() {
		canvas.setAutoHeight();
		VerticalAlignment verticalAlignment = toolbarButtonCanvas.getVerticalAlignment();
		canvas.setLayoutAlign(verticalAlignment);
		return canvas;
	}
	
	/** {@inheritDoc} */
	public void configure(String key, String value) {
		if (toolbarButtonCanvas instanceof ConfigurableAction) {
			ConfigurableAction ca = (ConfigurableAction) toolbarButtonCanvas;
			ca.configure(key, value);
		}
	}
	
	/** {@inheritDoc} */
	public boolean isEnabled() {
		return !canvas.isDisabled();
	}
	
	/** {@inheritDoc} */
	public void setEnabled(boolean enabled) {
		canvas.setDisabled(!enabled);
	}

	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so showTitles can not be set.
	 * 
	 * @param does nothing
	 */
	public void setShowTitles(boolean showTitles) {
	}

	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so null is returned.
	 * 
	 * @return null
	 */
	public boolean isShowTitles() {
		return (Boolean) null;
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so no titleAlignment can be set.
	 * 
	 * @param does nothing
	 */
	public void setTitleAlignment(TitleAlignment titleAlignment) {
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so null is returned.
	 * 
	 * @return null
	 */
	public TitleAlignment getTitleAlignment() {
		return null;
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so no buttonBaseStyle can be set.
	 * 
	 * @param does nothing
	 */
	public void setButtonBaseStyle(String buttonBaseStyle) {
	}
}
