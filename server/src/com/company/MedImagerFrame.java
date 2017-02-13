package com.company;

import medview.datahandling.CouldNotSearchException;
import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.PatientIdentifier;
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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Michel on 13.2.2017.
 */
public class MedImagerFrame extends JFrame implements ActionListener{
	
	//GUI
	JPanel upperPanel;
	JPanel imagePanel;
	JTextField searchField;
	JTextField termField;
	JLabel searchLabel;
	JLabel termLabel;
	JButton button;
	
	//MedView
	MedViewDataHandler handler = MedViewDataHandler.instance();
	ArrayList<ExaminationIdentifier> examinations = new ArrayList<>();
	ArrayList<ExaminationValueContainer> evcs = new ArrayList<>();
	
	public MedImagerFrame(){
		//GUI
		upperPanel = new JPanel();
		imagePanel = new JPanel();
		searchField = new JTextField(10);
		termField = new JTextField(10);
		searchLabel = new JLabel("Search value:");
		termLabel = new JLabel("Term (e.g. Occup, Adv-drug, Ref-in):");
		button = new JButton("Search");
		button.addActionListener(this);
		
		searchField.setSize(10,10);
		
		upperPanel.setBackground(Color.WHITE);
		upperPanel.add(searchLabel);
		upperPanel.add(searchField);
		upperPanel.add(termLabel);
		upperPanel.add(termField);
		upperPanel.add(button);
		
		imagePanel.setBackground(Color.ORANGE);
		
		add(upperPanel, BorderLayout.NORTH);
		add(imagePanel);
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		
		//MedView
		try{
			handler.setExaminationDataLocation("TestData.mvd");
			PatientIdentifier[] pids = handler.getPatients();
			for(PatientIdentifier pid : pids){
				for(ExaminationIdentifier eid : handler.getExaminations(pid)){
					examinations.add(eid);
				}
			}
			
			for(ExaminationIdentifier eid : examinations){
				evcs.add(handler.getExaminationValueContainer(eid));
			}
			
		} catch(IOException e){
			e.printStackTrace();
		} catch(NoSuchExaminationException e){
			e.printStackTrace();
//		} catch(NoSuchTermException e){
//			e.printStackTrace();
//		} catch(CouldNotSearchException e){
//			e.printStackTrace();
//		} catch(CouldNotParseException e){
//			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		imagePanel.removeAll();
		if(e.getSource() == button){
			String searchText = searchField.getText().toLowerCase();
			String term = termField.getText();
			
			for(ExaminationValueContainer evc : evcs){
				try{
					String[] values = evc.getValues(term);
					for(String value : values){
						if(value.toLowerCase().indexOf(searchText) != -1){
							ExaminationImage[] images = handler.getImages(evc.getExaminationIdentifier());
							
							for(ExaminationImage image : images){
								imagePanel.add(new JLabel(new ImageIcon(image.getThumbnail())));
							}
							break;
						}
					}
				} catch(NoSuchTermException e1){
					e1.printStackTrace();
				} catch(IOException e1){
					e1.printStackTrace();
				}
			}
			revalidate();
			repaint();
		}
	}
}
