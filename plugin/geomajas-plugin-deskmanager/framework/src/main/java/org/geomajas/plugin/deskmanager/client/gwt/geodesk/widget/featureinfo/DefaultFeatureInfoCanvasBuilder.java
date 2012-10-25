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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.featureinfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetBuilder;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * Default FeatureinfoBuilder for all non-customized vectorlayers.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * 
 * @since 1.0.0
 */
@Api
public class DefaultFeatureInfoCanvasBuilder implements FeatureDetailWidgetBuilder {

	/** {@inheritDoc} */
	private static final long serialVersionUID = 1L;

	private static final String TITLESTYLE = "defaultFeatureInfoCanvasGridTitle";

	private final NumberFormat numberFormatter = NumberFormat.getDecimalFormat();

	private final NumberFormat currencyFormatter = NumberFormat.getCurrencyFormat();

	private final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");

	private final Map<String, ValueFormatter> valueFormatters = 
		new HashMap<String, DefaultFeatureInfoCanvasBuilder.ValueFormatter>();

	private final Map<String, TitleFormatter> titleFormatters = 
		new HashMap<String, DefaultFeatureInfoCanvasBuilder.TitleFormatter>();

	// ----------------------------------------------------------

	public Window createFeatureDetailWindow(Feature feature, boolean editingAllowed) {
		Window w = new DockableWindow();
		w.setWidth(450);
		w.setMaxHeight(1000);
		w.setAutoSize(true);
		w.setCanDragReposition(true);
		w.setCanDragResize(true);
		w.setKeepInParentRect(true);
		w.setOverflow(Overflow.HIDDEN);
		w.setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(feature.getLabel()));
		w.addItem(new GenericVectorLayerFeatureInfoCanvas(feature));
		return w;
	}

	/**
	 * Add a valueformatter to the feature info canvas.
	 * 
	 * @param attributeName
	 * @param formatter
	 */
	public void addValueFormatter(String attributeName, ValueFormatter formatter) {
		valueFormatters.put(attributeName, formatter);
	}

	/**
	 * Add a titleformatter to the feature info canvas.
	 * 
	 * @param attributeName
	 * @param formatter
	 */
	public void addTitleFormatter(String attributeName, TitleFormatter formatter) {
		titleFormatters.put(attributeName, formatter);
	}

	// ----------------------------------------------------------

	@SuppressWarnings("deprecation")
	protected Canvas createRecordComponent(Feature feature) {
		Collection<AbstractAttributeInfo> attributeInfos = feature.getLayer().getLayerInfo().getFeatureInfo()
				.getAttributesMap().values();

		VLayout recordCanvas = new VLayout();
		recordCanvas.setBackgroundColor("#FFFFFF");
		recordCanvas.setWidth100();
		recordCanvas.setAutoHeight();
		String label;
		String value;
		String attName;

		for (AbstractAttributeInfo ai : attributeInfos) {
			if (ai instanceof AbstractReadOnlyAttributeInfo && !((AbstractReadOnlyAttributeInfo) ai).isHidden()) {
				attName = ai.getName();
				if (titleFormatters.containsKey(attName)) {
					label = titleFormatters.get(attName).getTitle();
				} else {
					label = ((AbstractReadOnlyAttributeInfo) ai).getLabel() + ": ";
				}
				if (valueFormatters.containsKey(attName)) {
					value = valueFormatters.get(attName).format(feature.getAttributes().get(attName).getValue());
				} else {
					value = format(feature.getAttributes().get(attName));
				}
				recordCanvas.addMember(new RowLayout(new TitleLabel(label), new DetailLabel(value)));
			}
		}
		return recordCanvas;
	}

	/**
	 * Get the id of the feature currently displayed in the feature info canvas.
	 * 
	 * @param feature
	 * @return the feature id
	 */
	public String getFeatureId(Feature feature) {
		return feature.getId();
	}

	/**
	 * The class our builder will return, notice we are using FeatureDetailWidget as parent class.
	 */
	private class GenericVectorLayerFeatureInfoCanvas extends VLayout {

		private Feature feature;

		public GenericVectorLayerFeatureInfoCanvas(final Feature feature) {
			if (feature == null) {
				return;
			}
			if (feature.getLayer() == null) {
				Log.logWarn("ClientLayer of feature is not set!!");
				return;
			}
			this.feature = feature;

			setWidth100();
			setAutoHeight();

			addZoomAction();
			addHeader();
			addMember(new GenericDetailListGrid());
		}

		// -------------------------------------------------

		private void addZoomAction() {
			HLayout layouttoolStrip = new HLayout();
			layouttoolStrip.setWidth100();
			layouttoolStrip.setHeight(30);

			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			toolStrip.setHeight100();
			toolStrip.setPadding(5);
			toolStrip.addMember((new ZoomButton(new ZoomHandler())).getCanvas());

			layouttoolStrip.addMember(toolStrip);
			addMember(layouttoolStrip);
		}

		private void addHeader() {
			HLayout layoutMainTitle = new HLayout(10);
			layoutMainTitle.setWidth100();
			layoutMainTitle.setHeight(1);
			layoutMainTitle.setPadding(5);

			Label lblTitle = new Label("<b>" + feature.getLayer().getLabel() + "</b>");
			lblTitle.setAlign(Alignment.LEFT);
			lblTitle.setWidth("*");
			lblTitle.setHeight100();

			Label lblIdNr = null;
			lblIdNr = new Label("<b>ID: " + getFeatureId(feature) + "</b>");
			lblIdNr.setAlign(Alignment.RIGHT);
			lblIdNr.setWidth("*");
			lblIdNr.setHeight100();

			layoutMainTitle.addMember(lblTitle);
			layoutMainTitle.addMember(lblIdNr);
			addMember(layoutMainTitle);
		}

		/**
		 * TODO.
		 * 
		 * @author Jan De Moerloose
		 * 
		 */
		private class GenericDetailListGrid extends ListGrid {

			public GenericDetailListGrid() {
				setWidth100();
				setAutoHeight();
				setCanEdit(false);
				setShowSelectedStyle(false);
				setShowRollOver(false);
				setShowHeader(false);
				setAutoFitData(Autofit.BOTH);

				setGroupStartOpen(GroupStartOpen.ALL);
				setGroupByField("groupField");

				setShowRecordComponents(true);
				setShowRecordComponentsByCell(true);
				setShowAllRecords(true);
				setBodyOverflow(Overflow.HIDDEN);
				setOverflow(Overflow.AUTO);
				setLeaveScrollbarGap(false);

				ListGridField idField = new ListGridField("idField", "Id");
				idField.setHidden(true);
				ListGridField groupField = new ListGridField("groupField");
				groupField.setHidden(true);
				ListGridField contentField = new ListGridField("contentField");

				setFields(idField, groupField, contentField);

				ListGridRecord record = null;
				RecordList recordList = new RecordList();

				// Eigenschappen
				record = new ListGridRecord();
				record.setAttribute("idField", "Eigenschappen");
				record.setAttribute("groupField", "1. Eigenschappen");
				record.setAttribute("contentField", "");
				recordList.add(record);

				setData(recordList);
			}

			@Override
			protected Canvas createRecordComponent(ListGridRecord record, Integer colNum) {
				if ("contentField".equals(this.getFieldName(colNum))) {
					return DefaultFeatureInfoCanvasBuilder.this.createRecordComponent(feature);
				}
				return null;
			}
		}

		// -------------------------------------------------------------------------
		// Private class ZoomHandler:
		// -------------------------------------------------------------------------

		/**
		 * TODO.
		 * 
		 * @author Jan De Moerloose
		 * 
		 */
		private class ZoomHandler implements com.smartgwt.client.widgets.events.ClickHandler {

			public void onClick(ClickEvent event) {
				feature.getLayer().getMapModel().zoomToFeatures(Arrays.asList(feature));
				feature.getLayer().selectFeature(feature);
			}
		}
	}

	public Canvas createWidget() {
		return null;
	}

	public void configure(String key, String value) {
	}

	// -------------------------------------------------

	protected String format(Attribute<?> a) {
		Object v = a.getValue();
		if (v == null) {
			return "";
		} else {
			if (a.isPrimitive()) {
				PrimitiveType t = ((PrimitiveAttribute<?>) a).getType();
				switch (t) {
					case DATE:
						return dateFormatter.format((Date) v);
					case DOUBLE:
					case FLOAT:
					case SHORT:
					case INTEGER:
					case LONG:
						return numberFormatter.format((Number) v);
					case BOOLEAN:
						return ((Boolean) v ? "Ja" : "Nee");
					case URL:
						return "<a href=\"" + v + "\" target=\"_blank\">v</a>";
					case IMGURL:
						return "<img src=\"" + v + "\" style=\"defaultFeatureInfoCanvasGridImgUrl\" />";
					case CURRENCY:
						return currencyFormatter.format(Double.parseDouble((String) v));
					default:
						return v.toString();
				}
			} else {
				return v.toString();
			}
		}
	}

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	protected class RowLayout extends HLayout {

		public RowLayout(Canvas title, Canvas detail) {
			setPadding(3);
			setWidth100();

			if (title != null) {
				addMember(title);
			}
			if (detail != null) {
				addMember(detail);
			}
		}
	}

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	protected class TitleLabel extends Label {

		public TitleLabel(String title) {
			super(title);
			setStyleName(TITLESTYLE);
			setWidth("30%");
		}
	}

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	protected class DetailLabel extends Canvas {

		public DetailLabel(String detail) {
			super();
			setContents(detail);
			setWidth("*");
		}
	}

	/**
	 * Custom formatter. Note that dates and numbers are already formatted automatically so no need to add a custom one
	 * for those fields.
	 */
	@Api
	public interface ValueFormatter {

		/**
		 * Format the rawValue.
		 * 
		 * @param rawValue
		 *            can be null.
		 * @return formatted value
		 */
		String format(Object rawValue);
	}

	/**
	 * Custom formatter for titles.
	 */
	@Api
	public interface TitleFormatter {

		/**
		 * @return new title for attribute (default is attribute label)
		 */
		String getTitle();
	}
}
