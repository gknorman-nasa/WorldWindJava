/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;

/**
 *
 * @author kjdickin
 */
public class CelestialMappingSystem 
{   
    public static final String APP_NAME = "Celestial Mapping System";
     
    public static void main(String[] args) 
    {
        ApplicationTemplate.start(APP_NAME, CelestialMapper.AppFrame.class);
    }
}
