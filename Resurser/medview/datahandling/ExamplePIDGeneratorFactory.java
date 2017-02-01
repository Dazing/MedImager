package medview.datahandling;

public class ExamplePIDGeneratorFactory implements MedViewDataConstants
{
	public ExamplePIDGenerator getExamplePIDGenerator()
	{
		try
		{
			Class c = Class.forName(EXAMPLE_PID_GENERATOR_CLASS);

			return (ExamplePIDGenerator) c.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			System.exit(1);

			return null; // satisfy compiler
		}
	}

	public static ExamplePIDGeneratorFactory instance()
	{
		if (instance == null)
		{
			instance = new ExamplePIDGeneratorFactory();
		}

		return instance;
	}

	private static ExamplePIDGeneratorFactory instance;
}