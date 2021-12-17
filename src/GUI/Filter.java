package GUI;

import javax.swing.*;
import java.awt.*;

public class Filter extends JFrame {

    JTextField minSize;
    JTextField maxSize;
    JCheckBox pdf;
    JCheckBox txt;
    JCheckBox pptx;
    JCheckBox xlsx;
    JCheckBox docx;
    JButton apply;

    
    public Filter() {
        super("Filter By");
        JPanel jpain = new JPanel();

        //create all j lables
        JLabel type = new JLabel("Type");
        JLabel size = new JLabel("Size in bytes");
        JLabel dash = new JLabel(" ---------- ");

        //type includes dot
        //create check boxes
        pdf = new JCheckBox("Pdf");
        txt = new JCheckBox("Text");
        xlsx = new JCheckBox("Excel");
        pptx = new JCheckBox("Power Point");
        docx = new JCheckBox("Word Document");

        //create text fields
        minSize = new JTextField(10);
        maxSize = new JTextField(10);

        //create all buttons
        apply = new JButton("Apply");

        jpain.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //add everything to the details panel
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;
        jpain.add(type, c);

        c.gridy = 1;
        jpain.add(pdf, c);

        c.gridy = 2;
        jpain.add(txt, c);

        c.gridy = 3;
        jpain.add(xlsx, c);

        c.gridy = 4;
        jpain.add(pptx, c);

        c.gridy = 5;
        jpain.add(docx, c);

        c.gridy = 6;
        jpain.add(size, c);

        c.gridy = 7;
        c.gridx = 0;
        jpain.add(minSize, c);

        c.gridx = 1;
        jpain.add(dash, c);

        c.gridx = 2;
        jpain.add(maxSize, c);

        c.gridx = 1;
        c.gridy = 8;
        jpain.add(apply, c);

        //Add panel to frame
        add(jpain);

        //Set the default size of the frame
        setSize(375,300);
    }
}
