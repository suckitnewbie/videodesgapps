/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.dommanager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

/**
 *
 * @author Jakub & Erik & Michal
 */

public class DomManagerImpl implements DomManager {

    private OdfSpreadsheetDocument inputDocument;

    public DomManagerImpl(OdfSpreadsheetDocument inputDocument) {
        this.inputDocument = inputDocument;
    }

    @Override
    public void addMediaType(String name, List<String> attributes) {
        
        try {
            OdfTable t = OdfTable.newTable(inputDocument);
            t.setTableName(name);
            this.addRecord(name, attributes);

        } catch (Exception ex) {
            Logger.getLogger(DomManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void deleteMediaType(String name) {
        try {
            OdfTable table = inputDocument.getTableByName(name);
            if (table != null) {
                table.remove();
            }
            else {
                throw new IllegalArgumentException("Media not found.");
            }
        } catch (Exception ex) {
            Logger.getLogger(DomManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addRecord(String media, List<String> attributes) {

        OdfTable table;
        table = inputDocument.getTableByName(media);

        if (table == null) throw new IllegalArgumentException("Media not found.");
        
        int rowNumber = this.findFirstEmptyRow(media);

        for (int i = 0; i < attributes.size(); i++) {
            OdfTableCell cell = table.getCellByPosition(i, rowNumber);
            cell.setStringValue(attributes.get(i));
        }

    }

    @Override
    public void deleteRecord(String media, int id) {

        OdfTable table;
        table = inputDocument.getTableByName(media);
        
        if (table == null) throw new IllegalArgumentException("Media not found.");

        table.removeRowsByIndex(id, id);

    }

    @Override
    public void editRecord(String media, int id, List<String> attributes) {
        OdfTable table;
        table = inputDocument.getTableByName(media);
        
        if (table == null) throw new IllegalArgumentException("Media not found.");

        int rowNumber = id;

        for (int i = 0; i < attributes.size(); i++) {
            OdfTableCell cell = table.getCellByPosition(i, rowNumber);
            cell.setStringValue(attributes.get(i));
        }
        
    }

    @Override
    public List<Integer> searchRecord(String searchValue, String media) {

        String cellText;
        List<Integer> result = new ArrayList<Integer>();
        
        OdfTable table;
        table = inputDocument.getTableByName(media);
        
        if (table == null) throw new IllegalArgumentException("Media not found.");


        for (int row = 0; row < table.getRowCount(); row++) {
            
            for (int column = 0; column < table.getColumnCount(); column++) {
                
                cellText = table.getCellByPosition(column, row).getDisplayText();
                
                if (cellText.equals(searchValue)) {
                    result.add(row + 1);
                    break;
                }

            }
        }

        return result;

    }

    public int findFirstEmptyRow(String media) {

        OdfTable table;
        table = inputDocument.getTableByName(media);
        
        if (table == null) throw new IllegalArgumentException("Media not found.");
        
        for (int row = 0; row < table.getRowCount(); row++) {
            String controlStr = "";
            
            for (int col = 0; col < table.getColumnCount(); col++) {
                controlStr += table.getCellByPosition(col, row).getDisplayText();
                if(!controlStr.equals("")) break; 
            }
            
            if (controlStr.equals("")) {
                return row;
            }
        }
        
        table.appendRow();
        return table.getRowCount() - 1;
    }
}
