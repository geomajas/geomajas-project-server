/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.extension.printing.component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * ???
 *
 * @author Jan De Moerloose
 */
public abstract class BaseLayerComponent extends BaseComponent {

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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

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
}
