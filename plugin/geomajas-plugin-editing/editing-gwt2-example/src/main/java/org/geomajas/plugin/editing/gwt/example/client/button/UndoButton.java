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

package org.geomajas.plugin.editing.gwt.example.client.button;

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
 * Button that executes an "UNDO" during the editing process.
 * 
 * @author Pieter De Graef
 */
public class UndoButton extends Button implements GeometryEditStopHandler, GeometryEditShapeChangedHandler {

	private GeometryEditService editService;

	public UndoButton() {
		super("Undo");
		this.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				try {
					editService.undo();
					onGeometryShapeChanged(null);
				} catch (GeometryOperationFailedException e) {
				}
			}
		});
		this.setEnabled(false);
	}
	
	public void setEditService(GeometryEditService editService) {
		this.editService = editService;
		editService.addGeometryEditShapeChangedHandler(this);
		editService.addGeometryEditStopHandler(this);
		this.setEnabled(editService.canUndo());
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		this.setEnabled(false);
	}

	@Override
	public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
		if (editService.canUndo()) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
}