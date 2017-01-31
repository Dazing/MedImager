package medview.datahandling.examination;

import java.io.*;

import java.util.*;

import javax.swing.event.*; // event listener list

import medview.datahandling.*;
import medview.datahandling.examination.filter.*;
import medview.datahandling.examination.tree.*;
import medview.datahandling.images.*;
import misc.foundation.*;

/**
 * <p>Title: The MedView Project</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class CachingExaminationDataHandler implements ExaminationDataHandler
{
	private HashMap pidVectorMap = null; // PatientIdentifier -> Vector (of ExaminationPair)

	private HashMap eidPairMap = null; // ExaminationIdentifier -> ExaminationPair

	private ExaminationDataHandler coreDataHandler;

	private EventListenerList listenerList = new EventListenerList(); // there is a reason for not directly adding to core edh

	public CachingExaminationDataHandler(ExaminationDataHandler h)
	{
		coreDataHandler = h;

		coreDataHandler.addExaminationDataHandlerListener(new ExaminationDataHandlerListener()
		{
			public void examinationLocationChanged(ExaminationDataHandlerEvent e)
			{
				fireExaminationLocationChanged(e);
			}

			public void examinationAdded(ExaminationDataHandlerEvent e)
			{
				try
				{
					// construct the cache if necessary (first time or cleared)

					if (!cacheMapsConstructed())
					{
						constructCacheMaps(null); // -> IOException
					}

					// now update the cache with the new examination

					ExaminationIdentifier eid = e.getIdentifier();

					ExaminationValueContainer evc = coreDataHandler.getExaminationValueContainer(eid);

					ExaminationPair pair = new ExaminationPair(eid, evc);

					eidPairMap.put(eid, pair); // adds to the eid -> pair map

					PatientIdentifier pid = eid.getPID();

					if (!pidVectorMap.containsKey(pid))
					{
						Vector vect = new Vector();

						vect.add(pair);

						pidVectorMap.put(pid, vect); // adds to the pid -> vector map
					}
					else
					{
						((Vector)pidVectorMap.get(pid)).add(pair); // adds to the vector
					}
				}
				catch (IOException exc)
				{
					exc.printStackTrace();
				}
				catch (NoSuchExaminationException exc)
				{
					exc.printStackTrace();
				}

				fireExaminationAdded(e);
			}

			public void examinationUpdated(ExaminationDataHandlerEvent e)
			{
				fireExaminationUpdated(e);
			}

			public void examinationRemoved(ExaminationDataHandlerEvent e)
			{
				try
				{
					// construct the cache if necessary (first time or cleared)

					if (!cacheMapsConstructed())
					{
						constructCacheMaps(null); // -> IOException
					}

					// now update cache contents

					ExaminationIdentifier eid = e.getIdentifier();

					ExaminationPair pair = (ExaminationPair) eidPairMap.get(eid);

					eidPairMap.remove(eid);

					( (Vector) pidVectorMap.get(eid.getPID())).remove(pair);
				}
				catch (IOException exc)
				{
					exc.printStackTrace();
				}

				fireExaminationRemoved(e);
			}

			public void examinationLocationIDChanged(ExaminationDataHandlerEvent e)
			{
				fireExaminationLocationIDChanged(e);
			}
		});
	}

	/**
	 * Adds an examination datahandler listener.
	 * @param l ExaminationDataHandlerListener
	 */
	public void addExaminationDataHandlerListener(ExaminationDataHandlerListener l)
	{
		listenerList.add(ExaminationDataHandlerListener.class, l);
	}

	/**
	 * Removes an examination datahandler listener.
	 * @param l ExaminationDataHandlerListener
	 */
	public void removeExaminationDataHandlerListener(ExaminationDataHandlerListener l)
	{
		listenerList.remove(ExaminationDataHandlerListener.class, l);
	}

	protected void fireExaminationAdded(ExaminationDataHandlerEvent event)
	{
		final Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationAdded(event);
			}
		}
	}

	protected void fireExaminationUpdated(ExaminationDataHandlerEvent event)
	{
		final Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationUpdated(event);
			}
		}
	}

	protected void fireExaminationRemoved(ExaminationDataHandlerEvent event)
	{
		final Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationRemoved(event);
			}
		}
	}

	protected void fireExaminationLocationChanged(ExaminationDataHandlerEvent event)
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationLocationChanged(event);
			}
		}
	}

	protected void fireExaminationLocationIDChanged(ExaminationDataHandlerEvent event)
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationLocationIDChanged(event);
			}
		}
	}

	/**
	 * Export specified patients to external new MVD.
	 */
	public int exportToMVD(PatientIdentifier[] patientIdentifiers, String newMVDlocation, ProgressNotifiable notifiable,

		ExaminationContentFilter filter, boolean allowPartialExport) throws IOException
	{
		return coreDataHandler.exportToMVD(patientIdentifiers, newMVDlocation, notifiable, filter, allowPartialExport);
	}

	/**
	 * Export specified examinations to external new MVD.
	 */
	public int exportToMVD(ExaminationIdentifier[] examinationIdentifiers, String newMVDlocation, ProgressNotifiable notifiable,

		ExaminationContentFilter filter, boolean allowPartialExport) throws IOException
	{
		return coreDataHandler.exportToMVD(examinationIdentifiers, newMVDlocation, notifiable, filter, allowPartialExport);
	}

	/**
	 * Returns the total number of examinations available in the data source
	 * handled by this ExaminationDataHandler.
	 * @return the number of examinations.
	 * @throws IOException
	 */
	public int getExaminationCount() throws IOException
	{
		if (!cacheMapsConstructed())
		{
			constructCacheMaps(null);
		}

		return eidPairMap.keySet().size();
	}

	/**
	 * Returns the total number of examinations for the specified patient.
	 * @param pid PatientIdentifier
	 * @return int
	 * @throws IOException
	 */
	public int getExaminationCount(PatientIdentifier pid) throws IOException
	{
		if (!cacheMapsConstructed())
		{
			constructCacheMaps(null);
		}

		return ((Vector)pidVectorMap.get(pid)).size();
	}

	/**
	 * Returns the set examination data location in it's 'raw' form.
	 * @return String
	 */
	public String getExaminationDataLocation()
	{
		return coreDataHandler.getExaminationDataLocation();
	}

	/**
	 * Returns the data location expressed in a (perhaps) more simple way than
	 * the 'raw' data location.
	 * @return String
	 */
	public String getExaminationDataLocationID()
	{
		return coreDataHandler.getExaminationDataLocationID();
	}

	/**
	 * Returns an array of all the patients listed at the currently set data
	 * location.
	 * @throws IOException
	 * @return PatientIdentifier[]
	 */
	public PatientIdentifier[] getPatients() throws IOException
	{
		return getPatients(null);
	}

	/**
	 * Returns an array of all the patients listed at the currently set data
	 * location.
	 * @param notifiable ProgressNotifiable
	 * @throws IOException
	 * @return PatientIdentifier[]
	 */
	public PatientIdentifier[] getPatients(ProgressNotifiable notifiable) throws IOException
	{
		if (!cacheMapsConstructed())
		{
			constructCacheMaps(notifiable);
		}

		Set keySet = pidVectorMap.keySet();

		PatientIdentifier[] retArr = new PatientIdentifier[keySet.size()];

		keySet.toArray(retArr);

		return retArr;
	}

	/**
	 * Returns an array of ExaminationIdentifiers describing the examinations for
	 * the specified pid.
	 * @param pid PatientIdentifier
	 * @throws IOException
	 * @return ExaminationIdentifier[]
	 */
	public ExaminationIdentifier[] getExaminations(PatientIdentifier pid) throws IOException
	{
		if (!cacheMapsConstructed())
		{
			constructCacheMaps(null);
		}

		Vector vect = (Vector) pidVectorMap.get(pid);

		ExaminationIdentifier[] retArr = new ExaminationIdentifier[vect.size()];

		for (int ctr=0; ctr<vect.size(); ctr++)
		{
			retArr[ctr] = ((ExaminationPair)vect.elementAt(ctr)).getExaminationIdentifier();
		}

		return retArr;
	}

	/**
	 * @param not ProgressNotifiable
	 * @return ExaminationValueContainer[]
	 * @throws IOException
	 */
	public ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not) throws
		IOException
	{
		return getAllExaminationValueContainers(not, MedViewDataConstants.OPTIMIZE_FOR_MEMORY);
	}

	/**
	 * @param not ProgressNotifiable
	 * @param hint int
	 * @return ExaminationValueContainer[]
	 * @throws IOException
	 */
	public ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not, int hint) throws
		IOException
	{
		if (!cacheMapsConstructed())
		{
			constructCacheMaps(not);
		}

		Collection c = eidPairMap.values();

		ExaminationValueContainer[] retArr = new ExaminationValueContainer[c.size()];

		Iterator iter = c.iterator();

		int index = 0;

		if (not != null)
		{
			not.setTotal(retArr.length);

			not.setDescription("Constructing value container return array");
		}

		while (iter.hasNext())
		{
			if (not != null) { not.setCurrent(index); }

			retArr[index++] = ((ExaminationPair)iter.next()).getExaminationValueContainer();
		}

		return retArr;
	}

	/**
	 * Returns an ExaminationValueContainer containing the values for the
	 * examination identified by the argument.
	 * @param id ExaminationIdentifier
	 * @param hint int
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 * @throws InvalidHintException
	 */
	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint) throws
		IOException, NoSuchExaminationException, InvalidHintException
	{
		return getExaminationValueContainer(id); // ignores the hint here
	}

	/**
	 * Returns an ExaminationValueContainer containing the values for the
	 * examination identified by the argument.
	 * @param id ExaminationIdentifier
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 */
	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
		IOException, NoSuchExaminationException
	{
		if (!cacheMapsConstructed())
		{
			constructCacheMaps(null);
		}

		return ((ExaminationPair)eidPairMap.get(id)).getExaminationValueContainer();
	}

	/**
	 * Returns an integer representing the total number of images taken for a
	 * patient with the specified patient identifier.
	 * @param pid PatientIdentifier
	 * @throws IOException
	 * @return int
	 */
	public int getImageCount(PatientIdentifier pid) throws IOException
	{
		return coreDataHandler.getImageCount(pid);
	}

	/**
	 * Get the images associated with the specified examination.
	 * @param id ExaminationIdentifier
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 * @return ExaminationImage[]
	 */
	public ExaminationImage[] getImages(ExaminationIdentifier id) throws
		IOException, NoSuchExaminationException
	{
		return coreDataHandler.getImages(id);
	}

	/**
	 * Returns whether or not the current implementation class for the
	 * ExaminationDataHandler interface deems the currently set location to be
	 * valid.
	 * @return boolean
	 */
	public boolean isExaminationDataLocationValid()
	{
		return coreDataHandler.isExaminationDataLocationValid();
	}

	/**
	 * Returns all examinations found in the knowledge base after the time of
	 * last cache construct.
	 * @return ExaminationIdentifier[] all examinations found in the knowledge
	 * base after the time of last cache construct.
	 * @throws IOException if something goes wrong.
	 */
	public ExaminationIdentifier[] refreshExaminations() throws IOException
	{
		return coreDataHandler.refreshExaminations();
	}

	/**
	 * Returns all examinations found in the knowledge base after the specified time.
	 * @param sinceTime the examinations returned are the ones added to the knowledge base
	 * after this time.
	 * @return ExaminationIdentifier[] all examinations found in the knowledge base after
	 * the specified time.
	 * @throws IOException if something goes wrong.
	 */
	public ExaminationIdentifier[] refreshExaminations(long sinceTime) throws IOException
	{
		return coreDataHandler.refreshExaminations(sinceTime);
	}

	/**
	 * Saves a Tree of examination data to the database.
	 * @param tree Tree the tree to save.
	 * @param imageArray ExaminationImage[] an array of associated examination images.
	 * @return int one of the two constants defined in the ExaminationDataHandler interface:
	 * NEW_EXAMINATION or PREVIOUS_EXAMINATION.
	 * @throws IOException if something goes wrong.
	 */
	public int saveExamination(Tree tree, ExaminationImage[] imageArray) throws IOException
	{
		return coreDataHandler.saveExamination(tree, imageArray); // will fire event (see constructor)
	}

	/**
	 * Saves the specified tree to the specified location.
	 * @param tree Tree the tree to save.
	 * @param imageArray ExaminationImage[] an array of associated examination images.
	 * @param location the location to store the examination to.
	 * @return int one of the two constants defined in the ExaminationDataHandler interface:
	 * NEW_EXAMINATION or PREVIOUS_EXAMINATION.
	 * @throws IOException if something goes wrong.
	 */
	public int saveExamination(Tree tree, ExaminationImage[] imageArray, String location) throws IOException
	{
		return coreDataHandler.saveExamination(tree, imageArray, location); // will fire event (see constructor)
	}

	public void removeExamination(ExaminationIdentifier eid) throws IOException
	{
		coreDataHandler.removeExamination(eid);
	}

	/**
	 * Sets the location of the examination data.
	 * @param loc String
	 */
	public void setExaminationDataLocation(String loc)
	{
		coreDataHandler.setExaminationDataLocation(loc);
	}

	/**
	 * Allows the datahandler to deal with the system shutting down.
	 */
	public void shuttingDown()
	{
		coreDataHandler.shuttingDown();
	}

	private boolean cacheMapsConstructed()
	{
		return pidVectorMap != null && eidPairMap != null;
	}

	private void constructCacheMaps(ProgressNotifiable pN) throws IOException
	{
		PatientIdentifier[] pids = coreDataHandler.getPatients(pN); // -> IOException

		pidVectorMap = new HashMap((int)(pids.length * 1.75));

		eidPairMap = new HashMap((int)(pids.length * 1.75));

		if (pN != null)
		{
			pN.setCurrent(0);

			pN.setTotal(pids.length);

			pN.setDescription("Constructing cache"); // TODO - language
		}

		for (int ctr=0; ctr<pids.length; ctr++)
		{
			if (pN != null)
			{
				pN.setCurrent(ctr);
			}

			ExaminationIdentifier[] eids = coreDataHandler.getExaminations(pids[ctr]); // -> IOException

			Vector vect = new Vector(eids.length);

			for (int ctr2=0; ctr2<eids.length; ctr2++)
			{
				try
				{
					ExaminationValueContainer evc = coreDataHandler.getExaminationValueContainer(eids[ctr2]); // -> IOException, NoSuchExaminationException

					ExaminationPair pair = new ExaminationPair(eids[ctr2], evc);

					eidPairMap.put(eids[ctr2], pair);

					vect.add(pair);
				}
				catch (NoSuchExaminationException exc)
				{
					exc.printStackTrace();

					throw new IOException("error during construction of pid -> vector map, check stack trace");
				}
			}

			pidVectorMap.put(pids[ctr], vect);
		}
	}

	/**
	 * Kept in the vector mapped by pid.
	 */
	private class ExaminationPair
	{
		private ExaminationIdentifier eid;

		private ExaminationValueContainer evc;

		public ExaminationPair(ExaminationIdentifier eid, ExaminationValueContainer evc)
		{
			this.eid = eid;

			this.evc = evc;
		}

		public ExaminationIdentifier getExaminationIdentifier() { return eid; }

		public ExaminationValueContainer getExaminationValueContainer() { return evc; }
	}

	/**
	 * UNIT TEST METHOD
	 */
	public static void main(String[] args)
	{
	}
}
