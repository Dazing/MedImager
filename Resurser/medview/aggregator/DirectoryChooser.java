package medview.aggregator;

import javax.swing.JFileChooser;
import java.io.*;
import javax.swing.filechooser.FileView;
import java.awt.event.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class DirectoryChooser extends JFileChooser {


    public DirectoryChooser() {
		super();
		init();
    }
	public DirectoryChooser(String aDir) {
		super(aDir);
		init();
	}
	public DirectoryChooser(FileView aView){
		this();
		setFileView(aView);
	}
	public DirectoryChooser(String aDir,FileView aView){
		this(aDir);
		setFileView(aView);
	}

	private void init(){
		setMultiSelectionEnabled(false);
		resetChoosableFileFilters();
		setAcceptAllFileFilterUsed(true);
		setFileSelectionMode(FILES_AND_DIRECTORIES);
		setFileHidingEnabled(true);
	}
	public void approveSelection() {
        DirectoryFileView fW = (DirectoryFileView)getFileView();
		File curDir = getCurrentDirectory();
		File file   = getSelectedFile();

		if(file == null)  super.approveSelection();
		if(fW   == null)  super.approveSelection();

        if(  (file.isDirectory()) && fW.isFiltered(file) ){

			super.approveSelection();
        }
		else{

			super.setCurrentDirectory(file);
		}
    }
}