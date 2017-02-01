/**
 * @(#) AudioLeafNodeModel.java
 */

package medview.medimager.model;

import medview.medimager.foundation.*;

/**
 * A node model containing audio data.
 * @author Fredrik Lindahl
 */
public interface AudioLeafNodeModel
{
	/**
	 * Obtain the associated image data, as a raw
	 * byte audio data stream.
	 */
	public abstract byte[] getAudioData( );

	/**
	 * Set the audio data obtainer, used to obtain the
	 * raw byte audio data stream.
	 */
	public abstract void setAudioDataObtainer(AudioDataObtainer o);

	/**
	 * Returns the currently set audio data obtainer.
	 */
	public abstract AudioDataObtainer getAudioDataObtainer();

	/**
	 * Returns whether or not the node contains any
	 * audio data.
	 */
	public abstract boolean containsAudioData();
}
