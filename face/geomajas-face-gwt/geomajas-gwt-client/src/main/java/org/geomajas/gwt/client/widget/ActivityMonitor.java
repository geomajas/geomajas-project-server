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

package org.geomajas.gwt.client.widget;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.event.DispatchStartedEvent;
import org.geomajas.gwt.client.command.event.DispatchStartedHandler;
import org.geomajas.gwt.client.command.event.DispatchStoppedEvent;
import org.geomajas.gwt.client.command.event.DispatchStoppedHandler;
import org.geomajas.gwt.client.i18n.I18nProvider;

import com.smartgwt.client.widgets.Canvas;

/**
 * <p>
 * This widget is an extension of the {@link Canvas} widget that displays client-server communication activity. It is
 * registered with the {@link GwtCommandDispatcher} static class to catch {@link DispatchStartedEvent}s and
 * {@link DispatchStoppedEvent}s.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class ActivityMonitor extends Canvas implements DispatchStoppedHandler, DispatchStartedHandler {

	/** The icon to use when the client is not in busy state. */
	private String notBusyIcon = "geomajas/not_busy.gif";

	/** The icon to use when the client is in busy state. */
	private String busyIcon = "geomajas/busy.gif";

	/** The busy state. */
	private boolean busy;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	/** The only constructor. It immediately sets the correct width and height: 100x17. */
	public ActivityMonitor() {
		setPadding(0);
		setSize("100%", "17px");
		setBusy(GwtCommandDispatcher.getInstance().isBusy());
		GwtCommandDispatcher.getInstance().addDispatchStoppedHandler(this);
		GwtCommandDispatcher.getInstance().addDispatchStartedHandler(this);
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

	/** Is the monitor currently in busy state? */
	public boolean isBusy() {
		return busy;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/** The icon to use when the client is not in busy state. */
	public String getNotBusyIcon() {
		return notBusyIcon;
	}

	/** Set a new icon to use when the client is not in busy state. */
	public void setNotBusyIcon(String notBusyIcon) {
		this.notBusyIcon = notBusyIcon;
	}

	/** The icon to use when the client is in busy state. */
	public String getBusyIcon() {
		return busyIcon;
	}

	/** Set a new icon to use when the client is in busy state. */
	public void setBusyIcon(String busyIcon) {
		this.busyIcon = busyIcon;
	}

	/**
	 * Set the busy state to false.
	 * 
	 * @param event
	 *            stopped event
	 */
	public void onDispatchStopped(DispatchStoppedEvent event) {
		setBusy(GwtCommandDispatcher.getInstance().isBusy());
	}

	/**
	 * Set the busy state to true.
	 * 
	 * @param event
	 *            started event
	 * @param event
	 *            started event
	 */
	public void onDispatchStarted(DispatchStartedEvent event) {
		setBusy(true);
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected void onDraw() {
		super.onDraw();
		setBusy(false);
	}

	private void setBusy(boolean busy) {
		this.busy = busy;
		String src = null;
		String text = null;
		String color = null;
		if (busy) {
			src = Geomajas.getIsomorphicDir() + busyIcon;
			text = I18nProvider.getGlobal().activityBusyText();
			color = "#000066";
		} else {
			src = Geomajas.getIsomorphicDir() + notBusyIcon;
			text = I18nProvider.getGlobal().activityNotBusyText();
			color = "#505050";
		}
		if (isDrawn()) {
			int w = getWidth();
			int h = getHeight();
			setContents("<div style='position:relative; width:" + w + "; height:" + h + ";'>" + "<div style='width:"
					+ w + "; height:" + h + ";'>" + "<img src='" + src + "' border='0' style='width:" + w + "; height:"
					+ h + ";' /></div>" + "<div style='position:absolute; width:" + w + "; height:" + h
					+ "; text-align: center; top: 0px; z-index: 1000; color:" + color + ";'>" + text + "</div></div>");
		}
	}
}
