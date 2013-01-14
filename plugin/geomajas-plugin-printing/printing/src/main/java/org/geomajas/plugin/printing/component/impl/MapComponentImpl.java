/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.MapComponentInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Rectangle;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Map component for inclusion in printed documents.
 *
 * @author Jan De Moerloose
 * 
 * @param <T> DTO object class
 */
@Component()
@Scope(value = "prototype")
public class MapComponentImpl<T extends MapComponentInfo> extends AbstractPrintComponent<T> implements MapComponent<T> {

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

	protected MapComponentImpl() {
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

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.MapComponent#getLocation()
	 */
	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.MapComponent#getMapId()
	 */
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

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.MapComponent#getPpUnit()
	 */
	public float getPpUnit() {
		return ppUnit;
	}

	public void setPpUnit(float ppUnit) {
		this.ppUnit = ppUnit;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.MapComponent#getRasterResolution()
	 */
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
		List<PrintComponent<?>> toDelete = new LinkedList<PrintComponent<?>>();

		for (PrintComponent<?> pr : children) {
			if (pr instanceof VectorLayerComponentImpl) {
				VectorLayerComponentImpl comp = (VectorLayerComponentImpl) pr;
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
		for (PrintComponent<?> child : getChildren()) {
			if (child instanceof ViewPortComponentImpl) {
				renderViewPort((ViewPortComponentImpl) child, context);
			}
		}
	}

	private void renderViewPort(ViewPortComponentImpl viewPort, PdfContext context) {
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


	public void clearLayers() {
		List<PrintComponent<?>> layers = new ArrayList<PrintComponent<?>>();
		for (PrintComponent<?> child : children) {
			if (child instanceof VectorLayerComponentImpl || child instanceof RasterLayerComponentImpl) {
				layers.add(child);
			}
		}
		for (PrintComponent<?> layer : layers) {
			removeComponent(layer);
		}
	}
	
	public void fromDto(T mapInfo) {
		super.fromDto(mapInfo);
		setApplicationId(mapInfo.getApplicationId());
		setMapId(mapInfo.getMapId());
		setLocation(createCoordinate(mapInfo.getLocation()));
		setPpUnit(mapInfo.getPpUnit());
		setRasterResolution(mapInfo.getRasterResolution());
	}

	private Coordinate createCoordinate(org.geomajas.geometry.Coordinate coordinate) {
		return new Coordinate(coordinate.getX(), coordinate.getY());
	}

}
