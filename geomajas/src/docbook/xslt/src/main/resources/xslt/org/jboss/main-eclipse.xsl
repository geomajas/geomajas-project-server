<?xml version='1.0'?>

<!--
	Copyright 2007 Red Hat, Inc.
	License: GPL
	Author: Jeff Fearn <jfearn@redhat.com>
	Author: Tammy Fox <tfox@redhat.com>
	Author: Andy Fitzsimon <afitzsim@redhat.com>
		Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:exsl="http://exslt.org/common"
				version="1.0"
				exclude-result-prefixes="exsl">

	<xsl:import href="http://docbook.sourceforge.net/release/xsl/1.72.0/eclipse/eclipse.xsl"/>

	<!-- We need to override the imported html/chunk.xsl from eclipse/eclipse.xsl to generate valid XHTML -->
	<xsl:import href="http://docbook.sourceforge.net/release/xsl/1.72.0/xhtml/chunk.xsl"/>

	<xsl:include href="redhat.xsl"/>
	<xsl:include href="xhtml-common.xsl"/>

	<!-- This is needed to generate the correct xhtml-strict DOCTYPE on the front page -->
	<xsl:output method="xml"
				encoding="UTF-8"
				indent="yes"
				doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
				doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
				standalone="no"/>

	<!-- We need to add this as it's needed later for a check -->
	<xsl:param name="confidential" select="0"/>

	<xsl:param name="generate.legalnotice.link" select="1"/>
	<xsl:param name="generate.revhistory.link" select="0"/>

	<xsl:param name="chunk.section.depth" select="4"/>
	<xsl:param name="chunk.first.sections" select="1"/>
	<xsl:param name="chunk.toc" select="''"/>

	<!-- We don't want to display titles in the header navigation as there are already breadcrumbs -->
	<xsl:param name="navig.showtitles" select="0"/>

	<!--
 From: xhtml/chunk-common.xsl
 Reason: need to add class attributes so we can style the pages using icons
 Version: 1.72.0
 -->
	<xsl:template name="header.navigation">
		<xsl:param name="prev" select="/foo"/>
		<xsl:param name="next" select="/foo"/>
		<xsl:param name="nav.context"/>

		<xsl:variable name="home" select="/*[1]"/>
		<xsl:variable name="up" select="parent::*"/>

		<xsl:variable name="row1" select="$navig.showtitles != 0"/>
		<xsl:variable name="row2"
					  select="count($prev) &gt; 0                                     or (count($up) &gt; 0                                          and generate-id($up) != generate-id($home)                                         and $navig.showtitles != 0)                                     or count($next) &gt; 0"/>

		<xsl:if test="$suppress.navigation = '0' and $suppress.header.navigation = '0'">
			<div class="navheader">
				<xsl:if test="$row1 or $row2">
					<table width="100%" summary="Navigation header">
						<xsl:if test="$row1">
							<tr>
								<th colspan="3" align="center">
									<xsl:apply-templates select="." mode="object.title.markup"/>
								</th>
							</tr>
						</xsl:if>

						<xsl:if test="$row2">
							<tr>
								<td width="20%" align="left" class="previous">
									<xsl:if test="count($prev)&gt;0">
										<a accesskey="p">
											<xsl:attribute name="href">
												<xsl:call-template name="href.target">
													<xsl:with-param name="object" select="$prev"/>
												</xsl:call-template>
											</xsl:attribute>
											<xsl:call-template name="navig.content">
												<xsl:with-param name="direction" select="'prev'"/>
											</xsl:call-template>
										</a>
									</xsl:if>
									<xsl:text>&#160;</xsl:text>
								</td>
								<td width="60%" align="center">
									<xsl:choose>
										<xsl:when
												test="count($up) &gt; 0                                   and generate-id($up) != generate-id($home)                                   and $navig.showtitles != 0">
											<xsl:apply-templates select="$up" mode="object.title.markup"/>
										</xsl:when>
										<xsl:otherwise>&#160;</xsl:otherwise>
									</xsl:choose>
								</td>
								<td width="20%" align="right" class="next">
									<xsl:text>&#160;</xsl:text>
									<xsl:if test="count($next)&gt;0">
										<a accesskey="n">
											<xsl:attribute name="href">
												<xsl:call-template name="href.target">
													<xsl:with-param name="object" select="$next"/>
												</xsl:call-template>
											</xsl:attribute>
											<xsl:call-template name="navig.content">
												<xsl:with-param name="direction" select="'next'"/>
											</xsl:call-template>
										</a>
									</xsl:if>
								</td>
							</tr>
						</xsl:if>
					</table>
				</xsl:if>
				<xsl:if test="$header.rule != 0">
					<hr/>
				</xsl:if>
			</div>
		</xsl:if>
	</xsl:template>

	<!--
 From: xhtml/chunk-common.xsl
 Reason: need to add class attributes so we can style the page using icons. Also changed the footer table to one row
		 so that the 'Top of page' and 'Front page' links are next to each other and correctly spaced.
 Version: 1.72.0
 -->
	<xsl:template name="footer.navigation">
		<xsl:param name="prev" select="/foo"/>
		<xsl:param name="next" select="/foo"/>
		<xsl:param name="nav.context"/>

		<xsl:variable name="home" select="/*[1]"/>
		<xsl:variable name="up" select="parent::*"/>

		<xsl:variable name="row1"
					  select="count($prev) &gt; 0                                     or count($up) &gt; 0                                     or count($next) &gt; 0"/>

		<xsl:variable name="row2"
					  select="($prev and $navig.showtitles != 0)                                     or (generate-id($home) != generate-id(.)                                         or $nav.context = 'toc')                                     or ($chunk.tocs.and.lots != 0                                         and $nav.context != 'toc')                                     or ($next and $navig.showtitles != 0)"/>

		<xsl:if test="$suppress.navigation = '0' and $suppress.footer.navigation = '0'">
			<div class="navfooter">
				<xsl:if test="$footer.rule != 0">
					<hr/>
				</xsl:if>

				<xsl:if test="$row1 or $row2">
					<table width="100%" summary="Navigation footer">
						<xsl:if test="$row1">
							<tr>
								<td width="25%" align="left" class="previous">
									<xsl:if test="count($prev)&gt;0">
										<a accesskey="p">
											<xsl:attribute name="href">
												<xsl:call-template name="href.target">
													<xsl:with-param name="object" select="$prev"/>
												</xsl:call-template>
											</xsl:attribute>
											<xsl:call-template name="navig.content">
												<xsl:with-param name="direction" select="'prev'"/>
											</xsl:call-template>
										</a>
									</xsl:if>
									<xsl:text>&#160;</xsl:text>
								</td>
								<td width="25%" align="right" class="up">
									<xsl:choose>
										<xsl:when
												test="count($up)&gt;0                                   and generate-id($up) != generate-id($home)">
											<a accesskey="u">
												<xsl:attribute name="href">
													<xsl:text>#</xsl:text>
													<!--<xsl:call-template name="href.target">
																			  <xsl:with-param name="object" select="$up"/>
																			</xsl:call-template>-->
												</xsl:attribute>
												<xsl:call-template name="navig.content">
													<xsl:with-param name="direction" select="'up'"/>
												</xsl:call-template>
											</a>
										</xsl:when>
										<xsl:otherwise>&#160;</xsl:otherwise>
									</xsl:choose>
								</td>
								<td width="25%" align="left" class="home">
									<xsl:choose>
										<xsl:when test="$home != . or $nav.context = 'toc'">
											<a accesskey="h">
												<xsl:attribute name="href">
													<xsl:call-template name="href.target">
														<xsl:with-param name="object" select="$home"/>
													</xsl:call-template>
												</xsl:attribute>
												<xsl:call-template name="navig.content">
													<xsl:with-param name="direction" select="'home'"/>
												</xsl:call-template>
											</a>
											<xsl:if test="$chunk.tocs.and.lots != 0 and $nav.context != 'toc'">
												<xsl:text>&#160;|&#160;</xsl:text>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>&#160;</xsl:otherwise>
									</xsl:choose>

									<xsl:if test="$chunk.tocs.and.lots != 0 and $nav.context != 'toc'">
										<a accesskey="t">
											<xsl:attribute name="href">
												<xsl:apply-templates select="/*[1]" mode="recursive-chunk-filename">
													<xsl:with-param name="recursive" select="true()"/>
												</xsl:apply-templates>
												<xsl:text>-toc</xsl:text>
												<xsl:value-of select="$html.ext"/>
											</xsl:attribute>
											<xsl:call-template name="gentext">
												<xsl:with-param name="key" select="'nav-toc'"/>
											</xsl:call-template>
										</a>
									</xsl:if>
								</td>
								<td width="25%" align="right" class="next">
									<xsl:text>&#160;</xsl:text>
									<xsl:if test="count($next)&gt;0">
										<a accesskey="n">
											<xsl:attribute name="href">
												<xsl:call-template name="href.target">
													<xsl:with-param name="object" select="$next"/>
												</xsl:call-template>
											</xsl:attribute>
											<xsl:call-template name="navig.content">
												<xsl:with-param name="direction" select="'next'"/>
											</xsl:call-template>
										</a>
									</xsl:if>
								</td>
							</tr>
						</xsl:if>

						<xsl:if test="$row2">
							<tr>
								<td align="left" valign="top">
									<xsl:if test="$navig.showtitles != 0">
										<xsl:apply-templates select="$prev" mode="object.title.markup"/>
									</xsl:if>
									<xsl:text>&#160;</xsl:text>
								</td>

								<td align="right" valign="top">
									<xsl:text>&#160;</xsl:text>
									<xsl:if test="$navig.showtitles != 0">
										<xsl:apply-templates select="$next" mode="object.title.markup"/>
									</xsl:if>
								</td>
							</tr>
						</xsl:if>
					</table>
				</xsl:if>
			</div>
		</xsl:if>
	</xsl:template>

	<!--
 From: xhtml/footnote.xsl
 Reason: remove inline css from hr
 Version: 1.72.0
 -->
	<xsl:template name="process.footnotes">
		<xsl:variable name="footnotes" select=".//footnote"/>
		<xsl:variable name="table.footnotes" select=".//tgroup//footnote"/>

		<!-- Only bother to do this if there's at least one non-table footnote -->
		<xsl:if test="count($footnotes)&gt;count($table.footnotes)">
			<div class="footnotes">
				<br/>
				<hr/>
				<xsl:apply-templates select="$footnotes" mode="process.footnote.mode"/>
			</div>
		</xsl:if>

		<xsl:if test="$annotation.support != 0 and //annotation">
			<div class="annotation-list">
				<div class="annotation-nocss">
					<p>The following annotations are from this essay. You are seeing
						them here because your browser doesn&#8217;t support the user-interface
						techniques used to make them appear as &#8216;popups&#8217; on modern browsers.
					</p>
				</div>

				<xsl:apply-templates select="//annotation" mode="annotation-popup"/>
			</div>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
