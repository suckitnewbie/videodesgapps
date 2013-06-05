/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.gui.components;

import com.google.api.services.drive.model.File;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author xnevrela
 */
public class GoogleDriveTreeRenderer implements TreeCellRenderer {
    DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
    
    public GoogleDriveTreeRenderer() {
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultTreeCellRenderer result = (DefaultTreeCellRenderer) defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        System.out.println(value.getClass());
            if (value instanceof File) {
                File file = (File)value;
                result.setText(file.getTitle());
            }
        return result;
    }
}
