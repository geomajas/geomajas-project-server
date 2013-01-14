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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link HtmlBuilder}.
 *
 * @author Emiel Ackermann
 */
public class HtmlBuilderTest {

	@Test
	public void openTagTest() {
		Assert.assertEquals("<p class='testClass' style='extra:BIG'>blabla",
				HtmlBuilder.openTag(Html.Tag.P, "testClass", "extra:BIG", "blabla"));
	}

	@Test
	public void closeTagTest() {
		Assert.assertEquals("</p>", HtmlBuilder.closeTag(Html.Tag.P));
	}

	@Test
	public void divClassTest() {
		Assert.assertEquals("<div class='testClass'>blabla</div>", HtmlBuilder.divClass("testClass", "blabla"));
	}

	@Test
	public void divClassHtmlContentTest() {
		Assert.assertEquals("<div class='testClass'><s>blabla</s></div>",
				HtmlBuilder.divClassHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void divStyleTest() {
		Assert.assertEquals("<div style='testClass'>blabla</div>", HtmlBuilder.divStyle("testClass", "blabla"));
	}

	@Test
	public void divStyleHtmlContentTest() {
		Assert.assertEquals("<div style='testClass'><s>blabla</s></div>",
				HtmlBuilder.divStyleHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void htmlEncodeTest() {
		Assert.assertEquals("&lt;&amp;&gt;", HtmlBuilder.htmlEncode("<&>"));
		Assert.assertEquals("", HtmlBuilder.htmlEncode(null));
	}

	@Test
	public void openTagClassTest() {
		Assert.assertEquals("<script class='testClass'>blabla",
				HtmlBuilder.openTagClass(Html.Tag.SCRIPT, "testClass", "blabla"));
	}

	@Test
	public void openTagClassHtmlContentTest() {
		Assert.assertEquals("<script class='testClass'><s>blabla</s>",
				HtmlBuilder.openTagClassHtmlContent(Html.Tag.SCRIPT, "testClass",
						HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void openTagHtmlContentTest() {
		Assert.assertEquals("<script class='testClass' style='testClass'><s>blabla</s>",
				HtmlBuilder.openTagHtmlContent(Html.Tag.SCRIPT, "testClass", "testClass",
						HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void openTagStyleTest() {
		Assert.assertEquals("<script style='testClass'>blabla",
				HtmlBuilder.openTagStyle(Html.Tag.SCRIPT, "testClass", "blabla"));
	}

	@Test
	public void openTagStyleHtmlContentTest() {
		Assert.assertEquals("<script style='testClass'><s>blabla</s>", HtmlBuilder
				.openTagStyleHtmlContent(Html.Tag.SCRIPT, "testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void tableClassTest() {
		Assert.assertEquals("<table class='testClass'>blabla</table>", HtmlBuilder.tableClass("testClass", "blabla"));
	}

	@Test
	public void tableClassHtmlContentTest() {
		Assert.assertEquals("<table class='testClass'><s>blabla</s></table>",
				HtmlBuilder.tableClassHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void tableStyleTest() {
		Assert.assertEquals("<table style='testClass'>blabla</table>",
				HtmlBuilder.tableStyle("testClass", "blabla"));
	}

	@Test
	public void tableStyleHtmlContentTest() {
		Assert.assertEquals("<table style='testClass'><s>blabla</s></table>",
				HtmlBuilder.tableStyleHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void tagTest() {
		Assert.assertEquals("<s>blabla</s>", HtmlBuilder.tag(Html.Tag.S, "blabla"));
	}

	@Test
	public void tagClassTest() {
		Assert.assertEquals("<script class='testClass'>blabla</script>",
				HtmlBuilder.tagClass(Html.Tag.SCRIPT, "testClass", "blabla"));
	}

	@Test
	public void tagClassHtmlContent() {
		Assert.assertEquals("<script class='testClass'><s>blabla</s></script>",
				HtmlBuilder.tagClassHtmlContent(Html.Tag.SCRIPT, "testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void tagHtmlContentTest() {
		Assert.assertEquals("<script><s>blabla</s></script>",
				HtmlBuilder.tagHtmlContent(Html.Tag.SCRIPT, HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void tagStyleTest() {
		Assert.assertEquals("<script style='testClass'>blabla</script>",
				HtmlBuilder.tagStyle(Html.Tag.SCRIPT, "testClass", "blabla"));
	}

	@Test
	public void tagStyleHtmlContentTest() {
		Assert.assertEquals("<script style='testClass'><s>blabla</s></script>",
				HtmlBuilder.tagStyleHtmlContent(Html.Tag.SCRIPT, "testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void tdClassTest() {
		Assert.assertEquals("<td class='testClass'>blabla</td>", HtmlBuilder.tdClass("testClass", "blabla"));
	}

	@Test
	public void tdClassHtmlContentTest() {
		Assert.assertEquals("<td class='testClass'><s>blabla</s></td>",
				HtmlBuilder.tdClassHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void tdStyleTest() {
		Assert.assertEquals("<td style='testClass'>blabla</td>", HtmlBuilder.tdStyle("testClass", "blabla"));
	}

	@Test
	public void tdStyleHtmlContentTest() {
		Assert.assertEquals("<td style='testClass'><s>blabla</s></td>",
				HtmlBuilder.tdStyleHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void trTest() {
		Assert.assertEquals("<tr>blabla</tr>", HtmlBuilder.tr("blabla"));
	}

	@Test
	public void trClassTest() {
		Assert.assertEquals("<tr class='testClass'>blabla</tr>", HtmlBuilder.trClass("testClass", "blabla"));
	}

	@Test
	public void trClassHtmlContentTest() {
		Assert.assertEquals("<tr class='testClass'><s>blabla</s></tr>",
				HtmlBuilder.trClassHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla")));
	}

	@Test
	public void trHtmlContentTest() {
		Assert.assertEquals("<tr><td style='testStyle'>blabla</td></tr>",
				HtmlBuilder.trHtmlContent(HtmlBuilder.tdStyle("testStyle", "blabla")));
	}

	@Test
	public void trStyleTest() {
		Assert.assertEquals("<tr style='testStyle'>blabla</tr>", HtmlBuilder.trStyle("testStyle", "blabla"));
	}

	@Test
	public void trStyleHtmlContentTest() {
		Assert.assertEquals("<tr style='testStyle'><td style='testStyle'>blabla</td></tr>",
				HtmlBuilder.trStyleHtmlContent("testStyle", HtmlBuilder.tdStyle("testStyle", "blabla")));
	}

	@Test
	public void tdTrTableTest() {
		String td = HtmlBuilder.tdStyle("testStyle", "blabla");
		Assert.assertEquals("<td style='testStyle'>blabla</td>", td);
		String tr = HtmlBuilder.trHtmlContent(td);
		Assert.assertEquals(("<tr>" + td + "</tr>"), tr);
		Assert.assertEquals(("<table class='testClass'>" + tr + "</table>"),
				HtmlBuilder.tableClassHtmlContent("testClass", tr));
	}

	@Test
	public void tdTrTableTest2() {
		String td = HtmlBuilder.tdStyle("testStyle", "blabla");
		Assert.assertEquals("<td style='testStyle'>blabla</td>", td);
		String tr = HtmlBuilder.trHtmlContent(td);
		Assert.assertEquals(("<tr>" + td + "</tr>"), tr);
		String[] rows = new String[2];
		rows[0] = tr;
		Assert.assertEquals(("<table class='testClass'>" + tr + "null</table>"),
				HtmlBuilder.tableClassHtmlContent("testClass", rows));
	}
}
