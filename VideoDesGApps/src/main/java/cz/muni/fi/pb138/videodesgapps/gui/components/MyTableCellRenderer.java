/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Jakub
 */
public class MyTableCellRenderer extends JLabel implements TableCellRenderer {

    private List rows = new ArrayList();

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

        String textValue = (String) value;
        this.setText(textValue);
        this.setOpaque(true);

        this.setFont(table.getFont());
        this.setForeground(table.getForeground());
        this.setBackground(table.getBackground());

        if (rows.contains(rowIndex)) {
            this.setBackground(Color.YELLOW);
        }

        if (isSelected) {
            this.setForeground(table.getSelectionForeground());
            this.setBackground(table.getSelectionBackground());
        }
        return this;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
