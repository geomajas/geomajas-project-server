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

import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditResumeEvent;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditSuspendEvent;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditSuspendResumeHandler;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button that applies a "redo" after one or more "undo" actions have been executed.
 * 
 * @author Pieter De Graef
 */
public class RedoBtn extends ToolStripButton implements GeometryEditShapeChangedHandler, GeometryEditStartHandler,
		GeometryEditStopHandler, GeometryEditSuspendResumeHandler {

	private GeometryEditService service;

	public RedoBtn(final GeometryEditService service) {
		this.service = service;
		setIcon("[ISOMORPHIC]/geomajas/silk/redo.png");
		setIconSize(24);
		setHeight(32);
		setDisabled(true);
		setHoverWrap(false);
		setTooltip("Redo the last change.");
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				try {
					service.redo();
					onGeometryShapeChanged(null);
				} catch (GeometryOperationFailedException e) {
				}
			}
		});
		service.addGeometryEditShapeChangedHandler(this);
		service.addGeometryEditStartHandler(this);
		service.addGeometryEditStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryEditStart(GeometryEditStartEvent event) {
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		setDisabled(true);
	}

	// ------------------------------------------------------------------------
	// GeometryEditOperationHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
		if (service.canRedo()) {
			setDisabled(false);
		} else {
			setDisabled(true);
		}
	}

	// ------------------------------------------------------------------------
	// GeometryEditSuspensionHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryEditSuspend(GeometryEditSuspendEvent event) {
		setDisabled(true);
	}

	public void onGeometryEditResume(GeometryEditResumeEvent event) {
		if (service.canRedo()) {
			setDisabled(false);
		} else {
			setDisabled(true);
		}
	}
}