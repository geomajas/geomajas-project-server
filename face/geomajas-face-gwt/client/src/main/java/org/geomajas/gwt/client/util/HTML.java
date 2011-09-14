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
 * Contains Strings of all used HTML tags and attributes.
 *
 * @author redlab_b
 * @author Emiel Ackermann
 *
 * @since 1.10.0
 */
@Api
public interface Html {

	/**
	 *
	 * All Tags used in HTML.
	 *
	 */
	public interface Tag {

		String XML = "xml";
		String THEAD = "thead";
		String TBODY = "thead";
		String TFOOT = "tfoot";
		String OL = "ol";
		String UL = "ul";
		String CAPTION = "caption";
		String PRE = "pre";
		String P = "p";
		String DIV = "div";
		String H1 = "h1";
		String H2 = "h2";
		String H3 = "h3";
		String H4 = "h4";
		String H5 = "h5";
		String H6 = "h6";
		String TD = "td";
		String BR = "br";
		String LI = "li";
		String DD = "dd";
		String DT = "dt";
		String TH = "th";
		String HR = "hr";
		String BODY = "body";
		String HTML = "html";
		String TABLE = "table";
		String SCRIPT = "script";
		String HEAD = "head";
		String LINK = "link";
		String META = "meta";
		String STYLE = "style";
		String ADDRESS = "address";
		String ARTICLE = "article";
		String ASIDE = "aside";
		String AUDIO = "audio";
		String BLOCKQUOTE = "blockquote";
		String CANVAS = "canvas";
		String FIELDSET = "fieldset";
		String FIGCAPTION = "figcaption";
		String FIGURE = "figure";
		String FOOTER = "footer";
		String FORM = "form";
		String HEADER = "header";
		String HGROUP = "hgroup";
		String NOSCRIPT = "noscript";
		String OUTPUT = "output";
		String SECTION = "section";
		String VIDEO = "video";
		String BASE = "base";
		String COMMAND = "command";
		String TITLE = "title";
		String A = "a";
		String ABBR = "abbr";
		String B = "b";
		String BDO = "bdo";
		String BUTTON = "button";
		String DETAILS = "details";
		String CODE = "code";
		String DEL = "del";
		String DATALIST = "datalist";
		String DFN = "dfn";
		String EMBED = "embed";
		String CITE = "cite";
		String DL = "dl";
		String EM = "em";
		String I = "i";
		String IFRAME = "iframe";
		String INPUT = "input";
		String IMG = "img";
		String INS = "ins";
		String MAP = "map";
		String KEYGEN = "keygen";
		String METER = "meter";
		String MENU = "menu";
		String NAV = "nav";
		String KBD = "kbd";
		String MATH = "math";
		String MARK = "mark";
		String LABEL = "label";
		String Q = "q";
		String SAMP = "samp";
		String PROGRESS = "progress";
		String RUBY = "ruby";
		String OBJECT = "object";
		String SMALL = "small";
		String SUB = "sub";
		String SUP = "sup";
		String STRONG = "strong";
		String SELECT = "select";
		String SPAN = "span";
		String SVG = "svg";
		String WBR = "wbr";
		String TIME = "time";
		String TEXTAREA = "textarea";
		String VAR = "var";
		String TR = "tr";
		String BIG = "big";
		String S = "s";
		String STRIKE = "strike";
		String TT = "tt";
		String U = "u";

	}

	/**
	 * Attributes used in HTML tags.
	 */
	public interface Attribute {

		String CELLPADDING = "cellpadding";
		String CELLSPACING = "cellspacing";
		String STYLE = "style";
		String CLASS = "class";
		String ID = "id";
		String HREF = "href";
		String NAME = "name";
		String SRC = "src";
		String WIDTH = "width";
		String HEIGHT = "height";
		String TYPE = "type";
		String COLSPAN = "colspan";
		String ROWSPAN = "rowspan";
	}
}
