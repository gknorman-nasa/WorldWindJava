package gov.nasa.worldwindx.examples;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.util.measure.MeasureTool;
import java.awt.*;
import javax.swing.JDialog;

public class MeasureDialog {

    private JDialog dialog;

    public MeasureDialog(WorldWindow wwdObject, MeasureTool measureToolObject, Component component) {
        dialog = new JDialog((Frame) component);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(new MeasureToolPanel(wwdObject, measureToolObject), BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(component);
    }

    public void setVisible(boolean visible) {
        dialog.setVisible(visible);
    }
}
