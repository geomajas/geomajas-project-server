<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:gml="http://www.opengis.net/gml">
	<NamedLayer>
		<Name>Polygon_Filters</Name>
		<UserStyle>
			<Title>Polygon Filter Examples</Title>
			<FeatureTypeStyle>
				<Rule>
					<Name>Bbox</Name>
					<Title>Bbox</Title>
					<ogc:Filter>
						<ogc:BBOX>
							<ogc:PropertyName>geometry</ogc:PropertyName>
							<gml:Box srsName="http://www.opengis.net/gml/srs/epsg.xml#4326">
								<gml:coordinates>0.0,1.0 3.0,4.0
								</gml:coordinates>
							</gml:Box>
						</ogc:BBOX>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
				<Rule>
					<Name>Contains</Name>
					<Title>Contains</Title>
					<ogc:Filter>
						<ogc:Contains>
							<ogc:PropertyName>geometry</ogc:PropertyName>
					         <gml:Point srsName="http://www.opengis.net/gml/srs/epsg.xml#4326">
					            <gml:coordinates>45.256,-71.92</gml:coordinates>
					         </gml:Point>
						</ogc:Contains>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
				<Rule>
					<Name>Crosses</Name>
					<Title>Crosses</Title>
					<ogc:Filter>
						<ogc:Crosses>
							<ogc:PropertyName>geometry</ogc:PropertyName>
							<gml:Polygon srsName="urn:x-ogc:def:crs:EPSG:102113">
								<gml:outerBoundaryIs>
									<gml:LinearRing>
										<gml:coordinates>-11990218.003256835,5596413.462148438
											-11613536.32791992,5723604.6771972645
											-11364045.867631836,5527925.884814454
											-11535264.810966797,5371382.850908203
											-11990218.003256835,5596413.462148438</gml:coordinates>
									</gml:LinearRing>
								</gml:outerBoundaryIs>
							</gml:Polygon>
						</ogc:Crosses>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
				<Rule>
					<Name>Disjoint</Name>
					<Title>Disjoint</Title>
					<ogc:Filter>
						<ogc:Disjoint>
							<ogc:PropertyName>geometry</ogc:PropertyName>
							<gml:LineString srsName="http://www.opengis.net/gml/srs/epsg.xml#4326">
								<gml:coord>
									<gml:X>0</gml:X>
									<gml:Y>50</gml:Y>
								</gml:coord>
								<gml:coord>
									<gml:X>70</gml:X>
									<gml:Y>60</gml:Y>
								</gml:coord>
								<gml:coord>
									<gml:X>100</gml:X>
									<gml:Y>50</gml:Y>
								</gml:coord>
							</gml:LineString>
						</ogc:Disjoint>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
				<Rule>
					<Name>Equals</Name>
					<Title>Equals</Title>
					<ogc:Filter>
						<ogc:Equals>
							<ogc:PropertyName>geometry</ogc:PropertyName>
							<gml:MultiGeometry srsName="EPSG:100099">
								<gml:geometryMember>
									<gml:MultiPolygon>
										<gml:polygonMember>
											<gml:Polygon>
												<gml:outerBoundaryIs>
													<gml:LinearRing>
														<gml:coordinates>225433.44721,213853.93801
															223434.16069,213813.1075 223474.98616,211813.81573
															221475.69943,211772.98501 219476.4127,211732.1545
															219394.75147,215730.733 217395.46474,215689.90249
															217354.63423,217689.18901 219353.92096,217730.01973
															219231.42901,223727.88496 221230.71574,223768.71547
															223230.00751,223809.54619 225229.29424,223850.3767
															225270.12496,221851.08997 225310.95547,219851.80324
															225351.78619,217852.51147 225367.09624,217102.78081
															226116.83194,217118.09107 226121.93704,216868.17574
															226371.84229,216873.28084 226376.94739,216623.37076
															226626.85747,216628.47586 226637.06767,216128.65549
															226886.97796,216133.76059 226892.08306,215883.84526
															227391.90343,215894.05546 227432.73394,213894.76873
															225433.44721,213853.93801
														</gml:coordinates>
													</gml:LinearRing>
												</gml:outerBoundaryIs>
											</gml:Polygon>
										</gml:polygonMember>
									</gml:MultiPolygon>
								</gml:geometryMember>
								<gml:geometryMember>
									<gml:MultiPolygon>
										<gml:polygonMember>
											<gml:Polygon>
												<gml:outerBoundaryIs>
													<gml:LinearRing>
														<gml:coordinates>232360.85959,219995.77714
															232362.12883,219905.70016 234361.75219,219933.8725
															234365.27116,219683.91643 236364.89956,219712.08877
															236361.38059,219962.04484 237309.15994,219975.3979
															237347.5114,218097.49516 237388.34191,216098.20843
															235389.05539,216057.37771 233389.76341,216016.5472
															231390.48193,215975.71669 229391.1952,215934.88597
															227391.90343,215894.05546 226892.08306,215883.84526
															226886.97796,216133.76059 226637.06767,216128.65549
															226626.85747,216628.47586 226376.94739,216623.37076
															226371.84229,216873.28084 226121.93704,216868.17574
															226116.83194,217118.09107 225367.09624,217102.78081
															225351.78619,217852.51147 225310.95547,219851.80324
															227310.2422,219892.63396 229309.53397,219933.46447
															231308.8207,219974.29519 232360.85959,219995.77714
														</gml:coordinates>
													</gml:LinearRing>
												</gml:outerBoundaryIs>
											</gml:Polygon>
										</gml:polygonMember>
									</gml:MultiPolygon>
								</gml:geometryMember>
							</gml:MultiGeometry>	
						</ogc:Equals>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
				<Rule>
					<Name>Intersects</Name>
					<Title>Intersects</Title>
					<ogc:Filter>
						<ogc:Intersects>
							<ogc:PropertyName>geometry</ogc:PropertyName>
								<gml:MultiLineString srsName="EPSG:4326">
									<gml:lineStringMember>
										<gml:LineString>
											<gml:coordinates>-2.163496,46.870251 -2.164254,46.870672
											</gml:coordinates>
										</gml:LineString>
									</gml:lineStringMember>
								</gml:MultiLineString>
						</ogc:Intersects>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
				<Rule>
					<Name>Overlaps</Name>
					<Title>Overlaps</Title>
					<ogc:Filter>
						<ogc:Overlaps>
													<ogc:PropertyName>geometry</ogc:PropertyName>
							<gml:MultiPolygon srsName="urn:x-ogc:def:crs:EPSG:102113">
								<gml:polygonMember>
									<gml:Polygon>
										<gml:outerBoundaryIs>
											<gml:LinearRing>
												<gml:coordinates>-11990218.003256835,5596413.462148438
													-11613536.32791992,5723604.6771972645
													-11364045.867631836,5527925.884814454
													-11535264.810966797,5371382.850908203
													-11990218.003256835,5596413.462148438
												</gml:coordinates>
											</gml:LinearRing>
										</gml:outerBoundaryIs>
									</gml:Polygon>
								</gml:polygonMember>
							</gml:MultiPolygon>
						</ogc:Overlaps>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
				<Rule>
					<Name>Touches</Name>
					<Title>Touches</Title>
					<ogc:Filter>
						<ogc:Touches>
							<ogc:PropertyName>geometry</ogc:PropertyName>
							<gml:Polygon srsName="urn:x-ogc:def:crs:EPSG:102113">
								<gml:outerBoundaryIs>
									<gml:LinearRing>
										<gml:coordinates>-11990218.003256835,5596413.462148438
											-11613536.32791992,5723604.6771972645
											-11364045.867631836,5527925.884814454
											-11535264.810966797,5371382.850908203
											-11990218.003256835,5596413.462148438</gml:coordinates>
									</gml:LinearRing>
								</gml:outerBoundaryIs>
							</gml:Polygon>
						</ogc:Touches>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
				<Rule>
					<Name>Within</Name>
					<Title>Within</Title>
					<ogc:Filter>
						<ogc:Within>
							<ogc:PropertyName>geometry</ogc:PropertyName>
							<gml:Polygon srsName="urn:x-ogc:def:crs:EPSG:102113">
								<gml:outerBoundaryIs>
									<gml:LinearRing>
										<gml:coordinates>-11990218.003256835,5596413.462148438
											-11613536.32791992,5723604.6771972645
											-11364045.867631836,5527925.884814454
											-11535264.810966797,5371382.850908203
											-11990218.003256835,5596413.462148438</gml:coordinates>
									</gml:LinearRing>
								</gml:outerBoundaryIs>
							</gml:Polygon>
						</ogc:Within>
					</ogc:Filter>
					<PolygonSymbolizer>
						<Fill>
							<CssParameter name="fill">#66FF66</CssParameter>
						</Fill>
					</PolygonSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</NamedLayer>
</StyledLayerDescriptor>
