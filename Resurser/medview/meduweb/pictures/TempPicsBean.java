package medview.meduweb.pictures;

import java.io.Serializable;
import java.util.ArrayList;

public class TempPicsBean implements Serializable {

    private ArrayList pictureList = null;
    
    public TempPicsBean() {
	pictureList = new ArrayList();
    }

    public ArrayList getPictureList() {
	return pictureList;
    }

    public void setPictureList(ArrayList list) {
	pictureList = list;
    }

    public void addPicture(String picture) {
	if (! pictureList.contains(picture)) {
	    pictureList.add(picture);
	}
    }

    public void removePicture(String picture) {
	if (pictureList.contains(picture)) {
	    Object o = pictureList.remove(pictureList.indexOf(picture));
	}
    }

    public void reset() {
	pictureList = new ArrayList();
    }

}
