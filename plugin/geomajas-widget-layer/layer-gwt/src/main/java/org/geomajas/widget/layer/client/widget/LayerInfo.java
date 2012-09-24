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

package org.geomajas.widget.layer.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.widget.layer.client.LayerMessages;
import org.geomajas.widget.layer.client.util.LayerIconUtil;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A simple layerinfo window.
 *
 * @author Kristof Heirwegh
 * @author Wout Swartenbroekx
 */
public class LayerInfo extends Window {

	private static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	private static final String NUMERIC_TYPES = "short integer long float double currency";
	private static final int WINDOW_HEIGHT = 400;
	private static final int WINDOW_WIDTH = 450;

	private static final String ATTRI = "att";
	private static final String LABEL = "lab";
	private static final String TYPE = "typ";
	private static final String EDITA = "edi";
	private static final String IDENT = "ide";
	private static final String HIDDE = "hid";
	private static final String NUMER = "num";

	/**
	 * Constructor.
	 *
	 * @param layer layer
	 */
	public LayerInfo(Layer<?> layer) {
		super();
		setTitle(MESSAGES.layerInfoWindowLegendTitle() + " - " + layer.getLabel());
		setAutoCenter(true);
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		setKeepInParentRect(true);
		setCanDragResize(true); // resize

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setPadding(5);
		layout.setMembersMargin(5);
		layout.setOverflow(Overflow.AUTO);

		if (layer instanceof VectorLayer) {
			layout.addMember(createVectorLegend((VectorLayer) layer));
			layout.addMember(createFeatureInfo((VectorLayer) layer));
		} else if (layer instanceof RasterLayer) { // handle unchecked cast below
			Canvas c = createLegendInfo((RasterLayer) layer);
			if (c != null) {
				layout.addMember(c);
				layout.setPadding(1);
			}
		}

		// ----------------------------------------------------------

		addItem(layout);
	}

	private Canvas createVectorLegend(VectorLayer layer) {
		VLayout legend = new VLayout();
		legend.setWidth100();
		legend.setIsGroup(true);
		legend.setPadding(5);
		legend.setGroupTitle(MESSAGES.layerInfoLayerInfoLegend());
		legend.addMember(new VectorLegendListGrid(layer));
		legend.setHeight100();
		return legend;
	}

	private Canvas createFeatureInfo(VectorLayer layer) {
		List<ListGridRecord> records = new ArrayList<ListGridRecord>();
		for (AbstractAttributeInfo attInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			ListGridRecord r = new ListGridRecord();
			records.add(r);
			String type = getType(attInfo);
			r.setAttribute(ATTRI, attInfo.getName());
			r.setAttribute(LABEL, ((AbstractReadOnlyAttributeInfo) attInfo).getLabel());
			r.setAttribute(TYPE, type);
			r.setAttribute(EDITA,
					(((AbstractReadOnlyAttributeInfo) attInfo).isEditable() ? MESSAGES.layerInfoLayerInfoAttYes()
							: MESSAGES.layerInfoLayerInfoAttNo()));
			r.setAttribute(IDENT, (((AbstractReadOnlyAttributeInfo) attInfo).isIdentifying()
					? MESSAGES.layerInfoLayerInfoAttYes() : MESSAGES.layerInfoLayerInfoAttNo()));
			r.setAttribute(HIDDE,
					(((AbstractReadOnlyAttributeInfo) attInfo).isHidden() ? MESSAGES.layerInfoLayerInfoAttYes()
							: MESSAGES.layerInfoLayerInfoAttNo()));
			r.setAttribute(NUMER,
					(isNumeric(type) ? MESSAGES.layerInfoLayerInfoAttYes() : MESSAGES.layerInfoLayerInfoAttNo()));
		}

		// ----------------------------------------------------------

		VLayout c = new VLayout();
		c.setIsGroup(true);
		c.setGroupTitle(MESSAGES.layerInfoLayerInfoFldInfo());
		c.setPadding(5);
		ListGrid info = new ListGrid();
		info.setShowAllRecords(true);
		info.setCanResizeFields(true);
		info.setWidth100();
		info.setHeight(400);

		ListGridField fld1 = new ListGridField(ATTRI, MESSAGES.layerInfoLayerInfoAttAttribute());
		ListGridField fld2 = new ListGridField(LABEL, MESSAGES.layerInfoLayerInfoAttLabel());
		ListGridField fld3 = new ListGridField(TYPE, MESSAGES.layerInfoLayerInfoAttType());
		ListGridField fld4 = new ListGridField(EDITA, MESSAGES.layerInfoLayerInfoAttEditable());
		ListGridField fld5 = new ListGridField(IDENT, MESSAGES.layerInfoLayerInfoAttIdentifying());
		ListGridField fld6 = new ListGridField(HIDDE, MESSAGES.layerInfoLayerInfoAttHidden());
		ListGridField fld7 = new ListGridField(NUMER, MESSAGES.layerInfoLayerInfoAttNumeric());
		fld1.setWidth(70);
		fld2.setWidth(90);
		info.setFields(fld1, fld2, fld3, fld4, fld5, fld6, fld7);

		info.setData(records.toArray(new ListGridRecord[records.size()]));
		c.addMember(info);

		return c;
	}

