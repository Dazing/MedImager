package minimed.core.datahandling;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import minimed.core.MinimedConstants;
import minimed.core.datahandling.term.TermDataHandler;
import minimed.core.properties.PropertiesHandler;


public class DataHandlerFactory {
	
	// String constants
	String DEFAULT_TERM_DATA_HANDLER_CLASS = "minimed.core.datahandling.term.ParsedTermDataHandler";
	String DEFAULT_PROPERTIES_HANDLER_CLASS = "minimed.core.properties.PropertiesHandler";
	
	// Member variables
	TermDataHandler defaultTermDataHandler;
	PropertiesHandler defaultPropertiesHandler;
	

	// Prevent instantiating
	private DataHandlerFactory() { }

	// Singleton instance
	private static DataHandlerFactory instance = null;
	
	
	// Create singleton instance
	public static DataHandlerFactory instance()
	{
		if (instance == null) {
			instance = new DataHandlerFactory();
		}
		return instance;
	}
	

	
	// Set datahandlers
	
	public void setDefaultTermDataHandler(TermDataHandler handler) {
		defaultTermDataHandler = handler;
	}
	
	
	// Obtain datahandlers
	
	/**
	 * Obtains the default term data handler object. If
	 * this cannot be constructed, it is constructed a fatal error
	 * and the application will exit.
	 */
	public TermDataHandler getDefaultTermDataHandler() {
		if (defaultTermDataHandler == null)	{
			String c = DEFAULT_TERM_DATA_HANDLER_CLASS;

			try	{
				defaultTermDataHandler = (TermDataHandler) Class.forName(c).newInstance();
			}
			catch (Exception e)	{
				e.printStackTrace();

				System.exit(1); // fatal error
			}
		}

		return defaultTermDataHandler;
	}
	

	/*
	public PropertiesHandler getDefaultPropertiesHandler() {
		if (defaultPropertiesHandler == null) {
			
			// Read properties from file
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(MinimedConstants.PROPERTIES_FILE));
				
				defaultPropertiesHandler = (PropertiesHandler) in.readObject();

			} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				
				// If first time program started, or file not found
				if (defaultPropertiesHandler == null) {
					defaultPropertiesHandler = new PropertiesHandler();
				}
			}
		}
	
		return defaultPropertiesHandler;
	}
	*/
	
}
