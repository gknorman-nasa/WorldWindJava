/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.worldwindx.examples.kml;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.Logging;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.util.*;

/**
 * Shows how to generate KML from WorldWind elements. This example creates several objects, and writes their KML
 * representation to stdout.
 *
 * @author pabercrombie
 * @version $Id: ExportKML.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class ExportKML
{
    protected static ShapeAttributes normalShapeAttributes;
    protected static ShapeAttributes highlightShapeAttributes;

    protected static PointPlacemark makePointPlacemark()
    {
        PointPlacemark placemark = new PointPlacemark(Position.fromDegrees(37.824713, -122.370028, 0.0));

        placemark.setLabelText("Treasure Island");
        placemark.setValue(AVKey.SHORT_DESCRIPTION, "Sample placemark");
        placemark.setValue(AVKey.BALLOON_TEXT, "This is a <b>Point Placemark</b>");

        placemark.setLineEnabled(false);
        placemark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);

        return placemark;
    }

    protected static Path makePath()
    {
        

        List<Position> positions = Arrays.asList(
            Position.fromDegrees(37.8304, -122.3720, 0),
            Position.fromDegrees(37.8293, -122.3679, 0),
            Position.fromDegrees(37.8282, -122.3710, 0));
            Path path = new Path(positions);


       // path.setAttributes(normalShapeAttributes);
       // path.setHighlightAttributes(highlightShapeAttributes);

     //   path.setValue(AVKey.SHORT_DESCRIPTION, "Short description of Path");
     //   path.setValue(AVKey.BALLOON_TEXT, "This is a Path.");

        return path;
    }

    protected static Polygon makePolygon()
    {
        

        List<Position> positions = Arrays.asList(
            Position.fromDegrees(28, -106, 0),
            Position.fromDegrees(35, -104, 0),
            Position.fromDegrees(28, -107, 100),
            Position.fromDegrees(28, -106, 0));

       Polygon poly = new Polygon(positions);
        
      //  poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        //poly.setAttributes(normalShapeAttributes);
      //  poly.setHighlightAttributes(highlightShapeAttributes);

        //poly.setValue(AVKey.SHORT_DESCRIPTION, "Short description of Polygon");
        //poly.setValue(AVKey.BALLOON_TEXT, "This is a Polygon.");

        return poly;
    }

    protected static SurfaceQuad makeSurfaceQuad()
    {
        return new SurfaceQuad(LatLon.fromDegrees(45, 100), 1e4, 2e4, Angle.ZERO);
    }

    /**
     * Generate sample PointPlacemarks, Paths, and Polygons, and write the KML representation to stdout.
     *
     * @param args Not used.
     */
    public static void main(String[] args)
    {
        try
        {
           
            normalShapeAttributes = new BasicShapeAttributes();
            normalShapeAttributes.setInteriorMaterial(Material.BLUE);
            normalShapeAttributes.setOutlineMaterial(Material.BLACK);

            highlightShapeAttributes = new BasicShapeAttributes();
            highlightShapeAttributes.setInteriorMaterial(Material.RED);
            highlightShapeAttributes.setOutlineMaterial(Material.BLACK);

            // Create a StringWriter to collect KML in a string buffer
            //Writer stringWriter = new StringWriter();

            // Create a new FileOutputStream to the user's home directory
            OutputStream os = new FileOutputStream(Configuration.getUserHomeDirectory() + "/ExportKMLTest3.kml");
            // Build the KML document from the file stream
            KMLDocumentBuilder kmlBuilder = new KMLDocumentBuilder(os);
            // Create a document builder that will write KML to the StringWriter
//            KMLDocumentBuilder kmlBuilder = new KMLDocumentBuilder(stringWriter);
//
            // Export the objects
            kmlBuilder.writeObjects(
                makeSurfaceQuad(),
                makePointPlacemark(),
                makePath(),
                makePolygon());

            kmlBuilder.close();
//
//            // Get the exported document as a string
//            String xmlString = stringWriter.toString();
//
//            // Set up a transformer to pretty-print the XML
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//
//            // Write the pretty-printed document to stdout
//            transformer.transform(new StreamSource(new StringReader(xmlString)), new StreamResult(System.out));
        }
        catch (Exception e)
        {
            String message = Logging.getMessage("generic.ExceptionAttemptingToWriteXml", e.toString());
            Logging.logger().severe(message);
            e.printStackTrace();
        }
    }
}
