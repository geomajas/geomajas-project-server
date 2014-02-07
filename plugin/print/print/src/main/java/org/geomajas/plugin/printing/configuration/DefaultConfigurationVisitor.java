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
package org.geomajas.plugin.printing.configuration;

import java.text.DateFormat;
import java.util.Date;

import org.geomajas.plugin.printing.component.LabelComponent;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.TopDownVisitor;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Visitor that configures the page of a default print template. Configures map
 * properties, title, optional arrow and so on...
 *
 * @author Jan De Moerloose
 */
public class DefaultConfigurationVisitor extends TopDownVisitor {

	private String applicationId;

	private String mapId;

	private Coordinate mapLocation;

	private float mapPpUnit;

	private float mapRasterResolution;

	private String title;

	private boolean withDate;

	private boolean withArrow;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isWithDate() {
		return withDate;
	}

	public void setWithDate(boolean withDate) {
		this.withDate = withDate;
	}

	public boolean isWithArrow() {
		return withArrow;
	}

	public void setWithArrow(boolean withArrow) {
		this.withArrow = withArrow;
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

	public Coordinate getMapLocation() {
		return mapLocation;
	}

	public void setMapLocation(Coordinate mapLocation) {
		this.mapLocation = mapLocation;
	}

	public float getMapPpUnit() {
		return mapPpUnit;
	}

	public void setMapPpUnit(float mapPpUnit) {
		this.mapPpUnit = mapPpUnit;
	}

	public float getMapRasterResolution() {
		return mapRasterResolution;
	}

	public void setMapRasterResolution(float mapRasterResolution) {
		this.mapRasterResolution = mapRasterResolution;
	}

	@Override
	public void visit(MapComponent map) {
		if (!withArrow) {
			map.removeComponent(map.getChild(PrintTemplate.ARROW));
		}
		map.setMapId(mapId);
		map.setApplicationId(applicationId);
		map.setLocation(mapLocation);
		map.setPpUnit(mapPpUnit);
		map.setRasterResolution(mapRasterResolution);
	}

	@Override
	public void visit(LegendComponent legend) {
		legend.setMapId(mapId);
		legend.setApplicationId(applicationId);
	}

	@Override
	public void visit(LabelComponent label) {
		if (label.getTag() != null && label.getTag().equals(PrintTemplate.TITLE)) {
			String text = title;
			if (isWithDate()) {
				DateFormat longDate = DateFormat.getDateInstance(DateFormat.LONG);
				text += " (" + longDate.format(new Date()) + ")";
			}
			label.setText(text);
		}
	}

	@Override
	public void visit(PageComponent page) {
		if (title == null) {
			page.removeComponent(page.getChild(PrintTemplate.TITLE));
		}
		super.visit(page);
	}

}
