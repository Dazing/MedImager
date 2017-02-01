// $Id: InfoInput.java,v 1.5 2010/07/01 08:17:25 oloft Exp $
package medview.medrecords.components.inputs;

import medview.medrecords.models.InfoModel;
import medview.medrecords.data.MRConst;
import medview.datahandling.examination.tree.Tree;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class InfoInput extends ValueInputComponent {

    public InfoInput(InfoModel infoModel) {
        super(infoModel);

        // MRConst.LINE_WIDTH = number of characters in a not very wide window
        // MRConst.BOLD_LINE_WIDTH = number of bold characters in a not very wide window

        int textWidth = infoModel.getText().length();
        // approximate number of lines for text
        int numberOfLines = (textWidth / MRConst.LINE_WIDTH) + 1; // if text is shorter than line, the result should be 1

        JTextArea textArea = null;
        int textType = infoModel.getTextType();

        switch (textType) {

            // The details here are based on trial-and-error, experimentation with nbr of lines, newlines etc
            case InfoModel.SUBHEADER:
                numberOfLines = (textWidth / MRConst.BOLD_LINE_WIDTH) + 1; // use another line width

                textArea = new JTextArea(/*" <info-header>*/infoModel.getText(), numberOfLines + 1, 0);
                // System.out.println("boldLineWidth: " + MRConst.BOLD_LINE_WIDTH + " textWidth: " + textWidth + " numberOfLines: " + numberOfLines);

                break;
            case InfoModel.TEXT:
                // System.out.println("lineWidth: " + lineWidth + " textWidth: "+textWidth + " numberOfLines: " + numberOfLines);

                // textArea = new JTextArea(" \n" + infoModel.getText()+'\n',numberOfLines+2,0);
                textArea = new JTextArea(/*" <info-text>" \n" + */ infoModel.getText() + '\n', numberOfLines + 1, 0);

                break;
            default:
                textArea = new JTextArea();

                System.out.println("Error: Wrong <INFO> type");
                break;
        }
        textArea.setLineWrap(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);

        JScrollPane textAreaScrollPane = new JScrollPane(textArea);

        textAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        textAreaScrollPane.setBorder(null);

        this.setLayout(new BorderLayout(0, 0));

        add(textAreaScrollPane);


        switch (textType) {
            case InfoModel.SUBHEADER:
                Font textAreaFont = textArea.getFont().deriveFont(Font.BOLD);
                textArea.setFont(textAreaFont);
                break;
            case InfoModel.TEXT:
                break;
            default:
                System.out.println("Error: Wrong <INFO> type");
                break;
        }
        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));




    }

    public String[] getValues() {
        return null;
    }

    public void clearContents() {
    }

    public void focusInput() {
    }

    public void verifyInput() {
    }

    public void putCustomPreset(String key, String value) {
    }

    public void putPreset(String value) {
    }

    public void setEditable(boolean e) {
    }

    public Tree getTreeRepresentation(Date date, String pCode) {
        return null;
    }
}
