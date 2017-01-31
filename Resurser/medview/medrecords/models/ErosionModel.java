package medview.medrecords.models;

public class ErosionModel extends AbstractInputModel
{
	public ErosionModel()
	{
		super("Erosion", "","", null);
    }
	public int getType()
	{
		return InputModel.INPUT_TYPE_EROSION;
	}
}
