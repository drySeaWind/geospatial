# geospatial
backend for geospatial app - queries geometry points from arbitrary area.

Uses postGIS to store and fetch geometry points. User can fetch geometry points inside arbitrary polygon shape.
There is no postgis (postgis.jar) extensions in classpath of postgres driver (in Wildfly container) so only Text Representations (WKT) of PostGis geometry objects are available. However function ST_GeomFromGeoJSON works and is useful to pass polygon with undefined amount of coordinate points into the query.

In this example input data streamed to database is in TM35FIN coordinate system (SRID 3067) and it is not very accurate on certain corners of the area.
If greater accuracy is desired on may try to update spatial reference system entries: http://latuviitta.org/documents/YKJ-TM35FIN_muunnos_ogr2ogr_cs2cs.txt

# endpoints

Use curl -v POST http://yourdockerhost:18080/geospatial/upload -d @trunks.json --header "Content-Type: application/json"
to stream points into the database. Input json should contain following json objects:

{"area": 0.000000000, (some numerical data describing the point, double)
 "height":0.000000000, (double)
 "species":"birch", (some string data describing the point)
 "x":000000.00000000000 (latitude in TM35FIN system),
 "y":0000000.00000000000} (longitude in TM35FIN system)

Use curl -v POST http://yourdockerhost:18080/geospatial/rest/trunks/inarea -d @test.json --header "Content-Type: application/json"

to pass polygon shape in geoJson and query points inside polygons area. json should look like this:

{
	"type": "Polygon",
	"coordinates": [
		[
			[22.367672757355923, 60.847698232804973],
			[22.366103072838264, 60.848481594419098],
			[22.36615704790329, 60.848501204978056],
			[22.366394580499936, 60.848582391507655]
		]
	]
}

# docker

building the image (in project root folder): " ./build.sh "

needed things:
maven 3.3.0 or greater and 
JDK 1.8 (8u172 is in the container)

running the container: docker-compose up -d

wildfly start is not delayed and sometimes postGIS might not be ready for connections while 
Wildfly has already started and failed to construct datasource and connection pool. In such case just restart wildfly container
in order to connect to running postGIS:

docker stop geospatial, 
docker start geospatial

# environmental variables

      POSTGRES_DB_HOST: "postgres-test"
      POSTGRES_DB_NAME: "test"
      POSTGRES_DB_USERNAME: "postgres"
      POSTGRES_DB_PASSWORD: "user123"
      RESULTSET_LIMIT: "20000" 

ResultSet limit can be used for simple paging pattern. User has to zoom closer and request 
data with new shape (which is limited by zoomed area)










