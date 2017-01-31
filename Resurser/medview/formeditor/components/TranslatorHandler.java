/*
 * $Id: TranslatorHandler.java,v 1.2 2003/11/12 09:22:14 oloft Exp $
 *
 * Created on den 26 juli 2002, 10:42
 *
 */

// There should be a newer version in MedRecords - OT 03-11-10
package medview.formeditor.components;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import java.io.*;

import medview.formeditor.models.*;
import medview.formeditor.tools.*;
import medview.formeditor.data.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;  // Examination identifier
import medview.summarycreator.view.*;

import medview.common.translator.*;
import medview.common.generator.*;
import medview.common.template.*;
import medview.common.dialogs.*;

import misc.foundation.io.*;
import misc.foundation.*;




/**
 *
 * @author  nader
 */

public class TranslatorHandler extends JFrame {
    
    private String              mTranslatorName;
    private String              mTermName;    
    
    private TranslatorModel     mTranslatorModel;
    private TranslationModel    mTranslationModel;
    private PresetModel         mPresetModel;
    
    private JPanel              mButtonPanel;

    private DatahandlingHandler mDH = DatahandlingHandler.getInstance();    
    
    
    public TranslatorHandler() {
        super();
        
        mTranslatorName  = Config.getTranslator();
        
        try {
            mTranslatorModel = MedViewDataHandler.instance().loadTranslator(mTranslatorName);
        }
        catch (CouldNotLoadException e) {
            Ut.error(" Can't create tanslator Model " + e.getMessage());
            //return null;
        }
    }
    /** Creates new TranslatorHandler */
    public TranslatorHandler(PresetModel in_presetModel, String in_newValue) {
        this(in_presetModel.getTermName(),in_newValue);
        
        mPresetModel = in_presetModel;
    }
    
    public TranslatorHandler(String  in_term, String in_newValue) {
        
        super();
        this.setTitle(mDH.getLanguageString(mDH.TITLE_TERM_EDITOR_LS_PROPERTY) + " " + in_term);

        mTermName = in_term;        
        
        mTranslatorName  =  Config.getTranslator();
        try {
            mTranslatorModel = MedViewDataHandler.instance().loadTranslator(mTranslatorName);
        }
        catch (CouldNotLoadException e) {e.printStackTrace();}
        
        if (mTranslatorModel == null) return;
        
        
        mTranslationModel = mTranslatorModel.getTranslationModel(mTermName);
        
        if (mTranslationModel == null) {
            try{
                mTranslationModel = mTranslatorModel.addTranslationModel(in_term);
            }
            catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
        
        if (mTranslationModel != null){
            mTranslationModel.addValue(in_newValue);            
        }
    }
    
    public Object[] getTerms() {
        return mTranslatorModel.getContainedTerms();
    }
    
    public void showTranslator(String aTermName) {
        mTermName = aTermName;
        
        mTranslationModel = mTranslatorModel.getTranslationModel(mTermName);
        
        if (mTranslationModel == null) { return; }
        
        this.setTitle(mDH.getLanguageString(mDH.TITLE_TERM_EDITOR_LS_PROPERTY) + " " + aTermName);
        
        showTranslator(mTranslationModel);
    }
    
    private void showTranslator(TranslationModel mTranslationModel) {
        
        int type = mTranslationModel.getType();
        
        AbstractTableTermView termView =
        (AbstractTableTermView) TermViewFactory.instance().createTermView(type);
        termView.setModel(mTranslationModel);
        setButtonPanel();
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(termView,BorderLayout.CENTER);
        this.getContentPane().add(mButtonPanel,BorderLayout.SOUTH);
        
        this.pack();
    }
    
    private void setButtonPanel(){
        
        mButtonPanel = new JPanel();
        mButtonPanel.setLayout(new GridLayout(1,3,5,0));
        
        JButton   tCloseBotton = new JButton(mDH.getLanguageString(mDH.BUTTON_CLOSE_LS_PROPERTY));
        JButton   tSaveBotton  = new JButton(mDH.getLanguageString(mDH.BUTTON_SAVE_TEXT_LS_PROPERTY));
        
        mButtonPanel.add(tSaveBotton);
        mButtonPanel.add(new JLabel());
        mButtonPanel.add(tCloseBotton);
        
        tSaveBotton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MedViewDataHandler.instance().saveTranslator(mTranslatorModel,mTranslatorName);
            }
        });
        
        tCloseBotton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
    }
    
    public TranslatorHandler(ExaminationModel pExModel) {
        super();
        showPreview(pExModel);
    }
    public void showPreview(ExaminationModel pExModel) {
        
        if(pExModel == null) return;
        
        StyledDocument prevDoc =  getPreview(pExModel);
        
        if(prevDoc != null){
           // MedViewDialog previewDialog = new PreviewDialog(null, prevDoc, "Testing");
		//previewDialog.show();       
            JTextPane    textPane    = new JTextPane(prevDoc);
            JScrollPane scrollPane = new JScrollPane(textPane);
            Container contentPane = this.getContentPane();
            contentPane.removeAll();            
            this.getContentPane().add(scrollPane, BorderLayout.CENTER);
            this.setSize(350,500);
           // this.pack();
            this.show();          
        }
        else Ut.error(" translator handler Can not generate the textpane");
    }
    
    private ExaminationValueContainer[] getContainer(ExaminationModel pExModel){
        ExaminationValueContainer[] containers  = new ExaminationValueContainer[1];
        HashMap        aMap                     = pExModel.getTremsHashTable();
        
        if(aMap == null) return null;
        
        HashVectorEVC  aVec  = new HashVectorEVC(aMap);
        containers[0]        = aVec;
        return containers ;
    }
    
    private  StyledDocument getPreview(ExaminationModel pExModel){
        TemplateModel               templateModel       = null;
        TranslatorModel             translatorModel     = null;
        ExaminationIdentifier[]     ids                 = new ExaminationIdentifier[1];
        ExaminationValueContainer[] containers;//          = new ExaminationValueContainer[1];
        String                      aPcode              = pExModel.getIdcode();
        try{
            MedViewDataHandler mVDH = MedViewDataHandler.instance();
            //mVDH.setExaminationDataLocation(Config.getMVDDir());
            
            templateModel       = mVDH.loadTemplate(Config.getPreviewTemplate());
            translatorModel     = mVDH.loadTranslator(Config.getTranslator());//translatorFilename);
            ids[0]              = new MedViewExaminationIdentifier(aPcode);
            containers          = getContainer(pExModel);
            
            if(containers == null) return null;
            
            // De undersökningar du vill visa i previewen.
            String[]                sections        = templateModel.getAllContainedSections();
            GeneratorEngineBuilder  engineBuilder   = new GeneratorEngineBuilder();// medview.common.generator;
            
            engineBuilder.buildIdentifiers( ids );
            engineBuilder.buildValueContainers( containers );
            engineBuilder.buildTemplateModel( templateModel );
            engineBuilder.buildTranslatorModel( translatorModel );
            engineBuilder.buildSections( sections );
            
            GeneratorEngine     engine      = engineBuilder.getEngine();
            StyledDocument      document    = engine.generateDocument();
            return document;
        }
        
        catch (Exception exc)  {
            Ut.error(exc.getMessage());
            exc.printStackTrace();
            return null;
        }
    }
    
    
    
    
    
    
    
    
    
    
}














