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
package org.geomajas.widget.advancedviews.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.client.util.LayerIconHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A simple layerinfo window.
 * 
 * @author Kristof Heirwegh
 * 
 */
public class LayerInfo extends Window {

	private AdvancedViewsMessages messages = GWT.create(AdvancedViewsMessages.class);

	private static final String NUMERIC_TYPES = "short integer long float double currency";
	private static final int MAX_LEGEND_WIDTH = 440;
	private static final int MAX_LEGEND_HEIGHT = 400;

	private static final String ATTRI = "att";
	private static final String LABEL = "lab";
	private static final String TYPE = "typ";
	private static final String EDITA = "edi";
	private static final String IDENT = "ide";
	private static final String HIDDE = "hid";
	private static final String NUMER = "num";
	private static final String VALUE = "val";

	public LayerInfo(Layer<?> layer) {
		super();
		setTitle(messages.layerInfoWindowTitle() + " - " + layer.getLabel());
		setAutoCenter(true);
		setAutoSize(true);
		setKeepInParentRect(true);

		VLayout layout = new VLayout();
		layout.setWidth(450);
		layout.setAutoHeight();
		layout.setPadding(5);
		layout.setMembersMargin(5);

		Img icon = LayerIconHelper.getLargeLayerIcon(layer);
		icon.setImageType(ImageStyle.NORMAL);
		layout.addMember(icon);
		layout.addMember(createLayerInfo(layer));

		if (layer instanceof VectorLayer) {
			layout.addMember(createFeatureInfo((VectorLayer) layer));
		} else {
			Canvas c = createLegendInfo((RasterLayer) layer);
			if (c != null) {
				layout.addMember(c);
			}
		}

		// ----------------------------------------------------------

		addItem(layout);
	}

	private Canvas createLayerInfo(Layer<?> layer) {
		String layerType, layerMax, layerMin;
		if (layer instanceof VectorLayer) {
			VectorLayer vl = (VectorLayer) layer;
			layerType = messages.layerInfoLayerInfoFldLayerTypeVector();
			layerType += " (" + vl.getLayerInfo().getLayerType().name() + ")";
			layerMax = buildScale(vl.getLayerInfo().getMaximumScale());
			layerMin = buildScale(vl.getLayerInfo().getMinimumScale());

		} else {
			RasterLayer rl = (RasterLayer) layer;
			layerType = messages.layerInfoLayerInfoFldLayerTypeRaster();
			layerMax = buildScale(rl.getLayerInfo().getMaximumScale());
			layerMin = buildScale(rl.getLayerInfo().getMinimumScale());
		}

		// ----------------------------------------------------------

		ListGridRecord[] records = new ListGridRecord[5];
		for (int i = 0; i < 5; i++) {
			records[i] = new ListGridRecord();
		}
		records[0].setAttribute(LABEL, messages.layerInfoLayerInfoFldLayer());
		records[0].setAttribute(VALUE, layer.getLabel());
		records[1].setAttribute(LABEL, messages.layerInfoLayerInfoFldLayerType());
		records[1].setAttribute(VALUE, layerType);
		records[2].setAttribute(LABEL, messages.layerInfoLayerInfoFldMaxViewScale());
		records[2].setAttribute(VALUE, layerMax);
		records[3].setAttribute(LABEL, messages.layerInfoLayerInfoFldMinViewScale());
		records[3].setAttribute(VALUE, layerMin);
		records[4].setAttribute(LABEL, messages.layerInfoLayerInfoFldVisible());
		records[4].setAttribute(VALUE, (layer.isShowing() ? messages.layerInfoLayerInfoFldVisibleStatusVisible()
				: messages.layerInfoLayerInfoFldVisibleStatusHidden()));

		// ----------------------------------------------------------

		ListGrid info = new ListGrid();
		info.setShowAllRecords(true);
		info.setCanResizeFields(true);
		info.setWidth100();
		info.setHeight(135);

		ListGridField labelField = new ListGridField(LABEL, messages.layerInfoLayerInfo());
		ListGridField valueField = new ListGridField(VALUE, messages.layerInfoLayerInfoValue());
		info.setFields(labelField, valueField);
		info.setData(records);

		return info;
	}

