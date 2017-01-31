/**
 * @(#) DefaultImageLeafNodeModel.java
 */

package medview.medimager.model;

import java.util.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medimager.foundation.*;

/**
 * The default implementation class of a node containing image
 * data, audio data, examination and patient data, as well as
 * a description and a note.
 * @author Fredrik Lindahl
 */
public class DefaultLeafNodeModel extends AbstractImageLeafNodeModel implements
	NoteLeafNodeModel, ExaminationLeafNodeModel, AudioLeafNodeModel
{
	// PRIVATE MEMBERS

	private String note = "";

	private PatientIdentifier pid = null;

	private ExaminationIdentifier eid = null;

	private AudioDataObtainer audioDataObtainer = null;


	// CONSTRUCTOR(S)

	public DefaultLeafNodeModel()
	{
		super();
	}

	public DefaultLeafNodeModel( String description )
	{
		super(description);
	}


	// CLONING

	public Object clone()
	{
		DefaultLeafNodeModel clonedNodeModel = (DefaultLeafNodeModel) super.clone();

		clonedNodeModel.note = this.note;

		clonedNodeModel.pid = this.pid;

		clonedNodeModel.eid = this.eid;

		clonedNodeModel.audioDataObtainer = this.audioDataObtainer;

		return clonedNodeModel;
	}


	// CONCRETE EXAMINATION IDENTIFYING METHODS

	public Date getExaminationDate( )
	{
		return eid.getTime();
	}

	public void setEID( ExaminationIdentifier eid )
	{
		this.eid = eid;

		fireNodeUpdated();
	}

	public ExaminationIdentifier getEID()
	{
		return eid;
	}

	public void setPID( PatientIdentifier pid )
	{
		this.pid = pid;

		fireNodeUpdated();
	}

	public PatientIdentifier getPID( )
	{
		return pid;
	}


	// CONCRETE DESCRIPTIVE METHODS

	public void setNote( String note )
	{
		this.note = note;

		fireNodeUpdated();
	}

	public String getNote( )
	{
		return note;
	}


	// AUDIO DATA

	public byte[] getAudioData( )
	{
		if (containsAudioData())
		{
			try
			{
				return audioDataObtainer.getAudioByteArray();
			}
			catch (Exception e)
			{
				e.printStackTrace();

				return null;
			}
		}
		else
		{
			return null;
		}
	}

	public void setAudioDataObtainer(AudioDataObtainer o)
	{
		this.audioDataObtainer = o;
	}

	public AudioDataObtainer getAudioDataObtainer()
	{
		return audioDataObtainer;
	}

	public boolean containsAudioData()
	{
		return (getAudioDataObtainer() != null);
	}

}
