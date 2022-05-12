package ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ProcessTableModel extends AbstractTableModel {
    private final Class<?>[] columns = new Class<?>[]{String.class, String.class, String.class, String.class, ImageIcon.class};
    private final int cols = columns.length;
    private ArrayList<Object[]> data;
    private final String[] columnNames;

    public ProcessTableModel(String[] columnNames) {
        this.columnNames = columnNames;
        this.data = new ArrayList<>();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.columns[columnIndex];
        //return Object.class;
    }

    public int getColumnCount() {
        return cols;
    }

    public int getRowCount() {
        return data.size();
    }

    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (rowIndex % 2 == 0);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data.get(row)[col] = value;
        fireTableCellUpdated(row, col);
    }

    public void addRow(Object[] row) {
        if (row[4] instanceof Color) {
            row[4] = createIcon((Color) row[4]);
        }
        this.data.add(row);
    }

    public void deleteRows(int startIndex, int count) {
        int index = startIndex + count - 1;
        while (index >= startIndex) {
            data.remove(index);
            index--;
        }
        fireTableRowsDeleted(startIndex, startIndex + count - 1);
    }

    private Icon createIcon(Color color) {
        return new Icon() {

            public int getIconHeight() {
                return 16;
            }

            public int getIconWidth() {
                return 16;
            }

            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillRect(x, y, getIconWidth(), getIconHeight());
            }
        };
    }
}
