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

package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;

/**
 * Class containing convenience methods for building Strings,
 * containing HTML opening tags with CSS, content and closing tags.
 *
 * @author Emiel Ackermann
 *
 * @since 1.10.0
 */
@Api(allMethods = true)
public final class HtmlBuilder {
	/**
	 * Instantiation not allowed.
	 */
	private HtmlBuilder() {
		// do not allow instantiation.
	}
	/**
	 * Build a HTML Table with given CSS class for a string.
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param clazz class for table element
	 * @param content content string
	 * @return HTML table element as string
	 */
	public static String tableClassHtmlContent(String clazz, String... content) {
		return tagClass(Html.Tag.TABLE, clazz, true, content);
	}
	
	/**
	 * Build a HTML Table with given CSS style for a string. 
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param style style for table element
	 * @param content content string
	 * @return HTML table element as string
	 */
	public static String tableStyleHtmlContent(String style, String... content) {
		return tagStyle(Html.Tag.TABLE, style, true, content);
	}
	
	/**
	 * Build a HTML Table with given CSS class for a string. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param clazz class for table element
	 * @param content content string
	 * @return HTML table element as string
	 */
	public static String tableClass(String clazz, String... content) {
		return tagClass(Html.Tag.TABLE, clazz, false, content);
	}
	
	/**
	 * Build a HTML Table with given CSS style for a string. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param style style for table element
	 * @param content content string
	 * @return HTML table element as string
	 */
	public static String tableStyle(String style, String... content) {
		return tagStyle(Html.Tag.TABLE, style, false, content);
	}
	
	/**
	 * Build a HTML TableRow with no CSS of its own. 
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param content content string
	 * @return HTML tr element as string
	 */
	public static String trHtmlContent(String... content) {
		return tag(Html.Tag.TR, true, content);
	}
	
	/**
	 * Build a HTML TableRow with no CSS of its own. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param content content string
	 * @return HTML tr element as string
	 */
	public static String tr(String... content) {
		return tag(Html.Tag.TR, false, content);
	}
	
	/**
	 * Build a HTML TableRow with given CSS class for a string. 
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param clazz class for tr element
	 * @param content content string
	 * @return HTML tr element as string
	 */
	public static String trClassHtmlContent(String clazz, String... content) {
		return tagClass(Html.Tag.TR, clazz, true, content);
	}
	
	/**
	 * Build a HTML TableRow with given CSS style attributes for a string. 
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param style style for tr element
	 * @param content content string
	 * @return HTML tr element as string
	 */
	public static String trStyleHtmlContent(String style, String... content) {
		return tagStyle(Html.Tag.TR, style, true, content);
	}
	
	/**
	 * Build a HTML TableRow with given CSS class for a string. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param clazz class for tr element
	 * @param content content string
	 * @return HTML tr element as string
	 */
	public static String trClass(String clazz, String... content) {
		return tagClass(Html.Tag.TR, clazz, false, content);
	}
	
	/**
	 * Build a HTML TableRow with given CSS style attributes for a string. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param style style for tr element
	 * @param content content string
	 * @return HTML tr element as string
	 */
	public static String trStyle(String style, String... content) {
		return tagStyle(Html.Tag.TR, style, false, content);
	}
	
	/**
	 * Build a HTML TableRow with given CSS class for a string. 
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param clazz class for tr element
	 * @param content content string
	 * @return HTML tr element as string
	 */
	public static String tdClassHtmlContent(String clazz, String... content) {
		return tagClass(Html.Tag.TD, clazz, true, content);
	}
	
	/**
	 * Build a HTML TableData with given CSS style attributes for a string. 
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param style style for td element
	 * @param content content string
	 * @return HTML td element as string
	 */
	public static String tdStyleHtmlContent(String style, String... content) {
		return tagStyle(Html.Tag.TD, style, true, content);
	}
	
	/**
	 * Build a HTML TableRow with given CSS class for a string. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param clazz class for tr element
	 * @param content content string
	 * @return HTML tr element as string
	 */
	public static String tdClass(String clazz, String... content) {
		return tagClass(Html.Tag.TD, clazz, false, content);
	}
	
	/**
	 * Build a HTML TableData with given CSS style attributes for a string. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param style style for td element
	 * @param content content string
	 * @return HTML td element as string
	 */
	public static String tdStyle(String style, String... content) {
		return tagStyle(Html.Tag.TD, style, false, content);
	}
	
	/**
	 * Build a HTML DIV with given style for a string. 
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param style style for div (plain CSS)
	 * @param content content string
	 * @return HTML DIV element as string
	 */
	public static String divStyleHtmlContent(String style, String... content) {
		return tagStyle(Html.Tag.DIV, style, true, content);
	}
	/**
	 * Build a HTML DIV with given CSS class for a string. 
	 * Use this method if given content contains HTML snippets, prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param clazz class for div element
	 * @param contentIsHtml if false content is prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 * @param content content string
	 * @return HTML DIV element as string
	 */
	public static String divClassHtmlContent(String clazz, String... content) {
		return tagClass(Html.Tag.DIV, clazz, true, content);
	}
	
