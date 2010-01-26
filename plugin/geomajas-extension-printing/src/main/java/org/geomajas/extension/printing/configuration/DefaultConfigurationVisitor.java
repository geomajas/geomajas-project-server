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
package org.geomajas.extension.printing.configuration;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.extension.printing.component.LabelComponent;
import org.geomajas.extension.printing.component.LegendComponent;
import org.geomajas.extension.printing.component.MapComponent;
import org.geomajas.extension.printing.component.PageComponent;
import org.geomajas.extension.printing.component.TopDownVisitor;

import java.text.DateFormat;
import java.util.Date;

/**
 * Visitor that configures the page of a default print template. Configures map
 * properties, title, optional arrow and so on...
 *
 * @author Jan De Moerloose
 */
public class DefaultConfigurationVisitor extends TopDownVisitor {

	private String mapId;

	private Coordinate mapLocation;

	private float mapPpUnit;

	private float mapRasterResolution;

	private String title;

	private boolean withDate;

	private boolean withArrow;

	private static DateFormat LONG_DATE = DateFormat.getDateInstance(DateFormat.LONG);

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
		map.setLocation(mapLocation);
		map.setPpUnit(mapPpUnit);
		map.setRasterResolution(mapRasterResolution);
	}

	@Override
	public void visit(LegendComponent legend) {
		legend.setMapId(mapId);
	}

	@Override
	public void visit(LabelComponent label) {
		if (label.getTag() != null && label.getTag().equals(PrintTemplate.TITLE)) {
			String text = title;
			if (isWithDate()) {
				text += " (" + LONG_DATE.format(new Date()) + ")";
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
