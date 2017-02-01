/*
 * InputFactory.java
 *
 * Created on den 4 augusti 2003, 15:04
 *
 * $Id: InputFactory.java,v 1.4 2007/10/17 15:17:03 it2aran Exp $
 *
 * $Log: InputFactory.java,v $
 * Revision 1.4  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.3  2004/12/08 14:42:51  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.2  2003/11/11 13:42:59  oloft
 * Added to main-branch
 *
 * Revision 1.1.2.2  2003/10/24 21:01:12  oloft
 * Debug edits only
 *
 * Revision 1.1.2.1  2003/09/03 22:16:31  erichson
 * First check-in.
 *
 *
 */

package medview.medrecords.components;

import medview.medrecords.components.*;
import medview.medrecords.components.inputs.*;

import medview.medrecords.exceptions.*;

import medview.medrecords.models.*;

/**
 * Factory class to create ValueInputComponents
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class InputFactory
{
	public static ValueInputComponent createInput(InputModel inputModel) throws UnknownTypeException
	{
		int inputType = inputModel.getType();
		switch (inputType) // the below is ugly - should be replaced by dynamic binding to an interface in the future
		{
			case InputModel.INPUT_TYPE_TEXTAREA:
			
				return createInput((FieldModel)inputModel);
				
			case InputModel.INPUT_TYPE_PICTURECHOOSER:
			
				return createInput((PictureChooserModel)inputModel);

            case InputModel.INPUT_TYPE_TRAUMA:

				return createInput((TraumaModel)inputModel);

            case InputModel.INPUT_TYPE_EROSION:

				return createInput((ErosionModel)inputModel);
            
            case InputModel.INPUT_TYPE_MINERALIZATION:

				return createInput((MineralizationModel)inputModel);

            case InputModel.INPUT_TYPE_INFO:

				return createInput((InfoModel)inputModel);

            default:
				throw new UnknownTypeException("InputFactory ERROR: Type unknown (" + inputType + ", inputModel class " +
    				inputModel.getClass() + ")");
		}
	}

	public static ValueInputComponent createInput(FieldModel fieldModel)
	{
		switch (fieldModel.getFieldType())
		{
			case FieldModel.FIELD_TYPE_NOTE:
			
				return new NoteInputComponent(fieldModel);
				
			default:
			
				return new FieldInputComponent(fieldModel);
		}
	}

	public static ValueInputComponent createInput(PictureChooserModel picChooserModel)
	{
		return new PictureChooserInput(picChooserModel);
	}
	public static ValueInputComponent createInput(ErosionModel erosionModel)
	{
		return new ErosionInput(erosionModel);
	}
	public static ValueInputComponent createInput(TraumaModel traumaModel)
	{
		return new TraumaInputComponent(traumaModel);
	}
	public static ValueInputComponent createInput(MineralizationModel minModel)
	{
		return new MineralizationInput(minModel);
	}
	public static ValueInputComponent createInput(InfoModel infoModel)
	{
		return new InfoInput(infoModel);
	}
}
