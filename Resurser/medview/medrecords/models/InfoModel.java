package medview.medrecords.models;

public class InfoModel extends AbstractInputModel
{
    public static final int SUBHEADER = 1;
	public static final int TEXT = 2;
    private String text;
    private int type;
    public InfoModel(String text, int type)
	{
		super("", "","", null);
        this.text = text;
        this.type = type;

    }
    public int getType()
	{
		return InputModel.INPUT_TYPE_INFO;
	}
    public int getTextType()
	{
		return type;
	}
    public String getText()
	{
		return text;
	}
}
