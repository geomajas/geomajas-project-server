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

	<!-- Modify the default navigation wording -->
	<xsl:param name="local.l10n.xml" select="document('')"/>
	<l:i18n xmlns:l="http://docbook.sourceforge.net/xmlns/l10n/1.0">
		<l:l10n language="en">
			<l:gentext key="nav-home" text="Front page"/>
		</l:l10n>
	</l:i18n>

	<l:i18n xmlns:l="http://docbook.sourceforge.net/xmlns/l10n/1.0">
		<l:l10n language="en">
			<l:gentext key="nav-up" text="Top of page"/>
		</l:l10n>
	</l:i18n>

	<!-- titles after all elements -->
	<xsl:param name="formal.title.placement">
		figure after
		example after
		equation after
		table after
		procedure before
	</xsl:param>

	<!--
 Copied from fo/params.xsl
 -->
	<xsl:param name="l10n.gentext.default.language" select="'en'"/>

	<!-- This sets the filename based on the ID.								-->
	<xsl:param name="use.id.as.filename" select="'1'"/>

	<xsl:template match="command">
		<xsl:call-template name="inline.monoseq"/>
	</xsl:template>

	<xsl:template match="application">
		<xsl:call-template name="inline.boldseq"/>
	</xsl:template>

	<xsl:template match="guibutton">
		<xsl:call-template name="inline.boldseq"/>
	</xsl:template>

	<xsl:template match="guiicon">
		<xsl:call-template name="inline.boldseq"/>
	</xsl:template>

	<xsl:template match="guilabel">
		<xsl:call-template name="inline.boldseq"/>
	</xsl:template>

	<xsl:template match="guimenu">
		<xsl:call-template name="inline.boldseq"/>
	</xsl:template>

	<xsl:template match="guimenuitem">
		<xsl:call-template name="inline.boldseq"/>
	</xsl:template>

	<xsl:template match="guisubmenu">
		<xsl:call-template name="inline.boldseq"/>
	</xsl:template>

	<xsl:template match="filename">
		<xsl:call-template name="inline.monoseq"/>
	</xsl:template>

</xsl:stylesheet>


