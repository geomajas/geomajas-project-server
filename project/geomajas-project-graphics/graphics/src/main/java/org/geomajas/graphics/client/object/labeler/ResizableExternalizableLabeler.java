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
package org.geomajas.graphics.client.object.labeler;

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.ExternalLabel;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.anchor.AnchoredTo;
import org.geomajas.graphics.client.object.anchor.ExternalLabelOfResizable;
import org.geomajas.graphics.client.object.role.ExternalizableLabeled;
import org.geomajas.graphics.client.object.role.Labeled;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Implementation of {@link ExternalizableLabeled} role for {@link Resizable} objects.
 * Two possible visualizations:
 *  Standard: INTERNAL: text in the center of {@link Resizable} objects, as a {@link AnchoredText} 
 *  that cannot be dragged, resized, ...
 *  or: EXTERNAL: text is in a special box, like {@link GText} with {@link AnchoredToResizable} role;
 *  it will however deffer from standard {@link GText}: some functions are inactive. 
 *  Remaining functions: edit text, edit style. 
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * 
 */
public class ResizableExternalizableLabeler implements ExternalizableLabeled {

	private boolean showLabelExternal;

	private ExternalLabel externalTextObject;
	
	private GraphicsObject masterObject;
	
	public ResizableExternalizableLabeler(GraphicsObject masterObject, boolean showLabelExternal) {
		this.masterObject = masterObject;
		this.showLabelExternal = showLabelExternal;
		externalTextObject = new ExternalLabel(this);
		if (externalTextObject.hasRole(AnchoredTo.TYPE)) {
			externalTextObject.removeRole(AnchoredTo.TYPE);
		}
		Resizable resizable = masterObject.getRole(Resizable.TYPE);
		externalTextObject.addRole(new ExternalLabelOfResizable(resizable));
		if (externalTextObject.getPosition().getX() == 0 && externalTextObject.getPosition().getY() == 0) {
			externalTextObject.setPosition(new Coordinate(resizable.getPosition().getX(), resizable.getPosition()
					.getY()));
		}
	}

	@Override
	public void setPositionExternalLabel(Coordinate coordinate) {
		externalTextObject.setPosition(coordinate);
	}

	@Override
	public Coordinate getPositionExternalLabel() {
		return externalTextObject.getPosition();
	}

	@Override
	public void setExternalLabel(ExternalLabel exLabel) {
		externalTextObject = exLabel;
	}

	@Override
	public ExternalLabel getExternalLabel() {
		return externalTextObject;
	}
	
	@Override
	public GraphicsObject getMasterObject() {
		return masterObject;
	}

	@Override
	public void setLabelExternal(boolean labelExternal) {
		this.showLabelExternal = labelExternal;
		// here: toggle the visibility of the standard Text
		if (labelExternal) {
			getInternalLabel().setVisible(false);
		} else {
			getInternalLabel().setVisible(true);
		}
		// FYI: the visibilty of the external text is toggled in LabelOperation class
	}

	@Override
	public boolean isLabelExternal() {
		return showLabelExternal;
	}


	@Override
	public Labeled getLabeled() {
		return masterObject.getRole(Labeled.TYPE);
	}
	
	private VectorObject getInternalLabel() {
		return ((ResizableLabeler) masterObject.getRole(Labeled.TYPE)).asObject();
	}

}
