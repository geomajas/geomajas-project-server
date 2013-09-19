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

import org.geomajas.geometry.Bbox;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableGraphicsObject;
import org.geomajas.graphics.client.object.anchor.AnchoredTo;
import org.geomajas.graphics.client.object.anchor.ExternalLabelOfResizable;
import org.geomajas.graphics.client.resource.GraphicsResource;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Action to duplicate a {@link GraphicsObject}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DuplicateAction implements Action {

	private GraphicsService service;
	
	private String iconUrl;

	public boolean supports(GraphicsObject object) {
		if (object.hasRole(AnchoredTo.TYPE) && object.getRole(AnchoredTo.TYPE) instanceof ExternalLabelOfResizable) {
			return false;
		}
		return true;
	}

	public void execute(GraphicsObject object) {
		GraphicsObject object2 = (GraphicsObject) object.cloneObject();
		if (object2 instanceof ResizableGraphicsObject) {
			ResizableGraphicsObject object2asRgo = (ResizableGraphicsObject) object2;
			Bbox bounds = object2asRgo.getBounds();
			bounds.setX(bounds.getX() + 10.0);
			bounds.setY(bounds.getY() + 10.0);
			object2asRgo.getRole(Resizable.TYPE).setUserBounds(bounds);
		} else {
			object2.asObject().setTranslation(10.0, 10.0);
		}
		service.getObjectContainer().add(object2);
	}

	@Override
	public void setService(GraphicsService service) {
		this.service = service;
	}

	@Override
	public String getLabel() {
		return GraphicsResource.MESSAGES.duplicateActionLabel();
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
