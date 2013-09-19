/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.action;

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.ResizableGraphicsObject;
import org.geomajas.graphics.client.object.anchor.Anchorable;
import org.geomajas.graphics.client.object.labeler.ResizableLabeler;
import org.geomajas.graphics.client.object.role.ExternalizableLabeled;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.operation.ToggleExternalizableLabelOperation;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Action to delete a {@link GraphicsObject}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ToggleLabelAction implements Action {

	private GraphicsService service;
	
	private String iconUrl;
	
	private String label = "toggle label";

	public boolean supports(GraphicsObject object) {
		if (object.hasRole(Anchorable.TYPE) && object.hasRole(Labeled.TYPE)
				&& object.getRole(Labeled.TYPE) instanceof ExternalizableLabeled) {
	// set correct resizable for the ExternalizableLabeled object (possible it is still the constrcution object)
			ExternalizableLabeled eLabeled = (ExternalizableLabeled) object.getRole(Labeled.TYPE);
			if (eLabeled instanceof ResizableLabeler) {
				((ResizableLabeler) eLabeled).setResizable((ResizableGraphicsObject) object);
			}
			
			// Set position of external label
//			ExternalLabel exLab = eLabeled.getExternalLabel();
//			exLab.setPosition(((ResizableGraphicsObject) object).getPosition());
			return true;
		}
		return false;
	}

	public void execute(GraphicsObject object) {
		service.execute(new ToggleExternalizableLabelOperation(object));
	}

	@Override
	public void setService(GraphicsService service) {
		this.service = service;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public void setIconUrl(String url) {
		this.iconUrl = url;
	}

	@Override
	public String getIconUrl() {
		return iconUrl;
	}

}
