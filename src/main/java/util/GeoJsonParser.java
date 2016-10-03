package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeoJsonParser {

    private Coordinate[] coordinates;
    private int numPoints;

    public GeoJsonParser(String geoJSON) throws Exception{
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem fromCRS = factory.createFromName("EPSG:32633");
        CoordinateReferenceSystem toCRS = fromCRS.createGeographic();
        BasicCoordinateTransform bct = new BasicCoordinateTransform(fromCRS, toCRS);
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(geoJSON);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray featuresArray = (JSONArray) jsonObject.get("features");
            JSONObject featuresObj = (JSONObject) featuresArray.get(0);
            JSONObject geometryObj = (JSONObject) featuresObj.get("geometry");
            String coordinatesRawJson = geometryObj.get("coordinates").toString(); //Henter ut raw JSON for å konvertere til WKT


            String sWKT = geojson2wkt(coordinatesRawJson);
            if (sWKT.isEmpty()) return;
            System.out.println("GeoUtility.sWKT= " + sWKT);

            Geometry geometry = new WKTReader().read(sWKT);
            numPoints = geometry.getNumPoints();
            coordinates = geometry.getCoordinates();

            for (int i = 0; i < numPoints; i++) {

                ProjCoordinate src = new ProjCoordinate(coordinates[i].x, coordinates[i].y);
                ProjCoordinate tgt = bct.transform(src, new ProjCoordinate());
                coordinates[i].x = tgt.x;
                coordinates[i].y = tgt.y;

            }
        } catch (ParseException | IllegalArgumentException | org.json.simple.parser.ParseException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Invalid geoJSON");
        }
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    private String geojson2wkt(String s) throws IllegalArgumentException {

        // input [[[271324.34716797,7039278.9169922],[271374.92382812,7039297.4287109],[271387.15478516,7039267.0166015],[271347.15625,7039248.5048828],[271324.34716797,7039278.9169922]
        // Fra etter coordinates
        System.out.println("GeoUtility s=" + s);
        s = s.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "");
        // Nå skal vi ha en string med bare tall
        System.out.println("GeoUtility s=" + s);
        String wkt = "";
        s = s + ",";

        int i = s.indexOf(",");
        String sA, sB;
        // Erstatt komma med space mellom tallene som utgjør et punkt (x y).
        // Behold komma mellom punktene
        while (i > -1) {
            sA = s.substring(0, i); //x
            s = s.substring(i + 1);

            i = s.indexOf(",");
            sB = s.substring(0, i); //y
            s = s.substring(i + 1);

            if (!wkt.isEmpty()) wkt = wkt + ",";
            wkt = wkt + sA + " " + sB;

            i = s.indexOf(",");
            if (i == -1) break;
        }
        System.out.println("GeoUtility wkt=" + wkt);

        return "LINESTRING (" + wkt + ")";

    }
}
