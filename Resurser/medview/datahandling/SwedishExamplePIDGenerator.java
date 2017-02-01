package medview.datahandling;

public class SwedishExamplePIDGenerator implements ExamplePIDGenerator
{
	public PatientIdentifier generateExamplePID()
	{
		return new PatientIdentifier("GOT0000019771", "19770329-7248");
	}
}