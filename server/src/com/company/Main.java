package com.company;


import medview.common.components.MedViewComponentUtilities;
import medview.common.data.MedViewUtilities;
import medview.datahandling.InvalidPIDException;
import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.PatientIdentifier;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.MVDHandler;
import medview.datahandling.examination.NoSuchExaminationException;
import medview.datahandling.images.ExaminationImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class Main {

    public static void main(String[] args) throws IOException, InvalidPIDException, NoSuchExaminationException {

        MedViewUtilities utilObj = new MedViewUtilities();
        MedViewDataHandler handler = MedViewDataHandler.instance();
        handler.setExaminationDataLocation(args[0]);
        ArrayList<Examination> examinationArrayList = new ArrayList<>();
        System.out.println(handler.getPatients().length);
        for(PatientIdentifier pid : handler.getPatients()){
            for(ExaminationIdentifier eid : handler.getExaminations(pid)){
                Examination examination = new Examination();
                examination.setAGE(handler.getAge(pid, eid.getTime()));
                ExaminationValueContainer container = handler.getExaminationValueContainer(eid);
                try {
                    for(String s : container.getValues("Allergy")){
                        examination.setALLERGY(s);

                    }
                } catch (NoSuchTermException e) {
                    //e.printStackTrace();
                }
                examinationArrayList.add(examination);
            }
        }
        System.out.println(examinationArrayList.size());
        for(Examination ex : examinationArrayList){
            System.out.println("Ålder: " + ex.getAGE() + " Allergi: " + ex.getALLERGY());
        }
    }
}
