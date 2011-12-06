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

package org.geomajas.plugin.editing.gwt.example.client.merging;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.merging.GeometryMergingException;
import org.geomajas.plugin.editing.client.merging.GeometryMergingService;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartHandler;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopHandler;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for cancelling the editing process.
 * 
 * @author Pieter De Graef
 */
public class CancelMergingBtn extends ToolStripButton implements GeometryMergingStartHandler,
		GeometryMergingStopHandler {

	public CancelMergingBtn(final MapWidget mapWidget, final GeometryMergingService service) {
		setIcon("[ISOMORPHIC]/geomajas/silk/cancel.png");
		setDisabled(true);
		setHoverWidth(400);
		setTooltip("Cancel the merging process. This will also clear the selection on the map.");
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				try {
					mapWidget.getMapModel().clearSelectedFeatures();
					service.cancel();
				} catch (GeometryMergingException e) {
					Window.alert(e.getMessage());
				}
			}
		});
		service.addGeometryMergingStartHandler(this);
		service.addGeometryMergingStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryMergingStop(GeometryMergingStopEvent event) {
		setDisabled(true);
	}

	public void onGeometryMergingStart(GeometryMergingStartEvent event) {
		setDisabled(false);
	}
}