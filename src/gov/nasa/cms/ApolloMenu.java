/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

import gov.nasa.cms.features.Apollo;
import gov.nasa.worldwind.Factory;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
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
    protected static final String IMAGE_PATH = "gov/nasa/cms/data/Apollo15.tif";
    private LayerList layerList;
    private Apollo apollo;
    private Layer apollo11;
    private Layer apollo12;
    private Layer apollo14;
    private Layer apollo15;
    private Layer apollo16;
    private Layer apollo17;

    public ApolloMenu(AppFrame frame, WorldWindow Wwd)
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
                    // Create apollo15 from the XML configuration file recieving georeferenced imagery via LROC WMS
                    apollo11 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo11.xml", null);
                    apollo11.setEnabled(true);
                    layerList.add(apollo11); // Add to the LayerList 

                    // Set the lat/lon, heading and pitch to show a "first-person" up close view of the landing site
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0.2), Angle.fromDegreesLongitude(23.47314), 10e3));
                    
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo11); // Removes Apollo 15 from LayerList
                    // Return to a global view of the moon

                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0),
                            Angle.fromDegreesLongitude(0),
                            8e6));
                }
                doClick(0); // keep layer menu open

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
                    // Create apollo15 from the XML configuration file recieving georeferenced imagery via LROC WMS
                    apollo12 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo12.xml", null);
                    apollo12.setEnabled(true);
                    layerList.add(apollo12); // Add to the LayerList 

                    // Set the lat/lon, heading and pitch to show a "first-person" up close view of the landing site
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(-3), Angle.fromDegreesLongitude(-23.5), 10.5e3));
                    getWwd().getView().setHeading(Angle.fromDegrees(10));
                    getWwd().getView().setPitch(Angle.fromDegrees(70));
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo12); // Removes Apollo 15 from LayerList
                    // Return to a global view of the moon

                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0),
                            Angle.fromDegreesLongitude(0),
                            8e6));
                }
                doClick(0); // keep layer menu open

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

                    // Set the lat/lon, heading and pitch to show a "first-person" up close view of the landing site
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(26), Angle.fromDegreesLongitude(3), 10e3));
                    getWwd().getView().setHeading(Angle.fromDegrees(90));
                    getWwd().getView().setPitch(Angle.fromDegrees(85));
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo15); // Removes Apollo 15 from LayerList
                    // Return to a global view of the moon

                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0),
                            Angle.fromDegreesLongitude(0),
                            8e6));
                }
                doClick(0); // keep layer menu open

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
                    // Create apollo15 from the XML configuration file recieving georeferenced imagery via LROC WMS
                    apollo16 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo16.xml", null);
                    apollo16.setEnabled(true);
                    layerList.add(apollo16); // Add to the LayerList 

                    // Set the lat/lon, heading and pitch to show a "first-person" up close view of the landing site
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(-8.9759), Angle.fromDegreesLongitude(15.5), 10e3));
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo16); // Removes Apollo 15 from LayerList
                    // Return to a global view of the moon

                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0),
                            Angle.fromDegreesLongitude(0),
                            8e6));
                }
                doClick(0); // keep layer menu open

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
                    // Create apollo15 from the XML configuration file recieving georeferenced imagery via LROC WMS
                    apollo17 = (Layer) factory.createFromConfigSource("gov/nasa/cms/config/apollo/Apollo17.xml", null);
                    apollo17.setEnabled(true);
                    layerList.add(apollo17); // Add to the LayerList 

                    // Set the lat/lon, heading and pitch to show a "first-person" up close view of the landing site
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(19.5), Angle.fromDegreesLongitude(30.7655), 10e3));
                    getWwd().getView().setHeading(Angle.fromDegrees(10));
                    getWwd().getView().setPitch(Angle.fromDegrees(70));
                } else
                {
                    Wwd.getModel().getLayers().remove(apollo17); // Removes Apollo 15 from LayerList
                    // Return to a global view of the moon
                    getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0),
                            Angle.fromDegreesLongitude(0),
                            8e6));
                }
                doClick(0); // keep layer menu open

            }
        });
        this.add(apolloMenuItem);
    }

    public WorldWindow getWwd()
    {
        return this.wwd;
    }

}
