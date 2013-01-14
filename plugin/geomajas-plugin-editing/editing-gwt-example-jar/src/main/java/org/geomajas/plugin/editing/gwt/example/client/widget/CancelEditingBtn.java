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

package org.geomajas.plugin.editing.gwt.example.client.widget;

import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for cancelling the editing process.
 * 
 * @author Pieter De Graef
 */
public class CancelEditingBtn extends ToolStripButton implements GeometryEditStartHandler, GeometryEditStopHandler {

	public CancelEditingBtn(final GeometryEditService service) {
		setIcon("[ISOMORPHIC]/geomajas/silk/cancel.png");
		setIconSize(24);
		setHeight(32);
		setDisabled(true);
		setHoverWidth(400);
		setTooltip("Cancel the editing process. This will undo all operations - nothing will be saved.");
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				service.stop();
			}
		});
		service.addGeometryEditStartHandler(this);
		service.addGeometryEditStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		setDisabled(false);
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		setDisabled(true);
	}
}