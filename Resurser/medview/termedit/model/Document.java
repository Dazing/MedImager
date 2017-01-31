//
//  Document.java
//  FETest
//
//  Created by Olof Torgersson on Mon Nov 17 2003.
//  $Id: Document.java,v 1.3 2003/12/16 00:11:31 oloft Exp $.
//

package medview.termedit.model;

import java.io.*;
import java.util.*;

import medview.datahandling.*;

public class Document {
    
    private MedViewDataHandler mDH;

    private List changeListeners;
    private List documentListeners;

    private int newTermNum;
    private int newValueNum;

    private String selectedTerm;
    private String selectedValue;

    private final static String TENoTermSelected = "TENoTermSelected";
    private final static String TENoValueSelected = "TENoValueSelected";
    
    public Document() {
        mDH = MedViewDataHandler.instance();
        changeListeners = new ArrayList();
        documentListeners = new ArrayList();
        selectedTerm = TENoTermSelected;
        selectedValue = TENoValueSelected;
    }

    public void addChangeListener(DocumentChangeListener dcl) {
        changeListeners.add(dcl);
    }

    public void addDocumentListener(TermEditDocumentListener dcl) {
        documentListeners.add(dcl);
    }
    
    public int getTermCount() {
        try {
            return mDH.getTerms().length;
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return 0;
        }
    }
    
    public boolean containsTerm(String trm) {
        return false;
    }

    // Faster if not calling getTerms
    public int indexOfTerm(String trm)  throws java.io.IOException {
        String[] terms = getTerms();
        return Arrays.binarySearch(terms, trm);
    }
    
