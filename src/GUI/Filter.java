package GUI;

import javax.swing.*;
import java.awt.*;

public class Filter extends JFrame {

    JTextField minSize;
    JTextField maxSize;
    JCheckBox pdf;
    JCheckBox txt;
    JButton apply;

    
    public Filter() {
        JPanel jpain = new JPanel();

        //create all j lables
        JLabel type = new JLabel("Type");
        JLabel size = new JLabel("Size in bytes");
        JLabel dash = new JLabel(" - ");

        //type includes dot
        //create check boxes
        pdf = new JCheckBox("Pdf");
        txt = new JCheckBox("Text");

        //create text fields
        minSize = new JTextField(10);
        maxSize = new JTextField(10);

        //create all buttons
        apply = new JButton("Apply");

        //add everything to the details panel
        jpain.add(type);
        jpain.add(pdf);
        jpain.add(txt);
        jpain.add(size);
        jpain.add(minSize);
        jpain.add(dash);
        jpain.add(maxSize);
        jpain.add(apply);

        //Add panel to frame
        add(jpain);

        //Set the default size of the frame
        setSize(300,400);
    }
}
