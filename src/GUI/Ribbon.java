package GUI;

import javax.swing.*;
import java.awt.*;

public class Ribbon extends JPanel {
    String zipPath;

    JTextField directoryPath;
    JTextField rootField;

    JButton search;
    JButton browse;
    JButton directorySearch;
    JButton filterBy;
    JButton help;

    JMenuItem fileNameA;
    JMenuItem fileNameB;
    JMenuItem dateModifiedA;
    JMenuItem dateModifiedB;
    JMenuItem fileTypeA;
    JMenuItem fileTypeB;
    JMenuItem fileSizeA;
    JMenuItem fileSizeB;
    JMenuItem matchedKeyTermsA;
    JMenuItem matchedKeyTermsB;
    JMenuItem matchedKeyImagesA;
    JMenuItem matchedKeyImagesB;


    public Ribbon() {
        //create all labels
        JLabel label = new JLabel("Comma Delimited Keyword Search: ");
        JLabel zipPathLabel = new JLabel(zipPath);

        //create all text fields
        directoryPath = new JTextField();
        rootField = new JTextField(10);

        //create all buttons
        search = new JButton("Search");
        browse = new JButton("Browse Images");
        directorySearch = new JButton("Directory Search");
        filterBy = new JButton("Filter By");
        help = new JButton("Help");

        //create sort by menu
        JMenu sortBy = new JMenu("Sort By");
        JMenu fileName = new JMenu("File Name");
        fileNameA = new JMenuItem("A-Z");
        fileNameB = new JMenuItem("Z-A");
        fileName.add(fileNameA);
        fileName.add(fileNameB);

        JMenu dateModified = new JMenu("Date Modified");
        dateModifiedA = new JMenuItem("Newest to Oldest");
        dateModifiedB = new JMenuItem("Oldest to Newest");
        dateModified.add(dateModifiedA);
        dateModified.add(dateModifiedB);

        JMenu fileType = new JMenu("File Type");
        fileTypeA = new JMenuItem("A-Z");
        fileTypeB = new JMenuItem("Z-A");
        fileType.add(fileTypeA);
        fileType.add(fileTypeB);

        JMenu fileSize = new JMenu("File Size");
        fileSizeA = new JMenuItem("Smallest to Largest");
        fileSizeB = new JMenuItem("Largest to Smallest");
        fileSize.add(fileSizeA);
        fileSize.add(fileSizeB);

        JMenu matchedKeyTerms = new JMenu("Matched Key Terms");
        matchedKeyTermsA = new JMenuItem("Smallest to Largest");
        matchedKeyTermsB = new JMenuItem("Largest to Smallest");
        matchedKeyTerms.add(matchedKeyTermsA);
        matchedKeyTerms.add(matchedKeyTermsB);

        JMenu matchedKeyImages = new JMenu("Matched Key Images");
        matchedKeyImagesA = new JMenuItem("Smallest to Largest");
        matchedKeyImagesB = new JMenuItem("Largest to Smallest");
        matchedKeyImages.add(matchedKeyImagesA);
        matchedKeyImages.add(matchedKeyImagesB);

        sortBy.add(fileName);
        sortBy.add(dateModified);
        sortBy.add(fileType);
        sortBy.add(fileSize);
        sortBy.add(matchedKeyTerms);
        sortBy.add(matchedKeyImages);

        //create menu bar and add sort by and filter by
        JMenuBar mb = new JMenuBar();
        mb.add(label);
        mb.add(rootField);
        mb.add(search);
        mb.add(browse);
        mb.add(zipPathLabel);
        mb.add(sortBy);
        mb.add(filterBy);
        mb.add(help);

        //create JPanel for 

        //setting up gridbag
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //add everything to the ribbon
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1;
        c.gridwidth = 5;
        add(mb,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 4;
        add(directoryPath,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 5;
        add(directorySearch,c);
        setBackground(Color.gray);
    }
}