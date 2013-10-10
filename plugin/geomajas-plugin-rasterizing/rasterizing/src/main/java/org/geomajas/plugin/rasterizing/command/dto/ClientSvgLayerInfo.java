package org.geomajas.plugin.rasterizing.command.dto;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;

public class ClientSvgLayerInfo extends ClientLayerInfo {

	private static final long serialVersionUID = 130L;

	private String svgContent;

	private int viewBoxWidth;

	private int viewBoxHeight;

	private Bbox viewBoxBounds;

	// default showing
	private boolean showing = true;

	/**
	 * Get the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @return The showing status of this layer
	 */
	public boolean isShowing() {
		return showing;
	}

	/**
	 * Sets the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @param showing
	 *            showing status of this layer
	 */
	public void setShowing(boolean showing) {
		this.showing = showing;
	}

	public String getSvgContent() {
		return svgContent;
	}

	public void setSvgContent(String svgContent) {
		this.svgContent = svgContent;
	}

	public int getViewBoxWidth() {
		return viewBoxWidth;
	}

	public void setViewBoxWidth(int viewBoxWidth) {
		this.viewBoxWidth = viewBoxWidth;
	}

	public int getViewBoxHeight() {
		return viewBoxHeight;
	}

	public void setViewBoxHeight(int viewBoxHeight) {
		this.viewBoxHeight = viewBoxHeight;
	}

	public Bbox getViewBoxBounds() {
		return viewBoxBounds;
	}

	public void setViewBoxBounds(Bbox viewBoxBounds) {
		this.viewBoxBounds = viewBoxBounds;
	}

}
