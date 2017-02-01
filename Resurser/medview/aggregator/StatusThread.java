package medview.aggregator;

import javax.swing.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class StatusThread extends Thread {

    String              mText;
    JLabel              mStatusBar;
	AggregatorFrame     mMainFrame;
	int                 mFileNo;

    public StatusThread(JLabel statusBar,String aText) {
		mText= aText;
		mStatusBar = statusBar;
    }
	 public StatusThread(AggregatorFrame aFrame,int noOf) {
		mMainFrame  = aFrame;
		mFileNo     = noOf;
    }

	 public void run() {
		//mMainFrame.writeInfo(mFileNo);
		//informFrame();



    }
	synchronized void informFrame( ){
		try{
			notifyAll();
		}
		catch(Exception e){
			Ut.prt(e.getMessage() );
			e.printStackTrace();
		}

	}


}