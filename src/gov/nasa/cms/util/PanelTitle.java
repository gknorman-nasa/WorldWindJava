/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms.util;

import gov.nasa.worldwindx.applications.worldwindow.util.ShadedPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author kaitlyn
 */
public class PanelTitle extends ShadedPanel
{
    private static final Color c1 = new Color(29, 78, 169, 200);
    private static Color c2 = new Color(93, 158, 223, 200);

    public PanelTitle(String title)
    {
        this(title, SwingConstants.LEFT);
    }

    public PanelTitle(String title, int alignment)
    {
        super(new BorderLayout());

        this.setColors(c1, c2);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(alignment);
        titleLabel.setFont(Font.decode("Arial-Bold-14"));
        titleLabel.setForeground(Color.WHITE);
        this.add(titleLabel, BorderLayout.CENTER);
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
    }
}
