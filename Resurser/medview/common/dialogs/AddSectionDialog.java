/*
 * @(#)AddSectionDialog.java
 *
 * $Id: AddSectionDialog.java,v 1.7 2010/06/28 08:46:17 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */
package medview.common.dialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import medview.datahandling.MedViewLanguageConstants;

class AddSectionDialog extends AbstractDialog implements MedViewLanguageConstants {

    private ContentPanel contentPanel;

    public AddSectionDialog(Frame owner, String[] sections) {
        super(owner, true, new Object[]{sections});

        buttons[0].setEnabled(false);
    }

    public AddSectionDialog(Dialog owner, String[] sections) {
        super(owner, true, new Object[]{sections});

        buttons[0].setEnabled(false);
    }

    public Object getObjectData() {
        if (wasDismissed()) {
            return null;
        } else {
            JTextField textField = contentPanel.textField;

            JComboBox bACombo = contentPanel.befAftCombo;

            JComboBox oSCombo = contentPanel.sectCombo;

            String newName = textField.getText();

            String befAft = (String) bACombo.getSelectedItem();

            String chosSect = (String) oSCombo.getSelectedItem();

            return new Object[]{newName, befAft, chosSect};
        }
    }

    protected JPanel createMainPanel() {
        return contentPanel = new ContentPanel();
    }

    protected String[] getButtonFacesLS() {
        return new String[]{
                    BUTTON_ADD_TO_LS_PROPERTY,
                    BUTTON_CANCEL_TEXT_LS_PROPERTY
                };
    }

    protected String getTitleLS() {
        return TITLE_ADD_NEW_SECTION_LS_PROPERTY;
    }

    protected void registerListeners() {
        buttons[0].addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setDismissed(false);

                AddSectionDialog.this.dispose();
            }
        });

        buttons[1].addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setDismissed(true);

                AddSectionDialog.this.dispose();
            }
        });

        final JTextField textField = contentPanel.textField;

        Document doc = textField.getDocument();

        doc.addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                String text = textField.getText();

                if (text.length() == 0) {
                    buttons[0].setEnabled(false);
                } else {
                    JComboBox sectCombo = contentPanel.sectCombo;

                    int sectionCount = sectCombo.getItemCount();

                    boolean exists = false;

                    for (int ctr = 0; ctr < sectionCount; ctr++) {
                        if (sectCombo.getItemAt(ctr).equals(text)) {
                            exists = true;
                        }
                    }

                    if (!exists) {
                        buttons[0].setEnabled(true);
                    } else {
                        buttons[0].setEnabled(false);
                    }
                }
            }

            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });
    }

    /**
     * Makes sure that the text field part of the add
     * section dialog is focused and that the caret is
     * placed there after show (so that the user doesn't
     * have to click there manually all the time).
     */
    @Override
    public void show() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                contentPanel.textField.requestFocus();
            }
        });

        super.show();
    }

    private class ContentPanel extends JPanel {

        public ContentPanel() {
            GridBagLayout gbl = new GridBagLayout();

            GridBagConstraints gbc = new GridBagConstraints();

            setLayout(gbl);

            // Section name label

            String prop = LABEL_SECTION_NAME_LS_PROPERTY;

            String face = mVDH.getLanguageString(prop);

            JLabel sectLabel = new JLabel(face);

            gbc.gridx = 0;

            gbc.gridy = 0;

            gbc.gridwidth = 1;

            gbc.insets = new Insets(0, 0, 4, 4);

            gbc.fill = GridBagConstraints.BOTH;

            add(sectLabel, gbc);

            // New Section text field

            textField = new JTextField(15);

            gbc.gridx = 1;

            gbc.gridy = 0;

            gbc.gridwidth = 2;

            gbc.insets = new Insets(0, 0, 4, 0);

            add(textField, gbc);

            // Placement Section label

            prop = LABEL_PLACE_SECTION_LS_PROPERTY;

            face = mVDH.getLanguageString(prop);

            JLabel placeLabel = new JLabel(face);

            gbc.gridx = 0;

            gbc.gridy = 1;

            gbc.gridwidth = 1;

            gbc.insets = new Insets(0, 0, 0, 4);

            add(placeLabel, gbc);

            // Before or after combo

            String befProp = LABEL_BEFORE_LS_PROPERTY;

            String aftProp = LABEL_AFTER_LS_PROPERTY;

            String bef = mVDH.getLanguageString(befProp);

            String aft = mVDH.getLanguageString(aftProp);

            befAftCombo = new JComboBox(new String[]{bef, aft});

            befAftCombo.setSelectedItem(aft);

            gbc.gridx = 1;

            gbc.gridy = 1;

            gbc.insets = new Insets(0, 0, 0, 4);

            add(befAftCombo, gbc);

            // Section List combo

            String[] sections = (String[]) additionalData[0];

            sectCombo = new JComboBox(sections);

            sectCombo.setSelectedIndex(sections.length - 1);

            gbc.gridx = 2;

            gbc.gridy = 1;

            gbc.insets = new Insets(0, 0, 0, 0);

            add(sectCombo, gbc);
        }
        public JTextField textField;
        public JComboBox befAftCombo;
        public JComboBox sectCombo;
    }
}
