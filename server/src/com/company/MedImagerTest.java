package com.company;

import medview.datahandling.*;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.NoSuchExaminationException;
import medview.datahandling.images.ExaminationImage;
import misc.foundation.text.CouldNotParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Michel on 13.2.2017.
 */
public class MedImagerTest{
	
	public static void main(String[] args){
		
		/* Program that can take search values together with terms (such as "Adv-drug", "Occup"), which are the
		"category" to filter the search value by. "Occup" stands for Occupation and Adv-drug for "Överkänslighet"
		
		Example of working search value and term combinations are: (snickare, Occup), (pensionär, Occup), 
		(asacol, Adv-drug).
		 */
		
		
		MedImagerFrame frame = new MedImagerFrame();
		
		System.out.println(frame.examinations.size());
		
	}
}
