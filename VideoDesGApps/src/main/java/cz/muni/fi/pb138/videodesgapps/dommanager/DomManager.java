/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.dommanager;

import java.util.List;

/**
 *
 * @author Jakub & Erik
 */
public interface DomManager {
    void addMediaType(String name, List<String> attributes);
    void deleteMediaType(String name);
    
    List<String> getMediaNames();
    void addRecord(String media, List<String> attributes);
    void deleteRecord(String media, int id);
    void editRecord(String media, int id, List<String> attributes);
    String getRecord(String media, int id);
    List<String> listRecords(String media);
    List<Integer> searchRecord(String searchValue, String media);
    MediaType loadTableToMediaType(String media);
}