	private Canvas createFeatureInfo(VectorLayer layer) {
		List<ListGridRecord> records = new ArrayList<ListGridRecord>();
		for (AttributeInfo attInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			ListGridRecord r = new ListGridRecord();
			records.add(r);
			String type = getType(attInfo);
			r.setAttribute(ATTRI, attInfo.getName());
			r.setAttribute(LABEL, attInfo.getLabel());
			r.setAttribute(TYPE, type);
			r.setAttribute(EDITA,
					(attInfo.isEditable() ? messages.layerInfoLayerInfoAttYes() : messages.layerInfoLayerInfoAttNo()));
			r.setAttribute(IDENT, (attInfo.isIdentifying() ? messages.layerInfoLayerInfoAttYes() : 
				messages.layerInfoLayerInfoAttNo()));
			r.setAttribute(HIDDE,
					(attInfo.isHidden() ? messages.layerInfoLayerInfoAttYes() : messages.layerInfoLayerInfoAttNo()));
			r.setAttribute(NUMER,
					(isNumeric(type) ? messages.layerInfoLayerInfoAttYes() : messages.layerInfoLayerInfoAttNo()));
		}

		// ----------------------------------------------------------

		VLayout c = new VLayout();
		ListGrid info = new ListGrid();
		info.setShowAllRecords(true);
		info.setCanResizeFields(true);
		info.setWidth100();
		info.setHeight(400);

		ListGridField fld1 = new ListGridField(ATTRI, messages.layerInfoLayerInfoAttAttribute());
		ListGridField fld2 = new ListGridField(LABEL, messages.layerInfoLayerInfoAttLabel());
		ListGridField fld3 = new ListGridField(TYPE, messages.layerInfoLayerInfoAttType());
		ListGridField fld4 = new ListGridField(EDITA, messages.layerInfoLayerInfoAttEditable());
		ListGridField fld5 = new ListGridField(IDENT, messages.layerInfoLayerInfoAttIdentifying());
		ListGridField fld6 = new ListGridField(HIDDE, messages.layerInfoLayerInfoAttHidden());
		ListGridField fld7 = new ListGridField(NUMER, messages.layerInfoLayerInfoAttNumeric());
		fld1.setWidth(70);
		fld2.setWidth(90);
		info.setFields(fld1, fld2, fld3, fld4, fld5, fld6, fld7);

		info.setData(records.toArray(new ListGridRecord[records.size()]));
		c.addChild(info);

		return c;
	}

	private boolean isNumeric(String type) {
		return (NUMERIC_TYPES.indexOf(type) > -1);
	}

	private String buildScale(ScaleInfo si) {
		return ((int) si.getNumerator()) + " : " + ((int) si.getDenominator()) + " ("
				+ NumberFormat.getFormat("#,##0.#########").format(si.getPixelPerUnit()) + ")";
	}

	private String getType(AttributeInfo attInfo) {
		if (attInfo instanceof PrimitiveAttributeInfo) {
			return ((PrimitiveAttributeInfo) attInfo).getType().toString();

		} else if (attInfo instanceof AssociationAttributeInfo) {
			return ((AssociationAttributeInfo) attInfo).getType().toString();

		} else {
			return attInfo.toString();
		}
	}

	private Canvas createLegendInfo(RasterLayer layer) {
		Img legend = LayerIconHelper.getLegendImage(layer);
		if (legend != null) {
			SectionStack sectionStack = new SectionStack();
			sectionStack.setWidth(MAX_LEGEND_WIDTH);
			sectionStack.setHeight(MAX_LEGEND_HEIGHT);
			SectionStackSection section = new SectionStackSection(messages.layerInfoLayerInfoLegend());
			section.setCanCollapse(false);
			section.setExpanded(true);

			legend.setOverflow(Overflow.AUTO);
			legend.setImageType(ImageStyle.NORMAL);

			section.setItems(legend);
			sectionStack.setSections(section);
			return sectionStack;
		} else {
			return null;
		}
	}
}