    public void createTerm(String trm) {
        System.out.println("Document createTerm: " + trm);
        try {
            if (!mDH.termExists(trm)) {
                // Terms is created as reguler, changes using setBasicType
                mDH.addTerm(trm,  TermDataHandler.REGULAR_TYPE);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        fireTermAdded(trm);
        documentDidChange();
    }

    public boolean renameTerm(String oldName, String newName) {
        System.out.println("Rename term: " + oldName + ", " + newName);
        if (!oldName.equals(newName)) {
            // Change backing storage
            try {
                int type = mDH.getType(oldName);
                // Changing term name means remove/add
                String[] values = mDH.getValues(oldName);
                mDH.removeTerm(oldName);					// Removes values
                mDH.addTerm(newName, type);

                for (int i=0; i<values.length; i++) {
                    mDH.addValue(newName, values[i]);
                }
                documentDidChange();
            }
            catch (Exception e) {
                System.out.println(e.toString());
                return false;
            }
        }
        return true;
    }

    // Lacks all sorts of error management
    public void removeTerm(String trm) throws java.io.IOException, NoSuchTermException {
        mDH.removeTerm(trm);
        fireTermRemoved();
        documentDidChange();
    }
    
    public String getFreshTermName() {
        String name = "New Term";

        if (newTermNum > 0) {
            name = name + " " + newTermNum;
        }
        newTermNum++;
        return name;
    }

    // Must be fixed, now very clumsy to get order right
    public String[] getTerms() throws java.io.IOException {
        String[] tma = mDH.getTerms();
        List tml = new ArrayList(Arrays.asList(tma));
        Collections.sort(tml);
        tma = (String[])tml.toArray(tma);
        return tma;
    }
    
    public int getValueCount(String term) {
        try {
            return mDH.getValues(term).length;
        }
        catch (Exception e) {
            return 0;
        }
    }
    
    public String[] getValues(String term) throws java.io.IOException, NoSuchTermException {
        return mDH.getValues(term);
    }

    public void addValue(String trm, String value)  throws java.io.IOException, NoSuchTermException, InvalidTypeException {
        mDH.addValue(trm, value);
        fireValueAdded(value);
        documentDidChange();
    }

    public void renameValue(String trm, String oldVal, String newVal)  throws IOException, NoSuchTermException, InvalidTypeException {
        mDH.removeValue(trm, oldVal);
        mDH.addValue(trm, newVal);
        fireValueRenamed(newVal);
        documentDidChange();
    }
    
    public void removeValue(String trm, String value) throws java.io.IOException, NoSuchTermException, InvalidTypeException {
        mDH.removeValue(trm, value);
        fireValueRemoved();
        documentDidChange();
    }

    public String getFreshValue() {
        String name = "New Value";

        if (newValueNum > 0) {
            name = name + " " + newValueNum;
        }
        newValueNum++;
        return name;
    }
    
    public void setBasicType(String trm, int type) {
        if (mDH.isValidTermType(type)) {
            // Change backing storage
            try {
                // Changing term type means remove/add
                if (mDH.getType(trm) != type) {
                    String[] values = mDH.getValues(trm);
                    mDH.removeTerm(trm);					// Removes values
                    mDH.addTerm(trm, type);

                    for (int i=0; i<values.length; i++) {
                        mDH.addValue(trm, values[i]);
                    }
                    fireTermTypeChanged();
                    documentDidChange();
                }
            }
            catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    public int getBasicType(String trm) throws IOException, NoSuchTermException, InvalidTypeException {
        return mDH.getType(trm);
    }

    public String getSelectedTerm() {
        return selectedTerm;
    }

    public void setSelectedTerm(String t) {
        if (!selectedTerm.equals(t)) {
            if (t!= null) {
                selectedTerm = t;
            }
            else {
                selectedTerm = TENoTermSelected;
            }
            System.out.println("setSelectedTerm: " + selectedTerm);
            fireTermSelectionChanged();
            documentDidChange();
        }
    }

    public void deselectTerm() {
        setSelectedTerm(TENoTermSelected);
    }
    
    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String v) {
        selectedValue = v;
        documentDidChange();
    }

    public void deselectValue() {
        setSelectedValue(TENoValueSelected);
    }
    
    public boolean isTermSelected() {
        return !getSelectedTerm().equals(TENoTermSelected);
    }

    public boolean isValueSelected() {
        return !getSelectedValue().equals(TENoValueSelected);
    }
    
    public void readFromFile(String path) throws IOException {
         System.out.println("Document readFromFile");
    }
    
    public void writeToFile(String path) throws IOException {
        System.out.println("Document writeToFile");
    }
    
    private void documentDidChange() {
        int count = changeListeners.size();
        for (int i=0; i<count; i++) {
            DocumentChangeListener dcl = (DocumentChangeListener)changeListeners.get(i);
            dcl.documentDidChange(this);
        }
    }

    private void fireTermSelectionChanged() {
        int count = documentListeners.size();
        for (int i=0; i<count; i++) {
            TermEditDocumentListener dl = (TermEditDocumentListener)documentListeners.get(i);
            dl.termSelectionChanged(new TermEditDocumentEvent(this));
        }
    }

    private void fireTermAdded(String trm) {
        int count = documentListeners.size();

        TermEditDocumentEvent evt = new TermEditDocumentEvent(this);
        evt.setTerm(trm);

        for (int i=0; i<count; i++) {
            TermEditDocumentListener dl = (TermEditDocumentListener)documentListeners.get(i);
            dl.termAdded(evt);
        }
    }

    private void fireTermRemoved() {
        int count = documentListeners.size();
        for (int i=0; i<count; i++) {
            TermEditDocumentListener dl = (TermEditDocumentListener)documentListeners.get(i);
            dl.termRemoved(new TermEditDocumentEvent(this));
        }
    }

    private void fireTermTypeChanged() {
        int count = documentListeners.size();
        for (int i=0; i<count; i++) {
            TermEditDocumentListener dl = (TermEditDocumentListener)documentListeners.get(i);
            dl.termTypeChanged(new TermEditDocumentEvent(this));
        }
    }

    private void fireValueAdded(String val) {
        int count = documentListeners.size();

        TermEditDocumentEvent evt = new TermEditDocumentEvent(this);
        evt.setValue(val);

        for (int i=0; i<count; i++) {
            TermEditDocumentListener dl = (TermEditDocumentListener)documentListeners.get(i);
            dl.valueAdded(evt);
        }
    }
    
    private void fireValueRemoved() {
        int count = documentListeners.size();
        for (int i=0; i<count; i++) {
            TermEditDocumentListener dl = (TermEditDocumentListener)documentListeners.get(i);
            dl.valueRemoved(new TermEditDocumentEvent(this));
        }
    }
    
    private void fireValueRenamed(String val) {
        int count = documentListeners.size();
        
        TermEditDocumentEvent evt = new TermEditDocumentEvent(this);
        evt.setValue(val);

        for (int i=0; i<count; i++) {
            TermEditDocumentListener dl = (TermEditDocumentListener)documentListeners.get(i);
            dl.valueRenamed(evt);
        }
    }
    
}
