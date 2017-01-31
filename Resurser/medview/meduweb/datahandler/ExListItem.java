package medview.meduweb.datahandler;
import java.sql.Date;

public class ExListItem {
    public int exId;
    public String title;
    public Date createDate;
    public Date showDate;
    public boolean current;
    public int questions;

    public ExListItem(int exId,String title,
		      Date createDate,Date showDate,
		      boolean current,int questions) {
	this.exId = exId;
	this.title = title;
	this.createDate = createDate;
	this.showDate = showDate;
	this.current = current;
	this.questions = questions;
    }
}
