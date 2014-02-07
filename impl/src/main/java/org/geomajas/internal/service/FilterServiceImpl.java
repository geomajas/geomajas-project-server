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

package org.geomajas.internal.service;

import java.util.Date;
import java.util.HashSet;

import org.geomajas.geometry.Crs;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.filter.NonLenientFilterFactoryImpl;
import org.geomajas.internal.layer.feature.FeatureModelRegistry;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.FilterService;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * Utility class for creating ECQL filters. Make sure all arguments are correct before you use these
 * functions. This class uses the OpenGIS FilterFactory2 interface, which extends the OGC FilterFactory specification
 * with JTS geometry support.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public final class FilterServiceImpl implements FilterService {

	private final Logger log = LoggerFactory.getLogger(FilterServiceImpl.class);
	
	private final IdReplacingVisitor idReplacer = new IdReplacingVisitor();

	private static final FilterFactory2 FF;
	static {
		// use hint to get at the correct filter factory
		// TODO: find better way to make this configurable ?
		Hints hints = new Hints();
		hints.put(Hints.FILTER_FACTORY, NonLenientFilterFactoryImpl.class);
		FF = CommonFactoryFinder.getFilterFactory2(hints);
	}

	@Override
	public Filter createBetweenFilter(String name, String lower, String upper) {
		PropertyName attribute = FF.property(name);
		Expression d1 = FF.literal(lower);
		Expression d2 = FF.literal(upper);
		return FF.between(attribute, d1, d2);
	}

	@Override
	public Filter createLikeFilter(String name, String pattern) {

		PropertyName attribute = FF.property(name);
		return FF.like(attribute, pattern, "*", "?", "\\");
	}

	@Override
	public Filter createCompareFilter(String name, String comparator, String value) {
		PropertyName attribute = FF.property(name);
		Expression val = FF.literal(value);
		if ("<".equals(comparator)) {
			return FF.less(attribute, val);
		} else if (">".equals(comparator)) {
			return FF.greater(attribute, val);
		} else if (">=".equals(comparator)) {
			return FF.greaterOrEqual(attribute, val);
		} else if ("<=".equals(comparator)) {
			return FF.lessOrEqual(attribute, val);
		} else if ("=".equals(comparator)) {
			return FF.equals(attribute, val);
		} else if ("==".equals(comparator)) {
			return FF.equals(attribute, val);
		} else if ("<>".equals(comparator)) {
			return FF.notEqual(attribute, val, false);
		} else {
			throw new IllegalArgumentException("Could not interpret compare filter. Argument (" + value
					+ ") not known.");
		}
	}

	@Override
	public Filter createCompareFilter(String name, String comparator, Date value) {
		PropertyName attribute = FF.property(name);
		Expression val = FF.literal(value);
		if ("<".equals(comparator)) {
			return FF.less(attribute, val);
		} else if (">".equals(comparator)) {
			return FF.greater(attribute, val);
		} else if (">=".equals(comparator)) {
			return FF.greaterOrEqual(attribute, val);
		} else if ("<=".equals(comparator)) {
			return FF.lessOrEqual(attribute, val);
		} else if ("=".equals(comparator)) {
			return FF.equals(attribute, val);
		} else if ("==".equals(comparator)) {
			return FF.equals(attribute, val);
		} else if ("<>".equals(comparator)) {
			return FF.notEqual(attribute, val, false);
		} else {
			throw new IllegalArgumentException("Could not interpret compare filter. Argument (" + value
					+ ") not known.");
		}
	}

	@Override
	public Filter createGeometryTypeFilter(String geomName, String geometryType) {
		return FF.equals(FF.literal(geometryType), FF.function("geometryType", FF.property(geomName)));
	}

	@Override
	public Filter createLogicFilter(Filter filter1, String logic, Filter filter2) {
		if ("and".equalsIgnoreCase(logic)) {
			return FF.and(filter1, filter2);
		} else if ("or".equalsIgnoreCase(logic)) {
			return FF.or(filter1, filter2);
		} else if ("not".equalsIgnoreCase(logic)) {
			return FF.not(filter1);
		} else {
			throw new IllegalArgumentException("Could not interpret logic filter. Argument (" + logic + ") not known.");
		}
	}

	@Override
	public Filter createFidFilter(String[] featureIDs) {
		HashSet<Identifier> ids = new HashSet<Identifier>();
		for (String id : featureIDs) {
			ids.add(new FeatureIdImpl(id));
		}
		return FF.id(ids);
	}

	@Override
	public Filter createContainsFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.contains(nameExpression, geomLiteral);
	}

	@Override
	public Filter createWithinFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.within(nameExpression, geomLiteral);
	}

	@Override
	public Filter createIntersectsFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.intersects(nameExpression, geomLiteral);
	}

	@Override
	public Filter createTouchesFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.touches(nameExpression, geomLiteral);
	}

	@Override
	public Filter createBboxFilter(String epsg, Envelope bbox, String geomName) {
		return FF.bbox(geomName, bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), epsg);
	}

	@Override
	public Filter createBboxFilter(Crs crs, Envelope bbox, String geomName) {
		return FF.bbox(FF.property(geomName), new ReferencedEnvelope(bbox, crs));
	}

	@Override
	public Filter createOverlapsFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.overlaps(nameExpression, geomLiteral);
	}

	@Override
	public Filter createTrueFilter() {
		return Filter.INCLUDE;
	}

	@Override
	public Filter createFalseFilter() {
		return Filter.EXCLUDE;
	}

	@Override
	public void registerFeatureModel(FeatureModel featureModel) {
		FeatureModelRegistry.getRegistry().register(featureModel);
	}

	@Override
	public Filter createAndFilter(Filter left, Filter right) {
		return FF.and(left, right);
	}

	@Override
	public Filter createOrFilter(Filter left, Filter right) {
		return FF.or(left, right);
	}

	/** @inheritDoc */
	public Filter parseFilter(String filter) throws GeomajasException {
		if (null == filter || filter.length() == 0) {
			return createTrueFilter();
		}
		try {
			if (idReplacer.shouldParse(filter)) {
				return idReplacer.parse(filter);
			} else {
				return ECQL.toFilter(filter, FF);
			}
		} catch (CQLException e) {
			// ECQL should be a superset of CQL, but there are apparently extra key words like "id"
			// fall back to CQL for backwards compatibility
			log.warn("Filter not parsable by ECQL, falling back to CQL", e);
			try {
				return CQL.toFilter(filter, FF);
			} catch (CQLException ce) {
				throw new GeomajasException(ce, ExceptionCode.FILTER_PARSE_PROBLEM, filter);
			}
		}
	}


	/**
	 * {@link DuplicatingFilterVisitor} class that replaces the '@id' keyword by an ECQL compatible alternative for
	 * parsing.
	 * 
	 * @author Jan De Moerloose
	 */
	private class IdReplacingVisitor extends DuplicatingFilterVisitor {

		private static final String FAKE_ID = "__id__";

		public Filter parse(String filter) throws GeomajasException {
			filter =  filter.replace(FilterService.ATTRIBUTE_ID, FAKE_ID);
			return (Filter) parseFilter(filter).accept(this, null);
		}

		public boolean shouldParse(String filter) {
			return filter.contains(FilterService.ATTRIBUTE_ID);
		}

		@Override
		public Object visit(PropertyName expression, Object extraData) {
			return getFactory(extraData).property(replaceId(expression.getPropertyName()),
					expression.getNamespaceContext());
		}

		private String replaceId(String propertyName) {
			return propertyName.replace(FAKE_ID, FilterService.ATTRIBUTE_ID);
		}

	}

	@Override
	public FilterFactory getFilterFactory() {
		return FF;
	}
}
