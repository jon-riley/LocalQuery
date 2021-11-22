package GUI;

import javax.swing.*;

public class Table extends JScrollPane {
    JTable fileData;
    JScrollPane jspTable;

    final String[] colHeads = { "File Name", "Date Modified", "File Type", "SIZE(in Bytes)", "Matched Key Terms", "Matched Key Images" };
    String[][] data = { { "", "", "", "", "", "", "" } };

    public Table() {
        //create table heads and table scroll pane
        fileData = new JTable(data, colHeads);
        jspTable = new JScrollPane(fileData);
        jspTable.setEnabled(false);
    }
}
