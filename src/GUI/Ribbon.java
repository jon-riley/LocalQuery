package GUI;

import javax.swing.*;
import java.awt.*;

public class Ribbon extends JPanel {
    String zipPath;

    JTextField directoryPath;
    JTextField rootField;

    JButton search;
    JToggleButton and;
    JToggleButton caseSens;
    JButton browse;
    JButton directorySearch;
    JButton filterBy;

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
        directoryPath = new JTextField(107);
        Dimension d = new Dimension(100,25);
        directoryPath.setPreferredSize(d);
        rootField = new JTextField(10);

        //create all buttons
        and = new JToggleButton("AND");
        caseSens = new JToggleButton("CASE SENSITIVE");
        search = new JButton("Search");
        browse = new JButton("Browse Images");
        directorySearch = new JButton("Directory Search");
        Dimension d2 = new Dimension(300,25);
        directorySearch.setPreferredSize(d2);
        filterBy = new JButton("Filter By");

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
        mb.add(and);
        mb.add(caseSens);
        mb.add(search);
        mb.add(browse);
        mb.add(zipPathLabel);
        mb.add(filterBy);
        mb.add(sortBy);

        //create JPanel for 

        //setting up gridbag
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //add everything to the ribbon
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1;
        c.gridwidth = 8;
        add(mb,c);
        c.fill = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 4;
        add(directoryPath,c);
        c.fill = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridwidth = 1;
        c.gridx = 4;
        add(directorySearch,c);
        setBackground(Color.gray);
    }
}