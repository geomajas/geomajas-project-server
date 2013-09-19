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
package org.geomajas.graphics.client.controller;

import org.geomajas.graphics.client.object.GPath;
import org.geomajas.graphics.client.object.labeler.ResizableTemplateLabeler;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.TemplateLabeled;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Controller that creates a line with {@link TemplateLabeled} role.
 * 
 * @author Jan Venstermans
 * 
 */
public class CreateLineWithTemplateLabeledController extends CreatePathController {
	
	public CreateLineWithTemplateLabeledController(GraphicsService graphicsService) {
		super(graphicsService, false);
	}

	protected void addObject(GPath path) {
		path.removeRole(Labeled.TYPE);
		path.addRole(new ResizableTemplateLabeler());
		((TemplateLabeled) path.getRole(Labeled.TYPE)).setLabelTemplateText("Line");
		execute(new AddOperation(path));
	}
}
