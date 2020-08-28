/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms.features;

import gov.nasa.cms.features.Apollo;
import gov.nasa.worldwind.Factory;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

/**
 * Menu bar for Apollo Currently in demo to show different ways to view the
 * georeferenced imagery
 *
 * @author kjdickin
 */
public class ApolloMenu extends JMenu
{

    private WorldWindow wwd;
    private boolean isItemEnabled;
    //protected AppFrame app;
    private LayerList layerList;
    private Apollo apollo;
    private Layer apollo11;
    private Layer apollo12;
    private Layer apollo15;
    private Layer apollo16;
    private Layer apollo17;

    public ApolloMenu(WorldWindow Wwd)
    {
        super("Apollo");
        this.setWwd(Wwd);
    }

    public void setWwd(WorldWindow Wwd)
    {
        this.wwd = Wwd;
        this.layerList = new LayerList();
        layerList = getWwd().getModel().getLayers(); // Retrive the layer list before adding the layers
        Factory factory = (Factory) WorldWind.createConfigurationComponent(AVKey.LAYER_FACTORY);

        //======== Annotations ========   
        apollo = new Apollo(this.getWwd());
        this.add(apollo);

        //======== Apollo 12 ========   
        JCheckBoxMenuItem apolloMenuItem = new JCheckBoxMenuItem("Apollo 11");
        apolloMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                // Enable and disable when clicked 
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    // Create from the XML configuration file recieving georeferenced imagery via LROC WMS
                    apollo11 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo11.xml", null);
                    apollo11.setEnabled(true);
                    layerList.add(apollo11); // Add to the LayerList 

                    // Zoom to a close up view of the Apollo landing site
                    zoomTo(LatLon.fromDegrees(0.6, 23.48), Angle.fromDegrees(10), Angle.fromDegrees(70), 2000);

                } else
                {
                    Wwd.getModel().getLayers().remove(apollo11); // Removes Apollo 11 from LayerList
                    
                    // Return to a global view of the moon
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0), Angle.fromDegreesLongitude(0), 8e6));
                }

            }
        });
        this.add(apolloMenuItem);
        //======== Apollo 12 ========   
        apolloMenuItem = new JCheckBoxMenuItem("Apollo 12");
        apolloMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                // Enable and disable when clicked 
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    apollo12 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo12.xml", null);
                    apollo12.setEnabled(true);
                    layerList.add(apollo12); 

                    zoomTo(LatLon.fromDegrees(-3.03, -23.43), Angle.fromDegrees(10), Angle.fromDegrees(70), 1200);
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo12); 
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0), Angle.fromDegreesLongitude(0), 8e6));
                }

            }
        });
        this.add(apolloMenuItem);

        //======== Apollo 15 ========   
        apolloMenuItem = new JCheckBoxMenuItem("Apollo 15");
        apolloMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                // Enable and disable when clicked 
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    // Create apollo15 from the XML configuration file recieving georeferenced imagery via LROC WMS
                    apollo15 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo15.xml", null);
                    apollo15.setEnabled(true);
                    layerList.add(apollo15); // Add to the LayerList 

                    zoomTo(LatLon.fromDegrees(26, 3.5), Angle.fromDegrees(90), Angle.fromDegrees(70), 3e4);
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo15); 
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0), Angle.fromDegreesLongitude(0), 8e6));
                }

            }
        });
        this.add(apolloMenuItem);

        //======== Apollo 16 ========   
        apolloMenuItem = new JCheckBoxMenuItem("Apollo 16");
        apolloMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                // Enable and disable when clicked 
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    apollo16 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo16.xml", null);
                    apollo16.setEnabled(true);
                    layerList.add(apollo16); 
                    
                    zoomTo(LatLon.fromDegrees(-8.9, 15.5), Angle.fromDegrees(30), Angle.fromDegrees(70), 2e4);
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo16);
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0), Angle.fromDegreesLongitude(0), 8e6));
                }

            }
        });
        this.add(apolloMenuItem);

        //======== Apollo 17 ========   
        apolloMenuItem = new JCheckBoxMenuItem("Apollo 17");
        apolloMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                // Enable and disable when clicked 
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    apollo17 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo17.xml", null);
                    apollo17.setEnabled(true);
                    layerList.add(apollo17); // Add to the LayerList 

                    zoomTo(LatLon.fromDegrees(19.5, 30.5), Angle.fromDegrees(30), Angle.fromDegrees(70), 3e4);
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo17);
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0), Angle.fromDegreesLongitude(0), 8e6));
                }

            }
        });
        this.add(apolloMenuItem);
    }

    // Zooms to the landing site at the passed in latitude/longitude, heading, pitch and zoom level
    protected void zoomTo(LatLon latLon, Angle heading, Angle pitch, double zoom)
    {
        BasicOrbitView view = (BasicOrbitView) this.getWwd().getView();
        view.stopMovement();
        view.addPanToAnimator(new Position(latLon, 0), heading, pitch, zoom, true);
    }

    public WorldWindow getWwd()
    {
        return this.wwd;
    }

}
