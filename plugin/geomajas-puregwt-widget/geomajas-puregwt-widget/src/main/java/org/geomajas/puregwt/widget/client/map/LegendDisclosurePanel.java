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
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class LegendDisclosurePanel extends Composite implements HasOpenHandlers<LegendDisclosurePanel>,
		HasCloseHandlers<LegendDisclosurePanel>, HasAnimation {

	/**
	 * UI binder definition for the {@link LayerLegendPanel} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface LegendDisclosurePanelUiBinder extends UiBinder<Widget, LegendDisclosurePanel> {
	}

	private static final LegendDisclosurePanelUiBinder UI_BINDER = GWT.create(LegendDisclosurePanelUiBinder.class);

	private final MapPresenter mapPresenter;

	private boolean animationEnabled;

	private boolean open;

	@UiField
	protected HTML title;

	@UiField
	protected SpanElement titleElement;

	@UiField
	protected ImageElement closureImageElement;

	@UiField
	protected VerticalPanel contentPanel;

	public LegendDisclosurePanel(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		initWidget(UI_BINDER.createAndBindUi(this));

		title.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				setOpen(!open);
			}
		});
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	public boolean addLayer(Layer<?> layer) {
		int index = getLayerIndex(layer);
		if (index < 0) {
			contentPanel.add(new LayerLegendPanel(mapPresenter.getEventBus(), layer));
			return true;
		}
		return false;
	}

	public boolean removeLayer(Layer<?> layer) {
		int index = getLayerIndex(layer);
		if (index >= 0) {
			contentPanel.remove(index);
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// HasAnimation implementation:
	// ------------------------------------------------------------------------

	public boolean isAnimationEnabled() {
		return animationEnabled;
	}

	public void setAnimationEnabled(boolean enable) {
		this.animationEnabled = enable;
	}

	// ------------------------------------------------------------------------
	// HasCloseHandlers implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addCloseHandler(CloseHandler<LegendDisclosurePanel> handler) {
		return addHandler(handler, CloseEvent.getType());
	}

	// ------------------------------------------------------------------------
	// HasOpenHandlers implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addOpenHandler(OpenHandler<LegendDisclosurePanel> handler) {
		return addHandler(handler, OpenEvent.getType());
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
		buildGui();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		contentPanel.setVisible(open);
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