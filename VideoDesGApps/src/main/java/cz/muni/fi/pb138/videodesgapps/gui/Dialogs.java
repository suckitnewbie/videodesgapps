/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.gui;

import cz.muni.fi.pb138.videodesgapps.dommanager.MediaType;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author ZidaneT
 */
public class Dialogs {

    public static List<String> newRecordDialog(MediaType mediaType) {
        JPanel newPanel = new JPanel(new GridLayout(0, 2));
        for (Object attribute : mediaType.getAttributes()) {
            newPanel.add(new JLabel((String) attribute));
            newPanel.add(new JTextField(10));
        }
        int result = JOptionPane.showConfirmDialog(null, newPanel, "New record", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            List<String> attributes = new ArrayList<String>();
            for (int i = 1; i < (newPanel.getComponentCount()); i += 2) {
                JTextField jTextField = (JTextField) newPanel.getComponent(i);
                attributes.add(jTextField.getText());
            }
            mediaType.addRecord((ArrayList<String>) attributes);
            return attributes;
        }
        return null;
    }

    public static List<String> editRecordDialog(int index, MediaType mediaType) {
        JPanel newPanel = new JPanel(new GridLayout(0, 2));
        for (Object attribute : mediaType.getAttributes()) {
            newPanel.add(new JLabel((String) attribute));
            newPanel.add(new JTextField(10));
        }

        for (int i = 1; i < newPanel.getComponentCount(); i += 2) {
            JTextField editTextField = (JTextField) newPanel.getComponent(i);
            ArrayList<String> record = (ArrayList) mediaType.getRecords().get(index);
            editTextField.setText(record.get(i / 2));
        }

        int result = JOptionPane.showConfirmDialog(null, newPanel, "New record", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            List<String> attributes = new ArrayList<String>();
            for (int i = 1; i < (newPanel.getComponentCount()); i += 2) {
                JTextField jTextField = (JTextField) newPanel.getComponent(i);
                attributes.add(jTextField.getText());
            }


            List<String> newRecord = (List<String>) mediaType.getRecords().get(index);
            newRecord.clear();
            newRecord.addAll(attributes);
            //mediaTyp;e.addRecord((ArrayList<String>) attributes);
            return attributes;
        }
        return null;
    }

    public static MediaType newMediaTypeDialog(List oldMediaNames) {
        JPanel spinnerPanel = new JPanel(new GridLayout(0, 2));
        JSpinner amountOfAttributes = new JSpinner();
        amountOfAttributes.setModel(new SpinnerNumberModel(1, 1, null, 1));
        JTextField nameTextField = new JTextField(10);
        spinnerPanel.add(new JLabel("Name: "));
        spinnerPanel.add(nameTextField);
        spinnerPanel.add(new JLabel("Amount of attributes: "));
        spinnerPanel.add(amountOfAttributes);
        int result = JOptionPane.showConfirmDialog(null, spinnerPanel, "New Media Type", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (!nameTextField.getText().equals("")) {

                if (!oldMediaNames.contains(nameTextField.getText())) {
                    JPanel attributesPanel = new JPanel(new GridLayout(0, 2));
                    for (int i = 1; i <= (Integer) amountOfAttributes.getValue(); ++i) {
                        attributesPanel.add(new JLabel("Attribute no. " + i + " "));
                        attributesPanel.add(new JTextField(10));
                    }
                    int result2 = JOptionPane.showConfirmDialog(null, attributesPanel, "New Media Type", JOptionPane.OK_CANCEL_OPTION);
                    if (result2 == JOptionPane.OK_OPTION) {


                        MediaType mediaType = new MediaType();
                        mediaType.setName(nameTextField.getText());
                        ArrayList<String> attributes = new ArrayList<String>();
                        JTextField jTextField;
                        for (int i = 1; i < (Integer) amountOfAttributes.getValue() * 2; i += 2) {
                            jTextField = (JTextField) attributesPanel.getComponent(i);
                            attributes.add(jTextField.getText());
                        }
                        Boolean control = true;
                        for (String s : attributes) {
                            if (s.equals("")) {
                                control = false;
                            }
                        }
                        if (control) {
                            mediaType.setAttributes(attributes);
                            return mediaType;
                        } else {
                            JOptionPane.showMessageDialog(null, "Some of the attributes is empty", "Empty attribute", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Name of the media already exists", "Existing name", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Name of the media is empty", "Empty name", JOptionPane.ERROR_MESSAGE);

            }

        }

        return null;
    }
}
