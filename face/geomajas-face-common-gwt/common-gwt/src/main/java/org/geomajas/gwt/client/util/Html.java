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

package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;


/**
 * Contains Strings of all used HTML tags and attributes.
 *
 * @author Balder Van Camp
 * @author Emiel Ackermann
 *
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface Html {

	/**
	 * All Tags used in HTML.
	 *
	 * @author Emiel Ackermann
	 */
	interface Tag {
		/**
		 * Name of the xml tag.
		 */
		String XML = "xml";
		/**
		 * Name of the thead tag.
		 */
		String THEAD = "thead";
		/**
		 * Name of the tbody tag.
		 */
		String TBODY = "tbody";
		/**
		 * Name of the tfoot tag.
		 */
		String TFOOT = "tfoot";
		/**
		 * Name of the ol tag.
		 */
		String OL = "ol";
		/**
		 * Name of the ul tag.
		 */
		String UL = "ul";
		/**
		 * Name of the caption tag.
		 */
		String CAPTION = "caption";
		/**
		 * Name of the pre tag.
		 */
		String PRE = "pre";
		/**
		 * Name of the p tag.
		 */
		String P = "p";
		/**
		 * Name of the div tag.
		 */
		String DIV = "div";
		/**
		 * Name of the h1 tag.
		 */
		String H1 = "h1";
		/**
		 * Name of the h2 tag.
		 */
		String H2 = "h2";
		/**
		 * Name of the h3 tag.
		 */
		String H3 = "h3";
		/**
		 * Name of the h4 tag.
		 */
		String H4 = "h4";
		/**
		 * Name of the h5 tag.
		 */
		String H5 = "h5";
		/**
		 * Name of the h6 tag.
		 */
		String H6 = "h6";
		/**
		 * Name of the td tag.
		 */
		String TD = "td";
		/**
		 * Name of the br tag.
		 */
		String BR = "br";
		/**
		 * Name of the li tag.
		 */
		String LI = "li";
		/**
		 * Name of the dd tag.
		 */
		String DD = "dd";
		/**
		 * Name of the dt tag.
		 */
		String DT = "dt";
		/**
		 * Name of the th tag.
		 */
		String TH = "th";
		/**
		 * Name of the hr tag.
		 */
		String HR = "hr";
		/**
		 * Name of the body tag.
		 */
		String BODY = "body";
		/**
		 * Name of the html tag.
		 */
		String HTML = "html";
		/**
		 * Name of the table tag.
		 */
		String TABLE = "table";
		/**
		 * Name of the script tag.
		 */
		String SCRIPT = "script";
		/**
		 * Name of the head tag.
		 */
		String HEAD = "head";
		/**
		 * Name of the link tag.
		 */
		String LINK = "link";
		/**
		 * Name of the meta tag.
		 */
		String META = "meta";
		/**
		 * Name of the style tag.
		 */
		String STYLE = "style";
		/**
		 * Name of the address tag.
		 */
		String ADDRESS = "address";
		/**
		 * Name of the article tag.
		 */
		String ARTICLE = "article";
		/**
		 * Name of the aside tag.
		 */
		String ASIDE = "aside";
		/**
		 * Name of the audio tag.
		 */
		String AUDIO = "audio";
		/**
		 * Name of the blockquote tag.
		 */
		String BLOCKQUOTE = "blockquote";
		/**
		 * Name of the canvas tag.
		 */
		String CANVAS = "canvas";
		/**
		 * Name of the fieldset tag.
		 */
		String FIELDSET = "fieldset";
		/**
		 * Name of the figcaption tag.
		 */
		String FIGCAPTION = "figcaption";
		/**
		 * Name of the figure tag.
		 */
		String FIGURE = "figure";
		/**
		 * Name of the footer tag.
		 */
		String FOOTER = "footer";
		/**
		 * Name of the form tag.
		 */
		String FORM = "form";
		/**
		 * Name of the header tag.
		 */
		String HEADER = "header";
		/**
		 * Name of the hgroup tag.
		 */
		String HGROUP = "hgroup";
		/**
		 * Name of the noscript tag.
		 */
		String NOSCRIPT = "noscript";
		/**
		 * Name of the output tag.
		 */
		String OUTPUT = "output";
		/**
		 * Name of the section tag.
		 */
		String SECTION = "section";
		/**
		 * Name of the video tag.
		 */
		String VIDEO = "video";
		/**
		 * Name of the base tag.
		 */
		String BASE = "base";
		/**
		 * Name of the command tag.
		 */
		String COMMAND = "command";
		/**
		 * Name of the title tag.
		 */
		String TITLE = "title";
		/**
		 * Name of the a tag.
		 */
		String A = "a";
		/**
		 * Name of the abbr tag.
		 */
		String ABBR = "abbr";
		/**
		 * Name of the b tag.
		 */
		String B = "b";
		/**
		 * Name of the bdo tag.
		 */
		String BDO = "bdo";
		/**
		 * Name of the button tag.
		 */
		String BUTTON = "button";
		/**
		 * Name of the details tag.
		 */
		String DETAILS = "details";
		/**
		 * Name of the code tag.
		 */
		String CODE = "code";
		/**
		 * Name of the del tag.
		 */
		String DEL = "del";
		/**
		 * Name of the datalist tag.
		 */
		String DATALIST = "datalist";
		/**
		 * Name of the dfn tag.
		 */
		String DFN = "dfn";
		/**
		 * Name of the embed tag.
		 */
		String EMBED = "embed";
		/**
		 * Name of the cite tag.
		 */
		String CITE = "cite";
		/**
		 * Name of the dl tag.
		 */
		String DL = "dl";
		/**
		 * Name of the em tag.
		 */
		String EM = "em";
		/**
		 * Name of the i tag.
		 */
		String I = "i";
		/**
		 * Name of the iframe tag.
		 */
		String IFRAME = "iframe";
		/**
		 * Name of the input tag.
		 */
		String INPUT = "input";
		/**
		 * Name of the img tag.
		 */
		String IMG = "img";
		/**
		 * Name of the ins tag.
		 */
		String INS = "ins";
		/**
		 * Name of the map tag.
		 */
		String MAP = "map";
		/**
		 * Name of the keygen tag.
		 */
		String KEYGEN = "keygen";
		/**
		 * Name of the meter tag.
		 */
		String METER = "meter";
		/**
		 * Name of the menu tag.
		 */
		String MENU = "menu";
		/**
		 * Name of the nav tag.
		 */
		String NAV = "nav";
		/**
		 * Name of the kbd tag.
		 */
		String KBD = "kbd";
		/**
		 * Name of the math tag.
		 */
		String MATH = "math";
		/**
		 * Name of the mark tag.
		 */
		String MARK = "mark";
		/**
		 * Name of the label tag.
		 */
		String LABEL = "label";
		/**
		 * Name of the q tag.
		 */
		String Q = "q";
		/**
		 * Name of the samp tag.
		 */
		String SAMP = "samp";
		/**
		 * Name of the progress tag.
		 */
		String PROGRESS = "progress";
		/**
		 * Name of the ruby tag.
		 */
		String RUBY = "ruby";
		/**
		 * Name of the rt tag.
		 */
		String RT = "rt";
		/**
		 * Name of the rp tag.
		 */
		String RP = "rp";
		/**
		 * Name of the object tag.
		 */
		String OBJECT = "object";
		/**
		 * Name of the small tag.
		 */
		String SMALL = "small";
		/**
		 * Name of the source tag.
		 */
		String SOURCE = "source";
		/**
		 * Name of the sub tag.
		 */
		String SUB = "sub";
		/**
		 * Name of the sup tag.
		 */
		String SUP = "sup";
		/**
		 * Name of the strong tag.
		 */
		String STRONG = "strong";
		/**
		 * Name of the select tag.
		 */
		String SELECT = "select";
		/**
		 * Name of the span tag.
		 */
		String SPAN = "span";
		/**
		 * Name of the svg tag.
		 */
		String SVG = "svg";
		/**
		 * Name of the wbr tag.
		 */
		String WBR = "wbr";
		/**
		 * Name of the time tag.
		 */
		String TIME = "time";
		/**
		 * Name of the textarea tag.
		 */
		String TEXTAREA = "textarea";
		/**
		 * Name of the var tag.
		 */
		String VAR = "var";
		/**
		 * Name of the tr tag.
		 */
		String TR = "tr";
		/**
		 * Name of the s tag.
		 */
		String S = "s";
		/**
		 * Name of the srike tag.
		 */
		String STRIKE = "strike";
	}

	/**
	 * Attributes used in HTML tags.
	 *
	 * @author Emiel Ackermann
	 */
	interface Attribute {
		/**
		 * Name of the cellpadding attribute.
		 */
		String CELLPADDING = "cellpadding";
		/**
		 * Name of the cellspacing attribute.
		 */
		String CELLSPACING = "cellspacing";
		/**
		 * Name of the style attribute.
		 */
		String STYLE = "style";
		/**
		 * Name of the class attribute.
		 */
		String CLASS = "class";
		/**
		 * Name of the id attribute.
		 */
		String ID = "id";
		/**
		 * Name of the href attribute.
		 */
		String HREF = "href";
		/**
		 * Name of the name attribute.
		 */
		String NAME = "name";
		/**
		 * Name of the src attribute.
		 */
		String SRC = "src";
		/**
		 * Name of the width attribute.
		 */
		String WIDTH = "width";
		/**
		 * Name of the height attribute.
		 */
		String HEIGHT = "height";
		/**
		 * Name of the type attribute.
		 */
		String TYPE = "type";
		/**
		 * Name of the colspan attribute.
		 */
		String COLSPAN = "colspan";
		/**
		 * Name of the rowspan attribute.
		 */
		String ROWSPAN = "rowspan";
	}
}
