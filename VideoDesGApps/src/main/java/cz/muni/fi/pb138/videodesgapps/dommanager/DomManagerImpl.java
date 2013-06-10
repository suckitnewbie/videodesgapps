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
    
    public DomManagerImpl (java.io.File file) {
        try {
            inputDocument = (OdfSpreadsheetDocument) OdfSpreadsheetDocument.loadDocument(file);
        } catch (Exception ex) {
            System.err.println("Unable to parse input file.");
            Logger.getLogger(DomManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            } else {
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

        if (table == null) {
            throw new IllegalArgumentException("Media not found.");
        }

        int rowNumber = this.findFirstEmptyRow(media);

        for (int i = 0; i < attributes.size(); i++) {
            OdfTableCell cell = table.getCellByPosition(i + this.findFirstAttributePosition(media), rowNumber);
            cell.setStringValue(attributes.get(i));
        }

    }

    @Override
    public void deleteRecord(String media, int id) {

        OdfTable table;
        table = inputDocument.getTableByName(media);

        if (table == null) {
            throw new IllegalArgumentException("Media not found.");
        }

        table.removeRowsByIndex(id, id);

    }

    @Override
    public void editRecord(String media, int id, List<String> attributes) {
        OdfTable table;
        table = inputDocument.getTableByName(media);

        if (table == null) {
            throw new IllegalArgumentException("Media not found.");
        }

        int rowNumber = id;

        for (int i = 0; i < attributes.size(); i++) {
            OdfTableCell cell = table.getCellByPosition(i + this.findFirstAttributePosition(media), rowNumber);
            cell.setStringValue(attributes.get(i));
        }

    }

    @Override
    public List<Integer> searchRecord(String searchValue, String media) {

        String cellText;
        List result = new ArrayList<Integer>();

        OdfTable table;
        table = inputDocument.getTableByName(media);

        if (table == null) {
            throw new IllegalArgumentException("Media not found.");
        }


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

    public void loadTableToMediaType(String media, MediaType type) {
        List attributes = new ArrayList<String>();
        List records = new ArrayList<ArrayList<String>>();

        OdfTable table;
        table = inputDocument.getTableByName(media);

        int firstColumn = this.findFirstAttributePosition(media);
        int lastColumn = this.findLastAttributePosition(media);

        for (int col = firstColumn; col < lastColumn+1 ; col++) {
            attributes.add(table.getCellByPosition(col, 0).getDisplayText());
        }

        for (int row = 1; row < table.getRowCount(); row++) {
            List rowCells = new ArrayList<String>();
            for (int col = firstColumn; col < lastColumn +1; col++) {
                rowCells.add(table.getCellByPosition(col, row).getDisplayText());
            }
            records.add(rowCells);
        }

        type.setAttributes(attributes);
        type.setRecords(records);

    }

    public int findFirstEmptyRow(String media) {

        OdfTable table;
        table = inputDocument.getTableByName(media);

        if (table == null) {
            throw new IllegalArgumentException("Media not found.");
        }

        for (int row = 0; row < table.getRowCount(); row++) {
            String controlStr = "";

            for (int col = 0; col < table.getColumnCount(); col++) {
                controlStr += table.getCellByPosition(col, row).getDisplayText();
                if (!controlStr.equals("")) {
                    break;   //works?
                }
            }

            if (controlStr.equals("")) {
                return row;
            }
        }

        table.appendRow();
        return table.getRowCount() - 1;
    }

    public int findFirstAttributePosition(String media) {

        OdfTable table;
        table = inputDocument.getTableByName(media);

        for (int col = 0; col < table.getColumnCount(); col++) {
            if (!table.getCellByPosition(col, 0).getDisplayText().equals("")) {
                return col;
            }
        }
        return table.getColumnCount()-1;
    }

    public int findLastAttributePosition(String media) {

        OdfTable table;
        table = inputDocument.getTableByName(media);
        
        for (int col = 0; col < table.getColumnCount() - 1; col++) {
            if (!table.getCellByPosition(col, 0).getDisplayText().equals("")
                    && table.getCellByPosition(col + 1, 0).getDisplayText().equals("")) {
                return col;
            }
        }
        return table.getColumnCount()-1;
    }
    
    public List<String> getMediaNames() {
        
        List result = new ArrayList<String>();
        
        for(int i = 0; i < inputDocument.getTableList().size(); i++) {
            result.add(inputDocument.getTableList().get(i).getTableName());
        }
        
        return result;
        
    }

    public List<String> listMediaTypes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getRecord(String media, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> listRecords(String media) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
