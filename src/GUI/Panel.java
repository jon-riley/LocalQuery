package GUI;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Logic.*;


/***********************************/
class PanelTest extends JFrame {

    PanelTest(String path) {
        super("Local Query");
        add(new Panel(path), "Center");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 750);
        setVisible(true);
    }

    public static void main(String[] args) {
        new PanelTest(".");
    }
}

class Panel extends JPanel {
    JTextField directoryPath;
    JTree tree;
    JButton refresh;
    JTable fileData;
    JScrollPane directoryScrollPane;
    JScrollPane dspTable;
    FileManager fman;
    String currentRoot;
    
    
    String zipPath = "Test";
    String currDirectory = null;

    SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    final String[] colHeads = { "File Name", "Date Modified", "File Type", "SIZE(in Bytes)", "Matched Key Terms", "Matched Key Images" };
    String[][] data = { { "", "", "", "", "", "", "" } };

    //constructor that takes directory path
    Panel(String path) {
        //create directory path field
        directoryPath = new JTextField();
        currentRoot = path;

        //create table heads and table scroll pane
        fileData = new JTable(data, colHeads);
        dspTable = new JScrollPane(fileData);

        //get file for path provided and set it as the top of the tree
        File temp = new File(path);
        fman = new FileManager(temp);

        //create left directory scroll pane for root files
        directoryScrollPane = new JScrollPane(tree);

        //Creating the ribbon
        JPanel ribbon = new JPanel();
        JLabel label = new JLabel("Comma Delimited Keyword Search: ");
        JLabel zipPathLabel = new JLabel(zipPath);
        JTextField tf = new JTextField(10);
        JButton browse = new JButton("Browse Images");

        //Create search button for keyword textbox
        JButton search = new JButton("Search");
        search.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.search(tf.getText(), false, true));
            }
        });

        //Create search button for directory search
        JButton directorySearch = new JButton("Directory Search");
        directorySearch.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                fman.setRoot(new File(directoryPath.getText()));
                currentRoot = directoryPath.getText();
                showFiles();
            } 
        });

        //Creating the MenuBar
        JMenuBar mb = new JMenuBar();

        //Create the sort by tab
        JMenu m1 = new JMenu("Sort By");
        JMenuItem filename = new JMenuItem("File Name");
        filename.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByFileName(true));
            } 
        });
        JMenuItem datemodified = new JMenuItem("Date Modified");
        datemodified.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByDateModified(true));
            } 
        });
        JMenuItem filetype = new JMenuItem("File Type");
        filetype.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByFileType(true));
            } 
        });
        JMenuItem size = new JMenuItem("Size");
        size.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortBySize(true));
            } 
        });
        JMenuItem keyterms = new JMenuItem("Matched Key Terms");
        keyterms.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByMatches(true));
            } 
        });
        JMenuItem keyimages = new JMenuItem("Matched Key Images");
        m1.add(filename);
        m1.add(datemodified);
        m1.add(filetype);
        m1.add(size);
        m1.add(keyterms);
        m1.add(keyimages);

        //Create the filter by tab
        JMenu m2 = new JMenu("Filter By");
        JMenu m3 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);

        //setting up gridbag
        ribbon.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //adding componets to ribbon
        ribbon.add(label);
        ribbon.add(tf);
        ribbon.add(search);
        ribbon.add(browse);
        ribbon.add(zipPathLabel);
        ribbon.add(mb);
        c.gridy = 1;
        c.gridwidth = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        ribbon.add(directoryPath,c);
        c.gridwidth = 1;
        ribbon.add(directorySearch,c);
        ribbon.setBackground(Color.gray);

        //set the layout and position the parts
        setLayout(new BorderLayout());
        add(directoryScrollPane, BorderLayout.WEST);
        add(dspTable, BorderLayout.CENTER);
        add(ribbon, BorderLayout.NORTH);
    }

    //shows files and their info from directory search
    void showFiles() {
        remove(dspTable);
        fileData = new JTable(data, colHeads);
        dspTable = new JScrollPane(fileData);
        setVisible(false);
        add(dspTable, BorderLayout.CENTER);
        setVisible(true);

        ArrayList<Document> filelist = fman.getFiles();
        int fileCounter = 0;
        data = new String[filelist.size()][6];
        for (int i = 0; i < filelist.size(); i++) {
            data[fileCounter][0] = new String(filelist.get(i).getName());
            data[fileCounter][1] = new String(filelist.get(i).getLastModified());
            data[fileCounter][2] = new String(filelist.get(i).getFileExtension());
            data[fileCounter][3] = new String(filelist.get(i).length() + " bytes");
            data[fileCounter][4] = new String("Not Supported");
            data[fileCounter][5] = new String("Not Supported");
            fileCounter++;
        }

        String dataTemp[][] = new String[fileCounter][6];
        for (int k = 0; k < fileCounter; k++)
            dataTemp[k] = data[k];
        data = dataTemp;

        remove(dspTable);
        fileData = new JTable(data, colHeads);
        dspTable = new JScrollPane(fileData);
        setVisible(false);
        add(dspTable, BorderLayout.CENTER);
        setVisible(true);

        directoryPath.setText(currentRoot);
    }

    //shows files from keyword search
    void searchShowFiles(ArrayList<Document> arraylist) {
        remove(dspTable);
        fileData = new JTable(data, colHeads);
        dspTable = new JScrollPane(fileData);
        setVisible(false);
        add(dspTable, BorderLayout.CENTER);
        setVisible(true);

        int fileCounter = 0;
        data = new String[arraylist.size()][6];
        for (int i = 0; i < arraylist.size(); i++) {
            data[fileCounter][0] = new String(arraylist.get(i).getName());
            data[fileCounter][1] = new String(arraylist.get(i).getLastModified());
            data[fileCounter][2] = new String(arraylist.get(i).getFileExtension());
            data[fileCounter][3] = new String(arraylist.get(i).length() + " bytes");
            data[fileCounter][4] = new String("Not Supported");
            data[fileCounter][5] = new String("Not Supported");
            fileCounter++;
        }

        String dataTemp[][] = new String[fileCounter][6];
        for (int k = 0; k < fileCounter; k++)
            dataTemp[k] = data[k];
        data = dataTemp;

        remove(dspTable);
        fileData = new JTable(data, colHeads);
        dspTable = new JScrollPane(fileData);
        setVisible(false);
        add(dspTable, BorderLayout.CENTER);
        setVisible(true);

        directoryPath.setText(currentRoot);
    }
}
