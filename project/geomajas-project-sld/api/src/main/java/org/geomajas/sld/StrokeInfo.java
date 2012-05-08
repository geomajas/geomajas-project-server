/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;

/**
 * 
 A "Stroke" specifies the appearance of a linear geometry. It is defined in parallel with SVG strokes. The following
 * CssParameters may be used: "stroke" (color), "stroke-opacity", "stroke-width", "stroke-linejoin", "stroke-linecap",
 * "stroke-dasharray", and "stroke-dashoffset".
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Stroke">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:choice minOccurs="0">
 *         &lt;!-- Reference to inner class ChoiceInfo -->
 *       &lt;/xs:choice>
 *       &lt;xs:element ref="ns:CssParameter" minOccurs="0" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class StrokeInfo implements Serializable {

	private static final String STROKE_OPACITY = "stroke-opacity";

	private static final String STROKE = "stroke";

	private static final String STROKE_WIDTH = "stroke-width";

	private static final long serialVersionUID = 100;

	private ChoiceInfo choice;

	private List<CssParameterInfo> cssParameterList = new ArrayList<CssParameterInfo>();

	/**
	 * Get the choice value.
	 * 
	 * @return value
	 */
	public ChoiceInfo getChoice() {
		return choice;
	}

	/**
	 * Set the choice value.
	 * 
	 * @param choice
	 */
	public void setChoice(ChoiceInfo choice) {
		this.choice = choice;
	}

	/**
	 * Get the list of 'CssParameter' element items.
	 * 
	 * @return list
	 */
	public List<CssParameterInfo> getCssParameterList() {
		if (cssParameterList == null) {
			cssParameterList = new ArrayList<CssParameterInfo>();
		}
		return cssParameterList;
	}

	/**
	 * Set the list of 'CssParameter' element items.
	 * 
	 * @param list
	 */
	public void setCssParameterList(List<CssParameterInfo> list) {
		cssParameterList = list;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:choice
	 * xmlns:ns="http://www.opengis.net/sld"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0">
	 *   &lt;xs:element ref="ns:GraphicFill"/>
	 *   &lt;xs:element ref="ns:GraphicStroke"/>
	 * &lt;/xs:choice>
	 * </pre>
	 */
	public static class ChoiceInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private int choiceSelect = -1;

		private static final int GRAPHIC_FILL_CHOICE = 0;

		private static final int GRAPHIC_STROKE_CHOICE = 1;

		private GraphicFillInfo graphicFill;

		private GraphicStrokeInfo graphicStroke;

		private void setChoiceSelect(int choice) {
			if (choiceSelect == -1) {
				choiceSelect = choice;
			} else if (choiceSelect != choice) {
				throw new IllegalStateException("Need to call clearChoiceSelect() before changing existing choice");
			}
		}

		/**
		 * Clear the choice selection.
		 */
		public void clearChoiceSelect() {
			choiceSelect = -1;
		}

		/**
		 * Check if GraphicFill is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifGraphicFill() {
			return choiceSelect == GRAPHIC_FILL_CHOICE;
		}

		/**
		 * Get the 'GraphicFill' element value.
		 * 
		 * @return value
		 */
		public GraphicFillInfo getGraphicFill() {
			return graphicFill;
		}

		/**
		 * Set the 'GraphicFill' element value.
		 * 
		 * @param graphicFill
		 */
		public void setGraphicFill(GraphicFillInfo graphicFill) {
			setChoiceSelect(GRAPHIC_FILL_CHOICE);
			this.graphicFill = graphicFill;
		}

		/**
		 * Check if GraphicStroke is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifGraphicStroke() {
			return choiceSelect == GRAPHIC_STROKE_CHOICE;
		}

		/**
		 * Get the 'GraphicStroke' element value.
		 * 
		 * @return value
		 */
		public GraphicStrokeInfo getGraphicStroke() {
			return graphicStroke;
		}

		/**
		 * Set the 'GraphicStroke' element value.
		 * 
		 * @param graphicStroke
		 */
		public void setGraphicStroke(GraphicStrokeInfo graphicStroke) {
			setChoiceSelect(GRAPHIC_STROKE_CHOICE);
			this.graphicStroke = graphicStroke;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "StrokeInfo.ChoiceInfo(choiceSelect=" + this.choiceSelect + ", graphicFill=" + this.getGraphicFill()
					+ ", graphicStroke=" + this.getGraphicStroke() + ")";
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof ChoiceInfo)) {
				return false;
			}
			final ChoiceInfo other = (ChoiceInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.choiceSelect != other.choiceSelect) {
				return false;
			}
			if (this.getGraphicFill() == null ? other.getGraphicFill() != null : !this.getGraphicFill().equals(
					(java.lang.Object) other.getGraphicFill())) {
				return false;
			}
			if (this.getGraphicStroke() == null ? other.getGraphicStroke() != null : !this.getGraphicStroke().equals(
					(java.lang.Object) other.getGraphicStroke())) {
				return false;
			}
			return true;
		}

		/** {@inheritDoc} */
		@java.lang.SuppressWarnings("all")
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof ChoiceInfo;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + this.choiceSelect;
			result = result * prime + (this.getGraphicFill() == null ? 0 : this.getGraphicFill().hashCode());
			result = result * prime + (this.getGraphicStroke() == null ? 0 : this.getGraphicStroke().hashCode());
			return result;
		}
	}

	/**
	 * Set the stroke width.
	 * 
	 * @param f the stroke width
	 */
	public void setStrokeWidth(float f) {
		for (CssParameterInfo param : getCssParameterList()) {
			if (param.getName().equals(STROKE_WIDTH)) {
				param.setValue(Float.toString(f));
				return;
			}
		}
		getCssParameterList().add(new CssParameterInfo(STROKE_WIDTH, Float.toString(f)));
	}

	/**
	 * Set the stroke color.
	 * 
	 * @param strokeColor the stroke color (css)
	 */
	public void setStrokeColor(String strokeColor) {
		for (CssParameterInfo param : getCssParameterList()) {
			if (param.getName().equals(STROKE)) {
				param.setValue(strokeColor);
				return;
			}
		}
		getCssParameterList().add(new CssParameterInfo(STROKE, strokeColor));
	}

	/**
	 * Set the stroke opacity.
	 * 
	 * @param f the stroke opacity
	 */
	public void setStrokeOpacity(float f) {
		for (CssParameterInfo param : getCssParameterList()) {
			if (param.getName().equals(STROKE_OPACITY)) {
				param.setValue(Float.toString(f));
				return;
			}
		}
		getCssParameterList().add(new CssParameterInfo(STROKE_OPACITY, Float.toString(f)));
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "StrokeInfo(choice=" + this.getChoice() + ", cssParameterList=" + this.getCssParameterList() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof StrokeInfo)) {
			return false;
		}
		final StrokeInfo other = (StrokeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getChoice() == null ? other.getChoice() != null : !this.getChoice().equals(
				(java.lang.Object) other.getChoice())) {
			return false;
		}
		if (this.getCssParameterList() == null ? other.getCssParameterList() != null : !this.getCssParameterList()
				.equals((java.lang.Object) other.getCssParameterList())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof StrokeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getChoice() == null ? 0 : this.getChoice().hashCode());
		result = result * prime + (this.getCssParameterList() == null ? 0 : this.getCssParameterList().hashCode());
		return result;
	}
}