/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

import gov.nasa.cms.CelestialMapper.AppFrame;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.*;
import gov.nasa.worldwind.layers.Layer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author twchoi
 */
public class CMSStereo extends JCheckBoxMenuItem
{

    private WorldWindow wwd;
    private boolean isItemEnabled;
    
    public CMSStereo(AppFrame cms, WorldWindow Wwd)
    {   
        super("Stereo");

        this.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {

                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled) {
                    // Set the stereo.mode property to request stereo. Request red-blue anaglyph in this case. Can also request
                    // "device" if the display device supports stereo directly. To prevent stereo, leave the property unset or set
                    // it to an empty string.
                    System.setProperty("gov.nasa.worldwind.stereo.mode", "redblue");

                    // Configure the initial view parameters so that the balloons are immediately visible.
                    Configuration.setValue(AVKey.INITIAL_LATITUDE, 46.7045);
                    Configuration.setValue(AVKey.INITIAL_LONGITUDE, -121.6242);
                    Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10e3);
                    Configuration.setValue(AVKey.INITIAL_HEADING, 342);
                    Configuration.setValue(AVKey.INITIAL_PITCH, 80);
                }
                else {
                    String chosenName = event.getActionCommand();
                    Layer selectedLayer = Wwd.getModel().getLayers().getLayerByName(chosenName);
                    Wwd.getModel().getLayers().remove(selectedLayer); //removes Stereo layer from layer list              
                }
            }
        });
        
    }
    
     public WorldWindow getWwd()
    {
        return this.wwd;
    }

    public void setWwd(WorldWindow wwd)
    {
        this.wwd = wwd;
    }
    
    
//    public static void main(String[] args)
//    {
//        
//
//        gov.nasa.cms.ApplicationTemplate.start("CMS Stereo", gov.nasa.cms.ApplicationTemplate.AppFrame.class);
//    }
}
