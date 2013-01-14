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

package org.geomajas.plugin.editing.gwt.example.client.merge;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.merge.GeometryMergeException;
import org.geomajas.plugin.editing.client.merge.GeometryMergeService;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStartEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStartHandler;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStopEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStopHandler;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for cancelling the editing process.
 * 
 * @author Pieter De Graef
 */
public class CancelMergeProcessButton extends ToolStripButton implements GeometryMergeStartHandler,
		GeometryMergeStopHandler {

	public CancelMergeProcessButton(final MapWidget mapWidget, final GeometryMergeService service) {
		setIcon("[ISOMORPHIC]/geomajas/silk/cancel.png");
		setDisabled(true);
		setHoverWidth(400);
		setTooltip("Cancel the merging process. This will also clear the selection on the map.");
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				try {
					mapWidget.getMapModel().clearSelectedFeatures();
					service.cancel();
				} catch (GeometryMergeException e) {
					Window.alert(e.getMessage());
				}
			}
		});
		service.addGeometryMergeStartHandler(this);
		service.addGeometryMergeStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryMergingStop(GeometryMergeStopEvent event) {
		setDisabled(true);
	}

	public void onGeometryMergingStart(GeometryMergeStartEvent event) {
		setDisabled(false);
	}
}