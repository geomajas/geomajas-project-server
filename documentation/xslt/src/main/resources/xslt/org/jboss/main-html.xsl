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

	<xsl:import href="http://docbook.sourceforge.net/release/xsl/1.72.0/xhtml/docbook.xsl"/>
	<xsl:import href="http://docbook.sourceforge.net/release/xsl/1.72.0/xhtml/chunk-common.xsl"/>
	<xsl:include href="http://docbook.sourceforge.net/release/xsl/1.72.0/xhtml/chunk-code.xsl"/>
	<xsl:include href="http://docbook.sourceforge.net/release/xsl/1.72.0/xhtml/manifest.xsl"/>

	<xsl:include href="redhat.xsl"/>
	<xsl:include href="xhtml-common.xsl"/>
	<xsl:param name="confidential" select="0"/>

	<xsl:param name="generate.legalnotice.link" select="1"/>
	<xsl:param name="generate.revhistory.link" select="0"/>

	<xsl:param name="chunk.section.depth" select="4"/>
	<xsl:param name="chunk.first.sections" select="1"/>
	<xsl:param name="chunk.toc" select="''"/>

	<!-- This is needed to generate the correct xhtml-strict DOCTYPE on the front page -->
	<xsl:output method="xml"
				encoding="UTF-8"
				indent="yes"
				doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
				doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
				standalone="no"/>

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

	<!--
 From: xhtml/chunk-common.xsl
 Reason: remove tables, truncate link text
 Version:
 -->
	<xsl:template name="header.navigation">
		<xsl:param name="prev" select="/foo"/>
		<xsl:param name="next" select="/foo"/>
		<xsl:param name="nav.context"/>
		<xsl:variable name="home" select="/*[1]"/>
		<xsl:variable name="up" select="parent::*"/>
		<xsl:variable name="row1" select="$navig.showtitles != 0"/>
		<xsl:variable name="row2"
					  select="count($prev) &gt; 0 or (count($up) &gt; 0 and generate-id($up) != generate-id($home) and $navig.showtitles != 0) or count($next) &gt; 0"/>
		<xsl:if test="$suppress.navigation = '0' and $suppress.header.navigation = '0'">
			<xsl:if test="$row1 or $row2">
				<xsl:if test="$row1">
					<p xmlns="http://www.w3.org/1999/xhtml">
						<xsl:attribute name="id">
							<xsl:text>title</xsl:text>
						</xsl:attribute>
						<a>
							<xsl:attribute name="href">
								<xsl:text>http://www.geomajas.org</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="class">
								<xsl:text>jbossOrg_href</xsl:text>
							</xsl:attribute>
							<strong>
								JBoss.org
							</strong>
						</a>
						<a>
							<xsl:attribute name="href">
								<xsl:text>http://www.geomajas.org/gis-documentation</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="class">
								<xsl:text>commDoc_href</xsl:text>
							</xsl:attribute>
							<strong>
								Community Documentation
							</strong>
						</a>
					</p>
				</xsl:if>
				<xsl:if test="$row2">
					<ul class="docnav" xmlns="http://www.w3.org/1999/xhtml">
						<li class="previous">
							<xsl:if test="count($prev)&gt;0">
								<a accesskey="p">
									<xsl:attribute name="href">
										<xsl:call-template name="href.target">
											<xsl:with-param name="object" select="$prev"/>
										</xsl:call-template>
									</xsl:attribute>
									<strong>
										<xsl:call-template name="navig.content">
											<xsl:with-param name="direction" select="'prev'"/>
										</xsl:call-template>
									</strong>
								</a>
							</xsl:if>
						</li>
						<li class="next">
							<xsl:if test="count($next)&gt;0">
								<a accesskey="n">
									<xsl:attribute name="href">
										<xsl:call-template name="href.target">
											<xsl:with-param name="object" select="$next"/>
										</xsl:call-template>
									</xsl:attribute>
									<strong>
										<xsl:call-template name="navig.content">
											<xsl:with-param name="direction" select="'next'"/>
										</xsl:call-template>
									</strong>
								</a>
							</xsl:if>
						</li>
					</ul>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$header.rule != 0">
				<hr/>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!--
 From: xhtml/chunk-common.xsl
 Reason: remove tables, truncate link text
 Version:
 -->
	<xsl:template name="footer.navigation">
		<xsl:param name="prev" select="/foo"/>
		<xsl:param name="next" select="/foo"/>
		<xsl:param name="nav.context"/>
		<xsl:param name="title-limit" select="'50'"/>
		<xsl:variable name="home" select="/*[1]"/>
		<xsl:variable name="up" select="parent::*"/>
		<xsl:variable name="row1" select="count($prev) &gt; 0 or count($up) &gt; 0 or count($next) &gt; 0"/>
		<xsl:variable name="row2"
					  select="($prev and $navig.showtitles != 0) or (generate-id($home) != generate-id(.) or $nav.context = 'toc') or ($chunk.tocs.and.lots != 0 and $nav.context != 'toc') or ($next and $navig.showtitles != 0)"/>

		<xsl:if test="$suppress.navigation = '0' and $suppress.footer.navigation = '0'">
			<xsl:if test="$footer.rule != 0">
				<hr/>
			</xsl:if>
			<xsl:if test="$row1 or $row2">
				<ul class="docnav" xmlns="http://www.w3.org/1999/xhtml">
					<xsl:if test="$row1">
						<li class="previous">
							<xsl:if test="count($prev) &gt; 0">
								<a accesskey="p">
									<xsl:attribute name="href">
										<xsl:call-template name="href.target">
											<xsl:with-param name="object" select="$prev"/>
										</xsl:call-template>
									</xsl:attribute>
									<strong>
										<xsl:call-template name="navig.content">
											<xsl:with-param name="direction" select="'prev'"/>
										</xsl:call-template>
									</strong>
									<xsl:variable name="text">
										<xsl:apply-templates select="$prev" mode="object.title.markup"/>
									</xsl:variable>
									<xsl:choose>
										<xsl:when test="string-length($text) &gt; $title-limit">
											<xsl:value-of select="concat(substring($text, 0, $title-limit), '...')"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$text"/>
										</xsl:otherwise>
									</xsl:choose>
								</a>
							</xsl:if>
						</li>
						<xsl:if test="count($up) &gt; 0">
							<li class="up">
								<a accesskey="u">
									<xsl:attribute name="href">
										<xsl:text>#</xsl:text>
									</xsl:attribute>
									<strong>
										<xsl:call-template name="navig.content">
											<xsl:with-param name="direction" select="'up'"/>
										</xsl:call-template>
									</strong>
								</a>
							</li>
						</xsl:if>
						<xsl:if test="$home != . or $nav.context = 'toc'">
							<li class="home">
								<a accesskey="h">
									<xsl:attribute name="href">
										<xsl:call-template name="href.target">
											<xsl:with-param name="object" select="$home"/>
										</xsl:call-template>
									</xsl:attribute>
									<strong>
										<xsl:call-template name="navig.content">
											<xsl:with-param name="direction" select="'home'"/>
										</xsl:call-template>
									</strong>
								</a>
							</li>
						</xsl:if>
						<xsl:if test="count($next)&gt;0">
							<li class="next">
								<a accesskey="n">
									<xsl:attribute name="href">
										<xsl:call-template name="href.target">
											<xsl:with-param name="object" select="$next"/>
										</xsl:call-template>
									</xsl:attribute>
									<strong>
										<xsl:call-template name="navig.content">
											<xsl:with-param name="direction" select="'next'"/>
										</xsl:call-template>
									</strong>
									<xsl:variable name="text">
										<xsl:apply-templates select="$next" mode="object.title.markup"/>
									</xsl:variable>
									<xsl:choose>
										<xsl:when test="string-length($text) &gt; $title-limit">
											<xsl:value-of select="concat(substring($text, 0, $title-limit),'...')"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$text"/>
										</xsl:otherwise>
									</xsl:choose>
								</a>
							</li>
						</xsl:if>
					</xsl:if>
				</ul>
			</xsl:if>
		</xsl:if>
	</xsl:template>


</xsl:stylesheet>
