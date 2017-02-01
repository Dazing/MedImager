package medview.medrecords.models;

public class TraumaModel extends AbstractInputModel
{
	public TraumaModel()
	{
		super("Trauma", "","", null);
    }
	public int getType()
	{
		return InputModel.INPUT_TYPE_TRAUMA;
	}
}
