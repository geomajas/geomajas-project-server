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

package org.geomajas.plugin.editing.puregwt.example.client.button;

import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * Button that cancels the editing process.
 * 
 * @author Pieter De Graef
 */
public class CancelButton extends Button implements GeometryEditStartHandler, GeometryEditStopHandler {

	private final GeometryEditService editService;

	public CancelButton(GeometryEditService editService) {
		super("Cancel");
		this.editService = editService;
		this.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				CancelButton.this.editService.stop();
			}
		});
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		this.setEnabled(false);
	}

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		this.setEnabled(true);
	}
}