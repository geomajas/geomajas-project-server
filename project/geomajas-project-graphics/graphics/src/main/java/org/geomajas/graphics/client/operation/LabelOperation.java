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
package org.geomajas.graphics.client.operation;

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.Textable;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Operation that labels an object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LabelOperation implements GraphicsOperation {

	private String beforeLabel;

	private String afterLabel;

	private String beforeColor;

	private String afterColor;

	private int beforeSize;

	private int afterSize;

	private String beforeFont;

	private String afterFont;

	private GraphicsObject labeled;
	
	public LabelOperation(GraphicsObject labeled, GraphicsService service, String beforeLabel, String afterLabel) {
		this(labeled, service , beforeLabel, "black", 20, "arial", afterLabel, "black", 20, "arial");
	}

	public LabelOperation(GraphicsObject labeled, GraphicsService service, String beforeLabel,
			String beforeColor, int beforeSize, String beforeFont, 
			String afterLabel, String afterColor, int afterSize,
			String afterFont) {
		this.beforeLabel = beforeLabel;
		this.afterLabel = afterLabel;
		this.beforeColor = beforeColor;
		this.afterColor = afterColor;
		this.beforeSize = beforeSize;
		this.afterSize = afterSize;
		this.beforeFont = beforeFont;
		this.afterFont = afterFont;
		this.labeled = labeled;
	}

	@Override
	public void execute() {
		asTextable().setLabel(afterLabel);
		asTextable().setFontColor(afterColor);
		asTextable().setFontSize(afterSize);
		asTextable().setFontFamily(afterFont);
	}

	@Override
	public void undo() {
		asTextable().setLabel(beforeLabel);
		asTextable().setFontColor(beforeColor);
		asTextable().setFontSize(beforeSize);
		asTextable().setFontFamily(beforeFont);
	}

	public Textable asTextable() {
		Textable textable = null;
		if (labeled.hasRole(Labeled.TYPE)) {
			textable = labeled.getRole(Labeled.TYPE).getTextable();
		} else if (labeled.hasRole(Textable.TYPE)) {
			textable = labeled.getRole(Textable.TYPE);
		}
		return textable;
	}

	@Override
	public GraphicsObject getObject() {
		return labeled;
	}

	@Override
	public Type getType() {
		return Type.UPDATE;
	}

}
