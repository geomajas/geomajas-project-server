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
package org.geomajas.plugin.rasterizing.sld;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.filter.Filter;
import org.opengis.style.Description;

/**
 * Implementation of {@link org.geotools.styling.StyleVisitor} that duplicates an SLD style while filtering out text
 * and/or geometry symbolizers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SymbolizerFilterVisitor extends DuplicatingStyleVisitor {
	
	private boolean includeText;
	
	private boolean includeGeometry;

	/**
	 * Overridden to skip some symbolizers.
	 */
	@Override
	public void visit(Rule rule) {
		Rule copy = null;
		Filter filterCopy = null;

		if (rule.getFilter() != null) {
			Filter filter = rule.getFilter();
			filterCopy = copy(filter);
		}

		List<Symbolizer> symsCopy = new ArrayList<Symbolizer>();
		for (Symbolizer sym : rule.symbolizers()) {
			if (!skipSymbolizer(sym)) {
				Symbolizer symCopy = copy(sym);
				symsCopy.add(symCopy);
			}
		}

		Graphic[] legendCopy = rule.getLegendGraphic();
		for (int i = 0; i < legendCopy.length; i++) {
			legendCopy[i] = copy(legendCopy[i]);
		}

		Description descCopy = rule.getDescription();
		descCopy = copy(descCopy);

		copy = sf.createRule();
		copy.symbolizers().addAll(symsCopy);
		copy.setDescription(descCopy);
		copy.setLegendGraphic(legendCopy);
		copy.setName(rule.getName());
		copy.setFilter(filterCopy);
		copy.setElseFilter(rule.isElseFilter());
		copy.setMaxScaleDenominator(rule.getMaxScaleDenominator());
		copy.setMinScaleDenominator(rule.getMinScaleDenominator());

		if (STRICT && !copy.equals(rule)) {
			throw new IllegalStateException("Was unable to duplicate provided Rule:" + rule);
		}
		pages.push(copy);
	}
	
	public boolean isIncludeText() {
		return includeText;
	}
	
	public void setIncludeText(boolean includeText) {
		this.includeText = includeText;
	}
	
	public boolean isIncludeGeometry() {
		return includeGeometry;
	}
	
	public void setIncludeGeometry(boolean includeGeometry) {
		this.includeGeometry = includeGeometry;
	}

	protected boolean skipSymbolizer(Symbolizer symbolizer) {
		if (symbolizer instanceof TextSymbolizer) {
			if (!isIncludeText()) {
				return true;
			}
		} else if (symbolizer instanceof LineSymbolizer) {
			if (!isIncludeGeometry()) {
				return true;
			}
		} else if (symbolizer instanceof PointSymbolizer) {
			if (!isIncludeGeometry()) {
				return true;
			}
		} else if (symbolizer instanceof PolygonSymbolizer) {
			if (!isIncludeGeometry()) {
				return true;
			}
		}
		return false;
	}
	
	

}
