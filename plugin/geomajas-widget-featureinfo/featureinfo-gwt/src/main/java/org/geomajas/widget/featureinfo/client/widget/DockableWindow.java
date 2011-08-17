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
package org.geomajas.widget.featureinfo.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Document;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.events.RestoreClickEvent;
import com.smartgwt.client.widgets.events.RestoreClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * This window will move to the given dock when minimized.
 * <p>
 * TODO doesn't handle overflow (eg. when there are too many minimized windows.
 * 
 * @author Kristof Heirwegh
 */
public class DockableWindow extends Window {

	private static Canvas DOCK;
	private static int MINIMIZED_WIDTH = 225;
	private static int MINIMIZED_HEIGHT = 34;
	private static int CURRENTPOS = 5;

	public DockableWindow() {
		mixin(this);
	}

	// ----------------------------------------------------------

	public static Canvas getDock() {
		return DOCK;
	}

	/**
	 * @param dock
	 *            A canvas (for instance a VLayout or HLayout) where minimized
	 *            windows should be moved to.
	 */
	public static void setDock(Canvas dock) {
		DockableWindow.DOCK = dock;
	}

	public static int getMinimizedWidth() {
		return MINIMIZED_WIDTH;
	}

	/**
	 * Windows will be resized to this width to allow for multiple windows to be
	 * added to the dock.
	 * 
	 * @param minimizedWidth
	 */
	public static void setMinimizedWidth(int minimizedWidth) {
		DockableWindow.MINIMIZED_WIDTH = minimizedWidth;
	}

	/**
	 * Add the functionality to an existing window.
	 * 
	 * @param window
	 *            the window to which you want to add docking functionality.
	 */
	public static void mixin(Window window) {
		final Docker docker = new Docker(window);
		window.addMinimizeClickHandler(docker);
		window.addRestoreClickHandler(docker);
	}

	/**
	 * This will create a windowDock based on a HLayout and placed in the
	 * dockParent if provided.
	 * 
	 * @return a WindowDock
	 */
	public static HLayout createDefaultDock() {
		HLayout windowDock = new HLayout(5);
		windowDock.setHeight(22);
		windowDock.setAutoWidth();
		windowDock.setSnapTo("BL");
		windowDock.setSnapOffsetTop(-0);
		windowDock.setSnapOffsetLeft(0);
		windowDock.setBorder("thin dashed red");
		return windowDock;
	}

	/**
	 * Using a helper class instead of extending so we can use it for existing
	 * Windows (there's no real 'mixin' in java...).
	 * 
	 * @author Kristof Heirwegh
	 * 
	 */
	private static class Docker implements MinimizeClickHandler, RestoreClickHandler {

		private boolean docked;
		private int originalWidth;
		private int originalLeft;
		private int originalTop;
		private Boolean originalDragRepo;
		private Window window;

		public Docker(Window window) {
			this.window = window;
		}

		// -- Dock
		public void onMinimizeClick(MinimizeClickEvent event) {
			originalWidth = window.getWidth();
			originalLeft = window.getLeft();
			originalTop = window.getTop();
			originalDragRepo = window.getCanDragReposition();
			window.setCanDragReposition(false); // doens't work !?
			window.setAutoCenter(false);
			docked = true;

			GWT.runAsync(new RunAsyncCallback() {
				public void onSuccess() {
					window.animateRect(CURRENTPOS, Document.get().getBody().getClientHeight() - MINIMIZED_HEIGHT,
							MINIMIZED_WIDTH, null, new AnimationCallback() {
								public void execute(boolean earlyFinish) {
									CURRENTPOS += window.getWidth(); // not
																		// necessarily
																		// equal
																		// to
																		// minimizedWidth.
								}
							});
				}

				public void onFailure(Throwable reason) {
				}
			});
		}

		// -- UnDock
		public void onRestoreClick(RestoreClickEvent event) {
			if (docked) {
				CURRENTPOS -= window.getWidth();
				docked = false;
				GWT.runAsync(new RunAsyncCallback() {
					public void onSuccess() {
						window.animateRect(originalLeft, originalTop, originalWidth, null);
						window.setCanDragReposition(originalDragRepo);
					}

					public void onFailure(Throwable reason) {
					}
				});
			}
		}
	}
}
