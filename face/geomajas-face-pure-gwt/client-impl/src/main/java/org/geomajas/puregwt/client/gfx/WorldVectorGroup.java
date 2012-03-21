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

package org.geomajas.puregwt.client.gfx;

import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * Default implementation of the {@link WorldVectorContainer}. This implementation only supports fixed size circles,
 * rectangles and images.
 * 
 * @author Pieter De Graef
 */
public class WorldVectorGroup extends VectorGroup implements WorldVectorContainer {

	private boolean useFixedSize;

	/** {@inheritDoc} */
	public void setUseFixedSize(boolean useFixedSize) {
		this.useFixedSize = useFixedSize;
	}

	/** {@inheritDoc} */
	public boolean isUseFixedSize() {
		return useFixedSize;
	}

	@Override
	public VectorObject add(VectorObject vo) {
		VectorObject result = super.add(vo);
		if (useFixedSize) {
			compensateScaleToKeepSize(result);
		}
		return result;
	}

	@Override
	public void drawTransformed() {
		if (hasTranslation()) {
			getImpl().translateGroup(getElement(), getDeltaX(), getDeltaY(), isAttached());
		}
		// scale each child, gives more flexibility than coordinate transformations
		if (hasScale()) {
			for (int i = 0; i < getVectorObjectCount(); i++) {
				VectorObject vo = getVectorObject(i);
				vo.setScale(getScaleX(), getScaleY());
				if (useFixedSize) {
					compensateScaleToKeepSize(vo);
				}
			}
		}
	}

	private void compensateScaleToKeepSize(VectorObject vo) {
		if (vo instanceof Circle) {
			Circle circle = (Circle) vo;
			circle.setRadius((int) circle.getUserRadius());
		} else if (vo instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) vo;
			rectangle.setWidth((int) rectangle.getUserWidth());
			rectangle.setHeight((int) rectangle.getUserHeight());
			rectangle.setX(rectangle.getX() - (int) rectangle.getUserWidth() / 2);
			rectangle.setY(rectangle.getY() - (int) rectangle.getUserHeight() / 2);
		} else if (vo instanceof Image) {
			Image image = (Image) vo;
			image.setWidth((int) image.getUserWidth());
			image.setHeight((int) image.getUserHeight());
			image.setX(image.getX() - (int) image.getUserWidth() / 2);
			image.setY(image.getY() - (int) image.getUserHeight() / 2);
		}
	}
}