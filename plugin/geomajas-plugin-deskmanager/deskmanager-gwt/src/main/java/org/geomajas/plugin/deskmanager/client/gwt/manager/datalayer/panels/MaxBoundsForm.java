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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels;

import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FloatItem;

/**
 * @author Kristof Heirwegh
 */
public class MaxBoundsForm extends DynamicForm {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private FloatItem minX;

	private FloatItem minY;

	private FloatItem maxX;

	private FloatItem maxY;

	private DynamicLayerConfiguration layerConfig;


	public MaxBoundsForm() {
		setNumCols(4);
		setColWidths(125, 125, 75, 125);

		minX = new FloatItem();
		minX.setTitle(MESSAGES.maxBoundsMinX());
		minX.setWidth(125);
		minX.setRequired(true);

		minY = new FloatItem();
		minY.setTitle(MESSAGES.maxBoundsMinY());
		minY.setWidth(125);
		minY.setRequired(true);

		maxX = new FloatItem();
		maxX.setTitle(MESSAGES.maxBoundsMaxX());
		maxX.setWidth(125);
		maxX.setRequired(true);

		maxY = new FloatItem();
		maxY.setTitle(MESSAGES.maxBoundsMaxY());
		maxY.setWidth(125);
		maxY.setRequired(true);

		// -------------------------------------------------

		setFields(minX, maxX, minY, maxY);
	}

	public void setData(DynamicLayerConfiguration layerConfig) {
		clearValues();
		this.layerConfig = layerConfig;
		Bbox bounds = layerConfig.getServerLayerInfo().getMaxExtent();
		if (bounds != null) {
			minX.setValue(bounds.getX());
			minY.setValue(bounds.getY());
			maxX.setValue(bounds.getMaxX());
			maxY.setValue(bounds.getMaxY());
		}
	}

	public DynamicLayerConfiguration getData() {
		Bbox bounds = layerConfig.getServerLayerInfo().getMaxExtent();
		if (bounds == null) {
			bounds = new Bbox();
			layerConfig.getServerLayerInfo().setMaxExtent(bounds);
			layerConfig.getClientLayerInfo().setMaxExtent(bounds);
		}

		bounds.setX(minX.getValueAsFloat());
		bounds.setY(minY.getValueAsFloat());
		bounds.setMaxX(maxX.getValueAsFloat());
		bounds.setMaxY(maxY.getValueAsFloat());
		
		return layerConfig;
	}
}
