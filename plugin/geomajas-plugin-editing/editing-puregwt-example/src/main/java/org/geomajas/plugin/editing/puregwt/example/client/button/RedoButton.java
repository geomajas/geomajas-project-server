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

import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * Button that executes an "REDO" during the editing process.
 * 
 * @author Pieter De Graef
 */
public class RedoButton extends Button implements GeometryEditStopHandler, GeometryEditShapeChangedHandler {

	private GeometryEditService editService;

	public RedoButton() {
		super("Redo");
		this.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				try {
					editService.redo();
					onGeometryShapeChanged(null);
				} catch (GeometryOperationFailedException e) {
					e.printStackTrace();
				}
			}
		});
		this.setEnabled(false);
	}
	
	public void setEditService(GeometryEditService editService) {
		this.editService = editService;
		editService.addGeometryEditShapeChangedHandler(this);
		editService.addGeometryEditStopHandler(this);
		this.setEnabled(editService.canRedo());
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		this.setEnabled(false);
	}

	@Override
	public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
		if (editService.canRedo()) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
}