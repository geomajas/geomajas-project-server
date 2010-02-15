/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.extension.printing.component;

import com.lowagie.text.Rectangle;
import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.extension.printing.PdfContext;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ???
 *
 * @author check subversion
 */
@XmlRootElement
public class MapComponent extends BaseComponent {

	/**
	 * Map id
	 */
	private String applicationId;

	/**
	 * Map id
	 */
	private String mapId;

	/**
	 * The lower left corner in map units
	 */
	private Coordinate location;

	/**
	 * resolution to be used for raster layers (unit = DPI, default = 72, which
	 * corresponds to 1 pixel per unit of user space in PDF)
	 */
	private double rasterResolution = 72;

	/**
	 * The number of points (user space units) per map unit
	 */
	private float ppUnit = 1.0f;

	public MapComponent() {
		getConstraint().setAlignmentX(LayoutConstraint.JUSTIFIED);
		getConstraint().setAlignmentY(LayoutConstraint.JUSTIFIED);
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
		super.calculateSize(context);
	}

	@Override
	public void layout(PdfContext context) {
		super.layout(context);
	}

	@Override
	public void render(PdfContext context) {
		context.fillRectangle(getSize());
		super.render(context);
		renderViewPorts(context);
		context.strokeRectangle(getSize());
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	public String getApplicationId() {
		return applicationId;
	}
	
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public float getPpUnit() {
		return ppUnit;
	}

	public void setPpUnit(float ppUnit) {
		this.ppUnit = ppUnit;
	}

	public double getRasterResolution() {
		return rasterResolution;
	}

	public void setRasterResolution(double rasterResolution) {
		this.rasterResolution = rasterResolution;
	}

	public void setFilter(Map<String, String> filters) {
		if (null == filters) {
			return;
		}
		List<PrintComponent> toDelete = new LinkedList<PrintComponent>();

		for (PrintComponent pr : children) {
			if (pr instanceof VectorLayerComponent) {
				VectorLayerComponent comp = (VectorLayerComponent) pr;
				String filter = filters.get(comp.getLayerId());
				if (filter == null) {
					toDelete.add(pr);
				} else if (!"".equals(filter)) { // "" == no filter needed
					if (comp.getFilter() == null || "".equals(comp.getFilter())) {
						comp.setFilter(filter);
					} else {
						comp.setFilter(comp.getFilter() + " AND " + filter);
					}
				}
			}
		}
		if (toDelete.size() > 0) {
			children.removeAll(toDelete);
		}
	}

	// ------------------------------------------------------------------------

	private void renderViewPorts(PdfContext context) {
		for (PrintComponent child : getChildren()) {
			if (child instanceof ViewPortComponent) {
				renderViewPort((ViewPortComponent) child, context);
			}
		}
	}

	private void renderViewPort(ViewPortComponent viewPort, PdfContext context) {
		Coordinate portOrigin = viewPort.getLocation();
		float x = (float) (portOrigin.x - location.x) * getPpUnit();
		float y = (float) (portOrigin.y - location.y) * getPpUnit();
		Rectangle shadowRect = new Rectangle(x, y, x + viewPort.getBounds().getWidth()
				/ viewPort.getZoomScale(), y + viewPort.getBounds().getHeight() / viewPort.getZoomScale());
		context.fillRectangle(shadowRect, context.makeTransparent(Color.lightGray, 0.5f));
		context.strokeRectangle(shadowRect, Color.white, 1);
		Rectangle rect = context.toRelative(viewPort.getBounds());
		// connection lines
		float deltaLeft = shadowRect.getLeft() - rect.getLeft();
		float deltaRight = shadowRect.getRight() - rect.getRight();

		float deltaBottom = shadowRect.getBottom() - rect.getBottom();
		float deltaTop = shadowRect.getTop() - rect.getTop();

		if ((deltaLeft <= 0 && deltaBottom >= 0) || (deltaLeft >= 0 && deltaBottom <= 0)) {
			context.drawLine(rect.getLeft(), rect.getBottom(), shadowRect.getLeft(), shadowRect.getBottom(),
					Color.white, 1);
		}
		if ((deltaLeft >= 0 && deltaTop >= 0) || (deltaLeft <= 0 && deltaTop <= 0)) {
			context.drawLine(rect.getLeft(), rect.getTop(), shadowRect.getLeft(), shadowRect.getTop(),
					Color.white, 1);
		}
		if ((deltaRight <= 0 && deltaBottom <= 0) || (deltaRight >= 0 && deltaBottom >= 0)) {
			context.drawLine(rect.getRight(), rect.getBottom(), shadowRect.getRight(),
					shadowRect.getBottom(), Color.white, 1);
		}
		if ((deltaRight >= 0 && deltaTop <= 0) || (deltaRight <= 0 && deltaTop >= 0)) {
			context.drawLine(rect.getRight(), rect.getTop(), shadowRect.getRight(), shadowRect.getTop(),
					Color.white, 1);
		}
	}

	/**
	 * Resets cyclic references like child -> parent relationship.
	 *
	 * @param u
	 * @param parent
	 */
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		setParent((PrintComponent) parent);
	}

	public void clearLayers() {
		List<PrintComponent> layers = new ArrayList<PrintComponent>();
		for (PrintComponent child : children) {
			if (child instanceof VectorLayerComponent || child instanceof RasterLayerComponent) {
				layers.add(child);
			}
		}
		for (PrintComponent layer : layers) {
			removeComponent(layer);
		}
	}

}
