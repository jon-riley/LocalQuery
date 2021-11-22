package GUI;

import javax.swing.*;
import java.awt.*;

public class Details extends JPanel{
    JLabel selectedFileName;

    JButton openFile;
    JButton copyPath;
    JButton extractImages;
    
    public Details() {
        //create all j lables
        selectedFileName = new JLabel("File Test");

        //create all buttons
        openFile = new JButton("Open File");
        copyPath = new JButton("Copy File Path");
        extractImages = new JButton("Extract Images");

        //setting up grid
        setLayout(new GridLayout(25,1));

        //add everything to the details panel
        add(selectedFileName);
        add(openFile);
        add(copyPath);
        add(extractImages);
    }
}
