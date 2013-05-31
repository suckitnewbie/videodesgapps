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
    void addRecord(String media, List<String> attributes);
    void deleteRecord(String media, int id);
    void editRecord(String media, int id, List<String> attributes);
    List<Integer> searchRecord(String searchValue);
}
