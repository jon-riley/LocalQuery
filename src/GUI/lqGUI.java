package GUI;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import Logic.*;

public class lqGUI extends JPanel {
    Table table = new Table();
    Ribbon ribbon = new Ribbon();
    Details details = new Details();
    String currentRoot = "D:/Documents";
    FileManager fman = new FileManager(new File(currentRoot));
    Query query = new Query(fman);

    public lqGUI() {
        setLayout(new BorderLayout());
        add(table.jspTable, BorderLayout.CENTER);
        add(ribbon, BorderLayout.NORTH);
        add(details, BorderLayout.EAST);
        createButtonListeners();
        createMenuItemListeners();
    }

    //method to add all of the functionality to all of the buttons
    void createButtonListeners() {
        
        // where to change for the buttons
        ribbon.and.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ribbon.and.isSelected())
                    ribbon.and.setText("OR");
                else
                    ribbon.and.setText("AND");
            }
        });

        ribbon.caseSens.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ribbon.caseSens.isSelected())
                    ribbon.caseSens.setText("NOT CASE SENSITIVE");
                else
                    ribbon.caseSens.setText("CASE SENSITIVE");

                }
            }
        );

        ribbon.search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Boolean andOperation;
                if (ribbon.and.getText() == "AND") 
                    andOperation = true;
                else
                    andOperation = false;

                Boolean caseSensitive;
                if (ribbon.caseSens.getText() == "CASE SENSITIVE") 
                    caseSensitive = true;
                else
                    caseSensitive = false;
                searchShowFiles(query.search(ribbon.rootField.getText(), andOperation, caseSensitive));
            }
        });

        ribbon.directorySearch.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                fman.setRoot(new File(ribbon.directoryPath.getText()));
                query.setManager(fman);
                currentRoot = ribbon.directoryPath.getText();
                try {
                    showFiles();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        //add browse and help buttons

    }

    //method to add all of the functionality to all of the menu items
    void createMenuItemListeners() {
        ribbon.fileNameA.addActionListener(e -> searchShowFiles(fman.sortByFileName(true)));
        ribbon.fileNameB.addActionListener(e -> searchShowFiles(fman.sortByFileName(false)));

        ribbon.dateModifiedA.addActionListener(e -> searchShowFiles(fman.sortByDateModified(true)));
        ribbon.dateModifiedB.addActionListener(e -> searchShowFiles(fman.sortByDateModified(false)));

        ribbon.fileTypeA.addActionListener(e -> searchShowFiles(fman.sortByFileType(true)));
        ribbon.fileTypeB.addActionListener(e -> searchShowFiles(fman.sortByFileType(false)));

        ribbon.fileSizeA.addActionListener(e -> searchShowFiles(fman.sortBySize(true)));
        ribbon.fileSizeB.addActionListener(e -> searchShowFiles(fman.sortBySize(false)));

        ribbon.matchedKeyTermsA.addActionListener(e -> searchShowFiles(fman.sortByMatches(true)));
        ribbon.matchedKeyTermsB.addActionListener(e -> searchShowFiles(fman.sortByMatches(false)));

        //for when we have image capability
        //ribbon.matchedKeyImagesA.addActionListener(e -> searchShowFiles(fman.sortByFileName(true)));
        //.matchedKeyImagesB.addActionListener(e -> searchShowFiles(fman.sortByFileName(false)));
    }

    //method to update the table
    void updateTable() {
        remove(table.jspTable);
        table.fileData = new JTable(table.data, table.colHeads);
        table.jspTable = new JScrollPane(table.fileData);
        table.fileData.setDefaultEditor(Object.class, null);
        setVisible(false);
        add(table.jspTable, BorderLayout.CENTER);
        setVisible(true);
    }
    
    //shows files and their info from directory search
    void showFiles() throws IOException {
        updateTable();

        //fix

        fman.setRoot(new File(currentRoot));
        query.setManager(fman);
        ArrayList<Document> filelist = fman.getFiles();
        int fileCounter = 0;
        table.data = new String[filelist.size()][6];
        for (int i = 0; i < filelist.size(); i++) {
            table.data[fileCounter][0] = new String(filelist.get(i).getName());
            table.data[fileCounter][1] = new String(filelist.get(i).getLastModified());
            table.data[fileCounter][2] = new String(filelist.get(i).getFileExtension());
            table.data[fileCounter][3] = new String(filelist.get(i).length() + " bytes");
            table.data[fileCounter][4] = new String("");
            table.data[fileCounter][5] = new String("Not Supported");
            fileCounter++;
        }

        String dataTemp[][] = new String[fileCounter][6];
        for (int k = 0; k < fileCounter; k++)
            dataTemp[k] = table.data[k];
        table.data = dataTemp;
        updateTable();
        ribbon.directoryPath.setText(currentRoot);
    }

    //shows sorted files
    void searchShowFiles(ArrayList<Document> arraylist) {
        updateTable();

        int fileCounter = 0;
        table.data = new String[arraylist.size()][6];
        for (int i = 0; i < arraylist.size(); i++) {
            table.data[fileCounter][0] = new String(arraylist.get(i).getName());
            table.data[fileCounter][1] = new String(arraylist.get(i).getLastModified());
            table.data[fileCounter][2] = new String(arraylist.get(i).getFileExtension());
            table.data[fileCounter][3] = new String(arraylist.get(i).length() + " bytes");
            table.data[fileCounter][4] = new String(query.getTextMatchesByDocument(arraylist.get(i)) + " matches");
            table.data[fileCounter][5] = new String("Not Supported");
            fileCounter++;
        }

        String dataTemp[][] = new String[fileCounter][6];
        for (int k = 0; k < fileCounter; k++)
            dataTemp[k] = table.data[k];
            table.data = dataTemp;
        updateTable();
        ribbon.directoryPath.setText(currentRoot);
    }

    }

