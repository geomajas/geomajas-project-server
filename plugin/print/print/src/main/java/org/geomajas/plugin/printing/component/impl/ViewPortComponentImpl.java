/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;

import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.ViewPortComponent;
import org.geomajas.plugin.printing.component.dto.ViewPortComponentInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * A view port is a part the main map which is shown at a different scale. A
 * view port is determined by the bounding box of the area it represents and the
 * scale factor by which the area is scaled w.r.t. the main map. Additionally,
 * view ports may show a different configuration of layers than the main map.
 * View ports may have arbitrary locations on the map.
 *
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class ViewPortComponentImpl extends MapComponentImpl<ViewPortComponentInfo> implements ViewPortComponent {

	/**
	 * Ratio of the view port scale and the map scale
	 */
	private float zoomScale;

	/**
	 * X coordinate of view port in user coordinates
	 */
	private float userX = -1;

	/**
	 * Y coordinate of view port in user coordinates
	 */
	private float userY = -1;

	public ViewPortComponentImpl() {
		this(100, 100, 1);
	}

	public ViewPortComponentImpl(float width, float height, float zoomScale) {
		getConstraint().setAlignmentX(LayoutConstraint.LEFT);
		getConstraint().setAlignmentY(LayoutConstraint.BOTTOM);
		getConstraint().setWidth(width);
		getConstraint().setHeight(height);
		this.zoomScale = zoomScale;
	}

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void calculateSize(PdfContext context) {
		setMapId(getMap().getMapId());
		setRasterResolution(getMap().getRasterResolution());

		// calculate origin (uses margins to position)
		Coordinate mapOrigin = getMap().getLocation();
		Coordinate portOrigin = getLocation();
		if (userX == -1) {
			getConstraint().setMarginX((float) (portOrigin.x - mapOrigin.x) * getMap().getPpUnit());
		} else {
			getConstraint().setMarginX(userX);
		}
		if (userY == -1) {
			getConstraint().setMarginY((float) (portOrigin.y - mapOrigin.y) * getMap().getPpUnit());
		} else {
			getConstraint().setMarginY(userY);
		}

		// calculate ppunit
		setPpUnit(getMap().getPpUnit() * zoomScale);
		super.calculateSize(context);
	}

	@Override
	public void render(PdfContext context) {
		super.render(context);
		context.strokeRectangle(getSize(), Color.white, 2);
	}

	protected MapComponent getMap() {
		return (MapComponent) getParent();
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.ViewPortComponent#getZoomScale()
	 */
	public float getZoomScale() {
		return zoomScale;
	}

	public void setZoomScale(float zoomScale) {
		this.zoomScale = zoomScale;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.ViewPortComponent#getUserX()
	 */
	public float getUserX() {
		return userX;
	}

	public void setUserX(float userX) {
		this.userX = userX;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.ViewPortComponent#getUserY()
	 */
	public float getUserY() {
		return userY;
	}

	public void setUserY(float userY) {
		this.userY = userY;
	}
	
	public void fromDto(ViewPortComponentInfo viewPortInfo) {
		super.fromDto(viewPortInfo);
		setUserX(viewPortInfo.getUserX());
		setUserY(viewPortInfo.getUserY());
		setZoomScale(viewPortInfo.getZoomScale());
	}


}
