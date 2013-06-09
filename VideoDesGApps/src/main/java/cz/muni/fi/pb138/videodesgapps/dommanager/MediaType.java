/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.dommanager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jakub
 */
public class MediaType {
    
    private List attributes;
    private List records;
    private String name;

    public MediaType() {
        this.attributes = new ArrayList<String>();
        this.records = new ArrayList<ArrayList<String>>();
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public void setRecords(List records) {
        this.records = records;
    }

    public List getAttributes() {
        return attributes;
    }

    public List getRecords() {
        return records;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addRecord(ArrayList<String> record) {
        records.add(record);
    }
    
}
