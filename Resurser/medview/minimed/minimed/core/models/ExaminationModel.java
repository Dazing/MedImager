package minimed.core.models;


import misc.foundation.HashVector;

/**
 * ExaminationModel: Models an input form (with categories) as a data structure. Does NOT handle values anymore.
 * Modified version of medview.medrecords.models.ExaminationModel
 * @author Nils  Erichson
 */
public class ExaminationModel {
    private HashVector mCategories;
    private String mTreeFile = null;

    /**
     * Creates new ExaminationModel.
     */
    public ExaminationModel() {
        mCategories = new HashVector();
        mTreeFile = null;
    }

    public void setTreeFile(String pFileName) {
        mTreeFile = pFileName;
    }

    public String  getTreeFile(){
        return mTreeFile;
    }

    public boolean containsCategory(String name) {
        return mCategories.containsKey(name);
    }

    public CategoryModel getCategory(String name) {
        return (CategoryModel) mCategories.get(name);
    }

    public String[] getCategoryNames() {
        int cnt = mCategories.size();
        String[] names = new String[cnt];
        CategoryModel[] cats = getCategories();

        for (int i=0; i<cnt; i++) {
            names[i]=cats[i].getTitle();
        }

        return names;
    }

    public CategoryModel[] getCategories() {
        CategoryModel[] cats = new CategoryModel[mCategories.size()];
        if (mCategories != null) {
            cats = (CategoryModel[]) mCategories.getValueArray(cats);
            return cats;
        }
        return null;
    }

    /**
     * Adds a category.
     * 
     * @param catModel the category to add.
     */
    public void addCategory(CategoryModel catModel) {
        mCategories.put(catModel.getTitle(),catModel);
    }

    /**
     * Moves a category in the order.
     * 
     * @param catModel the category to move.
     * @param relativeAdjustment the steps to move the category
     * (for example 2 (down) or -1 (up)
     */
    public void moveCategory(CategoryModel catModel, int relativeAdjustment) {
        mCategories.moveRelative(catModel,relativeAdjustment);
    }

    /**
     * Removes a category.
     * 
     * @param catModel the category to remove..
     */
    public void removeCategory(CategoryModel catModel) {
        mCategories.removeByKey(catModel.getTitle());
    }
}
