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
package org.geomajas.graphics.client.object.anchor;

import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableAwareRole;

/**
 * Standard role for the External Label of an It is extended from {@link AnchoredToResizable} class, to distinguish
 * between external labels and anchored graphicsobjects. E.g. an external label may not be deleted (or delete means
 * bringing it back to the internal state).
 * 
 * @author Jan Venstermans
 * 
 */
public class ExternalLabelOfResizable extends AnchoredToResizable {

	public ExternalLabelOfResizable(Resizable masterObject) {
		super(masterObject);
		setAnchorLineDashStyle(AnchorLineDashStyle.DASH_EQUAL_5);
		setRelativeAnchoringPositionAtMasterObject(RelativeAnchorPosition.CENTER);
	}

	@Override
	public ResizableAwareRole<AnchoredTo> cloneRole(Resizable resizable) {
		ExternalLabelOfResizable clone = new ExternalLabelOfResizable((Resizable) getMasterObject());
		addProperties(clone);
		return clone;
	}

	@Override
	public AnchoredTo cloneObject() {
		ExternalLabelOfResizable anchoredTo = new ExternalLabelOfResizable((Resizable) getMasterObject());
		anchoredTo.setResizable(slaveObject);
		anchoredTo.setAnchorPath(getAnchorLineClone());
		return anchoredTo;
	}
}
