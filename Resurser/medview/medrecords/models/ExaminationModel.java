/*
 * $Id: ExaminationModel.java,v 1.15 2005/07/18 13:28:33 erichson Exp $
 *
 * Created on June 15, 2001, 12:18 PM
 *
 * $Log: ExaminationModel.java,v $
 * Revision 1.15  2005/07/18 13:28:33  erichson
 * Added dump method (for debugging)
 *
 * Revision 1.14  2004/02/20 12:12:40  lindahlf
 * Valbart visa översättare vid inmatning av nytt värde
 *
 * Revision 1.13  2003/11/18 00:45:07  oloft
 * getCategoryNames caused class cast exception
 *
 * Revision 1.12  2003/11/11 14:44:48  oloft
 * Switching main-branch
 *
 * Revision 1.11.2.2  2003/09/09 14:00:32  erichson
 * Updated id code handling.
 *
 * Revision 1.11.2.1  2003/08/14 12:10:18  erichson
 * Removed value handling methods.
 *
 * Revision 1.11  2003/07/23 00:31:27  erichson
 * valueChanged method changed to setValueChanged(boolean)
 *
 */

package medview.medrecords.models;

import java.util.*;
import javax.swing.event.*;

import misc.foundation.HashVector;

/**
 *
 * ExaminationModel: Models an input form (with categories) as a data structure. Does NOT handle values anymore.
 *
 * @author  nils
 * @version
 */

public class ExaminationModel implements ChangeListener
{
    private Vector          mChangeListeners;

    private HashVector      mCategories; 	// maps category name to category model

    private String          mTreeFile = null;

    /**
     * Creates new ExaminationModel.
     */
    public ExaminationModel()
    {
        super();

        mCategories = new HashVector();

        mChangeListeners = new Vector();

        mTreeFile = null;
    }

    public void setTreeFile(String pFileName)
    {
        mTreeFile = pFileName;
    }

    public String  getTreeFile()
    {
        return mTreeFile;
    }

    public boolean containsCategory(String name)
    {
        return mCategories.containsKey(name);
    }

    public CategoryModel getCategory(String name)
    {
        return (CategoryModel) mCategories.get(name);
    }

    public String[] getCategoryNames()
    {
        int cnt = mCategories.size();

        String[] names = new String[cnt];

        CategoryModel[] cats = getCategories();

        for (int i=0; i<cnt; i++)
        {
            names[i]=cats[i].getTitle();
        }

        return names;
    }

    public CategoryModel[] getCategories()
    {
        CategoryModel[] cats = new CategoryModel[mCategories.size()];

        if(mCategories != null)
        {
            cats = (CategoryModel[]) mCategories.getValueArray(cats);

            return cats;
        }

        return null;
    }

    /**
     * Add a category
     */
    public void addCategory(CategoryModel catModel)
    {
        mCategories.put(catModel.getTitle(),catModel);

        catModel.addChangeListener(this);

        fireStateChanged();
    }

    /**
     * Move a category in the order.
     * @param relativeAdjustment the steps to move the category
     * (for example 2 (down) or -1 (up)
     */
    public void moveCategory(CategoryModel catModel, int relativeAdjustment)
    {
        mCategories.moveRelative(catModel,relativeAdjustment);

        catModel.removeChangeListener(this);

        fireStateChanged();
    }

    /**
     * Remove a category
     */
    public void removeCategory(CategoryModel catModel)
    {
        mCategories.removeByKey(catModel.getTitle());

        fireStateChanged();
    }

    public void addChangeListener(ChangeListener cl)
    {
        if (! mChangeListeners.contains(cl))
        {
            mChangeListeners.add(cl);
		}
    }

    public void removeChangeListener(ChangeListener cl)
    {
        mChangeListeners.remove(cl);
    }

    public void fireStateChanged()
    {
        for (Iterator it = mChangeListeners.iterator(); it.hasNext();)
        {
            ChangeListener c = (ChangeListener) it.next();

            c.stateChanged(new ChangeEvent(this));
        }
    }

    public void stateChanged(javax.swing.event.ChangeEvent changeEvent)
    {
        fireStateChanged(); // to listeners
    }
    
    // Debug method
    public void dump()
    {
        CategoryModel[] models = getCategories();
        for (int i = 0; i < models.length; i++)
        {
            System.out.println("Category: " + models[i].getTitle());
            InputModel[] inputModels = models[i].getInputs();
            for (int j = 0; j < inputModels.length; j++)
            {
                System.out.println("Input.Name: " + inputModels[j].getName() + ", InpType: " + inputModels[j].getType() + ", Desc: " + inputModels[j].getDescription());
            }
        }
    }
}

