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
		NORTH, EAST, SOUTH, WEST
	}

	private String northImage = Geomajas.getIsomorphicDir() + "geomajas/mapaddon/pan_up.gif";

	private String eastImage = Geomajas.getIsomorphicDir() + "geomajas/mapaddon/pan_right.gif";

	private String southImage = Geomajas.getIsomorphicDir() + "geomajas/mapaddon/pan_down.gif";

	private String westImage = Geomajas.getIsomorphicDir() + "geomajas/mapaddon/pan_left.gif";

	private PanButtonDirection direction;

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
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		// First place the image at the correct location:
		image.setBounds(new Bbox(getUpperLeftCorner().getX(), getUpperLeftCorner().getY(), getWidth(), getHeight()));

		// Then draw:
		if (parent != null) {
			map.getVectorContext().drawImage(parent, getId(), image.getHref(), image.getBounds(),
					(PictureStyle) image.getStyle());
		} else {
			map.getVectorContext().drawImage(group, getId(), image.getHref(), image.getBounds(),
					(PictureStyle) image.getStyle());
		}
	}

	/**
	 * When this pan button is drawn for the first time, add a pan controller to it that reacts on the click event.
	 */
	public void onDraw() {
		map.getVectorContext().setController(parent, getId(), controller, Event.MOUSEEVENTS);
		map.getVectorContext().setCursor(parent, getId(), Cursor.POINTER.getValue());
	}

	/**
	 * Does nothing.
	 */
	public void onRemove() {
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Return the current style used for the pan button image.
	 */
	public PictureStyle getStyle() {
		return style;
	}

	/**
	 * Apply a new style to be used on this pan button image. To make it visible, redraw the pan button.
	 * 
	 * @param style
	 *            The new style for the pan button image.
	 */
	public void setStyle(PictureStyle style) {
		this.style = style;
		image.setStyle(style);
	}

	/**
	 * When rendering, where should the pan button be drawn? If this parent is not null, the pan button will be added to
	 * the parent's group.
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * When rendering, where should the pan button be drawn? If this parent is not null, the pan button will be added to
	 * the parent's group. If the parent is null, the map's screen group will be used instead.
	 * 
	 * @param parent
	 *            The parent to whom to attach this pan button when drawing.
	 */
	public void setParent(Object parent) {
		this.parent = parent;
	}

	public String getNorthImage() {
		return northImage;
	}

	public void setNorthImage(String northImage) {
		this.northImage = northImage;
		applyDirection();
	}

	public String getEastImage() {
		return eastImage;
	}

	public void setEastImage(String eastImage) {
		this.eastImage = eastImage;
		applyDirection();
	}

	public String getSouthImage() {
		return southImage;
	}

	public void setSouthImage(String southImage) {
		this.southImage = southImage;
		applyDirection();
	}

	public String getWestImage() {
		return westImage;
	}

	public void setWestImage(String westImage) {
		this.westImage = westImage;
		applyDirection();
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void applyDirection() {
		String imgUrl = "";
		switch (direction) {
			case NORTH:
				panVector = new Coordinate(0, 1);
				imgUrl = northImage;
				break;
			case EAST:
				panVector = new Coordinate(1, 0);
				imgUrl = eastImage;
				break;
			case SOUTH:
				panVector = new Coordinate(0, -1);
				imgUrl = southImage;
				break;
			case WEST:
				panVector = new Coordinate(-1, 0);
				imgUrl = westImage;
				break;
		}
		controller = new PanArrowController(map, panVector);
		image = new Image(getId());
		image.setHref(imgUrl);
		image.setStyle(style);
		image.setBounds(new Bbox(0, 0, getWidth(), getHeight()));
	}
}
