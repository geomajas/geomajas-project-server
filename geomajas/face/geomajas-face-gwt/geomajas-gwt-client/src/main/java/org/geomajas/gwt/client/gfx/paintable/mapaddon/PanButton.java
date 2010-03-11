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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.PanArrowController;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.user.client.Event;
import com.smartgwt.client.types.Cursor;

/**
 * <p>
 * Definition of a MapAddon that renders a single panning button.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class PanButton extends MapAddon {

	/**
	 * The direction in which pan buttons can translate the map.
	 * 
	 * @author Pieter De Graef
	 */
	public enum PanButtonDirection {
		NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
	}

	private PanButtonDirection direction;

	private String imgUrl;

	private GraphicsController controller;

	private Coordinate panVector;

	private Image image;

	private MapWidget map;

	private PictureStyle style;

	private Object parent;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create a new pan button for the given map that pans in the given direction.
	 * 
	 * @param id
	 *            The unique identifier.
	 * @param map
	 *            The map onto whom this pan button is drawn.
	 * @param direction
	 *            The direction in which this pan button should move the map.
	 */
	public PanButton(String id, MapWidget map, PanButtonDirection direction) {
		super(id, 18, 18);
		this.direction = direction;
		this.map = map;
		style = new PictureStyle(0.7);
		applyDirection();
	}

	// -------------------------------------------------------------------------
	// MapAddon implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint this pan button!
	 */
	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		// First place the image at the correct location:
		image.setBounds(new Bbox(getUpperLeftCorner().getX(), getUpperLeftCorner().getY(), getWidth(), getHeight()));

		// Then draw:
		map.getGraphics().drawImage(parent, getId(), image.getHref(), image.getBounds(),
				(PictureStyle) image.getStyle());
	}

	/**
	 * When this pan button is drawn for the first time, add a pan controller to it that reacts on the click event.
	 */
	public void onDraw() {
		map.getGraphics().setController(parent, getId(), controller, Event.MOUSEEVENTS);
		map.getGraphics().setCursor(parent, getId(), Cursor.POINTER.getValue());
	}

	/**
	 * Does nothing.
	 */
	public void onRemove() {
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public PanButtonDirection getDirection() {
		return direction;
	}

	public void setDirection(PanButtonDirection direction) {
		this.direction = direction;
		applyDirection();
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Image getImage() {
		return image;
	}

	public PictureStyle getStyle() {
		return style;
	}

	public void setStyle(PictureStyle style) {
		this.style = style;
		image.setStyle(style);
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void applyDirection() {
		switch (direction) {
			case NORTH:
				panVector = new Coordinate(0, 1);
				imgUrl = Geomajas.getIsomorphicDir() + "geomajas/temp/pan_up.gif";
				break;
			case NORTHEAST:
				panVector = new Coordinate(1, 1);
				imgUrl = Geomajas.getIsomorphicDir() + "geomajas/nav_up_right.gif";
				break;
			case EAST:
				panVector = new Coordinate(1, 0);
				imgUrl = Geomajas.getIsomorphicDir() + "geomajas/temp/pan_right.gif";
				break;
			case SOUTHEAST:
				panVector = new Coordinate(1, -1);
				imgUrl = Geomajas.getIsomorphicDir() + "geomajas/nav_down_right.gif";
				break;
			case SOUTH:
				panVector = new Coordinate(0, -1);
				imgUrl = Geomajas.getIsomorphicDir() + "geomajas/temp/pan_down.gif";
				break;
			case SOUTHWEST:
				panVector = new Coordinate(-1, -1);
				imgUrl = Geomajas.getIsomorphicDir() + "geomajas/nav_down_left.gif";
				break;
			case WEST:
				panVector = new Coordinate(-1, 0);
				imgUrl = Geomajas.getIsomorphicDir() + "geomajas/temp/pan_left.gif";
				break;
			case NORTHWEST:
				panVector = new Coordinate(-1, 1);
				imgUrl = Geomajas.getIsomorphicDir() + "geomajas/nav_up_left.gif";
				break;
		}
		controller = new PanArrowController(map, panVector);
		image = new Image(getId());
		image.setHref(imgUrl);
		image.setStyle(style);
		image.setBounds(new Bbox(0, 0, getWidth(), getHeight()));
	}
}
