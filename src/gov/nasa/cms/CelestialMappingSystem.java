/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

import gov.nasa.worldwind.Configuration;
import javax.swing.JFrame;

/**
 *
 * @author kjdickin
 */
public class CelestialMappingSystem 
{   
    public static final String APP_NAME = "Celestial Mapping System";
     
    public static void main(String[] args) 
    {               
        if (Configuration.isMacOS()) {
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", APP_NAME);
        }

        try {
            CelestialMapper cms = new CelestialMapper();
            cms.initialize();
            cms.setTitle(APP_NAME);
            cms.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            java.awt.EventQueue.invokeLater(() -> {
                cms.setVisible(true);
            });

//            return frame;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
