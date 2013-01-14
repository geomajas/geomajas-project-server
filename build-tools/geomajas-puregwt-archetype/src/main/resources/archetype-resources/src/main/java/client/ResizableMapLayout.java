#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.client;

import org.geomajas.gwt.client.util.Log;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * A layout widget for automatic resizing of a map. This widget must be created with a {@link MapPresenter}. This given
 * {@link MapPresenter} will then be added to the inner layout within this class. Don't add the map anywhere else.
 * </p>
 * <p>
 * One warning though, the size of the map is calculated through the offsetWidth and offsetHeight of the containing
 * layout. It is not smart enough to keep padding, margin or border into account, so never use any of those on this
 * layout.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class ResizableMapLayout implements IsWidget {

	private final MapPresenter mapPresenter;

	private final ResizeLayoutPanel layout;

	/**
	 * Create a resizable map layout for the given map. The map will be added to this layout.
	 * 
	 * @param mapPresenter
	 *            The map to add.
	 */
	public ResizableMapLayout(final MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		mapPresenter.getEventBus().addHandler(MapInitializationHandler.TYPE, new RedrawMapInitializationHandler());

		layout = new ResizeLayoutPanel();
		layout.setSize("100%", "100%");
		layout.add(mapPresenter.asWidget());

		// Add an automatic resize handler to set the correct size when the window resizes:
		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				applySize();
			}
		});

		// Calculate the correct size on load:
		layout.addAttachHandler(new AttachEvent.Handler() {

			public void onAttachOrDetach(AttachEvent event) {
				Timer timer = new Timer() {

					@Override
					public void run() {
						if (!applySize()) {
							schedule(50);
						}
					}
				};
				timer.run();
			}
		});
	}

	/**
	 * Returns the layout containing the map.
	 */
	public Widget asWidget() {
		return layout;
	}

	/**
	 * Get the map that's within this layout.
	 * 
	 * @return The map that's within this layout.
	 */
	public MapPresenter getMapPresenter() {
		return mapPresenter;
	}

	// ------------------------------------------------------------------------
	// Private methods and classes:
	// ------------------------------------------------------------------------

	private boolean applySize() {
		int width = layout.getOffsetWidth();
		int height = layout.getOffsetHeight();
		if (width > 10 && height > 10) {
			mapPresenter.setSize(width, height);
			try {
				mapPresenter.getViewPort().applyBounds(mapPresenter.getViewPort().getBounds());
			} catch (Throwable t) {
				Log.logWarn("Could not center map..." + t.getMessage());
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Map initialization handler that zooms in.
	 * 
	 * @author Pieter De Graef
	 */
	private class RedrawMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			applySize();
		}
	}
}