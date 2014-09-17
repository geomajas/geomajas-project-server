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
package org.geomajas.plugin.printing.component.impl;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.geomajas.plugin.printing.parser.FontConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Inclusion of legend in printed document.
 * 
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class LegendComponentImpl extends AbstractPrintComponent<LegendComponentInfo> implements LegendComponent {

	/** Application id. */
	private String applicationId;

	/** Map id. */
	private String mapId;

	/** The font for the text. */
	@XStreamConverter(FontConverter.class)
	private Font font = new Font(LegendComponentInfo.DEFAULT_LEGEND_FONT_FAMILY, Font.PLAIN,
			LegendComponentInfo.DEFAULT_LEGEND_FONTSIZE); // Default font

	/** Heading text. */
	private String title;
	private String originalTitle;

	@Autowired
	@XStreamOmitField
	private PrintDtoConverterService converterService;

	public LegendComponentImpl() {
		this(null);
	}

	public LegendComponentImpl(String title) {
		this.originalTitle = title;
		setTitle(title);
		setConstraint(new LayoutConstraint(LayoutConstraint.RIGHT, LayoutConstraint.BOTTOM, LayoutConstraint.FLOW_Y, 0,
				0, 20, 20)); /* marginX: 20 ; marginY: 20 */
		LabelComponentImpl titleLabel = new LabelComponentImpl();
		titleLabel.getConstraint().setAlignmentX(LayoutConstraint.CENTER);
		titleLabel.getConstraint().setMarginY(4); // margin at the top
		titleLabel.setMargin(0.3F * getFont().getSize()); // extra margin around the text
		titleLabel.setTextOnly(true);
		titleLabel.setText(getTitle()); // Usually "Legend" (if English)
		titleLabel.setTag(PrintTemplate.TITLE);
		titleLabel.setFont(font);
		addComponent(titleLabel);
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LegendComponent#getFont()
	 */
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LegendComponent#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title != null) {
			this.title = title;
		} else {
			ResourceBundle resourceBundle = getCurrentResourceBundle();
			this.title = resourceBundle.getString("LegendTitle");
		}
	}

	@Override
	public void calculateSize(PdfContext context) {
		super.calculateSize(context);
	}

	@Override
	public void render(PdfContext context) {
		// border
		context.fillRectangle(getSize());
		super.render(context);
		// border
		context.strokeRectangle(getSize());
	}

	@SuppressWarnings("deprecation")
	protected MapComponent getMap() {
		PrintComponent<?> parent = getParent();
		assert (null != parent && parent instanceof MapComponent) : 
				"The parent of the LegendComponent must be a mapComponent.";
		return (MapComponent) getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LegendComponent#getMapId()
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

	@Override
	public void performPostLoadAction() {
		setTitle(originalTitle);
	}

	// /**
	// * A legend manages its own children at rendering time, so they shouldn't
	// be
	// * serialized. This magic callback method does the trick !
	// */
	// public void beforeMarshal(Marshaller m) {
	// getChildren().clear();
	// }

	public void clearItems() {
		List<LegendItemComponentImpl> items = new ArrayList<LegendItemComponentImpl>();
		for (PrintComponent child : children) {
			if (child instanceof LegendItemComponentImpl) {
				items.add((LegendItemComponentImpl) child);
			}
		}
		for (LegendItemComponentImpl item : items) {
			removeComponent(item);
		}
	}

	public void addVectorLayer(ClientVectorLayerInfo info) {
		String label = info.getLabel();
		List<FeatureStyleInfo> defs = info.getNamedStyleInfo().getFeatureStyles();
		for (FeatureStyleInfo styleDefinition : defs) {
			String text = "";
			if (defs.size() > 1) {
				text = label + "(" + styleDefinition.getName() + ")";
			} else {
				text = label;
			}
			LegendItemComponentImpl item = new LegendItemComponentImpl(styleDefinition, text, info.getLayerType(),
					getFont());
			addComponent(item);
		}
	}

	public void addRasterLayer(ClientRasterLayerInfo info) {
		LegendItemComponentImpl item = new LegendItemComponentImpl(null, info.getLabel(), info.getLayerType(),
				getFont());
		addComponent(item);
	}

	public void fromDto(LegendComponentInfo legendInfo) {
		super.fromDto(legendInfo);
		setApplicationId(legendInfo.getApplicationId());
		setMapId(legendInfo.getMapId());
		setFont(converterService.toInternal(legendInfo.getFont()));
		setTitle(legendInfo.getTitle());
	}
}