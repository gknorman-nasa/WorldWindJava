/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.worldwind.globes;

import gov.nasa.worldwind.avlist.AVKey;

/**
 * Defines a model of the Moon, using the <a href="http://en.wikipedia.org/wiki/World_Geodetic_System"
 * target="_blank">World Geodetic System</a> (WGS84).
 *
 * @author Tom Gaskins
 * @version $Id: Earth.java 1958 2014-04-24 19:25:37Z tgaskins $
 */

public class Earth extends EllipsoidalGlobe
{
    public static final double WGS84_EQUATORIAL_RADIUS = 1737400; // ellipsoid equatorial getRadius, in meters
    public static final double WGS84_POLAR_RADIUS = 1737400; // ellipsoid polar getRadius, in meters
    public static final double WGS84_ES = 0.0; // eccentricity squared, semi-major axis


    public static final double ELEVATION_MIN = -9000; // Depth of Antoniadi Crater
    public static final double ELEVATION_MAX = 10700; // Height of Selean Summit.

    public Earth()
    {
        super(WGS84_EQUATORIAL_RADIUS, WGS84_POLAR_RADIUS, WGS84_ES,
            EllipsoidalGlobe.makeElevationModel(AVKey.MOON_ELEVATION_MODEL_CONFIG_FILE,
                "cms-data/layers/EarthElevations2.xml"));
    }

    public String toString()
    {
        return "Earth";
    }
}
