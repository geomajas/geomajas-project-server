/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.widget.client.map;

import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Drop down button that displays a the legend of layers. The layers still have to be added manually. In order to
 * display the legend for a single layer, the {@link LayerLegendPanel} is used.
 * 
 * @author Pieter De Graef
 */
public class LegendDropDown extends Composite implements HasOpenHandlers<LegendDropDown>,
		HasCloseHandlers<LegendDropDown>, HasResizeHandlers {

	/**
	 * UI binder definition for the {@link LayerLegendPanel} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface LegendDropDownUiBinder extends UiBinder<Widget, LegendDropDown> {
	}

	private static final LegendDropDownUiBinder UI_BINDER = GWT.create(LegendDropDownUiBinder.class);

	private final MapPresenter mapPresenter;

	private boolean open;

	@UiField
	protected HTML title;

	@UiField
	protected SpanElement titleElement;

	@UiField
	protected VerticalPanel contentPanel;

	public LegendDropDown(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		initWidget(UI_BINDER.createAndBindUi(this));
		setWidth("100px");

		title.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				setOpen(!open);
			}
		});
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Add a layer to the legend drop down panel.
	 * 
	 * @param layer
	 *            The layer who's legend to add to the drop down panel.
	 * @return success or not.
	 */
	public boolean addLayer(Layer<?> layer) {
		int index = getLayerIndex(layer);
		if (index < 0) {
			contentPanel.add(new LayerLegendPanel(mapPresenter.getEventBus(), layer));
			return true;
		}
		return false;
	}

	/**
	 * Remove a layer from the drop down content panel again.
	 * 
	 * @param layer
	 *            The layer to remove.
	 * @return success or not.
	 */
	public boolean removeLayer(Layer<?> layer) {
		int index = getLayerIndex(layer);
		if (index >= 0) {
			contentPanel.remove(index);
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// HasCloseHandlers implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addCloseHandler(CloseHandler<LegendDropDown> handler) {
		return addHandler(handler, CloseEvent.getType());
	}

	// ------------------------------------------------------------------------
	// HasOpenHandlers implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addOpenHandler(OpenHandler<LegendDropDown> handler) {
		return addHandler(handler, OpenEvent.getType());
	}

	// ------------------------------------------------------------------------
	// HasResizeHandlers implementation:
	// ------------------------------------------------------------------------

	public HandlerRegistration addResizeHandler(ResizeHandler handler) {
		return addHandler(handler, ResizeEvent.getType());
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Is the drop down panel currently visible or not?
	 * 
	 * @return Is the drop down panel currently visible or not?
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Open or close the legend drop down panel.
	 * 
	 * @param open
	 *            The new value.
	 */
	public void setOpen(boolean open) {
		this.open = open;
		buildGui();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		contentPanel.setVisible(open);
		if (open) {
			int width = contentPanel.getOffsetWidth();
			setWidth(width + "px");
		} else {
			setWidth("100px");
		}
		ResizeEvent.fire(this, getOffsetWidth(), getOffsetHeight());
	}

	private int getLayerIndex(Layer<?> layer) {
		for (int i = 0; i < contentPanel.getWidgetCount(); i++) {
			LayerLegendPanel layerPanel = (LayerLegendPanel) contentPanel.getWidget(i);
			if (layerPanel.getLayer() == layer) {
				return i;
			}
		}
		return -1;
	}
}