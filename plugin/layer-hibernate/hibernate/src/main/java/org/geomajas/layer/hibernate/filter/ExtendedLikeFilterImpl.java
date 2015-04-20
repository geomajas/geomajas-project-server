/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.hibernate.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.filter.AbstractFilterImpl;
import org.geotools.filter.ExpressionType;
import org.geotools.filter.Filters;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LikeFilterImpl;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.MultiValuedFilter.MatchAction;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;

/**
 * ???
 *
 * @deprecated We should switch this class to opengis filters.
 * @author Pieter De Graef
 */
@Deprecated
public class ExtendedLikeFilterImpl extends AbstractFilterImpl implements PropertyIsLike {

	/** The attribute value, which must be an attribute expression. */
	private Expression attribute;

	/** The (limited) REGEXP pattern. */
	private String pattern;

	/** The single wildcard for the REGEXP pattern. */
	private String wildcardSingle = ".?";

	/** The multiple wildcard for the REGEXP pattern. */
	private String wildcardMulti = ".*";

	/** The escape sequence for the REGEXP pattern. */
	private String escape = "\\";

	/** The matcher to match patterns with. */
	private Matcher match;

	private boolean matchingCase;

	/**
	 * Given OGC PropertyIsLike Filter information, construct an SQL-compatible 'like' pattern.
	 * 
	 * SQL % --> match any number of characters _ --> match a single character
	 * 
	 * NOTE; the SQL command is 'string LIKE pattern [ESCAPE escape-character]' We could re-define the escape character,
	 * but I'm not doing to do that in this code since some databases will not handle this case.
	 * 
	 * Method: 1.
	 * 
	 * Examples: ( escape ='!', multi='*', single='.' ) broadway* -> 'broadway%' broad_ay -> 'broad_ay' broadway ->
	 * 'broadway'
	 * 
	 * broadway!* -> 'broadway*' (* has no significance and is escaped) can't -> 'can''t' ( ' escaped for SQL
	 * compliance)
	 * 
	 * 
	 * NOTE: we also handle "'" characters as special because they are end-of-string characters. SQL will convert ' to
	 * '' (double single quote).
	 * 
	 * NOTE: we don't handle "'" as a 'special' character because it would be too confusing to have a special char as
	 * another special char. Using this will throw an error (IllegalArgumentException).
	 * 
	 * @param escape escape character
	 * @param multi ?????
	 * @param single ?????
	 * @param pattern pattern to match
	 * @return SQL like sub-expression
	 * @throws IllegalArgumentException oops
	 */
	public static String convertToSQL92(char escape, char multi, char single, String pattern)
			throws IllegalArgumentException {
		if ((escape == '\'') || (multi == '\'') || (single == '\'')) {
			throw new IllegalArgumentException("do not use single quote (') as special char!");
		}
		
		StringBuilder result = new StringBuilder(pattern.length() + 5);
		int i = 0;
		while (i < pattern.length()) {
			char chr = pattern.charAt(i);
			if (chr == escape) {
				// emit the next char and skip it
				if (i != (pattern.length() - 1)) {
					result.append(pattern.charAt(i + 1));
				}
				i++; // skip next char
			} else if (chr == single) {
				result.append('_');
			} else if (chr == multi) {
				result.append('%');
			} else if (chr == '\'') {
				result.append('\'');
				result.append('\'');
			} else {
				result.append(chr);
			}
			i++;
		}

		return result.toString();
	}

	/**
	 * See convertToSQL92.
	 *
	 * @return SQL like sub-expression
	 * @throws IllegalArgumentException oops
	 */
	public String getSQL92LikePattern() throws IllegalArgumentException {
		if (escape.length() != 1) {
			throw new IllegalArgumentException("Like Pattern --> escape char should be of length exactly 1");
		}
		if (wildcardSingle.length() != 1) {
			throw new IllegalArgumentException("Like Pattern --> wildcardSingle char should be of length exactly 1");
		}
		if (wildcardMulti.length() != 1) {
			throw new IllegalArgumentException("Like Pattern --> wildcardMulti char should be of length exactly 1");
		}
		return LikeFilterImpl.convertToSQL92(escape.charAt(0), wildcardMulti.charAt(0), wildcardSingle.charAt(0),
				isMatchingCase(), pattern);
	}

