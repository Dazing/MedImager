/*
 * $It$
 *
 * $Log: ReadingThread.java,v $
 * Revision 1.2  2003/06/24 17:11:08  erichson
 * Mainly cleanup // NE
 *
 */

package medview.aggregator;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import java.util.TimerTask;

import java.io.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class ReadingThread implements ActionListener {
    
    private javax.swing.Timer   mReadingTimer;
    private javax.swing.Timer   mShowingTimer;
    
    //private ProgressMonitor     mProgressMonitor;
    
    private File                mForestFile;
    private File                mMVDFile;
    private int                 mNoOf;
    private int                 mFilesRead;
    private AggregatorFrame     mFrame;
    private boolean             isReadingStarted ;
    private boolean             isDataBaseRead;
    private boolean             isFirstTime;
    
    
    public ReadingThread(File aForest,File aFile,int aNo,AggregatorFrame aFrame) {
        mForestFile         = aForest;
        mMVDFile            = aFile;
        mNoOf               = aNo;
        mFrame              = aFrame;
        mFilesRead          = 0;
        isReadingStarted    = false;
        isDataBaseRead      = false;
        isFirstTime         = true;
        mReadingTimer       = new javax.swing.Timer(700, this);        
        mReadingTimer.start();
                                                
    }
    public void setIsReady(){ isDataBaseRead = true;}
    public void actionPerformed(ActionEvent evt) {
        startReading();
    }
    
    void startReading(){
        if (!isReadingStarted){
            mFrame.readDBFromTimmer(mForestFile, mMVDFile, mNoOf,this); // ReadingThread calls method in AggregatorFrame
            isReadingStarted = true;
            //Ut.prt("Reading Started  " + mReadingTimer.isCoalesce() );
        }
        else if(isDataBaseRead) {
            Toolkit.getDefaultToolkit().beep();
            mReadingTimer.stop();
            //Ut.prt("Reading Finished " );
        }
        //else  Ut.prt("ELSE NOT Finished " );
    }                        
}

/* Below is Nader's old stuff, I won't remove it // NE */

        /*class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
                        mFilesRead = mFilesRead + 40;
                    Ut.prt("Listener action");
                        if (isFirstTime){
                                isFirstTime = false;
                                Ut.prt("Showing Started " + mFilesRead + mShowingTimer.isCoalesce() );
                                mReadingTimer.start();
                        }
                        else if(isDataBaseRead) {
                                Toolkit.getDefaultToolkit().beep();
                                mShowingTimer.stop();
                                mProgressMonitor.close();
                                Ut.prt("Showing Is Ready  " + mFilesRead );
                        }
                        else {
                                mFilesRead = mFilesRead + 40;
                                mProgressMonitor.setNote("Wait....");
                                mProgressMonitor.setProgress(mFilesRead);
                                Ut.prt("Showing NOT Ready " );
                        }
         
        }
    }
         
        class RemindTask extends TimerTask {
                ReadingThread mRt;
                RemindTask(ReadingThread rt){
                        super();
                        mRt = rt;
                }
        public void run() {
                        System.out.println("Time's uprrr!");
                    mFrame.readDBFromTimmer(mForestFile, mMVDFile, mNoOf,mRt);
            System.out.println("Time's up!");
           // timer.cancel(); //Terminate the timer thread
        }
    }
         */