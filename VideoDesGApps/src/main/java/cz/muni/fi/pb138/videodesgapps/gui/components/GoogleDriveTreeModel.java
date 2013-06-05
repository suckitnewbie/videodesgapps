/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.gui.components;

import com.google.api.services.drive.model.File;
import cz.muni.fi.pb138.videodesgapps.google.GoogleDriveService;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author xnevrela
 */
public class GoogleDriveTreeModel implements TreeModel {

    private GoogleDriveService service;
    private Vector<TreeModelListener> treeModelListeners =
            new Vector<TreeModelListener>();
    private File root;
    private static String mimeType = GoogleDriveService.MIME_TYPE_SPREATSHEET_OO;

    public GoogleDriveTreeModel(GoogleDriveService service) {
        this.service = service;

        root = new File();
        root.setId("root");
        root.setMimeType(GoogleDriveService.MIME_TYPE_FOLDER);
        root.setTitle("Google Drive");
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        File folder;
        File result = null;

        System.out.println("Parent: " + parent + "\n   Index: " + index);
        if (parent instanceof File) {
            try {
                folder = (File) parent;
                result = service.listFilesAndFolders(folder, mimeType).get(index);
            } catch (IOException ex) {
                Logger.getLogger(GoogleDriveTreeModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

    public int getChildCount(Object parent) {
        File folder;
        int result = 0;

        if (parent instanceof File) {
            try {
                folder = (File) parent;
                result = service.listFilesAndFolders(folder, mimeType).size();
                System.out.println("Parent: " + parent + "\n   Size: " + result);
            } catch (IOException ex) {
                Logger.getLogger(GoogleDriveTreeModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

    public boolean isLeaf(Object node) {
        File file;
        boolean result = true;

        if (node instanceof File) {
                file = (File) node;
                result = !file.getMimeType().equals(GoogleDriveService.MIME_TYPE_FOLDER);
        }

        return result;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("*** valueForPathChanged : " + path + " --> " + newValue);
    }

    public int getIndexOfChild(Object parent, Object child) {
        File folder;
        int result = 0;

        if (parent instanceof File) {
            try {
                folder = (File) parent;
                result = service.listFilesAndFolders(folder, mimeType).indexOf(child);
            } catch (IOException ex) {
                Logger.getLogger(GoogleDriveTreeModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }
}
