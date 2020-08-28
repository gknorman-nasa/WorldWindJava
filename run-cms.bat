@echo off
REM Copyright (C) 2012 United States Government as represented by the Administrator of the
REM National Aeronautics and Space Administration.
REM All Rights Reserved.

@echo Running gov.nasa.cms.CelestialMappingSystem
java -Xmx1024m -Dsun.java2d.noddraw=true -classpath .\cms.jar;.\worldwind.jar;.\worldwindx.jar;.\gdal.jar;.\jogl-all.jar;.\gluegen-rt.jar gov.nasa.cms.CelestialMappingSystem
