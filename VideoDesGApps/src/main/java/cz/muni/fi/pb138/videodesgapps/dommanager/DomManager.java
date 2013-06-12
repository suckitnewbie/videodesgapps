/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.dommanager;

import java.util.List;

/*
 * This interface describes main functionality for classes, which want to 
 * process ODF Table and work with it in Java Swing.
 *
 * @author Jakub & Erik
 */
public interface DomManager {
    
    /**
     * Method addMediaType adds Media Type to ODF Table. Media Type consists of name 
     * list of attributes, which will be added to the first row of ODF Table.
     * 
     * @param name name of the media
     * @param attributes media attributes
     */
    void addMediaType(String name, List<String> attributes);
    
    
    /**
     * Method deleteMediaType deletes Media Type from ODF Table. It looks for the Media 
     * Type by name and if it is found, this Media Type is deleted from ODF Table.
     * 
     * @param name name of the media
     */
    void deleteMediaType(String name);
    
    
    /**
     * Method getMediaNames returns names of all media Types in the ODF Table. 
     * 
     * @return List of media names
     */
    List<String> getMediaNames();
    
    
    /**
     * Method addRecord adds record to the ODF Table. Record is added to the 
     * first free row in the Media Type found by media name and attributes represent 
     * columns of this row.
     * 
     * @param media name of the media
     * @param attributes attributes of the record
     */
    void addRecord(String media, List<String> attributes);
    
    
    /**
     * Method deleteRecord deletes record from the ODF Table found by media name. 
     * Record is deleted from the row with index id.
     * 
     * @param media name of the media
     * @param id index of the row
     */
    void deleteRecord(String media, int id);
    
    
    /**
     * Method editRecord finds media by media name. Row with index id is replaced
     * by new record represented with attributes.
     * 
     * @param media name of the media
     * @param id index of the row
     * @param attributes  attributes of new record
     */
    void editRecord(String media, int id, List<String> attributes);
    
    
    /**
     * Method searchRecord returns List of Integers with indexes of all rows,
     * where the search value has been found.
     * 
     * @param searchValue searched value
     * @param media name of the media
     * @return List of Integers, where the search value has been found
     */
    List<Integer> searchRecord(String searchValue, String media);
    
    
    /**
     * Method loadTableToMediaType loads all informations about Media found by
     * name to the Media Type.
     * 
     * @param media name of the media
     * @return representation of ODF Table
     */
    MediaType loadTableToMediaType(String media);
    
    
    /**
     * Method saveSpreadSheet returns File, where the modified ODF Table 
     * will be saved.
     * 
     * @return File with new ODF Table
     */
    java.io.File saveSpreadSheet();
    
    
}
