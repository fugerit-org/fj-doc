<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <!-- Identity template - copies everything as-is -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- Specific template for elements with id="end-element" -->
    <xsl:template match="*[@id='end-element']">
        <xsl:copy>
            <!-- Copy existing attributes -->
            <xsl:apply-templates select="@*"/>
            <!-- Add the keep-together attribute -->
            <xsl:attribute name="keep-together.within-page">always</xsl:attribute>
            <!-- Copy child nodes -->
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>