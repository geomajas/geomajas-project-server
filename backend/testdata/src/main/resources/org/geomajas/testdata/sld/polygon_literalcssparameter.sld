<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
    <sld:NamedLayer>
	<sld:Name>lot_tubize</sld:Name>
        <sld:UserStyle>
          <sld:Name>lot_tubize</sld:Name>
            <sld:FeatureTypeStyle>
                <sld:Rule>
                    <sld:Name>lot_tubize</sld:Name>
                    <sld:Title>lot_tubize</sld:Title>
                    <sld:PolygonSymbolizer>
						<sld:Fill>
						  <sld:GraphicFill>
							<sld:Graphic>
							  <sld:Mark>
								<sld:WellKnownName>shape://backslash</sld:WellKnownName>
								<sld:Stroke>
								  <sld:CssParameter name="stroke">#999999</sld:CssParameter>
								  <sld:CssParameter name="stroke-width">1</sld:CssParameter>
								</sld:Stroke>
							  </sld:Mark>
							</sld:Graphic>
						  </sld:GraphicFill>
						</sld:Fill>
                        <sld:Stroke>
                            <sld:CssParameter name="stroke">
                                <ogc:Literal>#999999</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-linecap">
                                <ogc:Literal>butt</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-linejoin">
                                <ogc:Literal>bevel</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-opacity">
                                <ogc:Literal>1.0</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-width">
                                <ogc:Literal>1.0</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-dashoffset">
                                <ogc:Literal>0.0</ogc:Literal>
                            </sld:CssParameter>
                        </sld:Stroke>
                    </sld:PolygonSymbolizer>
                </sld:Rule>
            </sld:FeatureTypeStyle>
        </sld:UserStyle>
    </sld:NamedLayer>
</sld:StyledLayerDescriptor>

