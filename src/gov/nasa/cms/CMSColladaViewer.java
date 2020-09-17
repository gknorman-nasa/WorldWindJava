/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.collada.ColladaRoot;
import gov.nasa.worldwind.ogc.collada.impl.ColladaController;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import static gov.nasa.worldwindx.examples.ApplicationTemplate.start;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.SwingUtilities;

/**
 * Shows how to load <a href="https://www.khronos.org/collada/">COLLADA</a> 3D models.
 */

/**
 *
 * @author twchoi
 */
public class CMSColladaViewer extends JCheckBoxMenuItem {

    private WorldWindow wwd;
    
    private boolean isItemEnabled;
    
    public CMSColladaViewer(WorldWindow Wwd)
    {
        super("3D Objects");
        setWwd(Wwd);
        
        this.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent event)
            {
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();
                
                if (isItemEnabled)
                {
                    createObjects();
                    
                    zoomTo(LatLon.fromDegrees(20.1653, 30.7658), Angle.fromDegrees(30), Angle.fromDegrees(70), 3e4);
 
                }
            }
    
        });
        
        
    }
   
    protected void createObjects()
    {
        Position LunarLanderPosition = Position.fromDegrees(20.1653, 30.7658, 200);
        File ColladaFile = new File("testData/collada/collada.dae");

        // Invoke the <code>Thread</code> to load the COLLADA model asynchronously.
        new WorkerThread(ColladaFile, LunarLanderPosition).start();
        
        Position FirstAstronautPosition = Position.fromDegrees(20.1640, 30.7255, 200);
        File ColladaFile2 = new File("testData/collada/cube_triangulate.dae");
        new WorkerThread(ColladaFile2, FirstAstronautPosition).start();
        
        
        Position SecondAstronautPosition = Position.fromDegrees(20.12, 30.7461, 200);
        File ColladaFile3 = new File("testData/collada/duck_triangulate.dae");
        new WorkerThread(ColladaFile3, SecondAstronautPosition).start();
    }
    
    /**
         * Adds the specified <code>colladaRoot</code> to this app frame's <code>WorldWindow</code> as a new
         * <code>Layer</code>.
         *
         * @param colladaRoot the ColladaRoot to add a new layer for.
         */
    protected void addColladaLayer(ColladaRoot colladaRoot) {
        // Create a ColladaController to adapt the ColladaRoot to the WorldWind renderable interface.
        ColladaController colladaController = new ColladaController(colladaRoot);

        // Adds a new layer containing the ColladaRoot to the end of the WorldWindow's layer list.
        RenderableLayer layer = new RenderableLayer();
        layer.addRenderable(colladaController);
        getWwd().getModel().getLayers().add(layer);
    }
    // A <code>Thread</code> that loads a COLLADA file and displays it in an <code>AppFrame</code>.
    public class WorkerThread extends Thread {

        // Indicates the source of the COLLADA file loaded by this thread. Initialized during construction.
        protected Object colladaSource;

        // Geographic position of the COLLADA model.
        protected Position position;

        // Indicates the <code>AppFrame</code> the COLLADA file content is displayed in. Initialized during
        // construction.
        protected AppFrame appFrame;

        /**
         * Creates a new worker thread from a specified <code>colladaSource</code> and <code>appFrame</code>.
         *
         * @param colladaSource the source of the COLLADA file to load. May be a {@link java.io.File}, a {@link
         *                      java.net.URL}, or an {@link java.io.InputStream}, or a {@link String} identifying a file path or URL.
         * @param position the geographic position of the COLLADA model.
         */
        public WorkerThread(Object colladaSource, Position position) {
            this.colladaSource = colladaSource;
            this.position = position;
        
        }

        /**
         * Loads this worker thread's COLLADA source into a new
         * <code>{@link gov.nasa.worldwind.ogc.collada.ColladaRoot}</code>, then adds the new <code>ColladaRoot</code>
         * to this worker thread's <code>AppFrame</code>.
         */
        @Override
        public void run() {
            try {
                final ColladaRoot colladaRoot = ColladaRoot.createAndParse(this.colladaSource);
                colladaRoot.setPosition(this.position);
                colladaRoot.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);

                // Schedule a task on the EDT to add the parsed document to a layer
                SwingUtilities.invokeLater(() -> {
                    addColladaLayer(colladaRoot);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    protected void zoomTo(LatLon latLon, Angle heading, Angle pitch, double zoom)
    {
        BasicOrbitView view = (BasicOrbitView) getWwd().getView();
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
    
//    public static class AppFrame extends ApplicationTemplate.AppFrame {
//
//        public AppFrame() {
//            super(true, true, false); // Don't include the layer panel; we're using the on-screen layer tree.
//
//            // Size the WorldWindow to take up the space typically used by the layer panel.
//            Dimension size = new Dimension(1400, 800);
//            this.setPreferredSize(size);
//            this.pack();
//            WWUtil.alignComponent(null, this, AVKey.CENTER);
//            LayerList layers = getWwd().getModel().getLayers();
//            for (Layer layer : layers) {
//                String layerName = layer.getName();
//                if (layerName != null && layerName.toLowerCase().startsWith("bing")) {
//                    layer.setEnabled(true);
//                    break;
//                }
//            }
//        }

//        /**
//         * Adds the specified <code>colladaRoot</code> to this app frame's <code>WorldWindow</code> as a new
//         * <code>Layer</code>.
//         *
//         * @param colladaRoot the ColladaRoot to add a new layer for.
//         */
//        protected void addColladaLayer(ColladaRoot colladaRoot) {
//            // Create a ColladaController to adapt the ColladaRoot to the WorldWind renderable interface.
//            ColladaController colladaController = new ColladaController(colladaRoot);
//
//            // Adds a new layer containing the ColladaRoot to the end of the WorldWindow's layer list.
//            RenderableLayer layer = new RenderableLayer();
//            layer.addRenderable(colladaController);
//            this.getWwd().getModel().getLayers().add(layer);
//        }
    

//    // A <code>Thread</code> that loads a COLLADA file and displays it in an <code>AppFrame</code>.
//    public static class WorkerThread extends Thread {
//
//        // Indicates the source of the COLLADA file loaded by this thread. Initialized during construction.
//        protected Object colladaSource;
//
//        // Geographic position of the COLLADA model.
//        protected Position position;
//
//        // Indicates the <code>AppFrame</code> the COLLADA file content is displayed in. Initialized during
//        // construction.
//        protected AppFrame appFrame;
//
//        /**
//         * Creates a new worker thread from a specified <code>colladaSource</code> and <code>appFrame</code>.
//         *
//         * @param colladaSource the source of the COLLADA file to load. May be a {@link java.io.File}, a {@link
//         *                      java.net.URL}, or an {@link java.io.InputStream}, or a {@link String} identifying a file path or URL.
//         * @param position the geographic position of the COLLADA model.
//         * @param appFrame the <code>AppFrame</code> in which to display the COLLADA source.
//         */
//        public WorkerThread(Object colladaSource, Position position, AppFrame appFrame) {
//            this.colladaSource = colladaSource;
//            this.position = position;
//            this.appFrame = appFrame;
//        }
//
//        /**
//         * Loads this worker thread's COLLADA source into a new
//         * <code>{@link gov.nasa.worldwind.ogc.collada.ColladaRoot}</code>, then adds the new <code>ColladaRoot</code>
//         * to this worker thread's <code>AppFrame</code>.
//         */
//        @Override
//        public void run() {
//            try {
//                final ColladaRoot colladaRoot = ColladaRoot.createAndParse(this.colladaSource);
//                colladaRoot.setPosition(this.position);
//                colladaRoot.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
//
//                // Schedule a task on the EDT to add the parsed document to a layer
//                SwingUtilities.invokeLater(() -> {
//                    appFrame.addColladaLayer(colladaRoot);
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public static void main(String[] args) {
//
//        // Set camera position and pitch angle.
//        Configuration.setValue(AVKey.INITIAL_LATITUDE, 40.028);
//        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -105.27284091410579);
//        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 4000);
//        Configuration.setValue(AVKey.INITIAL_PITCH, 50);
//
//        // Set the application frame to update, a position for the model, and a path to the COLLADA file.
//        final AppFrame af = (AppFrame) start("WorldWind COLLADA Viewer", AppFrame.class);
//        Position MackyAuditoriumPosition = Position.fromDegrees(40.028, -105.27284091410579, 200);
//        final File ColladaFile = new File("testData/collada/collada.dae");
//        
//        //cms-data/collada_files/lander_test.dae
//
//        // Invoke the <code>Thread</code> to load the COLLADA model asynchronously.
//        new WorkerThread(ColladaFile, MackyAuditoriumPosition, af).start();
//
//    }
}

