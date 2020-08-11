package database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Viewer {
    private JPanel mainPane;
    private JTable dbTable;
    private JTextField newRecordTextField;
    private JButton addRecordButton;
    private JButton refreshButton;
    private JButton calcAvgButton;
    private JLabel avgLabel;

    private Table table;
    private DefaultTableModel dtm;

    public Viewer(Table table) {
        this.table = table;
        addRecordButton.addActionListener(e -> {
            String data = newRecordTextField.getText();
            newRecordTextField.setText("");
            DataRow row = new DataRow();
            long id = table.write(row);
            dtm.addRow(new Object[] {Long.toString(id), data, Integer.toString(row.getCount())});
        });
        refreshButton.addActionListener(e -> {
            dtm = new DefaultTableModel(0, 0);
            String[] columnNames = {"ID", "Value", "Count"};
            dtm.setColumnIdentifiers(columnNames);
            dbTable.setModel(dtm);
            ArrayList<Map.Entry<Long, DataRow>> rows =
                    table.readAll(stream -> stream.collect(Collectors.toCollection(ArrayList::new)));
            for (Map.Entry<Long, DataRow> row : rows) {
                DataRow r = row.getValue();
                dtm.addRow(new Object[] {Long.toString(row.getKey()), r.getId(), Integer.toString(r.getCount())});
            }
        });
        calcAvgButton.addActionListener(e -> {
            double avg =
                    table.readAll(
                            stream ->
                                    stream.mapToInt(
                                            entry ->
                                                    entry.getValue()
                                                            .getCount())
                                            .average()
                                            .orElse(-1));
            avgLabel.setText(String.valueOf(avg));
        });
    }

    public JPanel getMainPane() {
        return mainPane;
    }

    private void createUIComponents() {
        dtm = new DefaultTableModel(0, 0);
        String[] columnNames = {"ID", "Value", "Count"};
        dtm.setColumnIdentifiers(columnNames);
        dbTable = new JTable();
        dbTable.setModel(dtm);
        dbTable.setFillsViewportHeight(true);
    }
}