	/**
	 * Build a HTML DIV with given style for a string. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param style style for div (plain CSS)
	 * @param content content string
	 * @return HTML DIV element as string
	 */
	public static String divStyle(String style, String... content) {
		return tagStyle(Html.Tag.DIV, style, false, content);
	}
	/**
	 * Build a HTML DIV with given CSS class for a string. 
	 * Given content does <b>not</b> consists of HTML snippets and 
	 * as such is being prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 *
	 * @param clazz class for div element
	 * @param contentIsHtml if false content is prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 * @param content content string
	 * @return HTML DIV element as string
	 */
	public static String divClass(String clazz, String... content) {
		return tagClass(Html.Tag.DIV, clazz, false, content);
	}
	
	//Core methods.
	
	/**
	 * Build a String containing a HTML opening tag with no styling of its own, content and closing tag.
	 *
	 * @param tag String name of HTML tag
	 * @param contentIsHtml if false content is prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 * @param content content string
	 * @return HTML tag element as string
	 */
	public static String tag(String tag, boolean contentIsHtml, String... content) {
		return openTag(tag, null, null, contentIsHtml, content) + closeTag(tag);
	}
	
	/**
	 * Build a String containing a HTML opening tag with given CSS style attribute(s), content and closing tag.
	 *
	 * @param tag String name of HTML tag
	 * @param style style for tag (plain CSS)
	 * @param contentIsHtml if false content is prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 * @param content content string
	 * @return HTML tag element as string
	 */
	public static String tagStyle(String tag, String style, boolean contentIsHtml, String... content) {
		return openTagStyle(tag, style, contentIsHtml, content) + closeTag(tag);
	}
	
	/**
	 * Build a String containing a HTML opening tag with given CSS class, content and closing tag.
	 *
	 * @param tag String name of HTML tag
	 * @param clazz CSS class of the tag
	 * @param content content string
	 * @param contentIsHtml if false content is prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 * @return HTML tag element as string
	 */
	public static String tagClass(String tag, String clazz, boolean contentIsHtml, String... content) {
		return openTagClass(tag, clazz, contentIsHtml, content) + closeTag(tag);
	}
	
	/**
	 * Build a String containing a HTML opening tag with given CSS style attribute(s) and concats the given content. 
	 * Content is prepared with {@link WidgetLayout#htmlEncode(String)}.
	 *
	 * @param tag String name of HTML tag
	 * @param style style for tag (plain CSS)
	 * @param contentIsHtml if false content is prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 * @param content content string
	 * @return HTML tag element as string
	 */
	public static String openTagStyle(String tag, String style, boolean contentIsHtml, String... content) {
		return openTag(tag, null, style, contentIsHtml, content);
	}
	
	/**
	 * Build a String containing a HTML opening tag with given CSS class and concats the given content. 
	 * Content is prepared with {@link WidgetLayout#htmlEncode(String)}.
	 *
	 * @param tag String name of HTML tag
	 * @param clazz CSS class of the tag
	 * @param contentIsHtml if false content is prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 * @param content content string
	 * @return HTML tag element as string
	 */
	public static String openTagClass(String tag, String clazz, boolean contentIsHtml, String... content) {
		return openTag(tag, clazz, null, contentIsHtml, content);
	}
	
	/**
	 * Build a String containing a HTML opening tag with given CSS class and/or style and concats the given content. 
	 *
	 * @param tag String name of HTML tag
	 * @param clazz CSS class of the tag
	 * @param style style for tag (plain CSS)
	 * @param contentIsHtml if false content is prepared with {@link HtmlBuilder#htmlEncode(String...)}.
	 * @param content content string
	 * @return HTML tag element as string
	 */
	public static String openTag(String tag, String clazz, String style, boolean contentIsHtml, String... content) {
		StringBuilder result = new StringBuilder("<" + tag);
		if (clazz != null && !"".equals(clazz)) {
			result.append(" class='" + clazz + "'");
		}
		if (style != null && !"".equals(style)) {
			result.append(" style='" + style + "'");
		}
		result.append(">");
		if (content != null && content.length > 0) {
			for (String c : content) {
				if (contentIsHtml) {
					result.append(c);
				} else {
					result.append(htmlEncode(c));
				}
			}
		}
		return result.toString();
	}
	

	/**
	 * Replaces & into {@literal &amp;}, < into {@literal &lt;} and > into {@literal &gt;}.
	 * @param content
	 * @return the encoded content
	 */
	public static String htmlEncode(String content) {
		if (null == content) {
			return "";
		}
		return content.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
	
	/**
	 * Build a HTML closing tag for the given tag name string.
	 *
	 * @param tag String name of HTML tag
	 * @return HTML tag element as string
	 */
	public static String closeTag(String tag) {
		return "</" + tag + ">";
	}
}
