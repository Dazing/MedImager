package com.company;


import medview.common.components.MedViewComponentUtilities;
import medview.common.data.MedViewUtilities;
import medview.datahandling.PatientIdentifier;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.MVDHandler;
import medview.datahandling.images.ExaminationImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        MedViewUtilities utilObj = new MedViewUtilities();
        MVDHandler handler = new MVDHandler();
        if(utilObj.isPathMVD(args[0])){
            File tree = new File(args[0]);
            handler.setExaminationDataLocation(args[0]);
            try{
                PatientIdentifier [] patients = handler.getPatients();
                JFrame frame = new JFrame();
                frame.getContentPane().setLayout(new FlowLayout());
                for (PatientIdentifier pid : patients){
                    ExaminationIdentifier[] examinations = handler.getExaminations(pid);
                    for (ExaminationIdentifier examination : examinations){
                        ExaminationImage [] imgs = handler.getImages(examination);
                        for (ExaminationImage img : imgs){
                            frame.getContentPane().add(new JLabel(new ImageIcon(img.getThumbnail())));
                        }
                    }
                }
                frame.pack();
                frame.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Ej giltig fil");
        }
    }
}
