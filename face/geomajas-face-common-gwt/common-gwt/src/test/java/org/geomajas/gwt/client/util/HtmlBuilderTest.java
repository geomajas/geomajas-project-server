package org.geomajas.gwt.client.util;


import org.junit.Test;
import org.junit.Assert;

/**
 * Test for {@link HtmlBuilder}.
 *
 * @author Emiel Ackermann
 */
public class HtmlBuilderTest {

	@Test
	public void openTagTest() {
		String openP = HtmlBuilder.openTag(Html.Tag.P, "testClass", "extra:BIG", false, "blabla");
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
	public void tdTrTableTest() {
		String td = HtmlBuilder.tdStyle("testStyle", "blabla");
		Assert.assertTrue("<td style='testStyle'>blabla</td>".equals(td));
		String tr = HtmlBuilder.trHtmlContent(td);
		Assert.assertTrue(("<tr>" + td + "</tr>").equals(tr));
		String table = HtmlBuilder.tableClassHtmlContent("testClass", tr);
		Assert.assertTrue(("<table class='testClass'>" + tr + "</table>").equals(table));
	}
}
