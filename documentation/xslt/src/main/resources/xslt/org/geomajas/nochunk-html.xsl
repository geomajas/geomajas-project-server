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

	<xsl:include href="geosparc.xsl"/>
	<xsl:include href="xhtml-common.xsl"/>
	<xsl:param name="confidential" select="0"/>

	<!-- This is needed to generate the correct xhtml-strict DOCTYPE on the page -->
	<xsl:output method="xml"
				encoding="UTF-8"
				indent="yes"
				doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
				doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
				standalone="no"/>

	<!--
 From: xhtml/titlepage-templates.xsl
 Reason: Needed to add JBoss.org and Community Documentation graphics to header
 Version: 1.72.0
 -->
	<xsl:template name="book.titlepage.recto">
		<p xmlns="http://www.w3.org/1999/xhtml">
			<xsl:attribute name="id">
				<xsl:text>title</xsl:text>
			</xsl:attribute>
			<a>
				<xsl:attribute name="href">
					<xsl:text>http://www.geomajas.org</xsl:text>
				</xsl:attribute>
				<xsl:attribute name="class">
					<xsl:text>geomajas_href</xsl:text>
				</xsl:attribute>
				<strong>
					Geomajas
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
		<xsl:choose>
			<xsl:when test="bookinfo/title">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/title"/>
			</xsl:when>
			<xsl:when test="info/title">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/title"/>
			</xsl:when>
			<xsl:when test="title">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="title"/>
			</xsl:when>
		</xsl:choose>

		<xsl:choose>
			<xsl:when test="bookinfo/subtitle">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/subtitle"/>
			</xsl:when>
			<xsl:when test="info/subtitle">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/subtitle"/>
			</xsl:when>
			<xsl:when test="subtitle">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="subtitle"/>
			</xsl:when>
		</xsl:choose>

		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/corpauthor"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/corpauthor"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/authorgroup"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/authorgroup"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/author"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/author"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/othercredit"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/othercredit"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/releaseinfo"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/releaseinfo"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/copyright"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/copyright"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/legalnotice"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/legalnotice"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/pubdate"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/pubdate"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/revision"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/revision"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/revhistory"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/revhistory"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/abstract"/>
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/abstract"/>
	</xsl:template>

</xsl:stylesheet>
