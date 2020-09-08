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
        Path path = new Path();

        List<Position> positions = Arrays.asList(
            Position.fromDegrees(37.8304, -122.3720, 0),
            Position.fromDegrees(37.8293, -122.3679, 0),
            Position.fromDegrees(37.8282, -122.3710, 0));

        path.setPositions(positions);
        path.setExtrude(true);

        path.setAttributes(normalShapeAttributes);
        path.setHighlightAttributes(highlightShapeAttributes);

        path.setValue(AVKey.SHORT_DESCRIPTION, "Short description of Path");
        path.setValue(AVKey.BALLOON_TEXT, "This is a Path.");

        return path;
    }

    protected static Polygon makePolygon()
    {
        Polygon poly = new Polygon();

        List<Position> outerBoundary = Arrays.asList(
            Position.fromDegrees(37.8224479345424, -122.3739784354151, 0.0),
            Position.fromDegrees(37.82239261906633, -122.3740285701554, 0.0),
            Position.fromDegrees(37.82240608112512, -122.3744696934806, 0.0),
            Position.fromDegrees(37.82228167878964, -122.3744693163394, 0.0),
            Position.fromDegrees(37.82226619249474, -122.3739902862858, 0.0),
            Position.fromDegrees(37.82219810227204, -122.3739510452131, 0.0),
            Position.fromDegrees(37.82191990027978, -122.3742004406226, 0.0),
            Position.fromDegrees(37.82186185177756, -122.3740740264531, 0.0),
            Position.fromDegrees(37.8221330487949, -122.3738377669854, 0.0),
            Position.fromDegrees(37.82213842777661, -122.3737599855226, 0.0),
            Position.fromDegrees(37.82184815805735, -122.3735538230499, 0.0),
            Position.fromDegrees(37.82188747252212, -122.3734202823307, 0.0),
            Position.fromDegrees(37.8222030233808, -122.37362176179, 0.0),
            Position.fromDegrees(37.8222686063349, -122.3735762207482, 0.0),
            Position.fromDegrees(37.82224254303025, -122.3731468984375, 0.0),
            Position.fromDegrees(37.82237319467147, -122.3731303943743, 0.0),
            Position.fromDegrees(37.82238194814573, -122.3735637823936, 0.0),
            Position.fromDegrees(37.8224405243797, -122.3736008458059, 0.0),
            Position.fromDegrees(37.82274355652806, -122.3734009024945, 0.0),
            Position.fromDegrees(37.8228008408153, -122.373091430554, 0.0),
            Position.fromDegrees(37.82251198652374, -122.3737489159765, 0.0),
            Position.fromDegrees(37.82251207172572, -122.3738269699774, 0.0),
            Position.fromDegrees(37.82280161524027, -122.3740332968739, 0.0),
            Position.fromDegrees(37.82275318071796, -122.3741825267907, 0.0),
            Position.fromDegrees(37.8224479345424, -122.3739784354151, 0.0));

        List<Position> innerBoundary = Arrays.asList(
            Position.fromDegrees(37.82237624346899, -122.3739179072036, 0.0),
            Position.fromDegrees(37.82226147323489, -122.3739053159649, 0.0),
            Position.fromDegrees(37.82221834573171, -122.3737889140025, 0.0),
            Position.fromDegrees(37.8222627093125, -122.3736772434448, 0.0),
            Position.fromDegrees(37.82237889526623, -122.3736727730745, 0.0),
            Position.fromDegrees(37.82243486851886, -122.3737811526564, 0.0),
            Position.fromDegrees(37.82237624346899, -122.3739179072036, 0.0));

        poly.setOuterBoundary(outerBoundary);
        poly.addInnerBoundary(innerBoundary);
        poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        poly.setAttributes(normalShapeAttributes);
        poly.setHighlightAttributes(highlightShapeAttributes);

        poly.setValue(AVKey.SHORT_DESCRIPTION, "Short description of Polygon");
        poly.setValue(AVKey.BALLOON_TEXT, "This is a Polygon.");

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
            OutputStream os = new FileOutputStream(Configuration.getUserHomeDirectory() + "/ExportKMLTest.kml");
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
