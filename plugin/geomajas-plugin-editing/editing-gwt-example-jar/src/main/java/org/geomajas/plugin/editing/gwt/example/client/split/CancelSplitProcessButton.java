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

package org.geomajas.plugin.editing.gwt.example.client.split;

import org.geomajas.plugin.editing.client.split.GeometrySplitService;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartHandler;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopHandler;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for cancelling the editing process.
 * 
 * @author Pieter De Graef
 */
public class CancelSplitProcessButton extends ToolStripButton implements GeometrySplitStartHandler,
		GeometrySplitStopHandler {

	public CancelSplitProcessButton(final GeometrySplitService service) {
		setIcon("[ISOMORPHIC]/geomajas/silk/cancel.png");
		setIconSize(24);
		setHeight(32);
		setDisabled(true);
		setHoverWrap(false);
		setTooltip("Cancel the splitting process. Nothing will be saved.");
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				service.stop(null);
			}
		});
		service.addGeometrySplitStartHandler(this);
		service.addGeometrySplitStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometrySplitStart(GeometrySplitStartEvent event) {
		setDisabled(false);
	}

	public void onGeometrySplitStop(GeometrySplitStopEvent event) {
		setDisabled(true);
	}
}