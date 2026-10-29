# Maharashtra Railway Map — Coordinate Starter Dataset

Prepared: 2026-10-25

## Files
- `maharashtra_railway_coordinates.xlsx`: Excel workbook with overview, all map labels, actual station nodes, and sources/caveats.
- `maharashtra_map_labels_coordinates.csv`: 36 map labels. A label can be a real station point or an approximate city/district marker.
- `maharashtra_railway_station_nodes.csv`: 32 actual station nodes with station code and WGS84 latitude/longitude.
- `maharashtra_railway_station_nodes.geojson`: GeoJSON Point features for the 32 actual station nodes. GeoJSON coordinate order is **[longitude, latitude]**.

## Important use notes
1. The reference map itself is marked “Map not to Scale” and is dated 2015. This dataset geocodes the named locations; it does **not** digitize the railway lines from the raster map and does not represent a complete Maharashtra station inventory or route graph.
2. In the map-label CSV / workbook, rows whose `point_type` says “District HQ / city marker” are **not train stops**. They use approximate city-centre coordinates for cartographic context only. Do not send routing or train search requests to those points as if they were stations.
3. Bhandara is treated as a district-HQ marker in the map-label list. The rail station is **Bhandara Road (BRD)** at Warthi, with its own coordinates in the station-node dataset.
4. The map uses some older city names: Ahmednagar is now Ahilyanagar, Aurangabad is now Chhatrapati Sambhajinagar, and Osmanabad is now Dharashiv. Legacy station names/codes are retained where that is what the coordinate source provides; verify these in your live railway API.
5. The Ministry of Railways reported on 11 February 2026 that the Wardha–Yavatmal–Nanded new line had been sanctioned, the Wardha–Kalamb section commissioned, and work on the balance section taken up. Yavatmal is therefore included only as a city marker, not as a confirmed active station node.
6. Beed station opened in September 2025 and the Beed–Pune service began in September 2026, so Beed is added to the station-node list even though the old 2015 map does not show it as a current node.

## Coordinate sources
- Indian Railway station-coordinate open dataset (coordinates, names and codes): https://raw.githubusercontent.com/prasenjit-27/Indian-Railway-Data/main/stations.json
- Nashik Road station details: https://en.wikipedia.org/wiki/Nasik_Road_railway_station
- Ahilyanagar station details: https://www.railopedia.com/stations/ahmednagar
- Beed station coordinates and opening: https://en.wikipedia.org/wiki/Beed_railway_station
- Mumbai CSMT coordinates (OpenStreetMap-based reference): https://mcgm.svs.io/facilities/89
- Ministry of Railways / PIB, 11 February 2026 (Wardha–Yavatmal–Nanded line status): https://www.pib.gov.in/PressReleasePage.aspx?PRID=2226418&lang=1&reg=1
- Ministry of Railways / PIB, 12 August 2026 (new-line progress, including the Ahilyanagar–Beed section): https://www.pib.gov.in/PressReleasePage.aspx?PRID=2298423&lang=1&reg=1

## Coordinate standard
All numeric coordinates are WGS84 decimal degrees. CSV columns are `latitude` then `longitude`; GeoJSON stores coordinates in the required `[longitude, latitude]` order. Some station codes or names from third-party datasets may be legacy values; verify codes and operating status against official Indian Railways / NTES / authorized API before using them for live schedules, fares, or seat availability.
