package me.Ramsey.uk.Panes;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matthew Ramsey
 */
public class MainWindow extends JFrame {

    /**
     * This is the main window that the JLayeredFrames i.e content
     * panels are opened by.
     */
    public MainWindow() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        setAlwaysOnTop(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFont(new Font("Arial", Font.ITALIC, 19));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        setSize(1240, 640);
        setLocationRelativeTo(getOwner());
    }
}