<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output 
  doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
  doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
/>

<xsl:template match="/">
  
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
  <title><xsl:value-of select="player/name"/></title>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
  </head>
  
  
  <body>

  <h1><xsl:value-of select="player/name"/></h1>
  <h3>Score: <xsl:value-of select="player/score"/></h3>
  <h3>Country: <xsl:value-of select="player/country/name"/></h3>

  <table border="1">
  <tr><th>City</th><th>Population</th><th>Dead</th><th>Nukes</th></tr>

  <xsl:for-each select="player/country/cities/city">
    <tr><td><xsl:value-of select="name"/></td><td><xsl:value-of select="population"/></td><td><xsl:value-of select="dead"/></td><td><xsl:value-of select="nukes"/></td></tr>
  </xsl:for-each>

  </table>


  <!--
  <xsl:for-each select="player/history/event">
    <xsl:choose>
      <xsl:when test="@type = 'Nuke'">
        <p><xsl:value-of select="nuker"/></p>
      </xsl:when>
    </xsl:choose>
  </xsl:for-each>
-->



  <br/>
  <a href="index.xml">Home</a>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>