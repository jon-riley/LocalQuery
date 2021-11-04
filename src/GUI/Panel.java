package GUI;

import java.io.*;
import java.sql.Types;
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
        new PanelTest("D:/Documents");
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
    
    
    String zipPath = "ZIP Path";
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
                try {
                    showFiles();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } 
        });

        //Creating the MenuBar
        JMenuBar mb = new JMenuBar();

        //Create the sort by tab
        JMenu m1 = new JMenu("Sort By");
        JMenu filename = new JMenu("File Name");
        JMenuItem[] fNameSubItems = new JMenuItem[2];
        fNameSubItems[0] = (new JMenuItem("A-Z"));
        fNameSubItems[1] = (new JMenuItem("Z-A"));
        filename.add(fNameSubItems[0]);
        filename.add(fNameSubItems[1]);
        fNameSubItems[0].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByFileName(true));
            } 
        });
        fNameSubItems[1].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByFileName(false));
            } 
        });

        JMenu datemodified = new JMenu("Date Modified");
        JMenuItem[] dateSubItems = new JMenuItem[2];
        dateSubItems[0] = (new JMenuItem("Newest to Oldest"));
        dateSubItems[1] = (new JMenuItem("Oldest to Newest"));
        datemodified.add(dateSubItems[0]);
        datemodified.add(dateSubItems[1]);
        dateSubItems[0].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByDateModified(true));
            } 
        });
        dateSubItems[1].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByDateModified(false));
            } 
        });

        JMenu filetype = new JMenu("File Type");
        JMenuItem[] typeSubItems = new JMenuItem[2];
        typeSubItems[0] = (new JMenuItem("A-Z"));
        typeSubItems[1] = (new JMenuItem("Z-A"));
        filetype.add(typeSubItems[0]);
        filetype.add(typeSubItems[1]);
        typeSubItems[0].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByFileType(true));
            } 
        });
        typeSubItems[1].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByFileType(false));
            } 
        });

        JMenu size = new JMenu("Size");
        JMenuItem[] sizeSubItems = new JMenuItem[2];
        sizeSubItems[0] = (new JMenuItem("Smallest to Largest"));
        sizeSubItems[1] = (new JMenuItem("Largest to Smallest"));
        size.add(sizeSubItems[0]);
        size.add(sizeSubItems[1]);
        sizeSubItems[0].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortBySize(true));
            } 
        });
        sizeSubItems[1].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortBySize(false));
            } 
        });

        JMenu keyterms = new JMenu("Matched Key Terms");
        JMenuItem[] termsSubItems = new JMenuItem[2];
        termsSubItems[0] = (new JMenuItem("Smallest to Largest"));
        termsSubItems[1] = (new JMenuItem("Largest to Smallest"));
        keyterms.add(termsSubItems[0]);
        keyterms.add(termsSubItems[1]);
        termsSubItems[0].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByMatches(true));
            } 
        });
        termsSubItems[1].addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                searchShowFiles(fman.sortByMatches(false));
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
    void showFiles() throws IOException {
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
