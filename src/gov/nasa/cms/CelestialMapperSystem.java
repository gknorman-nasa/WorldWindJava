/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

/**
 *
 * @author kjdickin
 */
public class CelestialMapperSystem 
{   
    public static final String APP_NAME = "Celestial Mapping System";
    
    static
    {
        System.setProperty("gov.nasa.worldwind.config.file","gov/nasa/cms/cms.config.xml");
    }

    public static void main(String[] args) 
    {
        ApplicationTemplate.start(APP_NAME, CelestialMapper.AppFrame.class);
    }
}
