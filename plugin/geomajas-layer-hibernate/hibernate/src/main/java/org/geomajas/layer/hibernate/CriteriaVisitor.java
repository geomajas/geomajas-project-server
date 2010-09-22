/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.layer.hibernate;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.geomajas.layer.LayerException;
import org.geotools.filter.LiteralExpressionImpl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
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

	private final Logger log = LoggerFactory.getLogger(CriteriaVisitor.class);

	/**
	 * Name of the geometry attribute. Stored here as a shortcut.
	 */
	private String geomName;

	/**
	 * The srid of the coordinate reference system used in the HibernateLayer. Stored here as a shortcut.
	 */
	private int srid;

	/**
	 * The HibernateFeatureModel that contains all Hibernate metadata. This is needed when creating criteria.
	 */
	private HibernateFeatureModel featureModel;

	private DateFormat dateFormat;

	/**
	 * List of aliases created when converting an OpenGis filter to a Hibernate criteria object.
	 */
	private List<String> aliases; // These never get cleaned!

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * The only constructor. A CriteriaVisitor object can only create criteria for one specific FeatureModel. This is
	 * because it always needs the Hibernate meta data that the FeatureModel stores.
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
		aliases = new ArrayList<String>();
	}

	// -------------------------------------------------------------------------
	// FilterVisitor implementation:
	// -------------------------------------------------------------------------

	public Object visit(And filter, Object userData) {
		Criterion c = null;
		Iterator<?> iter = filter.getChildren().iterator();
		while (iter.hasNext()) {
			Filter element = (Filter) iter.next();
			if (c == null) {
				c = (Criterion) element.accept(this, userData);
			} else {
				c = Restrictions.and(c, (Criterion) element.accept(this, userData));
			}
		}
		return c;
	}

	public Object visit(Not filter, Object userData) {
		Criterion c = (Criterion) filter.getFilter().accept(this, userData);
		return Restrictions.not(c);
	}

	public Object visit(Or filter, Object userData) {
		Criterion c = null;
		Iterator<?> iter = filter.getChildren().iterator();
		while (iter.hasNext()) {
			Filter element = (Filter) iter.next();
			if (c == null) {
				c = (Criterion) element.accept(this, userData);
			} else {
				c = Restrictions.or(c, (Criterion) element.accept(this, userData));
			}
		}
		return c;
	}

	public Object visit(PropertyIsBetween filter, Object userData) {
		String propertyName = ((PropertyName) filter.getExpression()).getPropertyName();
		String finalName = parsePropertyName(propertyName, userData);

		Object lo = castLiteral(((Literal) filter.getLowerBoundary()).getValue(), propertyName);
		Object hi = castLiteral(((Literal) filter.getUpperBoundary()).getValue(), propertyName);
		return Restrictions.between(finalName, lo, hi);
	}

	public Object visit(PropertyIsEqualTo filter, Object userData) {
		String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
		String finalName = parsePropertyName(propertyName, userData);

		Object value = castLiteral(((Literal) filter.getExpression2()).getValue(), propertyName);
		return Restrictions.eq(finalName, value);
	}

	public Object visit(PropertyIsNotEqualTo filter, Object userData) {
		String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
		String finalName = parsePropertyName(propertyName, userData);

		Object value = castLiteral(((Literal) filter.getExpression2()).getValue(), propertyName);
		return Restrictions.ne(finalName, value);
	}

	public Object visit(PropertyIsGreaterThan filter, Object userData) {
		String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
		String finalName = parsePropertyName(propertyName, userData);

		Object literal = ((Literal) filter.getExpression2()).getValue();
		return Restrictions.gt(finalName, castLiteral(literal, propertyName));
	}

	public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object userData) {
		String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
		String finalName = parsePropertyName(propertyName, userData);

		Object literal = ((Literal) filter.getExpression2()).getValue();
		return Restrictions.ge(finalName, castLiteral(literal, propertyName));
	}

	public Object visit(PropertyIsLessThan filter, Object userData) {
		String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
		String finalName = parsePropertyName(propertyName, userData);

		Object literal = ((Literal) filter.getExpression2()).getValue();
		return Restrictions.lt(finalName, castLiteral(literal, propertyName));
	}

	public Object visit(PropertyIsLessThanOrEqualTo filter, Object userData) {
		String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
		String finalName = parsePropertyName(propertyName, userData);

		Object literal = ((Literal) filter.getExpression2()).getValue();
		return Restrictions.le(finalName, castLiteral(literal, propertyName));
	}

	public Object visit(PropertyIsLike filter, Object userData) {
		String propertyName = ((PropertyName) filter.getExpression()).getPropertyName();
		String finalName = parsePropertyName(propertyName, userData);

		String value = filter.getLiteral();
		value = value.replaceAll("\\*", "%");
		value = value.replaceAll("\\?", "_");
		return Restrictions.like(finalName, value);
	}

	public Object visit(PropertyIsNull filter, Object userData) {
		throw new UnsupportedOperationException("visit(PropertyIsNull filter, Object userData)");
	}

	public Object visit(BBOX filter, Object userData) {
		Envelope env = new Envelope(filter.getMinX(), filter.getMaxX(), filter.getMinY(), filter.getMaxY());
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.filter(finalName, env, srid);
	}

	public Object visit(Beyond filter, Object userData) {
		throw new UnsupportedOperationException("visit(Beyond filter, Object userData)");
	}

	public Object visit(Contains filter, Object userData) {
		throw new UnsupportedOperationException("visit(Contains filter, Object userData)");
	}

	public Object visit(Crosses filter, Object userData) {
		throw new UnsupportedOperationException("visit(Crosses filter, Object userData)");
	}

	public Object visit(Disjoint filter, Object userData) {
		throw new UnsupportedOperationException("visit(Disjoint filter, Object userData)");
	}

	public Object visit(DWithin filter, Object userData) {
		throw new UnsupportedOperationException("visit(DWithin filter, Object userData)");
	}

	public Object visit(Equals filter, Object userData) {
		throw new UnsupportedOperationException("visit(Equals filter, Object userData)");
	}

	public Object visit(Intersects filter, Object userData) {
		LiteralExpressionImpl exp = (LiteralExpressionImpl) filter.getExpression2();
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.intersects(finalName, asGeometry(exp.getValue()));
	}

	public Object visit(Overlaps filter, Object userData) {
		LiteralExpressionImpl exp = (LiteralExpressionImpl) filter.getExpression2();
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.overlaps(finalName, asGeometry(exp.getValue()));
	}

	public Object visit(Touches filter, Object userData) {
		LiteralExpressionImpl exp = (LiteralExpressionImpl) filter.getExpression2();
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.touches(finalName, asGeometry(exp.getValue()));
	}

	public Object visit(Within filter, Object userData) {
		LiteralExpressionImpl exp = (LiteralExpressionImpl) filter.getExpression2();
		String finalName = parsePropertyName(geomName, userData);
		return SpatialRestrictions.within(finalName, asGeometry(exp.getValue()));
	}

	public Object visit(ExcludeFilter filter, Object userData) {
		throw new UnsupportedOperationException("visit(ExcludeFilter filter, Object userData)");
	}

	public Object visit(IncludeFilter filter, Object userData) {
		return userData;
	}

	public Object visit(Id filter, Object userData) {
		String idName;
		try {
			idName = featureModel.getEntityMetadata().getIdentifierPropertyName();
		} catch (LayerException e) {
			log.warn("Cannot read idName, defaulting to 'id'", e);
			idName = "id";
		}
		Collection<?> c = (Collection<?>) castLiteral(filter.getIdentifiers(), idName);
		return Restrictions.in(idName, c);
	}

	public Object visitNullFilter(Object userData) {
		throw new UnsupportedOperationException("visit(Object userData)");
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Go through the property name to see if it is a complex one. If it is, aliases must be declared.
	 * 
	 * @param propertyName
	 *            The propertyName. Can be complex.
	 * @param userData
	 *            The userData object that is passed in each method of the FilterVisitor. Should always be of the info
	 *            "Criteria".
	 */
	private String parsePropertyName(String propertyName, Object userData) {
		// try to assure the correct separator is used
		propertyName = propertyName.replace(HibernateLayerUtil.XPATH_SEPARATOR, HibernateLayerUtil.SEPARATOR);

		// split the path (separator is defined in the HibernateLayerUtil)
		String[] props = propertyName.split(HibernateLayerUtil.SEPARATOR_REGEXP);
		String finalName = null;
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
			if (literal instanceof Collection<?>) {
				Collection<?> c = (Collection<?>) literal;
				Iterator<?> iterator = c.iterator();
				List<Object> cast = new ArrayList<Object>();
				while (iterator.hasNext()) {
					cast.add(castLiteral(iterator.next(), propertyName));
				}
				return cast;
			}
			if (literal instanceof Object[]) {
				Object[] array = (Object[]) literal;
				Object[] cast = new Object[array.length];
				for (int i = 0; i < array.length; i++) {
					cast[i] = castLiteral(array[i], propertyName);
				}
				return cast;
			}
			Class<?> clazz = featureModel.getPropertyClass(featureModel.getEntityMetadata(), propertyName);
			if (!clazz.equals(literal.getClass())) {
				if (clazz.equals(Boolean.class)) {
					return Boolean.valueOf(literal.toString());
				} else if (clazz.equals(Date.class)) {
					try {
						dateFormat.parse(literal.toString());
					} catch (ParseException e) {
						return literal.toString();
					}
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
		} catch (LayerException e) {
			log.error(e.getMessage(), e);
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
		}

		return literal;
	}
	
	private Geometry asGeometry(Object geometry) {
		if (null != geometry && geometry instanceof Geometry) {
			Geometry geom = (Geometry) geometry;
			geom.setSRID(srid);
			return geom;
		} else {
			return null;
		}
	}

}