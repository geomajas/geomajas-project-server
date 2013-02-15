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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget;

import org.geomajas.gwt.client.action.toolbar.ZoomToSelectionAction;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.i18n.GeodeskMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * <p>
 * Shows some info when features are (de)selected.
 * <p>
 * Do not forget to initialize the widget.
 * 
 * @author Kristof Heirwegh
 * 
 * TODO: this should move to the GWT face 
 */
public class FeatureSelectionInfoWindow implements FeatureSelectionHandler {

	private static final GeodeskMessages MESSAGES = GWT.create(GeodeskMessages.class);

	private MapWidget mapWidget;

	private int width = 190;

	private int selectionCount;

	private Integer leftPosition;

	private VLayout tooltip;

	private Label info;
	
	private static final String CLEAR_SELECTION = "[ISOMORPHIC]/geomajas/osgeo/selected-delete.png";


	public void initialize(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		for (VectorLayer vl : mapWidget.getMapModel().getVectorLayers()) {
			vl.addFeatureSelectionHandler(this);
		}
	}

	public Integer getLeftPosition() {
		return leftPosition;
	}

	public void setLeftPosition(Integer leftPosition) {
		this.leftPosition = leftPosition;
	}

	// -------------------------------------------------

	private void update() {
		if (selectionCount > 0) {
			if (tooltip == null) {
				tooltip = new VLayout(5);
				info = new Label(getInfoText());
				info.setAutoHeight();
				info.setWidth100();
				info.setAlign(Alignment.CENTER);
				tooltip.setWidth(width);
				tooltip.setAutoHeight();
				tooltip.setParentElement(mapWidget);
				tooltip.setShowEdges(true);
				tooltip.setPadding(5);
				tooltip.setTop(-100);
				int left;
				if (leftPosition == null) {
					left = mapWidget.getWidth() - (width + 10); // right
				} else {
					left = leftPosition;
				}
				tooltip.setLeft(left);
				tooltip.setBackgroundColor("#FFFFFF");
				tooltip.setAnimateTime(500);

				tooltip.addMember(info);
				tooltip.addMember(getZoomToButton());
				tooltip.addMember(getDeselectButton());

				tooltip.animateMove(left, 10);
			} else {
				info.setContents(getInfoText());
			}
		} else {
			if (tooltip != null) {
				// might get a new selection while fading so need to remove old canvas first
				final VLayout tt = tooltip;
				tooltip = null;
				tt.animateFade(0, new AnimationCallback() {

					public void execute(boolean earlyFinish) {
						tt.destroy();
					}
				});
			}
		}
	}

	private String getInfoText() {
		if (selectionCount == 1) {
			return MESSAGES.featureSelectionInfoWindowOneFeatureSelected();
		} else {
			return MESSAGES.featureSelectionInfoWindowXFeaturesSelected(selectionCount );
		}
	}

	private ToolStripButton getZoomToButton() {
		ToolStripButton btn = new ToolStripButton(I18nProvider.getToolbar().zoomToSelectionTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png");
		btn.setWidth100();
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ZoomToSelectionAction ztsa = new ZoomToSelectionAction(mapWidget);
				ztsa.onClick(null);
			}
		});
		return btn;
	}

	private ToolStripButton getDeselectButton() {
		ToolStripButton btn = new ToolStripButton(I18nProvider.getMenu().deselectAll(), CLEAR_SELECTION);
		btn.setWidth100();
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				mapWidget.getMapModel().clearSelectedFeatures();
			}
		});
		return btn;
	}

	// ----------------------------------------------------------
	// -- FeatureSelectionHandler --
	// ----------------------------------------------------------

	public void onFeatureSelected(FeatureSelectedEvent event) {
		selectionCount++;
		update();
	}

	public void onFeatureDeselected(FeatureDeselectedEvent event) {
		selectionCount--;
		update();
	}
}
