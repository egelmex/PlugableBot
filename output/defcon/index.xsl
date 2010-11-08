<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output 
  doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
  doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
/>

<xsl:template match="/">
  
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
  <title>Defcon</title>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
  </head>
  <body>

  <h1>Defcon</h1>

  <xsl:for-each select="players/player">
    <p><a><xsl:attribute name="href"><xsl:value-of select="name"/></xsl:attribute><xsl:value-of select="name"/></a></p>
  </xsl:for-each>

  </body>
  </html>
</xsl:template>

</xsl:stylesheet>