	private boolean isNumeric(String type) {
		return (NUMERIC_TYPES.indexOf(type) > -1);
	}

	private String getType(AbstractAttributeInfo attInfo) {
		if (attInfo instanceof PrimitiveAttributeInfo) {
			return ((PrimitiveAttributeInfo) attInfo).getType().toString();

		} else if (attInfo instanceof AssociationAttributeInfo) {
			return ((AssociationAttributeInfo) attInfo).getType().toString();

		} else {
			return attInfo.toString();
		}
	}

	private Canvas createLegendInfo(RasterLayer layer) {
		final HTMLPane legendPane = new HTMLPane();
		String url = LayerIconUtil.getLegendUrl(layer);
		if (url != null) {
			legendPane.setContents("<img src=\"" + url + "\" border=\"0\">");
			legendPane.setOverflow(Overflow.AUTO);
//			legendPane.addDomHandler(new LoadHandler() {
//				public void onLoad(LoadEvent event) {
//					legendPane.markForRedraw();
//				}
//			}, LoadEvent.getType());
		} else {
			legendPane.setContents("<br /><div style=\"text-align: center\">"
					+ MESSAGES.layerInfoLayerInfoLegendNoLegend() +	"</div>");
		}
		return legendPane;
	}

	// -------------------------------------------------

	/**
	 * ListGrid to show Legend elements.
	 */
	private class VectorLegendListGrid extends ListGrid {

		private static final String KEY_FLD = "keyField";
		private static final String VALUE_FLD = "valueField";

		private static final String LEGEND_ICONS_PATH = "legendgraphic";
		private static final String LEGEND_ICONS_TYPE = ".png";
		private static final int ICONSIZE = 18;

		public VectorLegendListGrid(VectorLayer layer) {
			super();
			setWidth100();
			setHeight(10);
			setCanEdit(false);
			setShowSelectedStyle(false);
			setShowRollOver(false);
			setShowHeader(false);
			setShowAllRecords(true);
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);
			setLeaveScrollbarGap(false);
			setWrapCells(true);
			setFixedRecordHeights(false);
			setImageSize(ICONSIZE);

			String name = layer.getLayerInfo().getNamedStyleInfo().getName();
			UrlBuilder url = new UrlBuilder(Geomajas.getDispatcherUrl());
			url.addPath(LEGEND_ICONS_PATH);
			url.addPath(layer.getServerLayerId());
			url.addPath(name);

			// -- FIELDS
			ListGridField keyField = new ListGridField(KEY_FLD, "Veldnaam", 45);
			keyField.setAlign(Alignment.CENTER);
			keyField.setType(ListGridFieldType.IMAGE);
			keyField.setImageURLPrefix(url.toString() + "/");
			keyField.setImageURLSuffix(LEGEND_ICONS_TYPE);

			ListGridField valueField = new ListGridField(VALUE_FLD, "Waarde");
			valueField.setWidth("*");
			setFields(keyField, valueField);

			// -- VALUES
			final RecordList recordList = new RecordList();
			ClientVectorLayerInfo layerInfo = layer.getLayerInfo();
			UserStyleInfo userStyle = layerInfo.getNamedStyleInfo().getUserStyle();
			FeatureTypeStyleInfo info = userStyle.getFeatureTypeStyleList().get(0);
			for (int i = 0; i < info.getRuleList().size(); i++) {
				RuleInfo rule = info.getRuleList().get(i);
				String title = (rule.getTitle() != null ? rule.getTitle() : rule.getName());
				if (title == null) {
					title = layerInfo.getNamedStyleInfo().getName();
				}
				recordList.add(createRecord(title, i));
			}

			setData(recordList);
		}

		private ListGridRecord createRecord(String title, int index) {
			ListGridRecord r = new ListGridRecord();
			r.setAttribute(KEY_FLD, index);
			r.setAttribute(VALUE_FLD, title);
			return r;
		}
	}
}
