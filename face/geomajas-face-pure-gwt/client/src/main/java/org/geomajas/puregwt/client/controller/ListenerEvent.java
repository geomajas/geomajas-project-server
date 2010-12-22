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

package org.geomajas.puregwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.puregwt.client.map.TransformationService;

import com.google.gwt.dom.client.Element;

/**
 * A listener event. When applying passive listeners on the map, this type of event is used to notify the listener of
 * mouse events that have occurred. It does not provide the event itself, but information about it. Also, since this
 * listener is MapWidget-oriented, all the getX, getScreenX, getClientX, getRelativeX etc, have been replace by
 * getScreenPosition and getWorldPosition.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ListenerEvent {

	private int event;

	private Coordinate screenPosition;

	private Element target;

	private boolean altKeyDown;

	private boolean controlKeyDown;

	private boolean shiftKeyDown;

	private int button;

	private TransformationService transformer;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Protected constructor for a listener event. It immediately provides all the necessary information.
	 * 
	 * @param event
	 *            The type of event. See <code>com.google.gwt.user.client.Event</code>.
	 * @param screenPosition
	 *            The position of the mouse, expressed in screen coordinates (pixels).
	 * @param target
	 *            The target element (HTML/VML/SVG) over which the mouse was hovering.
	 * @param transformer
	 *            Transformer that can transform the screen position to a world position.
	 * @param altKeyDown
	 *            Is <code>alt</code> key down.
	 * @param controlKeyDown
	 *            Is <code>control</code> key down.
	 * @param shiftKeyDown
	 *            Is <code>shift</code> key down.
	 * @param button
	 *            Gets the button value. Compare it to {@link com.google.gwt.dom.client.NativeEvent#BUTTON_LEFT},
	 *            {@link com.google.gwt.dom.client.NativeEvent#BUTTON_RIGHT},
	 *            {@link com.google.gwt.dom.client.NativeEvent#BUTTON_MIDDLE}
	 */
	protected ListenerEvent(int event, Coordinate screenPosition, Element target, TransformationService transformer,
			boolean altKeyDown, boolean controlKeyDown, boolean shiftKeyDown, int button) {
		this.event = event;
		this.screenPosition = screenPosition;
		this.target = target;
		this.transformer = transformer;
		this.altKeyDown = altKeyDown;
		this.controlKeyDown = controlKeyDown;
		this.shiftKeyDown = shiftKeyDown;
		this.button = button;
	}

	// ------------------------------------------------------------------------
	// Getters only:
	// ------------------------------------------------------------------------

	/**
	 * Returns the type of mouse event. Possible values are:
	 * <ul>
	 * <li>com.google.gwt.user.client.Event.ONMOUSEDOWN</li>
	 * <li>com.google.gwt.user.client.Event.ONMOUSEUP</li>
	 * <li>com.google.gwt.user.client.Event.ONMOUSEMOVE</li>
	 * <li>com.google.gwt.user.client.Event.ONMOUSEOUT</li>
	 * <li>com.google.gwt.user.client.Event.ONMOUSEOVER</li>
	 * <li>com.google.gwt.user.client.Event.ONMOUSEWHEEL</li>
	 * </ul>
	 * Any combination of the above is also acceptable. For example: <code>(Event.ONMOUSEDOWN | Event.ONMOUSEUP)</code>.
	 * 
	 * @return Returns the mouse event as an integer.
	 */
	public int getEvent() {
		return event;
	}

	/**
	 * Returns the position of the mouse, expressed in screen coordinates (pixels).
	 * 
	 * @return The coordinate representing the location.
	 */
	public Coordinate getScreenPosition() {
		return screenPosition;
	}

	/**
	 * Returns the position of the mouse, expressed in world coordinates (map CRS).
	 * 
	 * @return The coordinate representing the location.
	 */
	public Coordinate getWorldPosition() {
		return transformer.viewToWorld(screenPosition);
	}

	/**
	 * Returns the target element (HTML/VML/SVG) over which the mouse was hovering.
	 * 
	 * @return The element object.
	 */
	public Element getEventTarget() {
		return target;
	}

	/**
	 * Gets the button value. Compare it to {@link com.google.gwt.dom.client.NativeEvent#BUTTON_LEFT},
	 * {@link com.google.gwt.dom.client.NativeEvent#BUTTON_RIGHT},
	 * {@link com.google.gwt.dom.client.NativeEvent#BUTTON_MIDDLE}
	 * 
	 * @return the button value
	 */
	public int getNativeButton() {
		return button;
	}

	/**
	 * Is <code>alt</code> key down.
	 * 
	 * @return whether the alt key is down
	 */
	public boolean isAltKeyDown() {
		return altKeyDown;
	}

	/**
	 * Is <code>control</code> key down.
	 * 
	 * @return whether the control key is down
	 */
	public boolean isControlKeyDown() {
		return controlKeyDown;
	}

	/**
	 * Is <code>shift</code> key down.
	 * 
	 * @return whether the shift key is down
	 */
	public boolean isShiftKeyDown() {
		return shiftKeyDown;
	}
}