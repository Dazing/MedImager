package medview.meduweb.data;

import javax.swing.*;
import javax.swing.text.*;

import java.io.*;

import java.util.*;

import medview.common.generator.*;
import medview.common.template.*;
import misc.gui.utilities.*;

public class MeduwebTemplateModel extends TemplateModel {

	public MeduwebTemplateModel() {
		super();	
	}
	
	public void markAsSection(String name, int start, int end) throws BadLocationException
	{
		Position sP = document.createPosition(start);

		Position eP = document.createPosition(end);

		/*if (emptyContentIcon == null) { createEmptyContentIcon(); }

		SimpleAttributeSet attr = new SimpleAttributeSet();

		StyleConstants.setIcon(attr, emptyContentIcon);

		document.setCharacterAttributes(start, 1, attr, false);
		*/
		SectionModel sM = new SectionModel(name, sP, eP);

		sectionModels.add(sM);

		Collections.sort(sectionModels);

	}


}