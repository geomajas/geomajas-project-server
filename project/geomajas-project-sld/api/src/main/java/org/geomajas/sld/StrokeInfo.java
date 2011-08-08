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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class StrokeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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

		private static final long serialVersionUID = 1100;

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
	}
}
