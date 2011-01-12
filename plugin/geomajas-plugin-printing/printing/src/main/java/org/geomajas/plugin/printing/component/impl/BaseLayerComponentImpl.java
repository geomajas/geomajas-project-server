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
package org.geomajas.plugin.printing.component.impl;

import org.geomajas.plugin.printing.component.BaseLayerComponent;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.dto.BaseLayerComponentInfo;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Base class for layer components.
 * 
 * @author Jan De Moerloose
 * @param <T>
 *            DTO object class
 */
public abstract class BaseLayerComponentImpl<T extends BaseLayerComponentInfo> extends PrintComponentImpl<T> implements
		BaseLayerComponent<T> {

	/**
	 * True if layer is visible.
	 */
	private boolean visible;

	/**
	 * True if layer is selected.
	 */
	private boolean selected;

	/**
	 * ID of this layer.
	 */
	private String layerId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.IBaseLayerComponent#isVisible()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.BaseLayerComponent#isVisible()
	 */
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.IBaseLayerComponent#isSelected()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.BaseLayerComponent#isSelected()
	 */
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.IBaseLayerComponent#getLayerId()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.BaseLayerComponent#getLayerId()
	 */
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	MapComponent getMap() {
		return (MapComponent) getParent();
	}

	Envelope createBbox() {
		double x1 = getMap().getLocation().x;
		double x2 = x1 + (getBounds().getWidth() / getMap().getPpUnit());
		double y1 = getMap().getLocation().y;
		double y2 = y1 + (getBounds().getHeight() / getMap().getPpUnit());
		return new Envelope(x1, x2, y1, y2);
	}

	public void fromDto(T baseLayerInfo) {
		super.fromDto(baseLayerInfo);
		setLayerId(baseLayerInfo.getLayerId());
		setSelected(baseLayerInfo.isSelected());
		setVisible(baseLayerInfo.isVisible());
	}

}
