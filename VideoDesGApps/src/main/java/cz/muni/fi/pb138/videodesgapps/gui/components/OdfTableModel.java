/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.gui.components;

import cz.muni.fi.pb138.videodesgapps.dommanager.MediaType;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jakub
 */
public class OdfTableModel extends AbstractTableModel {

    private MediaType mediaType = new MediaType();

    public OdfTableModel() {
    }

    @Override
    public int getRowCount() {
        return mediaType.getRecords().size();
    }

    @Override
    public int getColumnCount() {
        return mediaType.getAttributes().size();
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<String> record = (ArrayList<String>) mediaType.getRecords().get(rowIndex);

        if (columnIndex < getColumnCount()) {
            return record.get(columnIndex).toString();
        } else {
            throw new IllegalArgumentException("WRONG");
        }
    }

    @Override
    public String getColumnName(int columnIndex) {

        return mediaType.getAttributes().get(columnIndex).toString();
    }

    public MediaType getMediaType() {
        return mediaType;
    }
    
    public void fireInserted(){
        fireTableRowsInserted(mediaType.getRecords().size()-1, mediaType.getRecords().size()-1);
    }
    
    public void fireDeleted(int index){
        fireTableRowsDeleted(index, index);
    }
    
    public void fireUpdate(int index) {
        fireTableRowsUpdated(index, index);
    }
    
    public void fireUpdateAll() {
        fireTableRowsUpdated(0, mediaType.getRecords().size() - 1);
    }
}
