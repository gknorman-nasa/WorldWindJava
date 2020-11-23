package gov.nasa.cms.features;

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
 * Apollo Menu bar created from a JMenu. The menu uses
 * {@link gov.nasa.cms.features.ApolloAnnotations} to display the Annotations
 * feature and six Apollo landing sites as JCheckBoxMenuItems. The landing
 * sites use approximate Apollo landing coordinates to place the user in a good
 * location for viewing surroundings.
 * 
 * Landing sites display Collada 3D Astronauts and the Lunar Lander.
 *
 *
 * @author kjdickin
 */
public class ApolloMenu extends JMenu
{

    private WorldWindow wwd;
    private boolean isItemEnabled;
    private LayerList layerList;
    private ApolloAnnotations apollo;
    private Layer apollo11;
    private Layer apollo12;
    private Layer apollo14;
    private Layer apollo15;
    private Layer apollo16;
    private Layer apollo17;
    private final CMSColladaViewer colladaViewer;

    public ApolloMenu(WorldWindow Wwd)
    {
        super("Apollo");
        this.setWwd(Wwd);
        setupApolloMenu();
        colladaViewer = new CMSColladaViewer(this.getWwd());
    }

    // Sets up the ApolloMenu bar by creating the 6 Apollo landing sites and Apollo annotations layer as JCheckBoxMenuItems
    protected void setupApolloMenu()
    {
        this.layerList = new LayerList();
        layerList = getWwd().getModel().getLayers(); // Retrive the layer list before adding the layers
        Factory factory = (Factory) WorldWind.createConfigurationComponent(AVKey.LAYER_FACTORY);

        //======== Annotations ========   
        apollo = new ApolloAnnotations(this.getWwd());
        this.add(apollo);

        //======== Apollo 11 ========   
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
                    apollo11 = (Layer) factory.createFromConfigSource("cms-data/apollo/Apollo11.xml", null);
                    apollo11.setEnabled(true);
                    layerList.add(apollo11); // Add to the LayerList 

                    colladaViewer.createObjects("Apollo 11");
                    // Zoom to a close up view of the Apollo landing site
                    zoomTo(LatLon.fromDegrees(0.67, 23.49), Angle.fromDegrees(30), Angle.fromDegrees(75), 2100);                   
                } else
                {
                    getWwd().getModel().getLayers().remove(apollo11); // Removes Apollo 11 from LayerList
                    colladaViewer.removeColladaObjects();
                    // Return to a global view of the moon
                    zoomTo(LatLon.fromDegrees(0, 0), Angle.fromDegrees(0), Angle.fromDegrees(0), 8e6);
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
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    apollo12 = (Layer) factory.createFromConfigSource("cms-data/apollo/Apollo12.xml", null);
                    apollo12.setEnabled(true);
                    layerList.add(apollo12);
                    colladaViewer.createObjects("Apollo 12");
                    zoomTo(LatLon.fromDegrees(-3.07, -23.44), Angle.fromDegrees(20), Angle.fromDegrees(75), 2500);
                } else
                {
                    getWwd().getModel().getLayers().remove(apollo12);
                    colladaViewer.removeColladaObjects();
                    zoomTo(LatLon.fromDegrees(0, 0), Angle.fromDegrees(0), Angle.fromDegrees(0), 8e6);
                }

            }
        });
        this.add(apolloMenuItem);

        //======== Apollo 14 ========   
        apolloMenuItem = new JCheckBoxMenuItem("Apollo 14");
        apolloMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    apollo14 = (Layer) factory.createFromConfigSource("cms-data/apollo/Apollo14.xml", null);
                    apollo14.setEnabled(true);
                    layerList.add(apollo14);
                    
                    colladaViewer.createObjects("Apollo 14");

                    zoomTo(LatLon.fromDegrees(-3.7, -17.5), Angle.fromDegrees(30), Angle.fromDegrees(75), 2600);
                } else
                {
                    getWwd().getModel().getLayers().remove(apollo14);
                    colladaViewer.removeColladaObjects();
                    zoomTo(LatLon.fromDegrees(0, 0), Angle.fromDegrees(0), Angle.fromDegrees(0), 8e6);
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
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    apollo15 = (Layer) factory.createFromConfigSource("cms-data/apollo/Apollo15.xml", null);
                    apollo15.setEnabled(true);
                    layerList.add(apollo15); // Add to the LayerList 
                    
                    colladaViewer.createObjects("Apollo 15");

                    zoomTo(LatLon.fromDegrees(26.1, 3.65), Angle.fromDegrees(140), Angle.fromDegrees(80), 4300);
                } else
                {
                    getWwd().getModel().getLayers().remove(apollo15);
                    colladaViewer.removeColladaObjects();
                    zoomTo(LatLon.fromDegrees(0, 0), Angle.fromDegrees(20), Angle.fromDegrees(0), 8e6);
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
                    apollo16 = (Layer) factory.createFromConfigSource("cms-data/apollo/Apollo16.xml", null);
                    apollo16.setEnabled(true);
                    layerList.add(apollo16);
                    
                    colladaViewer.createObjects("Apollo 16");

                    zoomTo(LatLon.fromDegrees(-8.9975, 15.51), Angle.fromDegrees(40), Angle.fromDegrees(75), 3000);
                } else
                {
                    getWwd().getModel().getLayers().remove(apollo16);
                    colladaViewer.removeColladaObjects();
                    zoomTo(LatLon.fromDegrees(0, 0), Angle.fromDegrees(0), Angle.fromDegrees(0), 8e6);
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
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    apollo17 = (Layer) factory.createFromConfigSource("cms-data/apollo/Apollo17.xml", null);
                    apollo17.setEnabled(true);
                    layerList.add(apollo17);
                    
                    colladaViewer.createObjects("Apollo 17");

                    zoomTo(LatLon.fromDegrees(20.15, 30.72), Angle.fromDegrees(90), Angle.fromDegrees(70), 2800);
                } else
                {
                    getWwd().getModel().getLayers().remove(apollo17);
                    colladaViewer.removeColladaObjects();
                    zoomTo(LatLon.fromDegrees(0, 0), Angle.fromDegrees(0), Angle.fromDegrees(0), 8e6);
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

    public void setWwd(WorldWindow Wwd)
    {
        this.wwd = Wwd;
    }

    public WorldWindow getWwd()
    {
        return this.wwd;
    }

}
