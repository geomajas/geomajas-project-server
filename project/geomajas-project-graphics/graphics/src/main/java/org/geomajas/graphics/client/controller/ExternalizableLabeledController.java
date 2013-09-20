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

import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsOperationEvent;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.role.ExternalizableLabeled;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.operation.LabelOperation;
import org.geomajas.graphics.client.operation.ToggleExternalizableLabelOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Controller to change object's {ExternalizableLabeled} role.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ExternalizableLabeledController extends AbstractGraphicsController 
			implements GraphicsObjectContainerEvent.Handler, GraphicsOperationEvent.Handler {
	
	private boolean active;
	
	private ExternalizableLabeled externalizableLabel;

	public ExternalizableLabeledController(GraphicsService graphicsService,	GraphicsObject object) {
		super(graphicsService, object);
		this.externalizableLabel = (ExternalizableLabeled) (getObject().getRole(ExternalizableLabeled.TYPE));
		getService().getObjectContainer().addGraphicsObjectContainerHandler(this);
		getService().getObjectContainer().addGraphicsOperationEventHandler(this);
		setLabelExternal(externalizableLabel.isLabelExternal());
		setLabelProperties();
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (event.getObject() == getObject()) {
			switch (event.getActionType()) {
			case ADD:
			case UPDATE:
				setLabelExternal(externalizableLabel.isLabelExternal());
				break;
			case REMOVE:
			case CLEAR:
				if (getService().getObjectContainer().getObjects().contains(externalizableLabel)) {
					getService().getObjectContainer().remove(externalizableLabel.getExternalLabel());
				}
				break;
			default: // handled by meta controller
				break;
			}
		}
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onOperation(GraphicsOperationEvent event) {
		if (event.getOperation().getObject() == getObject()) {
			if (event.getOperation() instanceof ToggleExternalizableLabelOperation) {
				setLabelExternal(externalizableLabel.isLabelExternal());
			} else if (event.getOperation() instanceof LabelOperation) {
				setLabelProperties();
			} else if (event.getOperation() instanceof AddOperation) {
				setLabelExternal(externalizableLabel.isLabelExternal());
			}
		}
		if (event.getOperation().getObject() == externalizableLabel.getExternalLabel()) {
			if (event.getOperation() instanceof LabelOperation) {
				setLabelProperties();
			}
		}
	}
	
	private void setLabelProperties() {
		Labeled labeled = getObject().getRole(Labeled.TYPE);
		externalizableLabel.setFontColor(labeled.getFontColor());
		externalizableLabel.setFontFamily(labeled.getFontFamily());
		externalizableLabel.setFontSize(labeled.getFontSize());
		externalizableLabel.setLabel(labeled.getLabel());
		boolean visible = !labeled.getLabel().isEmpty();
		((MetaController) getService().getMetaController()).
			setControllersOfObjectVisible(externalizableLabel.getExternalLabel(), visible);
	}
	
	public void setLabelExternal(boolean labelExternal) {
		getObject().getRole(Labeled.TYPE).setLabelVisible(!labelExternal);
		if (labelExternal) {
			if (!getService().getObjectContainer().getObjects().contains(externalizableLabel.getExternalLabel())) {
				getService().getObjectContainer().add(externalizableLabel.getExternalLabel());
			}
		} else {
			if (getService().getObjectContainer().getObjects().contains(externalizableLabel.getExternalLabel())) {
				getService().getObjectContainer().remove((GraphicsObject) externalizableLabel.getExternalLabel());
			}
		}
	}
}
