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

package org.geomajas.puregwt.client.widget;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map widget that displays four panning arrows at the top-left of the map. This widget is meant to be added to the
 * map's widget pane (see {@link MapPresenter#getWidgetPane()}).
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class PanningWidget extends AbstractMapWidget {

	/**
	 * UI binder definition for the {@link PanningWidget} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface PanningGadgetUiBinder extends UiBinder<Widget, PanningWidget> {
	}

	private static final PanningGadgetUiBinder UI_BINDER = GWT.create(PanningGadgetUiBinder.class);

	private static final int DEFAULT_LEFT = 5;

	private static final int DEFAULT_TOP = 5;

	private Widget layout;

	@UiField
	protected SimplePanel topElement;

	@UiField
	protected SimplePanel rightElement;

	@UiField
	protected SimplePanel bottomElement;

	@UiField
	protected SimplePanel leftElement;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param mapPresenter
	 *            The map presenter.
	 */
	public PanningWidget(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	/** Get the widget layout. */
	public Widget asWidget() {
		if (layout == null) {
			buildGui();
		}
		return layout;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		layout = UI_BINDER.createAndBindUi(this);
		layout.getElement().getStyle().setPosition(Position.ABSOLUTE);
		layout.getElement().getStyle().setTop(DEFAULT_TOP, Unit.PX);
		layout.getElement().getStyle().setLeft(DEFAULT_LEFT, Unit.PX);
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		layout.addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		// Create TOP button:
		topElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Bbox bounds = mapPresenter.getViewPort().getBounds();
				double deltaY = bounds.getHeight() / 5;
				PanAnimation animation = new PanAnimation(mapPresenter.getViewPort());
				animation.panTo(0, deltaY, 300);
				event.stopPropagation();
			}
		}, ClickEvent.getType());

		// Create RIGHT button:
		rightElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Bbox bounds = mapPresenter.getViewPort().getBounds();
				double deltaX = bounds.getWidth() / 5;
				PanAnimation animation = new PanAnimation(mapPresenter.getViewPort());
				animation.panTo(deltaX, 0, 300);
				event.stopPropagation();
			}
		}, ClickEvent.getType());

		// Create BOTTOM button:
		bottomElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Bbox bounds = mapPresenter.getViewPort().getBounds();
				double deltaY = -bounds.getHeight() / 5;
				PanAnimation animation = new PanAnimation(mapPresenter.getViewPort());
				animation.panTo(0, deltaY, 300);
				event.stopPropagation();
			}
		}, ClickEvent.getType());

		// Create LEFT button:
		leftElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Bbox bounds = mapPresenter.getViewPort().getBounds();
				double deltaX = -bounds.getWidth() / 5;
				PanAnimation animation = new PanAnimation(mapPresenter.getViewPort());
				animation.panTo(deltaX, 0, 300);
				event.stopPropagation();
			}
		}, ClickEvent.getType());
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Animation for the panning buttons.
	 * 
	 * @author Pieter De Graef
	 */
	private class PanAnimation extends Animation {

		private ViewPort viewPort;

		private double translateX;

		private double translateY;

		private double previousProgress;

		public PanAnimation(ViewPort viewPort) {
			this.viewPort = viewPort;
		}

		public void panTo(double translateX, double translateY, int milliseconds) {
			this.translateX = translateX;
			this.translateY = translateY;
			previousProgress = 0;
			run(milliseconds);
		}

		protected void onUpdate(double progress) {
			double deltaX = (progress * translateX) - (previousProgress * translateX);
			double deltaY = (progress * translateY) - (previousProgress * translateY);

			Coordinate position = viewPort.getPosition();
			viewPort.applyPosition(new Coordinate(position.getX() + deltaX, position.getY() + deltaY));
			previousProgress = progress;
		}
	}
}