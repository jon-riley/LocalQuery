package GUI;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.text.SimpleDateFormat;

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

class Panel extends JPanel implements ActionListener {
    JTextField directoryPath;
    JTree tree;
    JButton refresh;
    JTable fileData;
    JScrollPane directoryScrollPane;
    JScrollPane dspTable;
    
    String zipPath = "Test";
    String currDirectory = null;

    SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    final String[] colHeads = { "File Name", "Date Modified", "File Type", "SIZE(in Bytes)", "Matched Key Terms", "Matched Key Images" };
    String[][] data = { { "", "", "", "", "", "", "" } };

    //constructor that takes directory path
    Panel(String path) {

        //create directory path field
        directoryPath = new JTextField();
        //create refresh button
        refresh = new JButton("Refresh");

        //create table heads and table scroll pane
        fileData = new JTable(data, colHeads);
        dspTable = new JScrollPane(fileData);

        //get file for path provided and set it as the top of the tree
        File temp = new File(path);
        DefaultMutableTreeNode top = createTree(temp);
        tree = new JTree(top);

        //create left directory scroll pane for root files
        directoryScrollPane = new JScrollPane(tree);

        //Creating the ribbon
        JPanel ribbon = new JPanel();
        JLabel label = new JLabel("Comma Delimited Keyword Search: ");
        JLabel zipPathLabel = new JLabel(zipPath);
        JTextField tf = new JTextField(10);
        JButton browse = new JButton("Browse Images");
        JButton search = new JButton("Search");
        //Creating the MenuBar
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("Sort By");
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
        ribbon.add(browse);
        ribbon.add(zipPathLabel);
        ribbon.add(search);
        ribbon.add(refresh);
        ribbon.add(mb);
        c.gridy = 1;
        c.gridwidth = 7;
        c.fill = GridBagConstraints.HORIZONTAL;
        ribbon.add(directoryPath,c);
        ribbon.setBackground(Color.gray);

        //set the layout and position the parts
        setLayout(new BorderLayout());
        add(directoryScrollPane, BorderLayout.WEST);
        add(dspTable, BorderLayout.CENTER);
        add(ribbon, BorderLayout.NORTH);

        //add event to expand tree and change directroy path
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                doMouseClicked(me);
            }
        });
        directoryPath.addActionListener(this);
        //add event to refresh
        refresh.addActionListener(this);
    }

    //event for udating root tree path
    public void actionPerformed(ActionEvent ev) {
        File temp = new File(directoryPath.getText());
        DefaultMutableTreeNode newtop = createTree(temp);
        if (newtop != null)
            tree = new JTree(newtop);
        remove(directoryScrollPane);
        directoryScrollPane = new JScrollPane(tree);
        setVisible(false);
        add(directoryScrollPane, BorderLayout.WEST);
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                doMouseClicked(me);
            }
        });

        setVisible(true);
    }

    //method to create Tree
    DefaultMutableTreeNode createTree(File temp) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(temp.getPath());

        if (!(temp.exists() && temp.isDirectory()))
            return top;
        
        fillTree(top, temp.getPath());
        return top;
    }

    //method to populate the tree
    void fillTree(DefaultMutableTreeNode root, String filename) {
        File temp = new File(filename);

        if (!(temp.exists() && temp.isDirectory()))
            return;
        File[] filelist = temp.listFiles();

        for (int i = 0; i < filelist.length; i++) {
            if (!filelist[i].isDirectory())
                continue;
            final DefaultMutableTreeNode tempDmtn = new DefaultMutableTreeNode(filelist[i].getName());
            root.add(tempDmtn);
            final String newfilename = new String(filename + "\\" + filelist[i].getName());
            Thread t = new Thread() {
                public void run() {
                    fillTree(tempDmtn, newfilename);
                }
            };
            t.start();
        }
    }

    //updates direcotry path
    void doMouseClicked(MouseEvent me) {
        TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
        if (tp == null)
            return;
        String s = tp.toString();
        s = s.replace("[", "");
        s = s.replace("]", "");
        s = s.replace(", ", "\\");
        directoryPath.setText(s);
        showFiles(s);
    }

    //shows files and their info
    void showFiles(String filename) {
        File temp = new File(filename);
        remove(dspTable);
        fileData = new JTable(data, colHeads);
        dspTable = new JScrollPane(fileData);
        setVisible(false);
        add(dspTable, BorderLayout.CENTER);
        setVisible(true);

        if (!temp.exists())
            return;
        if (!temp.isDirectory())
            return;

        File[] filelist = temp.listFiles();
        int fileCounter = 0;
        data = new String[filelist.length][6];
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory())
                continue;
            data[fileCounter][0] = new String(filelist[i].getName());
            data[fileCounter][1] = date.format(filelist[i].lastModified());
            data[fileCounter][2] = new String("Not Supported");
            data[fileCounter][3] = new String(filelist[i].length() + " bytes");
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
    }
}