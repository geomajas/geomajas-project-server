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

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendGraphicComponentInfo;
import org.geomajas.service.LegendGraphicService;
import org.geomajas.service.legend.LegendGraphicMetadata;
import org.geomajas.sld.NamedStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Legend graphic component that uses {@link LegendGraphicService}.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component()
@Scope(value = "prototype")
public class LegendGraphicComponentImpl extends AbstractPrintComponent<LegendGraphicComponentInfo> {

	private String label;

	private String layerId;

	private RuleInfo ruleInfo;

	@Autowired
	private LegendGraphicService legendGraphicService;

	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(LegendGraphicComponentImpl.class);

	public LegendGraphicComponentImpl() {
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 *            visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void calculateSize(PdfContext context) {
		LegendComponent legendComponent = getLegend();
		assert (null != legendComponent) : "LegendGraphicComponent must be an instance of LegendComponent";
		assert (null != legendComponent.getFont()) : "LegendGraphicComponent must be an instance of LegendComponent";

		Rectangle textSize = context.getTextSize(label, legendComponent.getFont());
		float margin = 0.25f * legendComponent.getFont().getSize();
		getConstraint().setMarginX(margin);
		getConstraint().setMarginY(margin);
		setBounds(new Rectangle(textSize.getHeight(), textSize.getHeight()));
	}

	protected LegendComponent getLegend() {
		@SuppressWarnings("deprecation")
		PrintComponent<?> ancestor = getParent();

		while (null != ancestor && !(ancestor instanceof LegendComponent)) {
			ancestor = ancestor.getParent();
		}
		if (null != ancestor && ancestor instanceof LegendComponent) {
			return (LegendComponent) ancestor;
		}
		return null;
	}

	@Override
	public void render(PdfContext context) {
		final float w = getSize().getWidth();
		final float h = getSize().getHeight();
		Rectangle iconRect = new Rectangle(0, 0, w, h);
		RenderedImage img;
		try {
			img = legendGraphicService.getLegendGraphic(new RuleMetadata(h, w));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "PNG", baos);
			context.drawImage(Image.getInstance(baos.toByteArray()), iconRect, null);
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public RuleInfo getRuleInfo() {
		return ruleInfo;
	}

	public void setRuleInfo(RuleInfo ruleInfo) {
		this.ruleInfo = ruleInfo;
	}

	@Override
	public void fromDto(LegendGraphicComponentInfo graphicInfo) {
		super.fromDto(graphicInfo);
		setLabel(graphicInfo.getLabel());
		setRuleInfo(graphicInfo.getRuleInfo());
		setLayerId(graphicInfo.getLayerId());
	}

	/**
	 * {@link LegendGraphicMetadata} for a rule.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private final class RuleMetadata implements LegendGraphicMetadata {

		private final float h;

		private final float w;

		private RuleMetadata(float h, float w) {
			this.h = h;
			this.w = w;
		}

		@Override
		public int getWidth() {
			return (int) w;
		}

		@Override
		public UserStyleInfo getUserStyle() {
			return null;
		}

		@Override
		public double getScale() {
			return 0;
		}

		@Override
		public RuleInfo getRule() {
			return ruleInfo;
		}

		@Override
		public NamedStyleInfo getNamedStyle() {
			return null;
		}

		@Override
		public String getLayerId() {
			return layerId;
		}

		@Override
		public int getHeight() {
			return (int) h;
		}
	}
}