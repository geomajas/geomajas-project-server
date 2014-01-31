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
package org.geomajas.layer.hibernate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.geomajas.layer.LayerException;
import org.geomajas.service.FilterService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernatespatial.criterion.SpatialRestrictions;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;
import org.opengis.temporal.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * FilterVisitor implementation for the HibernateLayer. This class transforms OpenGis filters into a Hibernate
 * criteria object. This is how the HibernateLayer is able to use OpenGis filters.
 * </p>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class CriteriaVisitor implements FilterVisitor {

	private static final String HIBERNATE_ID = "id";

	private final Logger log = LoggerFactory.getLogger(CriteriaVisitor.class);

	/**
	 * Name of the geometry attribute. Stored here as a shortcut.
	 */
	private String geomName;

	/**
	 * The srid of the coordinate reference system used in the HibernateLayer. Stored here as a shortcut.
	 */
	private final int srid;

	/**
	 * The HibernateFeatureModel that contains all Hibernate metadata. This is needed when creating criteria.
	 */
	private final HibernateFeatureModel featureModel;

	private final DateFormat dateFormat;

	/**
	 * List of aliases created when converting an OpenGis filter to a Hibernate criteria object.
	 */
	private final List<String> aliases = new ArrayList<String>(); // These never get cleaned!

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * The only constructor. A CriteriaVisitor object can only create criteria for one specific FeatureModel. This is
	 * because it always needs the Hibernate meta data that the FeatureModel stores.
	 *
	 * @param featureModel feature model
	 * @param dateFormat date format
	 */
	public CriteriaVisitor(HibernateFeatureModel featureModel, DateFormat dateFormat) {
		this.featureModel = featureModel;
		this.dateFormat = dateFormat;
		srid = featureModel.getSrid();
		try {
			geomName = featureModel.getGeometryAttributeName();
		} catch (LayerException e) {
			log.warn("Cannot read geomName, defaulting to 'geometry'", e);
			geomName = "geometry";
		}
	}

	// -------------------------------------------------------------------------
	// FilterVisitor implementation:
	// -------------------------------------------------------------------------

	/** {@inheritDoc} */
	public Object visit(And filter, Object userData) {
		Criterion c = null;
		for (Filter element : filter.getChildren()) {
			if (c == null) {
				c = (Criterion) element.accept(this, userData);
			} else {
				c = Restrictions.and(c, (Criterion) element.accept(this, userData));
			}
		}
		return c;
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Not filter, Object userData) {
		Criterion c = (Criterion) filter.getFilter().accept(this, userData);
		return Restrictions.not(c);
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Or filter, Object userData) {
		Criterion c = null;
		for (Filter element : filter.getChildren()) {
			if (c == null) {
				c = (Criterion) element.accept(this, userData);
			} else {
				c = Restrictions.or(c, (Criterion) element.accept(this, userData));
			}
		}
		return c;
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsBetween filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression());
		String finalName = parsePropertyName(propertyName, userData);

		Object lo = castLiteral(getLiteralValue(filter.getLowerBoundary()), propertyName);
		Object hi = castLiteral(getLiteralValue(filter.getUpperBoundary()), propertyName);
		return Restrictions.between(finalName, lo, hi);
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsEqualTo filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression1());
		String finalName = parsePropertyName(propertyName, userData);

		Object value = castLiteral(getLiteralValue(filter.getExpression2()), propertyName);
		return Restrictions.eq(finalName, value);
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsNotEqualTo filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression1());
		String finalName = parsePropertyName(propertyName, userData);

		Object value = castLiteral(getLiteralValue(filter.getExpression2()), propertyName);
		return Restrictions.ne(finalName, value);
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsGreaterThan filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression1());
		String finalName = parsePropertyName(propertyName, userData);

		Object literal = getLiteralValue(filter.getExpression2());
		return Restrictions.gt(finalName, castLiteral(literal, propertyName));
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression1());
		String finalName = parsePropertyName(propertyName, userData);

		Object literal = getLiteralValue(filter.getExpression2());
		return Restrictions.ge(finalName, castLiteral(literal, propertyName));
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsLessThan filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression1());
		String finalName = parsePropertyName(propertyName, userData);

		Object literal = getLiteralValue(filter.getExpression2());
		return Restrictions.lt(finalName, castLiteral(literal, propertyName));
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsLessThanOrEqualTo filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression1());
		String finalName = parsePropertyName(propertyName, userData);

		Object literal = getLiteralValue(filter.getExpression2());
		return Restrictions.le(finalName, castLiteral(literal, propertyName));
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsLike filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression());
		String finalName = parsePropertyName(propertyName, userData);

		String value = filter.getLiteral();
		value = value.replaceAll("\\*", "%");
		value = value.replaceAll("\\?", "_");
		if (filter.isMatchingCase()) {
			return Restrictions.like(finalName, value);
		} else {
			return Restrictions.ilike(finalName, value);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsNull filter, Object userData) {
		String propertyName = getPropertyName(filter.getExpression());
		String finalName = parsePropertyName(propertyName, userData);
		return Restrictions.isNull(finalName);
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(BBOX filter, Object userData) {
		Envelope env = new Envelope(filter.getMinX(), filter.getMaxX(), filter.getMinY(), filter.getMaxY());
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.filter(finalName, env, srid);
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Beyond filter, Object userData) {
		throw new UnsupportedOperationException("visit(Beyond filter, Object userData)");
	}

	/** {@inheritDoc} */
	public Object visit(Contains filter, Object userData) {
		throw new UnsupportedOperationException("visit(Contains filter, Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Crosses filter, Object userData) {
		throw new UnsupportedOperationException("visit(Crosses filter, Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Disjoint filter, Object userData) {
		throw new UnsupportedOperationException("visit(Disjoint filter, Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(DWithin filter, Object userData) {
		throw new UnsupportedOperationException("visit(DWithin filter, Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Equals filter, Object userData) {
		throw new UnsupportedOperationException("visit(Equals filter, Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Intersects filter, Object userData) {
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.intersects(finalName, asGeometry(getLiteralValue(filter.getExpression2())));
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Overlaps filter, Object userData) {
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.overlaps(finalName, asGeometry(getLiteralValue(filter.getExpression2())));
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Touches filter, Object userData) {
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.touches(finalName, asGeometry(getLiteralValue(filter.getExpression2())));
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Within filter, Object userData) {
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.within(finalName, asGeometry(getLiteralValue(filter.getExpression2())));
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(ExcludeFilter filter, Object userData) {
		return Restrictions.not(Restrictions.conjunction());
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(IncludeFilter filter, Object userData) {
		return Restrictions.conjunction();
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Id filter, Object userData) {
		String idName;
		try {
			idName = featureModel.getEntityMetadata().getIdentifierPropertyName();
		} catch (LayerException e) {
			log.warn("Cannot read idName, defaulting to 'id'", e);
			idName = HIBERNATE_ID;
		}
		Collection<?> c = (Collection<?>) castLiteral(filter.getIdentifiers(), idName);
		return Restrictions.in(idName, c);
	}

	/** {@inheritDoc} */
	@Override
	public Object visitNullFilter(Object userData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(PropertyIsNil filter, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(After after, Object extraData) {
		String propertyName = getPropertyName(after.getExpression1());
		String finalName = parsePropertyName(propertyName, after);
		Object literal = getLiteralValue(after.getExpression2());
		if (literal instanceof Date) {
			return Restrictions.gt(finalName, literal);
		} else {
			throw new UnsupportedOperationException("visit(Object userData)");
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(AnyInteracts anyInteracts, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Before before, Object extraData) {
		String propertyName = getPropertyName(before.getExpression1());
		String finalName = parsePropertyName(propertyName, before);
		Object literal = getLiteralValue(before.getExpression2());
		if (literal instanceof Date) {
			return Restrictions.lt(finalName, literal);
		} else {
			throw new UnsupportedOperationException("visit(Object userData)");
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Begins begins, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(BegunBy begunBy, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(During during, Object userData) {
		String propertyName = getPropertyName(during.getExpression1());
		String finalName = parsePropertyName(propertyName, userData);
		Object literal = getLiteralValue(during.getExpression2());
		if (literal instanceof Period) {
			Period p = (Period) literal;
			Date begin = p.getBeginning().getPosition().getDate();
			Date end = p.getEnding().getPosition().getDate();
			return Restrictions.between(finalName, begin, end);
		} else {
			throw new UnsupportedOperationException("visit(Object userData)");
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(EndedBy endedBy, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Ends ends, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(Meets meets, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(MetBy metBy, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(OverlappedBy overlappedBy, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(TContains contains, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(TEquals equals, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	/** {@inheritDoc} */
	@Override
	public Object visit(TOverlaps contains, Object extraData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Get the property name from the expression.
	 * 
	 * @param expression expression
	 * @return property name
	 */
	private String getPropertyName(Expression expression) {
		if (!(expression instanceof PropertyName)) {
			throw new IllegalArgumentException("Expression " + expression + " is not a PropertyName.");
		}
		String name = ((PropertyName) expression).getPropertyName();
		if (name.endsWith(FilterService.ATTRIBUTE_ID)) {
			// replace by Hibernate id property, always refers to the id, even if named differently
			name = name.substring(0, name.length() - FilterService.ATTRIBUTE_ID.length()) + HIBERNATE_ID;
		}
		return name;
	}

	/**
	 * Get the literal value for an expression.
	 * 
	 * @param expression expression
	 * @return literal value
	 */
	private Object getLiteralValue(Expression expression) {
		if (!(expression instanceof Literal)) {
			throw new IllegalArgumentException("Expression " + expression + " is not a Literal.");
		}
		return ((Literal) expression).getValue();
	}

	/**
	 * Go through the property name to see if it is a complex one. If it is, aliases must be declared.
	 * 
	 * @param orgPropertyName
	 *            The propertyName. Can be complex.
	 * @param userData
	 *            The userData object that is passed in each method of the FilterVisitor. Should always be of the info
	 *            "Criteria".
	 * @return property name
	 */
	private String parsePropertyName(String orgPropertyName, Object userData) {
		// try to assure the correct separator is used
		String propertyName = orgPropertyName.replace(HibernateLayerUtil.XPATH_SEPARATOR, HibernateLayerUtil.SEPARATOR);

		// split the path (separator is defined in the HibernateLayerUtil)
		String[] props = propertyName.split(HibernateLayerUtil.SEPARATOR_REGEXP);
		String finalName;
		if (props.length > 1 && userData instanceof Criteria) {
			// the criteria API requires an alias for each join table !!!
			String prevAlias = null;
			for (int i = 0; i < props.length - 1; i++) {
				String alias = props[i] + "_alias";
				if (!aliases.contains(alias)) {
					Criteria criteria = (Criteria) userData;
					if (i == 0) {
						criteria.createAlias(props[0], alias);
					} else {
						criteria.createAlias(prevAlias + "." + props[i], alias);
					}
					aliases.add(alias);
				}
				prevAlias = alias;
			}
			finalName = prevAlias + "." + props[props.length - 1];
		} else {
			finalName = propertyName;
		}
		return finalName;
	}

	/**
	 * Literals from filters do not always have the right class (i.e. integer instead of long). This function can cast
	 * those objects.
	 * 
	 * @param literal
	 *            The literal object that needs casting to the correct class.
	 * @param propertyName
	 *            The name of the property. Only by knowing what property we're talking about, can we derive it's class
	 *            from the Hibernate metadata.
	 * @return Always returns a value!
	 */
	private Object castLiteral(Object literal, String propertyName) {
		try {
			if (literal instanceof Collection) {
				return castCollection(literal, propertyName);
			}
			if (literal instanceof Object[]) {
				return castObjectArray(literal, propertyName);
			}
			Class<?> clazz = featureModel.getPropertyClass(featureModel.getEntityMetadata(), propertyName);
			if (!clazz.equals(literal.getClass())) {
				if (clazz.equals(Boolean.class)) {
					return Boolean.valueOf(literal.toString());
				} else if (clazz.equals(Date.class)) {
					dateFormat.parse(literal.toString());
				} else if (clazz.equals(Double.class)) {
					return Double.valueOf(literal.toString());
				} else if (clazz.equals(Float.class)) {
					return Float.valueOf(literal.toString());
				} else if (clazz.equals(Integer.class)) {
					return Integer.valueOf(literal.toString());
				} else if (clazz.equals(Long.class)) {
					return Long.valueOf(literal.toString());
				} else if (clazz.equals(Short.class)) {
					return Short.valueOf(literal.toString());
				} else if (clazz.equals(String.class)) {
					return literal.toString();
				}
			}
		} catch (Exception e) { // NOSONAR
			log.error(e.getMessage(), e);
		}
		return literal;
	}

	private Object castCollection(Object literal, String propertyName) {
		try {
			Collection<?> c = (Collection) literal;
			Iterator iterator = c.iterator();
			List<Object> cast = new ArrayList<Object>();
			while (iterator.hasNext()) {
				cast.add(castLiteral(iterator.next(), propertyName));
			}
			return cast;
		} catch (Exception e) { // NOSONAR
			log.error(e.getMessage(), e);
		}
		return literal;
	}

	private Object castObjectArray(Object literal, String propertyName) {
		try {
			Object[] array = (Object[]) literal;
			Object[] cast = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				cast[i] = castLiteral(array[i], propertyName);
			}
			return cast;
		} catch (Exception e) { // NOSONAR
			log.error(e.getMessage(), e);
		}
		return literal;
	}

	private Geometry asGeometry(Object geometry) {
		if (geometry instanceof Geometry) {
			Geometry geom = (Geometry) geometry;
			geom.setSRID(srid);
			return geom;
		} else {
			throw new IllegalStateException("Cannot handle " + geometry + " as geometry.");
		}
	}

}