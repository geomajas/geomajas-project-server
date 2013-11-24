<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
<StyledLayerDescriptor version="1.0.0" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld">
	<NamedLayer>
		<Name>pnt_merge</Name>
		<UserStyle>
			<FeatureTypeStyle>
				<Rule>
					<Name>Cabine diverse</Name>
					<Title>Cabine diverse</Title>
					<ogc:Filter>
						<ogc:PropertyIsEqualTo>
							<ogc:PropertyName>type_p</ogc:PropertyName>
							<ogc:Literal>334</ogc:Literal>
						</ogc:PropertyIsEqualTo>
					</ogc:Filter>
					<MaxScaleDenominator>2000</MaxScaleDenominator>
					<PointSymbolizer>
						<Graphic>
								<ExternalGraphic>
								    <OnlineResource 
									xlink:type="simple"
									xlink:href="images/png/cabinediverse.png" />
									<Format>image/png</Format>
								</ExternalGraphic>
						</Graphic>
					</PointSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</NamedLayer>
</StyledLayerDescriptor>