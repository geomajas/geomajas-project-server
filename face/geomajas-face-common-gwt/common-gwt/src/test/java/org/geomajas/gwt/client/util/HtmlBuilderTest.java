
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
		String openP = HtmlBuilder.openTag(Html.Tag.P, "testClass", "extra:BIG", "blabla");
		Assert.assertTrue("<p class='testClass' style='extra:BIG'>blabla".equals(openP));
	}

	@Test
	public void closeTagTest() {
		String closeP = HtmlBuilder.closeTag(Html.Tag.P);
		Assert.assertTrue("</p>".equals(closeP));
	}

	@Test
	public void divClassTest() {
		String div = HtmlBuilder.divClass("testClass", "blabla");
		Assert.assertTrue("<div class='testClass'>blabla</div>".equals(div));
	}

	@Test
	public void divClassHtmlContentTest() {
		String div = HtmlBuilder.divClassHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<div class='testClass'><s>blabla</s></div>".equals(div));
}

	@Test
	public void divStyleTest() {
		String div = HtmlBuilder.divStyle("testClass", "blabla");
		Assert.assertTrue("<div style='testClass'>blabla</div>".equals(div));
}

	@Test
	public void divStyleHtmlContentTest() {
		String div = HtmlBuilder.divStyleHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<div style='testClass'><s>blabla</s></div>".equals(div));
}

	@Test
	public void htmlEncodeTest() {
		String htmlEncode = HtmlBuilder.htmlEncode("<&>");
		Assert.assertTrue("&lt;&amp;&gt;".equals(htmlEncode));
}

	@Test
	public void openTagClassTest() {
		String openTagClass = HtmlBuilder.openTagClass(Html.Tag.SCRIPT, "testClass", "blabla");
		Assert.assertTrue("<script class='testClass'>blabla".equals(openTagClass));
}

	@Test
	public void openTagClassHtmlContentTest() {
		String openTagClassHtmlContent = HtmlBuilder.openTagClassHtmlContent(Html.Tag.SCRIPT, "testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<script class='testClass'><s>blabla</s>".equals(openTagClassHtmlContent));
}

	@Test
	public void openTagHtmlContentTest() {
		String openTagHtmlContent = HtmlBuilder.openTagHtmlContent(Html.Tag.SCRIPT, "testClass", "testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<script class='testClass' style='testClass'><s>blabla</s>".equals(openTagHtmlContent));
}

	@Test
	public void openTagStyleTest() {
		String openTagStyle = HtmlBuilder.openTagStyle(Html.Tag.SCRIPT, "testClass", "blabla");
		Assert.assertTrue("<script style='testClass'>blabla".equals(openTagStyle));
}

	@Test
	public void openTagStyleHtmlContentTest() {
		String openTagStyleHtmlContent = HtmlBuilder.openTagStyleHtmlContent(Html.Tag.SCRIPT, "testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<script style='testClass'><s>blabla</s>".equals(openTagStyleHtmlContent));
}

	@Test
	public void tableClassTest() {
		String tableClass = HtmlBuilder.tableClass("testClass", "blabla");
		Assert.assertTrue("<table class='testClass'>blabla</table>".equals(tableClass));
}

	@Test
	public void tableClassHtmlContentTest() {
		String tableClassHtmlContent = HtmlBuilder.tableClassHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<table class='testClass'><s>blabla</s></table>".equals(tableClassHtmlContent));
}

	@Test
	public void tableStyleTest() {
		String tableStyle = HtmlBuilder.tableStyle("testClass", "blabla");
		Assert.assertTrue("<table style='testClass'>blabla</table>".equals(tableStyle));
}

	@Test
	public void tableStyleHtmlContentTest() {
		String tableStyleHtmlContent = HtmlBuilder.tableStyleHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<table style='testClass'><s>blabla</s></table>".equals(tableStyleHtmlContent));
}

	@Test
	public void tagTest() {
		String tag = HtmlBuilder.tag(Html.Tag.S, "blabla");
		Assert.assertTrue("<s>blabla</s>".equals(tag));
}

	@Test
	public void tagClassTest() {
		String tagClass = HtmlBuilder.tagClass(Html.Tag.SCRIPT, "testClass", "blabla");
		Assert.assertTrue("<script class='testClass'>blabla</script>".equals(tagClass));
}

	@Test
	public void tagClassHtmlContent() {
		String tagClassHtmlContent = HtmlBuilder.tagClassHtmlContent(Html.Tag.SCRIPT, "testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<script class='testClass'><s>blabla</s></script>".equals(tagClassHtmlContent));
}

	@Test
	public void tagHtmlContentTest() {
		String tagHtmlContent = HtmlBuilder.tagHtmlContent(Html.Tag.SCRIPT, HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<script><s>blabla</s></script>".equals(tagHtmlContent));
}

	@Test
	public void tagStyleTest() {
		String tagStyle = HtmlBuilder.tagStyle(Html.Tag.SCRIPT, "testClass", "blabla");
		Assert.assertTrue("<script style='testClass'>blabla</script>".equals(tagStyle));
}

	@Test
	public void tagStyleHtmlContentTest() {
		String tagStyleHtmlContent = HtmlBuilder.tagStyleHtmlContent(Html.Tag.SCRIPT, "testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<script style='testClass'><s>blabla</s></script>".equals(tagStyleHtmlContent));
}

	@Test
	public void tdClassTest() {
		String tdClass = HtmlBuilder.tdClass("testClass", "blabla");
		Assert.assertTrue("<td class='testClass'>blabla</td>".equals(tdClass));
}

	@Test
	public void tdClassHtmlContentTest() {
		String tdClassHtmlContent = HtmlBuilder.tdClassHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<td class='testClass'><s>blabla</s></td>".equals(tdClassHtmlContent));
}

	@Test
	public void tdStyleTest() {
		String tdStyle = HtmlBuilder.tdStyle("testClass", "blabla");
		Assert.assertTrue("<td style='testClass'>blabla</td>".equals(tdStyle));
}

	@Test
	public void tdStyleHtmlContentTest() {
		String tdStyleHtmlContent = HtmlBuilder.tdStyleHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<td style='testClass'><s>blabla</s></td>".equals(tdStyleHtmlContent));
}

	@Test
	public void trTest() {
		String tr = HtmlBuilder.tr("blabla");
		Assert.assertTrue("<tr>blabla</tr>".equals(tr));
}

	@Test
	public void trClassTest() {
		String trClass = HtmlBuilder.trClass("testClass", "blabla");
		Assert.assertTrue("<tr class='testClass'>blabla</tr>".equals(trClass));
}

	@Test
	public void trClassHtmlContentTest() {
		String trClassHtmlContent = HtmlBuilder.trClassHtmlContent("testClass", HtmlBuilder.tag(Html.Tag.S, "blabla"));
		Assert.assertTrue("<tr class='testClass'><s>blabla</s></tr>".equals(trClassHtmlContent));
}

	@Test
	public void trHtmlContentTest() {
		String tr = HtmlBuilder.trHtmlContent(HtmlBuilder.tdStyle("testStyle", "blabla"));
		Assert.assertTrue("<tr><td style='testStyle'>blabla</td></tr>".equals(tr));
	}

	@Test
	public void trStyleTest() {
		String tr = HtmlBuilder.trStyle("testStyle", "blabla");
		Assert.assertTrue("<tr style='testStyle'>blabla</tr>".equals(tr));
	}

	@Test
	public void trStyleHtmlContentTest() {
		String tr = HtmlBuilder.trStyleHtmlContent("testStyle", HtmlBuilder.tdStyle("testStyle", "blabla"));
		Assert.assertTrue("<tr style='testStyle'><td style='testStyle'>blabla</td></tr>".equals(tr));
	}

	@Test
	public void tdTrTableTest() {
		String td = HtmlBuilder.tdStyle("testStyle", "blabla");
		Assert.assertTrue("<td style='testStyle'>blabla</td>".equals(td));
		String tr = HtmlBuilder.trHtmlContent(td);
		Assert.assertTrue(("<tr>" + td + "</tr>").equals(tr));
		String table = HtmlBuilder.tableClassHtmlContent("testClass", tr);
		Assert.assertTrue(("<table class='testClass'>" + tr + "</table>").equals(table));
	}
}
