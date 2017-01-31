package medview.medrecords.models;

public class MineralizationModel extends AbstractInputModel
{
	public MineralizationModel()
	{
		super("Mineralisering", "","", null);
    }
	public int getType()
	{
        return InputModel.INPUT_TYPE_MINERALIZATION;
	}
}
