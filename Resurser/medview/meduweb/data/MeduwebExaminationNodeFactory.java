/*
 * @(#)MeduwebExaminationNodeFactory.java
 *
 * $Id: MeduwebExaminationNodeFactory.java,v 
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import java.util.*;

import javax.swing.text.*;

import medview.common.template.*;
import medview.common.translator.*;
import medview.common.generator.*;
import medview.datahandling.*;

public class MeduwebExaminationNodeFactory 
{


	public ExaminationRootNode createExaminationRootNode(StyledDocument doc, int sOffs,
		int eOffs, String pid, Date date) throws CouldNotCreateNodeException
	{
		try
		{
			ExaminationRootNode retNode = new ExaminationRootNode();

			Position start = doc.createPosition(sOffs);

			Position end = doc.createPosition(eOffs);

			retNode.setStartPosition(start);

			retNode.setEndPosition(end);

			retNode.setDocument(doc);

			retNode.setDate(date);

			retNode.setPID(pid);

			return retNode;
		}
		catch (BadLocationException e)
		{
			throw new CouldNotCreateNodeException(e.getMessage());
		}
	}





	public SectionNode createSectionNode(StyledDocument doc, int offs, SectionModel model)
		throws CouldNotCreateNodeException
	{
		try
		{
			SectionNode sectNode = new SectionNode();

			Position startPos = model.getStartPosition(); // original document

			Position endPos = model.getEndPosition(); // original document

			startPos = doc.createPosition(startPos.getOffset() + offs);

			endPos = doc.createPosition(endPos.getOffset() + offs);

			sectNode.setStartPosition(startPos);

			sectNode.setEndPosition(endPos);

			sectNode.setID(model.getName());

			sectNode.setDocument(doc);

			return sectNode;

			/* NOTE: it is important to distinguish between the
			 * two separate documents that exists in this context.
			 * We have the original document, kept in the templateModel
			 * variable, and we have the 'stamped' document, which is
			 * passed into this method. The templateModel section positions
			 * and offsets are only valid in the templateModel document, and
			 * does not accurately represent the offsets when alteration of
			 * the stamped document has begun. When alteration has begun of
			 * the stamped document, the positions need to slide along to
			 * still represent correct positions in the document. */
		}
		catch (BadLocationException e)
		{
			throw new CouldNotCreateNodeException(e.getMessage());
		}
	}





	/**
	 * Will create an array of line nodes. A line node contains
	 * only character data (new lines etc. are ignored and are thus
	 * not included in a line node).
	 */
	public LineNode[] createLineNodes(StyledDocument doc, int start, int end)
		throws CouldNotCreateNodeException
	{
		try
		{
			int lineSOffs = start;

			int lineEOffs = start;

			LineNode currNode = null;

			Vector vect = new Vector();

			while (lineEOffs < end)
			{
				currNode = new LineNode();

				currNode.setStartPosition(doc.createPosition(lineSOffs));

				lineEOffs = GeneratorUtilities.calculateLineContentEnd(doc, lineSOffs);

				currNode.setEndPosition(doc.createPosition(lineEOffs));

				currNode.setID(LINE_NODE_ID);

				currNode.setDocument(doc);

				vect.add(currNode);

				try
				{
					lineSOffs = GeneratorUtilities.calculateNextLineStartFromEnd(doc, lineEOffs);

					lineEOffs = lineSOffs;
				}
				catch (NoMoreLinesException e)
				{
					break; // may happen if last section
				}
			}

			LineNode[] retArr = new LineNode[vect.size()];

			vect.toArray(retArr);

			return retArr;
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();

			throw new CouldNotCreateNodeException(e.getMessage());
		}
	}

	/* NOTE: each sectionmodel always starts with a 'section model
	 * starter' content (occupies one slot in the document content),
	 * that is why we start our extraction of lines at the model
	 * start offset added with one. Note also that the 'no more lines
	 * exception' is not an error exception in some cases, for example
	 * when the last section has been reached in the document, and all
	 * lines of that section has been extracted. That is why we suppress
	 * this exception in the code above, since the line start offset is
	 * only used in the next iteration, which will not occur since the
	 * end offset is set to the start offset which in turn is larger than
	 * the section end, and the loop will break automatically. */


	/**
	 * Creates a TermNode that represents a term contained
	 * in the template and thus also contained as a leaf
	 * under a line node in the parse tree. There are two
	 * general types of term nodes - 1) the nontranslated
	 * node, not associated with a translator and only uses
	 * the specified values, and 2) the translated node,
	 * which takes the specified values and processes them
	 * by using the translator model. The parent coupling can
	 * not be done in this method, since the method has no
	 * knowledge of the node's place in the parse tree. This
	 * coupling needs to be done by the caller.<br>
	 * <br>
	 * Note: by giving a <i>null</i> value for the values
	 * argument, you obtain a term node that removes the line
	 * it is contained in.
	 */
	public TermNode createTermNode(StyledDocument doc, int offs, TermModel termModel,
		String[] values, MeduwebTranslatorModel translatorModel) throws CouldNotCreateNodeException
	{
		try
		{
			int termType = -1;

			TermNode termNode = null;

			String termName = termModel.getName();

			boolean isDerived = dTH.isDerivedTerm(termName);

			boolean inDefinitions = mVDH.termExists(termName);

			boolean inv = !isDerived && !inDefinitions;

			if ((values == null) || (values.length == 0) || (inv))
			{
				termNode = new LineRemovingTermNode(termName);
			}
			else
			{
				if (isDerived)
				{
					termType = dTH.getDerivedTermType(termName);
				}
				else
				{
					termType = mVDH.getType(termName);
				}

				switch (termType)
				{
					case TermDataHandler.FREE_TYPE:
					{
						termNode = new FreeNode(termName);

						break;
					}

					case TermDataHandler.INTERVAL_TYPE:
					{
						termNode = new MeduwebIntervalNode(termName);

						setupTranslatedNode(termNode, termName, translatorModel);

						break;
					}

					case TermDataHandler.MULTIPLE_TYPE:
					{
						termNode = new MeduwebSeparatedNode(termName);

						setupTranslatedNode(termNode, termName, translatorModel);

						break;
					}

					case TermDataHandler.REGULAR_TYPE:
					{
						termNode = new MeduwebRegularNode(termName);

						setupTranslatedNode(termNode, termName, translatorModel);

						break;
					}

					default:
					{
						throw new CouldNotCreateNodeException(termName);
					}
				}
			}

			Position startPos = termModel.getStartPosition();

			Position endPos = termModel.getEndPosition();

			startPos = doc.createPosition(startPos.getOffset() + offs);

			endPos = doc.createPosition(endPos.getOffset() + offs);

			termNode.setStartPosition(startPos);

			termNode.setEndPosition(endPos);

			termNode.setDerived(isDerived);

			termNode.setValues(values);

			termNode.setDocument(doc);

			return termNode;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new CouldNotCreateNodeException(e.getMessage());
		}
	}

	private void setupTranslatedNode(TermNode tNode, String tName, MeduwebTranslatorModel tModel)
	{
		MeduwebTranslationModel translationModel = tModel.getTranslationModel(tName);

		((MeduwebTranslatedNode)tNode).setTranslationModel(translationModel);
	}





	public static MeduwebExaminationNodeFactory instance()
	{
		if (instance == null) { instance = new MeduwebExaminationNodeFactory(); }

		return instance;
	}





	private void initSimpleMembers()
	{
		mVDH = MeduwebDataHandler.instance();

		dTH = MeduwebDerivedTermHandler.instance();
	}

	private MeduwebExaminationNodeFactory()
	{
		initSimpleMembers();
	}

	private static MeduwebExaminationNodeFactory instance;

	private static final String LINE_NODE_ID = "Line node";

	private MeduwebDataHandler mVDH;

	private MeduwebDerivedTermHandler dTH;

}