	public void setWildCard(String wildCard) {
		this.wildcardMulti = wildCard;
		match = null;
	}

	public void setSingleChar(String singleChar) {
		this.wildcardSingle = singleChar;
		match = null;
	}

	public void setEscape(String escape) {
		this.escape = escape;
		match = null;
	}

	private Matcher getMatcher() {
		if (match == null) {
			// The following things happen for both wildcards:
			// (1) If a user-defined wildcard exists, replace with Java wildcard
			// (2) If a user-defined escape exists, Java wildcard + user-escape
			// Then, test for matching pattern and return result.
			char esc = escape.charAt(0);
			LOGGER.finer("wildcard " + wildcardMulti + " single " + wildcardSingle);
			LOGGER.finer("escape " + escape + " esc " + esc + " esc == \\ " + (esc == '\\'));

			String escapedWildcardMulti = fixSpecials(wildcardMulti);
			String escapedWildcardSingle = fixSpecials(wildcardSingle);

			// escape any special chars which are not our wildcards
			StringBuilder tmp = new StringBuilder();

			boolean escapedMode = false;

			int i = 0;
			while (i < pattern.length()) {
				char chr = pattern.charAt(i);
				LOGGER.finer("tmp = " + tmp + " looking at " + chr);

				if (pattern.regionMatches(false, i, escape, 0, escape.length())) {
					// skip the escape string
					LOGGER.finer("escape ");
					escapedMode = true;

					i += escape.length();
					chr = pattern.charAt(i);
				}

				if (pattern.regionMatches(false, i, wildcardMulti, 0, wildcardMulti.length())) { // replace
					// with
					// java
					// wildcard
					LOGGER.finer("multi wildcard");

					if (escapedMode) {
						LOGGER.finer("escaped ");
						tmp.append(escapedWildcardMulti);
					} else {
						tmp.append(".*");
					}

					i += wildcardMulti.length();
					escapedMode = false;
				} else if (pattern.regionMatches(false, i, wildcardSingle, 0, wildcardSingle.length())) {
					// replace with java single wild card
					LOGGER.finer("single wildcard");

					if (escapedMode) {
						LOGGER.finer("escaped ");
						tmp.append(escapedWildcardSingle);
					} else {
						// From the OpenGIS filter encoding spec,
						// "the single singleChar character matches exactly one character"
						tmp.append(".{1}");
					}

					i += wildcardSingle.length();
					escapedMode = false;
				} else if (isSpecial(chr)) {
					LOGGER.finer("special");
					tmp.append(this.escape);
					tmp.append(chr);
					escapedMode = false;
				} else {
					tmp.append(chr);
					escapedMode = false;
					i++;
				}
			}

			String finalPattern = tmp.toString();
			LOGGER.finer("final pattern " + finalPattern);
			Pattern compPattern = Pattern.compile(finalPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			match = compPattern.matcher("");
		}
		return match;
	}

	/**
	 * Constructor which flags the operator as like.
	 */
	protected ExtendedLikeFilterImpl() {
		super();
	}

	/**
	 * Set the expression to be evalutated as being like the pattern.
	 * 
	 * @param value
	 *            The value of the attribute for comparison.
	 * 
	 * @throws IllegalFilterException
	 *             Filter is illegal.
	 */
	public final void setValue(Expression value) throws IllegalFilterException {
		setExpression(value);
	}

	/**
	 * Get the Value (left hand side) of this filter.
	 * 
	 * @return The expression that is the value of the filter.
	 * 
	 * @deprecated use {@link #getExpression()}.
	 */
	@Deprecated
	public final Expression getValue() {
		return attribute;
	}

	/**
	 * Get the expression for hte filter.
	 * <p>
	 * This method calls th deprecated {@link #getValue()} for backwards compatability with subclasses.
	 * </p>
	 */
	public org.opengis.filter.expression.Expression getExpression() {
		return getValue();
	}

	public void setExpression(org.opengis.filter.expression.Expression e) {
		if (!(e instanceof Expression)) {
			throw new IllegalArgumentException("Expression " + e + " should be an Expression.");
		}
		Expression exprAttribute = (Expression) e;
		if ((Filters.getExpressionType(exprAttribute) != ExpressionType.ATTRIBUTE_STRING)) {
			this.attribute = exprAttribute;
		} else {
			throw new IllegalFilterException("Attempted to add something other than a string attribute "
					+ "expression to a like filter.");
		}
	}

	/**
	 * Set the match pattern for this FilterLike.
	 * 
	 * @param p
	 *            the expression which evaluates to the match pattern for this filter
	 * @param wildcardMultiChar
	 *            the string that represents a multiple character (1->n) wildcard
	 * @param wildcardOneChar
	 *            the string that represents a single character (1) wildcard
	 * @param escapeString
	 *            the string that represents an escape character
	 * 
	 * @deprecated use one of {@link org.opengis.filter.PropertyIsLike#setExpression(Expression)}
	 *             {@link org.opengis.filter.PropertyIsLike#setWildCard(String)}
	 *             {@link org.opengis.filter.PropertyIsLike#setSingleChar(String)}
	 *             {@link org.opengis.filter.PropertyIsLike#setEscape(String)}
	 */
	@Deprecated
	public final void setPattern(Expression p, String wildcardMultiChar, String wildcardOneChar, String escapeString) {
		setPattern(p.toString(), wildcardMultiChar, wildcardOneChar, escapeString);
	}

	/**
	 * Set the match pattern for this FilterLike.
	 * 
	 * @param matchPattern
	 *            the string which contains the match pattern for this filter
	 * @param wildcardMultiChar
	 *            the string that represents a multiple character (1->n) wildcard
	 * @param wildcardOneChar
	 *            the string that represents a single character (1) wildcard
	 * @param escapeString
	 *            the string that represents an escape character
	 * 
	 * @deprecated use one of {@link org.opengis.filter.PropertyIsLike#setLiteral(String)}
	 *             {@link org.opengis.filter.PropertyIsLike#setWildCard(String)}
	 *             {@link org.opengis.filter.PropertyIsLike#setSingleChar(String)}
	 *             {@link org.opengis.filter.PropertyIsLike#setEscape(String)}
	 */
	@Deprecated
	public final void setPattern(String matchPattern, String wildcardMultiChar, String wildcardOneChar,
			String escapeString) {
		setLiteral(matchPattern);
		setWildCard(wildcardMultiChar);
		setSingleChar(wildcardOneChar);
		setEscape(escapeString);
	}

	/**
	 * Accessor method to retrieve the pattern.
	 * 
	 * @return the pattern being matched.
	 * 
	 * @deprecated use {@link #getLiteral()}
	 */
	@Deprecated
	public final String getPattern() {
		return getLiteral();
	}

	/**
	 * Return the pattern.
	 */
	public String getLiteral() {
		return this.pattern;
	}

	/**
	 * Set the pattern.
	 *
	 * @param literal pattern
	 */
	public void setLiteral(String literal) {
		this.pattern = literal;
		match = null;
	}

	/**
	 * Determines whether or not a given feature matches this pattern.
	 * 
	 * @param feature
	 *            Specified feature to examine.
	 * 
	 * @return Flag confirming whether or not this feature is inside the filter.
	 */
	public boolean evaluate(Object feature) {
		// Checks to ensure that the attribute has been set
		if (attribute == null) {
			return false;
		}
		// Note that this converts the attribute to a string
		// for comparison. Unlike the math or geometry filters, which
		// require specific types to function correctly, this filter
		// using the mandatory string representation in Java
		// Of course, this does not guarantee a meaningful result, but it
		// does guarantee a valid result.
		// LOGGER.finest("pattern: " + pattern);
		// LOGGER.finest("string: " + attribute.getValue(feature));
		// return attribute.getValue(feature).toString().matches(pattern);
		Object value = attribute.evaluate(feature);

		if (null == value) {
			return false;
		}

		Matcher matcher = getMatcher();
		matcher.reset(value.toString());

		return matcher.matches();
	}

	/**
	 * Return this filter as a string.
	 * 
	 * @return String representation of this like filter.
	 */
	public String toString() {
		return "[ " + attribute.toString() + " like '" + pattern + "' ]";
	}

	/**
	 * Getter for property escape.
	 * 
	 * @return Value of property escape.
	 */
	public java.lang.String getEscape() {
		return escape;
	}

	/**
	 * Getter for property wildcardMulti.
	 * 
	 * @return Value of property wildcardMulti.
	 * 
	 * @deprecated use {@link #getWildCard()}.
	 */
	@Deprecated
	public final String getWildcardMulti() {
		return wildcardMulti;
	}

	/**
	 * <p>
	 * THis method calls {@link #getWildcardMulti()} for subclass backwards compatibility.
	 * </p>
	 * 
	 * @see org.opengis.filter.PropertyIsLike#getWildCard().
	 */
	public String getWildCard() {
		return getWildcardMulti();
	}

	/**
	 * Getter for property wildcardSingle.
	 * 
	 * @return Value of property wildcardSingle.
	 * 
	 * @deprecated use {@link #getSingleChar()}
	 */
	@Deprecated
	public final String getWildcardSingle() {
		return wildcardSingle;
	}

	/**
	 * <p>
	 * THis method calls {@link #getWildcardSingle()()} for subclass backwards compatibility.
	 * </p>
	 * 
	 * @see org.opengis.filter.PropertyIsLike#getSingleChar()().
	 */
	public String getSingleChar() {
		return getWildcardSingle();
	}

	/**
	 * Convenience method to determine if a character is special to the regex system.
	 * 
	 * @param chr
	 *            the character to test
	 * 
	 * @return is the character a special character.
	 */
	private boolean isSpecial(final char chr) {
		return ((chr == '.') || (chr == '?') || (chr == '*') || (chr == '^') || (chr == '$') || (chr == '+')
				|| (chr == '[') || (chr == ']') || (chr == '(') || (chr == ')') || (chr == '|') || (chr == '\\')
				|| (chr == '&'));
	}

	/**
	 * Convenience method to escape any character that is special to the regex system.
	 * 
	 * @param inString
	 *            the string to fix
	 * 
	 * @return the fixed string
	 */
	private String fixSpecials(final String inString) {
		StringBuilder tmp = new StringBuilder();

		for (int i = 0; i < inString.length(); i++) {
			char chr = inString.charAt(i);

			if (isSpecial(chr)) {
				tmp.append(this.escape);
				tmp.append(chr);
			} else {
				tmp.append(chr);
			}
		}

		return tmp.toString();
	}

	/**
	 * Compares this filter to the specified object. Returns true if the passed in object is the same as this filter.
	 * Checks to make sure the filter types, the value, and the pattern are the same.
	 * 
	 * @param obj
	 *            - the object to compare this LikeFilter against.
	 * 
	 * @return true if specified object is equal to this filter; false otherwise.
	 */
	public boolean equals(Object obj) {
		if (obj instanceof LikeFilterImpl) {
			LikeFilterImpl lFilter = (LikeFilterImpl) obj;

			// REVISIT: check for nulls.
			return ((Filters.getFilterType(lFilter) == Filters.getFilterType(this))
					&& lFilter.getExpression().equals(this.attribute) && lFilter.getPattern().equals(this.pattern));
		}
		return false;
	}

	/**
	 * Override of hashCode method.
	 * 
	 * @return the hash code for this like filter implementation.
	 */
	public int hashCode() {
		int result = 17;
		result = (37 * result) + ((attribute == null) ? 0 : attribute.hashCode());
		result = (37 * result) + ((pattern == null) ? 0 : pattern.hashCode());

		return result;
	}

	/**
	 * Used by FilterVisitors to perform some action on this filter instance. Typically used by Filter decoders, but
	 * may also be used by any thing which needs information from filter structure. Implementations should always call:
	 * visitor.visit(this); It is important that this is not left to a parent class unless the parents API is
	 * identical.
	 * 
	 * @param visitor
	 *            The visitor which requires access to this filter, the method must call visitor.visit(this);
	 */
	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit(this, extraData);
	}

	public boolean isMatchingCase() {
		return matchingCase;
	}

	public void setMatchingCase(boolean matchingCase) {
		this.matchingCase = matchingCase;
	}

	public MatchAction getMatchAction() {
		throw new UnsupportedOperationException("getMatchAction()");
	}
}