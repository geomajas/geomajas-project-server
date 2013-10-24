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

package org.geomajas.gwt2.widget.client.map;

import com.google.gwt.core.client.GWT;
import org.geomajas.annotation.Api;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import org.geomajas.gwt2.widget.client.i18n.WidgetCoreInternationalization;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Drop down button that displays the {@link LegendPopupPanel}. When this widget is added to a MapPresenter's widget
 * panel, it will automatically try to keep the {@link LegendPopupPanel} within the map bounds.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public class MapLegendDropDown extends Button {
	private static final WidgetCoreInternationalization MSG = GWT.create(WidgetCoreInternationalization.class);

	private final MapPresenter mapPresenter;

	private PopupPanel popup;

	private Timer timer;

	/**
	 * Create a drop down button that displays a {@link MapLegendPanel} in a popup.
	 * 
	 * @param mapPresenter
	 *            The map to display a legend widget for.
	 */
	public MapLegendDropDown(final MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		setHTML(MSG.legendButName());
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (popup == null) {
					popup = new PopupPanel();
					popup.setAutoHideEnabled(true);
					popup.setAutoHideOnHistoryEventsEnabled(true);
					popup.setWidget(new MapLegendPanel(mapPresenter));
					popup.addAutoHidePartner(MapLegendDropDown.this.getElement());
					showPopup();
				} else if (popup.isShowing()) {
					popup.hide();
				} else {
					showPopup();
				}
			}
		});
	}

	protected void showPopup() {
		positionPopup();
		popup.show();

		popup.setPopupPositionAndShow(new PositionCallback() {

			public void setPosition(int offsetWidth, int offsetHeight) {
				positionPopup();
				if (timer != null) {
					timer.cancel();
				}
				timer = new Timer() {

					public void run() {
						positionPopup();
					}
				};
				timer.schedule(1);
			}
		});
	}

	protected void positionPopup() {
		int top = calculateTop(0);
		int left = calculateLeft(0);
		if (getParent() == mapPresenter.getWidgetPane()) {
			left = calculateLeftInMap(0);
		}
		popup.setPopupPosition(left, top);
	}

	private int calculateTop(int offsetHeight) {
		// Calculate top position for the popup
		int top = this.getAbsoluteTop();

		// Make sure scrolling is taken into account, since
		// box.getAbsoluteTop() takes scrolling into account.
		int windowTop = Window.getScrollTop();
		int windowBottom = Window.getScrollTop() + Window.getClientHeight();

		// Distance from the top edge of the window to the top edge of the
		// text box
		int distanceFromWindowTop = top - windowTop;

		// Distance from the bottom edge of the window to the bottom edge of
		// the text box
		int distanceToWindowBottom = windowBottom - (top + this.getOffsetHeight());

		// If there is not enough space for the popup's height below the text
		// box and there IS enough space for the popup's height above the text
		// box, then then position the popup above the text box. However, if there
		// is not enough space on either side, then stick with displaying the
		// popup below the text box.
		if (distanceToWindowBottom < offsetHeight && distanceFromWindowTop >= offsetHeight) {
			top -= offsetHeight;
		} else {
			// Position above the text box
			top += this.getOffsetHeight();
		}
		return top;
	}

	private int calculateLeftInMap(int offsetWidth) {
		int left = calculateLeft(offsetWidth);

		int popupRight = left + popup.getOffsetWidth();
		int mapRight = mapPresenter.getWidgetPane().getAbsoluteLeft() + mapPresenter.getWidgetPane().getOffsetWidth();
		if (popupRight > mapRight) {
			// Align right:
			int buttonRight = getAbsoluteLeft() + getOffsetWidth();
			left = buttonRight - popup.getOffsetWidth();
		}

		return left;
	}

	private int calculateLeft(int offsetWidth) {
		// Calculate left position for the popup. The computation for
		// the left position is bidi-sensitive.
		int textBoxOffsetWidth = this.getOffsetWidth();

		// Compute the difference between the popup's width and the
		// textbox's width
		int offsetWidthDiff = offsetWidth - textBoxOffsetWidth;

		int left = this.getAbsoluteLeft();
		// If the suggestion popup is not as wide as the text box, always align to
		// the left edge of the text box. Otherwise, figure out whether to
		// left-align or right-align the popup.
		if (offsetWidthDiff > 0) {
			// Make sure scrolling is taken into account, since
			// box.getAbsoluteLeft() takes scrolling into account.
			int windowRight = Window.getClientWidth() + Window.getScrollLeft();
			int windowLeft = Window.getScrollLeft();

			// Distance from the left edge of the text box to the right edge
			// of the window
			int distanceToWindowRight = windowRight - left;

			// Distance from the left edge of the text box to the left edge of the
			// window
			int distanceFromWindowLeft = left - windowLeft;

			// If there is not enough space for the overflow of the popup's
			// width to the right of hte text box, and there IS enough space for the
			// overflow to the left of the text box, then right-align the popup.
			// However, if there is not enough space on either side, then stick with
			// left-alignment.
			if (distanceToWindowRight < offsetWidth && distanceFromWindowLeft >= offsetWidthDiff) {
				// Align with the right edge of the text box.
				left -= offsetWidthDiff;
			}
		}
		return left;
	}
}