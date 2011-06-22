/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.featureinfo.client.widget.factory;

import java.util.Map.Entry;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.layer.feature.Attribute;

import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;


/**
 * <p>
 * The <code>RasterLayerAttriuteWindow</code> is a floating window that displays raster layer feature information
 * using a {@link ListGrid}.
 * </p>
 * 
 * @author Oliver May
 *
 */
public class RasterLayerAttributeWindow extends Window {

	/**
	 * @param rasterFeature
	 * @param layer 
	 */
	public RasterLayerAttributeWindow(Feature rasterFeature, Layer layer) {
		setWidth(300);
		setHeight(200);
		setCanDragReposition(true);
		setCanDragResize(true);
		setOverflow(Overflow.AUTO);
		setKeepInParentRect(true);
		setTitle("Object Detail - " + rasterFeature.getId());
		addItem(new RasterLayerAttributeCanvas(rasterFeature));
	}
	
	/**
	 * Helper canvas that contains the attribute grid.
	 * @author Oliver May
	 *
	 */
	private class RasterLayerAttributeCanvas extends VLayout {
		
		private Feature feature;
		
		public RasterLayerAttributeCanvas(final Feature feature) {
			this.feature = feature;

			ListGrid grid = new ListGrid();

			grid.setCanEdit(false);
			grid.setShowSelectedStyle(false);
			grid.setShowRollOver(false);
			grid.setShowHeader(false);

			grid.setShowRecordComponents(true);
			grid.setShowRecordComponentsByCell(true);
			grid.setShowAllRecords(true);
			grid.setLeaveScrollbarGap(false);

			ListGridField keyField = new ListGridField("keyField");
			ListGridField valueField = new ListGridField("valueField");

			grid.setFields(keyField, valueField);

			final RecordList recordList = new RecordList();
			
			for (Entry<String, Attribute> entry : feature.getAttributes().entrySet()) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("keyField", entry.getKey());
				record.setAttribute("valueField", toString(entry));
				recordList.add(record);
			}
			
			grid.setData(recordList);
			
			addMember(grid);
		}
		
		private String toString(Entry entry) {
			return entry.getValue() == null ? "null" : entry.getValue().toString();
			
		}
	}

}
