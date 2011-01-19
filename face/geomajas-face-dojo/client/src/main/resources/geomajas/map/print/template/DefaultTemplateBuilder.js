dojo.provide("geomajas.map.print.template.DefaultTemplateBuilder");
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
dojo.declare("DefaultTemplateBuilder", TemplateBuilder, {

	/**
	 * @class 
	 * Default print template builder, parameters include title, size, raster DPI, orientation, etc...
	 * 
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
//		private double pageWidth;
//		private double pageHeight;
//		private int marginX;
//		private int marginY;
//		private String titleText;
//		private int rasterDpi;
//		private boolean withScaleBar;
//		private boolean withArrow;
//		private MapModel mapModel;
//		private String applicationId;
	},
	
	buildTemplate : function () {
		var template = this.inherited(arguments);
		template.setTemplate(false);
		template.setId(1);
		template.setName("default");
		return template;
	},	
	
	buildPage : function () {
		var page = this.inherited(arguments);
		page.getLayoutConstraint().setWidth(this.pageWidth);
		page.getLayoutConstraint().setHeight(this.pageHeight);
		return page;
	},
	
	buildMap : function () {
		var map = this.inherited(arguments);
		map.getLayoutConstraint().setMarginX(this.marginX);
		map.getLayoutConstraint().setMarginY(this.marginY);
		var view = this.mapModel.getMapView();
		var mapWidth = this.pageWidth - 2 * this.marginX;
		var mapHeight = this.pageHeight - 2 * this.marginY;
		var bounds = view.getCurrentBbox().createFittingBox(mapWidth, mapHeight);
		var origin = bounds.getOrigin();
		map.setLocation(new Coordinate(origin.getX(), origin.getY()));
		map.setPpUnit((mapWidth / bounds.getWidth()));
		map.setTag("map");
		map.setMapId(this.mapModel.getId());
		map.setApplicationId(this.applicationId);
		map.setRasterResolution(this.rasterDpi);
		var layerChildren = map.getChildren().list; /*PrintComponentInfo*/
		
		var layers = this.mapModel.getVisibleLayerList();
		for(var i = 0; i < layers.count; i++){
			var layer = layers.item(i);
			if (layer instanceof VectorLayer && layer.checkVisibility()) {
				var info = new VectorLayerComponentInfo();
				var vectorLayer = layer;
				info.setLayerId(vectorLayer.getLayerId());
				info.setStyleInfo(vectorLayer.getNamedStyle().toJSON());
				info.setFilter(vectorLayer.getFilterString());
				info.setLabelsVisible(vectorLayer.isLabeled());
				info.setSelected(vectorLayer.isSelected());
				info.setSelectedFeatureIds(vectorLayer.getSelectionStore().getKeyList());
				layerChildren.splice(0,0,info);
			} else if (layer instanceof RasterLayer && layer.checkVisibility()) {
				var info = new RasterLayerComponentInfo();
				var rasterLayer = layer;
				info.setLayerId(rasterLayer.getLayerId());
				info.setStyle(rasterLayer.getStyle().opacity);
				layerChildren.splice(0,0,info);
			}
		}
		return map;
	},
	
	buildArrow : function () {
		if (this.withArrow == true) {
			var northarrow = this.inherited(arguments);
			northarrow.setImagePath("/images/northarrow.gif");
			northarrow.getLayoutConstraint().setAlignmentX(geomajas.LayoutConstraints.RIGHT);
			northarrow.getLayoutConstraint().setAlignmentY(geomajas.LayoutConstraints.TOP);
			northarrow.getLayoutConstraint().setMarginX(20);
			northarrow.getLayoutConstraint().setMarginY(20);
			northarrow.getLayoutConstraint().setWidth(10);
			northarrow.setTag("arrow");
			return northarrow;
		} else {
			return null;
		}
	},
	
	buildLegend : function () {
		var legend = this.inherited(arguments);
		var style = new FontStyleInfo();
		style.setFamily("Dialog");
		style.setStyle("Italic");
		style.setSize(14);
		legend.setFont(style);
		legend.setMapId(this.mapModel.getId());
		legend.setTag("legend");
		
		var layers = this.mapModel.getVisibleLayerList();
		for(var i = 0; i < layers.count; i++){
			var layer = layers.item(i);
			if (layer instanceof VectorLayer && layer.checkVisibility()) {
				var vectorLayer = layer;
				var defs = vectorLayer.getStyles();
				for(var j = 0; j < defs.count; j++){
					var styleDefinition = defs.item(j);
					var text = "";
					if (defs.count > 1) {
						text = vectorLayer.getLabel() + " (" + styleDefinition.getName() + ")";
					} else {
						text = vectorLayer.getLabel();
					}
					var item = new LegendItemComponentInfo();
					var icon = new LegendIconComponentInfo();
					icon.setLabel(text);
					icon.setStyleInfo(styleDefinition.toJSON());
					icon.setLayerType(vectorLayer.getLayerType());
					var legendLabel = new LabelComponentInfo();
					legendLabel.setBackgroundColor("0xFFFFFF");
					legendLabel.setBorderColor("0x000000");
					legendLabel.setFontColor("0x000000");
					legendLabel.setFont(legend.getFont());
					legendLabel.setText(text);
					legendLabel.setTextOnly(true);
					item.addChild(icon);
					item.addChild(legendLabel);
					legend.addChild(item);
				}
			} else if (layer instanceof RasterLayer && layer.checkVisibility()) {
				var rasterLayer = layer;
				var item = new LegendItemComponentInfo();
				var icon = new LegendIconComponentInfo();
				icon.setLabel(rasterLayer.getLabel());
				icon.setLayerType(rasterLayer.getLayerType());
				var legendLabel = new LabelComponentInfo();
				legendLabel.setFont(legend.getFont());
				legendLabel.setBackgroundColor("0xFFFFFF");
				legendLabel.setBorderColor("0x000000");
				legendLabel.setFontColor("0x000000");
				legendLabel.setText(rasterLayer.getLabel());
				legendLabel.setTextOnly(true);
				item.addChild(icon);
				item.addChild(legendLabel);
				legend.addChild(item);
			}
		}
		return legend;
	},

	buildScaleBar : function () {
		if (this.withScaleBar == true) {
			var bar = this.inherited(arguments);
			bar.setTicNumber(3);
			bar.setTag("scalebar");
			return bar;
		} else {
			return null;
		}
	},

	buildTitle : function () {
		if (this.titleText != null) {
			var title = this.inherited(arguments);
			title.setText(this.titleText);
			title.getLayoutConstraint().setMarginY(2 * this.marginY);
			return title;
		} else {
			return null;
		}
	},
	
	// -----------------------------------------------------
	
	setPageWidth : function (pageWidth) {
		this.pageWidth = pageWidth;
	},

	setPageHeight : function (pageHeight) {
		this.pageHeight = pageHeight;
	},

	setTitleText : function (titleText) {
		this.titleText = titleText;
	},

	setRasterDpi : function (rasterDpi) {
		this.rasterDpi = rasterDpi;
	},

	setWithScaleBar : function (withScaleBar) {
		this.withScaleBar = withScaleBar;
	},

	setWithArrow : function (withArrow) {
		this.withArrow = withArrow;
	},

	setMarginX : function (marginX) {
		this.marginX = marginX;
	},

	setMarginY : function (marginY) {
		this.marginY = marginY;
	},

	setMapModel : function (mapModel) {
		this.mapModel = mapModel;
	},

	setApplicationId : function (applicationId) {
		this.applicationId = applicationId;
	}

});
