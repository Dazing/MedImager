/*
 * @(#)MeduwebDefaultGeneratorEngine.java
 *
 * $Id: MeduwebDefaultGeneratorEngine.java,v 
 *
 * --------------------------------
 * Original author: Fredrik Lindahl extended by Figge
 * --------------------------------
 */

package medview.meduweb.data;

import java.util.*;

import javax.swing.text.*;

import medview.common.template.*;
import medview.common.translator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.common.generator.*;
import misc.gui.utilities.*;

import misc.foundation.*;

class MeduwebDefaultGeneratorEngine extends MeduwebGeneratorEngine implements
	TranslationConstants, MedViewLanguageConstants
{

	public StyledDocument generateDocument( ) throws CouldNotGenerateException
	{
		return this.generateDocument(null);
	}

	public StyledDocument generateDocument( ProgressNotifiable not ) throws
		CouldNotGenerateException
	{
		try
		{
			TemplateModel orig = (TemplateModel) templateModel.clone();

			removeNonChosenSections(); // <- removes from templateModel...

			StyledDocument origDoc = templateModel.getDocument();

			StyledDocument retDoc = new DefaultStyledDocument();
			//System.out.println("0 " + origDoc.getText(0,origDoc.getLength()));

			int[] offs = spawnContent(origDoc, retDoc, valueContainers.length, not);
			//System.out.println("1 " + retDoc.getText(0,retDoc.getLength()));
			ExaminationRootNode[] eNodes = buildParseTrees(offs, retDoc, not);
			//System.out.println("2 " + retDoc.getText(0,retDoc.getLength()));
			// -------------------------------------------->
			// removeSpecialSectionCharacters(retDoc, offs); <- should be done in the future...
			// <--------------------------------------------

			parseTrees(eNodes, not);
			//System.out.println("3 " + retDoc.getText(0,retDoc.getLength()));
			templateModel = orig;

			return retDoc;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new CouldNotGenerateException(e.getMessage());
		}
	}

	/* NOTE: the removeNonChosenSections() method will alter
	 * the template model and remove all sections in the
	 * template that are not to be included in the generated
	 * output. Thus, we need to restore the templateModel member
	 * after the document to return has been extracted or the
	 * next call to generateDocument() will start with a
	 * template model that has been stripped of almost all
	 * sections. */








	/**
	 * Will remove the non-chosen sections and
	 * their content from the template model. This
	 * is done first in the generation process, to
	 * avoid unnecessary processing. Since the kept
	 * template model is a clone, and not the
	 * original template model in use in the
	 * application (the cloning is done in the
	 * builder of the generator), we are free to
	 * operate on the template model as we see fit
	 * - it will not affect anything outside the
	 * scope of the generation.
	 */
	private void removeNonChosenSections()
	{
		SectionModel[] sM = templateModel.getAllSectionModels();

		for (int ctr=0; ctr<sM.length; ctr++)
		{
			if (!shouldContainSection(sM[ctr]))
			{
				templateModel.removeSection(sM[ctr]);

				/* NOTE: the 'removeSection()' method
				 * removes both the section model and
				 * the content in the document that is
				 * represented by the model. */
			}
		}
	}

	private boolean shouldContainSection(SectionModel model)
	{
		String name = model.getName();

		for (int ctr=0; ctr<sections.length; ctr++)
		{
			if (sections[ctr].equalsIgnoreCase(name)) { return true; }
		}

		return false;
	}








	/**
	 * Will spawn the content of the original document,
	 * passed in as a parameter of the method, both
	 * the text as well as the element structure that
	 * marks up the text attributes. The number of spawns
	 * is also specified as a parameter of the method. The
	 * number of spawns needs to be at least 1, since a
	 * spawn count of 1 implies a 1:1 copy of the original
	 * document (so actually, the true spawn count is the
	 * specified spawn count - 1, depending on how you see
	 * it).
	 *
	 * @returns an array of integers indicating each
	 * examination's start offset into the document.
	 */
	private int[] spawnContent(StyledDocument origDoc, StyledDocument retDoc, int spawns, ProgressNotifiable not)
		throws BadLocationException, InvalidSpawnCountException
	{
		if (spawns < 1)
		{
			String message = "Spawn count (" + spawns + ") must be > 0";

			throw new InvalidSpawnCountException(message);
		}

		int endOffs = 0;

		boolean notify = (not != null);

		int[] retArr = new int[valueContainers.length];

		if (notify)
		{
			not.setTotal(spawns);

			String lS = LABEL_SPAWNING_CONTENT_LS_PROPERTY;

			not.setDescription(mVDH.getLanguageString(lS));
		}

		for (int ctr=0; ctr<spawns; ctr++)
		{
			if (notify) { not.setCurrent(ctr); }

			retArr[ctr] = endOffs;

			endOffs = spawn(endOffs, origDoc, retDoc);
		}

		return retArr;
	}

	private int spawn(int offs, StyledDocument origDoc, StyledDocument retDoc) throws BadLocationException
	{
		String origText = origDoc.getText(0, origDoc.getLength());

		retDoc.insertString(offs, origText, null);

		Element origRoot = origDoc.getDefaultRootElement();

		GUIUtilities.markDocument(retDoc, origRoot, offs);

		return offs + origText.length();
	}








	/**
	 * Will build a parse tree (a tree consisting of
	 * parse nodes) for each examination that is to
	 * be included in the generated journal. Each
	 * parse node is responsible for parsing it's
	 * part of the document, and may not exceed it's
	 * positions in the document.
	 */
	private ExaminationRootNode[] buildParseTrees(int[] offs, StyledDocument doc, ProgressNotifiable not)
		throws BadLocationException, CouldNotCreateNodeException
	{
		int eEnd = -1;

		int sEnd = -1;

		int lEnd = -1;

		int eStart = -1;

		int sStart = -1;

		int lStart = -1;

		int lLength = -1;

		Date date = null;

		String pid = null;

		TermNode tNode = null;

		int origDocStart = -1;

		TermModel[] tM = null;

		String derivee = null;

		String[] values = null;

		String termName = null;

		boolean notify = (not != null);

		MeduwebTranslatorModel trans = translatorModel;

		ExaminationIdentifier[] eI = identifiers;

		ExaminationValueContainer[] vC = valueContainers;

		SectionModel[] sM = templateModel.getAllSectionModels();

		ExaminationRootNode[] retArr = new ExaminationRootNode[vC.length];

		if (notify)
		{
			not.setTotal(vC.length);

			String lS = LABEL_BUILDING_PARSE_TREES_LS_PROPERTY;

			not.setDescription(mVDH.getLanguageString(lS));
		}

		for (int ctr1=0; ctr1<vC.length; ctr1++)
		{
			if (notify) { not.setCurrent(ctr1); }

			eStart = offs[ctr1];

			pid = eI[ctr1].getPcode();

			date = eI[ctr1].getTime();

			eEnd = (ctr1 == (offs.length-1)) ? doc.getLength() : offs[ctr1+1] - 1 ;

			ExaminationRootNode eNode = nodeFactory.createExaminationRootNode(doc, eStart, eEnd, pid, date);

			for (int ctr2=0; ctr2<sM.length; ctr2++)
			{
				SectionNode sNode = nodeFactory.createSectionNode(doc, offs[ctr1], sM[ctr2]);

				eNode.addChildNode(sNode);

				sNode.setParent(eNode);

				sStart = sNode.getStartPosition().getOffset();

				sEnd = sNode.getEndPosition().getOffset();

				LineNode[] lNodes = nodeFactory.createLineNodes(doc, sStart+1, sEnd);

				for (int ctr3=0; ctr3<lNodes.length; ctr3++)
				{
					sNode.addChildNode(lNodes[ctr3]);

					lNodes[ctr3].setParent(sNode);

					lStart = lNodes[ctr3].getStartPosition().getOffset();

					lEnd = lNodes[ctr3].getEndPosition().getOffset();

					lLength = lEnd - lStart;

					origDocStart = lStart - offs[ctr1];

					tM = sM[ctr2].getTermModelsInInterval(origDocStart, lLength);

					for (int ctr4=0; ctr4<tM.length; ctr4++)
					{
						try
						{
							termName = tM[ctr4].getName();

							if (dTH.isDerivedTerm(termName))
							{
								if (dTH.isOnlyPCodeDateDerived(termName))
								{
									values = dTH.getDerivedValues(termName, null, date, pid);
								}
								else
								{
									derivee = dTH.getDerivedTermDerivee(termName);

									String[] vCVals = vC[ctr1].getValues(derivee); // NSTE - see note...

									if ((vCVals == null) || (vCVals.length == 0))
									{
										values = null;
									}
									else
									{
										values = dTH.getDerivedValues(termName, vCVals[0], date, pid);
									}
								}
							}
							else
							{
								values = vC[ctr1].getValues(termName); // NSTE - see note...
							}
						}
						catch (NoSuchTermException e) { values = null; } // see note...

						tNode = nodeFactory.createTermNode(doc, offs[ctr1], tM[ctr4], values, trans);

						lNodes[ctr3].addChildNode(tNode);

						tNode.setParent(lNodes[ctr3]);
					}
				}
			}

			retArr[ctr1] = eNode;
		}

		return retArr;
	}

	/* NOTE: It is important to realize that the caught
	 * NoSuchTermException in the code above is a result
	 * from a call to the value container to return values
	 * for a term that it does not contain. It may be cases,
	 * though, when both the value container and the template model
	 * contains terms that are non-existant in the term
	 * definition files. This must be handled in the examination
	 * node factory, ie the factory needs to obtain the type of
	 * the term in order to know which type of examination node
	 * it is to produce. It does this by the only way possible,
	 * namely by asking the data handler for the type of the
	 * term. If the term is non-existant in the term definition
	 * files, this must be handled by the factory. Note also
	 * that a values parameter being null results in a node
	 * being placed in the tree that removes the line that the
	 * term is in. */








	/**
	 * Calls the parseNode() method of each
	 * parse tree root, which will subsequently
	 * invoke parseNode() on each contained
	 * section nodes, which in turn invokes
	 * parseNode() on each contained term etc.
	 */
	private void parseTrees(ExaminationRootNode[] eNodes, ProgressNotifiable not) throws
		CouldNotParseTreeException
	{
		boolean notify = (not != null);

		try
		{
			if (notify)
			{
				not.setTotal(eNodes.length);

				String lS = LABEL_PARSING_TREES_LS_PROPERTY;

				not.setDescription(mVDH.getLanguageString(lS));
			}

			for (int ctr=0; ctr<eNodes.length; ctr++)
			{
				if (notify) { not.setCurrent(ctr); }

				boolean good = eNodes[ctr].parseNode();
				if(!good)
					System.out.println("Parser removed this line!");
			}
		}
		catch (CouldNotParseNodeException e)
		{
			e.printStackTrace();

			throw new CouldNotParseTreeException(e.getMessage());
		}
	}








	private void initSimpleMembers()
	{
		dTH = MeduwebDerivedTermHandler.instance();

		mVDH = MeduwebDataHandler.instance();

		nodeFactory = MeduwebExaminationNodeFactory.instance();
	}

	public MeduwebDefaultGeneratorEngine()
	{
		initSimpleMembers();
	}

	private MeduwebDerivedTermHandler dTH;

	private MeduwebDataHandler mVDH;

	private MeduwebExaminationNodeFactory nodeFactory;

}
